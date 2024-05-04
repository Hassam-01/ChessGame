package main;

import java.util.ArrayList;

import bestMoves.bestMoves;
import piece.Bishop;
import piece.King;
import piece.Knight;
import piece.Pawn;
import piece.Queen;
import piece.Rook;

// Pieces Value

//	Pawn 	1
//	Knight 	3
//	Bishop 	3
//	Rook 	5
//	Queen 	9

// pawn on the center are worth more than the pawns at the ends
// a pair of bishop is more valuable than knight
// king is arbitarily given a very high score
// forced move 

// NOTE:
// Stalemate and checkmate logic to be implemented
// and if the piece is under attack the piece must try to save it self
// debug king in check and queen jumping to 7 2



public class Computer {

	public int targetCol, targetRow; // the target row and column where the computer piece can move
	public ArrayList<ChessPieces> checkedPieces = new ArrayList<>(); // array to store pieces that have been checked for valid move
	
	int PAWN = 1, KNIGHT = 3, BISHOP = 3, ROOK = 5, QUEEN = 9, KING = 1000;
	int UP = 1, DOWN = 2, RIGHT = 3, LEFT = 4, UPRIGHT = 5, UPLEFT = 6, DOWNRIGHT = 7, DOWNLEFT = 8;
	
	int bestCol, bestRow;
	double moveRating = 1;
	int kingCol;
	int kingRow;
	
//	PanelGame gameobj = new PanelGame();
	
	ChessPieces hitPiece;
	ChessPieces checkingP;
	int validMovesCount = 0;
    double maxMoveRating = Double.MIN_VALUE;
    int maxCol= -1, maxRow =  - 1;
	
	
	
	boolean isValid = false;
	
	ArrayList<bestMoves> validMoves = new ArrayList<>(); // this will store the best moves of each piece available
	ArrayList<bestMoves> checkMoves = new ArrayList<>();
	
	ChessPieces cActiveP; // Computers Active Piece which will be moved
	
	public void choosePiece() {
		// Choosing a piece
		
		for(ChessPieces P: PanelGame.simpieces) {
			isValid = false;
			if(P.color == PanelGame.compColor  && !checkedPieces.contains(P) ) {
				 // when piece found setting it as active
				 cActiveP = P;
			}
//			&& (P instanceof Pawn  || P instanceof Bishop || P instanceof Knight)
			 // call appropriate methods to check for moves

			maxMoveRating = Double.MIN_VALUE;
			validMovesCount = 0;
			 if(cActiveP instanceof Pawn) {cPawn();}
			 else if(cActiveP instanceof King) { cKing();}
			 else if(cActiveP instanceof Bishop) {cBishop();}
			 else if(cActiveP instanceof Knight) {cKnight();}
			 else if(cActiveP instanceof Queen) {cQueen();}
			 else if(cActiveP instanceof Rook) {cRook();}
			
	 }

		makeMove();
//		cActiveP.getIndex();
		
	}
	
	public ChessPieces Cpiece() {
		checkedPieces.clear();
		return cActiveP;
	}
	
	
//	public void saveKing() {
//		// first we check if the checking piece can be captured
//		// then we check if we can block the check
//		// then last we see if moving the king saves the king
//		bestMoves saveKingP = null;
//		for(ChessPieces P: PanelGame.simpieces) { 
//			
//			if(P.color == PanelGame.compColor ) {
//				if(P.canMove(checkingP.col, checkingP.row)) { // checking if a piece can capture the checking piece
//					saveKingP.bestCol = checkingP.col;
//					saveKingP.bestRow = checkingP.row;
//					saveKingP.piece = P;
//					saveKingP.moveRating = tradeValue(saveKingP);
//				
//					checkMoves.add(saveKingP);
//				}
//
//			// check if we can block the check
//			
//				if(!(checkingP instanceof Knight) &&  P.canMove(checkingP.col, checkingP.row) ){
//					// checking if a piece can move and that doesn't cause king in check
//					
//					if(checkingP.col == kingCol && kingRow != checkingP.row) {
//						if(kingRow > checkingP.row) {
//							
//						}if(kingRow < checkingP.row) {
//							
//						}
//					}
//					if(checkingP.row == kingRow && kingCol != checkingP.col) {
//						if(kingCol > checkingP.col) {
//							
//						}if(kingCol < checkingP.col) {
//							
//						}
//					}
//					if(checkingP.row != kingRow && checkingP.col != kingCol) {
//						// for diagonals
//					}
//					
//				}
//			}
//		}
//
//	
//		
//		
//		
//	}
	
	public boolean staleMate() {
		if(validMoves.size() == 0)
			return true;
		return false; 
	}
	
	public boolean checkMate() {
		if(checkKingInCheck() && staleMate()) 
			return true;
		return false;
	}
	
	public void saveKing() {
	    // Iterate through all pieces of the defending color
		
	    for (ChessPieces P : PanelGame.simpieces) {
	    	if(P.color == PanelGame.compColor ) {
				if(P.canMove(checkingP.col, checkingP.row)) { // checking if a piece can capture the checking piece
					bestMoves saveKingP = new bestMoves(P.getIndex(),checkingP.col, checkingP.row, P.col, P.row,0, P);
//					saveKingP.bestCol = checkingP.col;
//					saveKingP.bestRow = checkingP.row;
//					saveKingP.piece = P;
                	System.out.println("Calling from outer loop save king");
					saveKingP.moveRating = tradeValue(saveKingP);
				
					checkMoves.add(saveKingP);
				}

	            // Check if the piece can block the check
	            if (!(checkingP instanceof Knight) && !(P instanceof King)) {
	                // Determine the direction of the check
	                int dCol = Integer.compare(checkingP.col, kingCol);
	                int dRow = Integer.compare(checkingP.row, kingRow);
                	System.out.println("dCol:  "+ dCol+ " dRow:  "+ dRow);

	                // Check positions between the checking piece and the king
	                for (int col = kingCol + dCol, row = kingRow + dRow; col != checkingP.col || row != checkingP.row; col += dCol, row += dRow) {
	                    // Check if the piece can move to this position without putting the king in check
	                    if (P.canMove(col, row)) {
	                        // Add blocking move to the list of moves
	                    	bestMoves saveKingP = new bestMoves(P.getIndex(), checkingP.col, checkingP.row,P.col, P.row ,0, P);
//	                    	saveKingP.bestCol = checkingP.col;
//	    					saveKingP.bestRow = checkingP.row;
//	    					saveKingP.piece = P;
	                    	System.out.println("Calling from inner loop save king");
	    					saveKingP.moveRating = tradeValue(saveKingP);
	    					System.out.println(saveKingP.piece+ " "+ saveKingP.bestCol+" "+ saveKingP.bestRow+" KING IN CHECK SAVE KING FUCNTION");
	    					checkMoves.add(saveKingP);
	                    }
	                }
	            }
	        }
	    }
	    
	    for(bestMoves BM: checkMoves) {
	    	
	    	System.out.println(BM.piece+ " "+ BM.bestCol+" "+ BM.bestRow+" KING IN CHECK SAVE KING FUCNTION");
	    }
	    
	    decideTheMove(checkMoves);
	}

	
	
	private void decideTheMove(ArrayList<bestMoves> moves){
		
		bestMoves bestMove = null;
		
		bestMove = moves.get(0); // setting the first object in valid Move array as best move piece
		
		
		
		for(int i = 0; i< moves.size()-1; i++) {

				if(bestMove.moveRating >= moves.get(i+1).moveRating) {
					continue;
				}else
					bestMove = moves.get(i+1);
		}
		// locating the choosen piece on the board and setting it as Active
		for(ChessPieces p: PanelGame.simpieces) {
							
			if(p == bestMove.piece && p.getIndex() == bestMove.index && p.canMove(bestMove.bestCol, bestMove.bestRow) 
					&& PanelGame.simpieces.contains(bestMove.piece)) {
				
				cActiveP = bestMove.piece;
			
			targetCol = bestMove.bestCol;
			targetRow = bestMove.bestRow;
	
			cActiveP.col = bestMove.bestCol;
			cActiveP.row = bestMove.bestRow;
			System.out.println(bestMove.moveRating);

			}
		}
		
		reset(); // reset method called to reset the bestMove after a move is played
	
		
	}
	
	public void makeMove() {
//		bestMoves bestMove = null;
		if(checkKingInCheck()) {
				// try to kill the checking piece highest priority
				System.out.println("KING IN CHECK NO MOVE SHOULD BE MADE!!!");
				saveKing();
				// check if the piece can capture the checking piece
//				if(bestMove.piece.canMove(checkingP.col, checkingP.row)) {
//					// then compare the rating with the piece that can also check the piece 
//					if(bestMove.moveRating > validMoves.get(i+1).moveRating && validMoves.get(i+1).piece.canMove(checkingP.col, checkingP.row)) {
//						continue; 
//					}else
//						bestMove = validMoves.get(i+1);
//				}else
//					continue;
		}
		else if(isValid && !checkKingInCheck()) { // make a move only if there are valid moves
				
			
			decideTheMove(validMoves);
			
			
		}if(!isValid || !PanelGame.simpieces.contains(cActiveP)){ // if there are no valid moves add the piece to checked so that next time before an move is played this piece is not checked again
			checkedPieces.add(cActiveP);
			choosePiece(); // call the choose piece again to check another piece for valid moves
		}
	}
		

	// method to check the moves of pawn and add the highest rated move to the array
	public void cPawn() {
		
		targetCol = cActiveP.col;
	    targetRow = cActiveP.row + 1;
	    
	    
	    if (cActiveP.canMove(targetCol, targetRow)) {
	    	validMovesCount++;
	    	
	        if (cActiveP.col >= 0 && cActiveP.col < 3 || cActiveP.col > 3 && cActiveP.col <= 7) 
	            moveRating = 0.5;
	        else 
	            moveRating = 0.75;	
	        
	        setMax(moveRating, targetCol, targetRow);
	        
	    }
	    
	    if (cActiveP.canMove(targetCol, targetRow + 1)) {
	    	validMovesCount++;
	    	if (cActiveP.col >= 0 && cActiveP.col < 3 || cActiveP.col > 3 && cActiveP.col <= 7)
	        	moveRating = 0.5;
	        else
	            moveRating = 1.25;
	    	
	    	setMax(moveRating, targetCol, targetRow + 1);
	    
	    }
	    
	    
	    if (cActiveP.canMove(targetCol + 1, targetRow)) {
	    	validMovesCount++;
	    	
	    	moveRating = 1;
	    	
	    	setMax(moveRating, targetCol + 1, targetRow);
	    }
	    
	    if (cActiveP.canMove(targetCol - 1, targetRow)) {
	    	validMovesCount++;
	    	
	    	moveRating = 1;
	    	
	    	setMax(moveRating, targetCol - 1, targetRow);

	    }
	    
	    if(validMovesCount > 0) {
	    	bestMoves p = new bestMoves(cActiveP.getIndex(), maxCol, maxRow,cActiveP.col, cActiveP.row ,maxMoveRating, cActiveP);
	    	isValid = true;
	    	setMoveRating(p);
	    	validMoves.add(p);
	    	
	    }
	    moveRating = 1;
	}
	

	public void cKing(){
//		cActiveP.detectPiece(targetCol, targetRow);	
	// same row col +1/-1, same col row+1/-1, col row +1/-1
		
		int directions[][] = {{0,1}, {0,-1}, {1,0}, {-1, 0}, {1, 1},{-1, -1}, {1, -1}, {-1,1}};
		
		for(int dir[]: directions) {
			
			int col = cActiveP.col + dir[0];
			int row = cActiveP.col + dir[1];
			
			if(cActiveP.canMove(col, row)){
				
				validMovesCount++;
				
				moveRating = -10; // moving king without any trade value is not a recommended move in chess
				
				isValid = true;
				bestMoves p = new bestMoves(cActiveP.getIndex(), maxCol, maxRow,cActiveP.col, cActiveP.row, maxMoveRating, cActiveP);
					
				setMoveRating(p);
				
				setMax(moveRating, col, row);	
				validMoves.add(p);
			}

			
		}
		moveRating = 1;
	}
	
	public void cQueen(){
		
		// col same row++, row same col++, col and row -1,-1,and +1,+1 and -1, +1 and +1, -1; 
		
	int[][] directions  = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {-1, -1}, {1,1}, {-1, 1}, {1, -1}};
	
	for(int[] dir: directions) {
		
		int colDelta =  dir[0];
		int rowDelta =  dir[1];
		
		int col = cActiveP.col + colDelta;
		int row = cActiveP.row + rowDelta;

		while(cActiveP.canMove(col, row)){
			
			validMovesCount++;
		
			isValid = true;
		
//			setMax(moveRating, col, row);	
			
			bestMoves p = new bestMoves(cActiveP.getIndex(), col, row, cActiveP.col, cActiveP.row, moveRating, cActiveP);
				
			setMoveRating(p);
			
			
			validMoves.add(p);
			
			col+= colDelta; 
			row += rowDelta;
		}
			
	}
		
	}
	
	public void cBishop(){
		 
		// Define deltas for movement directions
		    int[][] directions = {{1, 1}, {-1, -1}, {1, -1}, {-1, 1}};
		    
		    for (int[] dir : directions) {
		        int colDelta = dir[0];
		        int rowDelta = dir[1];
		        
		        int col = cActiveP.col + colDelta;
		        int row = cActiveP.row + rowDelta;
		        
		        while (cActiveP.canMove(col, row)) {
//		            double moveRating = 1.0;
		        	
		        	validMovesCount++;
		        	
		        	isValid = true;
		        
		        	maxCol = col;
		            maxRow = row;
		            
		            setMax(moveRating, col, row);
		        	bestMoves p = new bestMoves(cActiveP.getIndex(), maxCol, maxRow,cActiveP.col, cActiveP.row, maxMoveRating, cActiveP);
			    	
			    	setMoveRating(p);
		            
		            validMoves.add(p);
		            
		            col += colDelta;
		            row += rowDelta;
		        }
		    }
		}

	
	public void cRook(){
		
		int directions[][] = {{0,1}, {0,-1}, {1, 0},{-1, 0}};
		
		for(int[] dir: directions) {
			
			int colDelta = dir[0];
			int rowDelta = dir[1];
			
			int col = cActiveP.col + colDelta;
			int row = cActiveP.row + rowDelta;
			
			while(cActiveP.canMove(col, row)) {
	
				validMovesCount++;
				
				 
				 maxCol = col;
		         maxRow = row;
				
		     	setMax(moveRating, col, row);
			    
				bestMoves p = new bestMoves(cActiveP.getIndex(), maxCol, maxRow,cActiveP.col, cActiveP.row, maxMoveRating, cActiveP);
		    	
		    	setMoveRating(p);
		                
		    	     
		         validMoves.add(p);
		         
		         isValid = true;
		         
		         col += colDelta;
		         row += rowDelta;
		         
			}
		    }	
	}
			
	
public void cKnight(){
		
	// (targetcol - activeCol) * (targetrow - activerow) == 2 
	// row and column +1 atleast and max +2 each
		
	int[][] directions = {{1,-2},{1,2},{-1, -2},{-1,2}, {2,1}, {2,-1}, {-2, 1},{-2, -1}};
	
	for(int dir[]: directions) {	
	
	
		int col = cActiveP.col + dir[0];
		int row = cActiveP.row + dir[1];
		while(cActiveP.canMove(col, row)) {
		
			validMovesCount++;
				
			maxCol = col;
			maxRow = row;
		
			
			setMax(moveRating, maxCol, maxRow);
			// object of type best move is stored and added to  valid moves
			bestMoves p = new bestMoves(cActiveP.getIndex(), maxCol, maxRow,cActiveP.col, cActiveP.row, maxMoveRating, cActiveP);
			
			setMoveRating(p);
			
			// isValid is also set to true to make a move
			isValid = true;
		
			validMoves.add(p);
			col++; row++;
		}
	}
}

	// method to set the max move rating
	// this method is called after every valid move is found with it's move rating
	public void setMax(double moveRating, int maxCol, int maxRow) {
		
		// if the received moverating is greater than the previously set move Rating then it is set max
		if(moveRating > this.maxMoveRating) {
		
		this.maxMoveRating = moveRating;
		this.maxCol = maxCol; // the col and row for the move is also saved as max col and row
		this.maxRow = maxRow;

		}
	}
	
	// function to clear all the implementations and objects created after a move because the positions and standings all change after a move 
	// so they must re-calculated according to the new positions
	public void reset() {		
		validMoves.clear();
		checkMoves.clear();
		checkedPieces.clear();
	}
	
	
	public boolean checkKingInCheck() { // method to check whether a king is in check or not
	
		// first the row and col of the king must be found
		setKingCoordinates(); 
		
		for(ChessPieces p: PanelGame.simpieces) {
			
			// we check if an opponent piece can move to the king's col and row
			// this is the condition for check
			if(p.color != PanelGame.compColor && p.canMove(kingCol, kingRow)) {
				checkingP = p;
				return true; // return true if checking
			}
		}

		
		return false; // else retrun false
		
		// if king is in check
			// king can move to a safe location
			// a piece must capture the checking piece
			// a piece must block the check
	}
	
	public void setKingCoordinates() { // this method finds the king's row and column
		
		for(ChessPieces p: PanelGame.simpieces) {
			if(p.color == PanelGame.compColor && p instanceof King) { // matching the color and checking if it is a king
				kingCol = p.col;
				kingRow = p.row;
			}
		}
	}
	
//	// method to find the piece that is checking the king
//	public ChessPieces getCheckingP() {
//		
//		
//		ChessPieces checkingpiece = null;
//		
//		for(ChessPieces P: PanelGame.simpieces) {
//			if(P.color != PanelGame.compColor && P.canMove(kingCol, kingRow))
//				checkingpiece = P;
//		}
//		
//		return checkingpiece;
//	}
	
	// method to find the hitting piece, the piece which the computer is able to capture
	public boolean hittingPiece(bestMoves Bpiece) {

		// hitting piece is found so that the trade value can be found
		// capturing all the available pieces is not a good strategy
		// thus the trade value needs to be positive
		
		for(ChessPieces P: PanelGame.simpieces) {
			if(P.color != Bpiece.piece.color && P.col == Bpiece.bestCol && P.row == Bpiece.bestRow && !(P instanceof King)) {
				hitPiece = P; // hit Piece is the being captured by the computer
//				PanelGame.checkingP
				return true;
			}
		}
		
		return false;
	}
	
	// returns a double value, which is the difference between the computer piece value and opponent piece value
	public double tradeValue(bestMoves Piece) {
		
		double compPieceValue = getPieceValue(Piece.piece);
		double oppPieceValue = getPieceValue(hitPiece); 
		double tradeValue = 0;
		
		
		// when checking if a piece can move to that position after we capture a piece we must check for all pieces
		//except the piece we assume to be captured
		
		
		if(hittingPiece(Piece))
			tradeValue += oppPieceValue;
		
		// receives an object from the class type bestmoves
		// gets the piece from the class and finds the difference
		
		// in trade value tow things must be checked :
		// 1. if the piece captures an opponent piece and an opponent piece can capture that piece
		//trade value of that must be also considered
		
		// 2. if the piece moves on to capture an opponent piece and was defending one of its important piece than
		// that trade value must be also considered
		
		// case 1
		System.out.println("Checking 0.1: " + Piece.piece + " "+Piece.activeCol +" "+Piece.activeRow);
		System.out.println(Piece.bestCol +" "+ Piece.bestRow);

		for(ChessPieces P: PanelGame.simpieces) {

			if(P.color != PanelGame.compColor) {
			
				if( (P instanceof Pawn) ) {	
					if(((Pawn) P).canMove(Piece.bestCol, Piece.bestRow, 1)){
						System.out.println(tradeValue+" pawn before"+ Piece.bestCol+" "+ Piece.bestRow);
					
						tradeValue -= compPieceValue; 
						System.out.println(tradeValue+" pawn after");
						}
				}else if(!(P instanceof Pawn)
					&& P.canMove(Piece.bestCol, Piece.bestRow, 1)) {
				
				System.out.println(P + " can move to: ");
				System.out.println(tradeValue+" piece before "+ Piece.bestCol+" "+ Piece.bestRow);

				tradeValue -= compPieceValue; 
				System.out.println(tradeValue+" piece after");
				}
			
			}
		}	
		for(ChessPieces P: PanelGame.simpieces) {
		
		if(P.color == PanelGame.compColor && P == Piece.piece) { // check if the piece is of computer and is the piece for which shield should be determined
			for(ChessPieces P2: PanelGame.simpieces) { 
					
				if(P2.color != PanelGame.compColor && P2.canMove(Piece.activeCol, Piece.activeRow) ) { // find if any opponent piece can capture the activeP of comp
					System.out.println("Opponent Piece "+ P2);
					for(ChessPieces P3: PanelGame.simpieces) {
						if((P3.color == PanelGame.compColor) && (P3 != Piece.piece) && (P2.canMove(P3.col, P3.row,true)) && hitPiece != P2) {// find if the opponent can capture any other except activeP
//							System.out.println("Computer piece being captured " + P3 + " "+P3.col+" " +P3.row);
//							System.out.println("Piece in line "+ Piece.piece +" "+ Piece.piece.col + " "+ Piece.piece.row);
//						
						
							if(getAttackDirection(P3.col, P3.row, P2) == getAttackDirection(Piece.activeCol, Piece.activeRow, P2)) { // if both are in same attack direction
								System.out.println(tradeValue+" shield before");
						
								tradeValue -= getPieceValue(P3);
								System.out.println("PIECE : "+ P3);
								System.out.println(tradeValue+" shield after");
								break;
							}
						
						}
					}
						

				}
			}
		}
		}
		
		
		System.out.println(tradeValue+" exit staus value");
		return tradeValue;
	}
	
	
	// method to set the move rating if it captures or is captured by some other piece
	public void setMoveRating(bestMoves Piece){
		
		hittingPiece(Piece); // Check for hitting piece
		Piece.moveRating += tradeValue(Piece);	
	}
	
	
	// method to return the value of the piece
	
	public int getAttackDirection(int targetCol, int targetRow, ChessPieces P) {
		if(targetCol - P.col == 0) {
			if(targetRow - P.row < 0)
				return LEFT;
			else
				return RIGHT;
		}else if(targetRow - P.row == 0) {
			if(targetCol - P.col < 0)
				return  UP;
			else
				return DOWN;
		}else if(targetRow - P.row  > 0) {
			if(targetCol - P.col > 0)
				return  DOWNRIGHT;
			else
				return UPRIGHT;
		}else if(targetRow - P.row  < 0) {
			if(targetCol - P.col > 0)
				return  DOWNLEFT;
			else
				return UPLEFT;
		}
		
		return 0;
	}
	public int getPieceValue(ChessPieces Piece) {
		int pieceValue;
		
		if (Piece instanceof Pawn) {
	        pieceValue = 1;
	    } else if (Piece instanceof Bishop || Piece instanceof Knight) {
	        pieceValue = 3;
	    } else if (Piece instanceof Rook) {
	        pieceValue = 5;
	    } else if (Piece instanceof  Queen) {
	        pieceValue = 9;
	    } else
	    	pieceValue = 100;		
	
		return pieceValue;
	}

}
