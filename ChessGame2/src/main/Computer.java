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

public class Computer {

	public int targetCol, targetRow; // the target row and column where the computer piece can move
	public ArrayList<ChessPieces> checkedPieces = new ArrayList<>(); // array to store pieces that have been checked for
																		// valid move

	int PAWN = 1, KNIGHT = 3, BISHOP = 3, ROOK = 5, QUEEN = 9, KING = 1000;
	final int UP = 1, DOWN = 2, RIGHT = 3, LEFT = 4, UPRIGHT = 5, UPLEFT = 6, DOWNRIGHT = 7, DOWNLEFT = 8;

	int bestCol, bestRow;
	double moveRating = 1;
	int kingCol;
	int kingRow;

	ChessPieces hitPiece;
	ChessPieces checkingP;
	int validMovesCount = 0;
	double maxMoveRating = Double.MIN_VALUE;
	int maxCol = -1, maxRow = -1;

	boolean isValid = false;

	ArrayList<bestMoves> validMoves = new ArrayList<>(); // this will store the best moves of each piece available
	ArrayList<bestMoves> checkMoves = new ArrayList<>();
	ArrayList<bestMoves>  history = new ArrayList<>();
	ChessPieces cActiveP; // Computers Active Piece which will be moved

	public void choosePiece() {
		// Choosing a piece
		cActiveP = null;
		for (ChessPieces P : PanelGame.simpieces) {
			isValid = false;
			if (P.color == PanelGame.compColor && !checkedPieces.contains(P)) {
				// when piece found setting it as active
				cActiveP = P;
			}
			// call appropriate methods to check for moves

			maxMoveRating = Double.MIN_VALUE;
			validMovesCount = 0;
			if (cActiveP instanceof Pawn) {
				cPawn();
			} else if (cActiveP instanceof King) {
				cKing();
			} else if (cActiveP instanceof Bishop) {
				cBishop();
			} else if (cActiveP instanceof Knight) {
				cKnight();
			} else if (cActiveP instanceof Queen) {
				cQueen();
			} else if (cActiveP instanceof Rook) {
				cRook();
			}
		}

		makeMove();

	}

	public ChessPieces Cpiece() {
		checkedPieces.clear();
		System.out.println("Chosen Piece "+ cActiveP + " At Col: "+ cActiveP.col + " Row: "+cActiveP.row);
		PanelGame.copyArrayListComp(PanelGame.pieces, PanelGame.simpieces);

		Pause();
		return cActiveP;
	}

	
	
	public boolean staleMate() {
		if (validMoves.size() == 0 && checkMoves.size() == 0)
			return true;
		return false;
	}

	public boolean checkMate() {
		if (checkKingInCheck() && staleMate())
			return true;
		return false;
	}
	
	public void PawnPromotion(ChessPieces P){
		if(P instanceof Pawn) {
			if(PanelGame.compColor  == PanelGame.WHITE && P.row == 0 ||
					(PanelGame.compColor == PanelGame.BLACK && P.row == 7)) {
				
				PanelGame.simpieces.remove(P.getIndex());

				PanelGame.simpieces.add(new Queen(PanelGame.compColor, P.row, P.col));
				
				PanelGame.copyArrayListComp(PanelGame.pieces, PanelGame.simpieces);
				isValid = true;
				
			}
		}
	}
	public void saveKing() {
		// Iterate through all pieces of the defending color
		boolean checkKingDanger = false;
		
		boolean checkCapture = false, checkBlock = false;

		for (ChessPieces P : PanelGame.simpieces) {
			if (P.color == PanelGame.compColor) {
				if (P.canMove(checkingP.col, checkingP.row)) {// checking if a piece can capture the checking piece
					if (P instanceof King) {
						for (ChessPieces Pk : PanelGame.simpieces) {
							if (Pk.color != P.color && !Pk.canMove(checkingP.col, checkingP.row, true)) 
								checkKingDanger = true;
						}
						if(!checkKingDanger){
							bestMoves saveKingP = new bestMoves(P.getIndex(), checkingP.col, checkingP.row, P.col,
									P.row, 0, P);

							saveKingP.moveRating = tradeValue(saveKingP);

							checkMoves.add(saveKingP);
							checkCapture = true;
						}
					}else {
						
						bestMoves saveKingP = new bestMoves(P.getIndex(), checkingP.col, checkingP.row, P.col, P.row, 0, P);

						saveKingP.moveRating = tradeValue(saveKingP);

						checkMoves.add(saveKingP);
						checkCapture = true; // if the checking piece can be captured
					
					}
					
				}

			}
		}
		if(!checkCapture) {
			
		for (ChessPieces P2 : PanelGame.simpieces) {
			if (P2.color == PanelGame.compColor) {
				// Check if the piece can block the check
				// a knight's check can't be blocked
				if (!(checkingP instanceof Knight) && !(P2 instanceof King)) {
					// Determine the direction of the check
					int attackDir = getAttackDirection(kingCol, kingRow, checkingP);

					int dCol = 0, dRow =0;
					switch(attackDir) {
					case UP:  dCol = 0; dRow = -1;    break;
					case DOWN:  dCol = 0; dRow = 1;    break;
					case LEFT:  dCol = -1; dRow = 0;    break;
					case RIGHT:  dCol = 1; dRow = 0;    break;
					case UPRIGHT:  dCol = 1; dRow = -1;    break;
					case UPLEFT:  dCol = -1; dRow = -1;    break;
					case DOWNRIGHT:  dCol = -1; dRow = -1;    break;
					case DOWNLEFT:  dCol = 1; dRow = -1;    break;
					}
					
					int col = checkingP.col + dCol;
					int row = checkingP.row + dRow;

					while(!(col == kingCol && row == kingRow)) {
						if(P2.canMove(col, row)) {
						// Add blocking move to the list of moves
						bestMoves saveKingP = new bestMoves(P2.getIndex(), col, row, P2.col, P2.row, 0, P2);

						saveKingP.moveRating = tradeValue(saveKingP);
						checkMoves.add(saveKingP);
						checkBlock = true;
						}
						col += dCol;
						row += dRow;
					}
				}
			}
		}
	}	
		if(!checkBlock && !checkCapture) {
		for (ChessPieces P3 : PanelGame.simpieces) {
			if (P3.color == PanelGame.compColor) {
				if (P3 instanceof King) {
					int direction[][] = { { 1, 1 }, { 0, 1 }, { -1, 1 }, { 1, -1 }, { -1, -1 }, { 0, -1 }, { -1, 0 },
							{ 1, 0 } };
					for (int dir[] : direction) {
						boolean danger = false;
						int col = P3.col + dir[0];
						int row = P3.row + dir[1];
						if (P3.canMove(col, row)) {
							for (ChessPieces pi : PanelGame.simpieces) {
								if (pi.color != PanelGame.compColor && pi.canMove(col, row, true)) {
									System.out.println("PI: "+pi+ " "+ pi.col+" "+pi.row);
									danger = true;
									break;
								}
							}
							if(!danger) {
								bestMoves saveKingP = new bestMoves(P3.getIndex(), col, row, P3.col, P3.row, 0, P3);
								saveKingP.moveRating = tradeValue(saveKingP);
								checkMoves.add(saveKingP);
							}
						}
					}
				}
			}
		}
	}
		decideTheMove(checkMoves);
	}

	private void decideTheMove(ArrayList<bestMoves> moves) {

		history.clear();
		if (staleMate() || checkMate()) {
			PanelGame.compLose = true;
		}

		else {
			bestMoves bestMove = null;

			bestMove = moves.get(0); // setting the first object in valid Move array as best move piece
			System.out.println("\n+++++++++++++++++++++++++ All Possible Moves +++++++++++++++++++++ ");
			for (int i = 0; i < moves.size() - 1; i++) {
				System.out.println("PIECE : "+ moves.get(i).piece+ "\nFrom: Col" +moves.get(i).activeCol+" ROW: "+
						moves.get(i).activeRow +"\nTo:  Col" +moves.get(i).bestCol+" ROW: "+ moves.get(i).bestRow +
						" RATE: "+ moves.get(i).moveRating+"\n");
				if (bestMove.moveRating > moves.get(i + 1).moveRating) {
					continue;
				} else
					bestMove = moves.get(i + 1);

			}
			// locating the choosen piece on the board and setting it as Active
			for (ChessPieces p : PanelGame.simpieces) {
				if (p == bestMove.piece && p.getIndex() == bestMove.index
						&& p.canMove(bestMove.bestCol, bestMove.bestRow)
						&& PanelGame.simpieces.contains(bestMove.piece)) {
					cActiveP = bestMove.piece;

					targetCol = bestMove.bestCol;
					targetRow = bestMove.bestRow;
										
					cActiveP.col = bestMove.bestCol;
					cActiveP.row = bestMove.bestRow;
					
					history.add(bestMove);
					
					System.out.println("Highest Move Rating: "+bestMove.moveRating);
					
					
				}
			}
			PawnPromotion(cActiveP);

			reset(); // reset method called to reset the bestMove after a move is played

		}

	}

	public void makeMove() {
		if (checkKingInCheck()) {

			// clearing the validmoves because when the king is in check no move should be played
			validMoves.clear();
			// go to saveKing when king in check
			saveKing();
		} else if (isValid && !checkKingInCheck()) { // make a move only if there are valid moves

			// if the king is not in check then pass the validmoves list to decideMOve
			decideTheMove(validMoves);

		}
		if (!isValid ) { // if there are no valid moves add the piece to
			// checked so that next time before an move is
			checkedPieces.add(cActiveP);
			choosePiece(); // call the choose piece again to check another piece for valid moves
		}
	}

	// method to check the moves of pawn and add the highest rated move to the array
	public void cPawn() {

		int step = 0;
		if(PanelGame.compColor == 0){
			step = 1;
		}else
			step = -1;
		targetCol = cActiveP.col;
		targetRow = cActiveP.row + step;

		// setting condition to priotize the center pawns to capture the center
		if (cActiveP.canMove(targetCol, targetRow)) {
			validMovesCount++;

			if (cActiveP.col >= 0 && cActiveP.col < 3 || cActiveP.col > 3 && cActiveP.col <= 7)
				moveRating = 0.5;
			else
				moveRating = 0.75;

			setMax(moveRating, targetCol, targetRow);

		}

		if (cActiveP.canMove(targetCol, targetRow + step)) {
			validMovesCount++;
			if (cActiveP.col >= 0 && cActiveP.col < 3 || cActiveP.col > 3 && cActiveP.col <= 7)
				moveRating = 0.5;
			else
				moveRating = 1.25;

			setMax(moveRating, targetCol, targetRow + step);

		}

		if (cActiveP.canMove(targetCol + step, targetRow)) {
			validMovesCount++;

			moveRating = 1;

			setMax(moveRating, targetCol + step, targetRow);
		}

		if (cActiveP.canMove(targetCol - step, targetRow)) {
			validMovesCount++;

			moveRating = 1;

			setMax(moveRating, targetCol - step, targetRow);

		}

		if (validMovesCount > 0) {
			bestMoves p = new bestMoves(cActiveP.getIndex(), maxCol, maxRow, cActiveP.col, cActiveP.row, maxMoveRating,
					cActiveP);
			isValid = true;
			setMoveRating(p);
			validMoves.add(p);

		}
		moveRating = 1;
	}

	public void cKing() {

		// formula derived: row = +0 and column +-1; column = +0 and row +-1, row and column both +-1
		int directions[][] = { {0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 }, { 1, 1 }, { -1, -1 }, { 1, -1 }, { -1, 1 } };

		for (int dir[] : directions) {

			int col = cActiveP.col + dir[0];
			int row = cActiveP.row + dir[1];

			if (cActiveP.canMove(col, row)) {

				validMovesCount++;

				moveRating = -2; // moving king without any trade value is not a recommended move in chess

				isValid = true;
				bestMoves p = new bestMoves(cActiveP.getIndex(), col, row, cActiveP.col, cActiveP.row, maxMoveRating, cActiveP);

				setMoveRating(p);

				setMax(moveRating, col, row);
				validMoves.add(p);
			}

		}
		moveRating = 1;
	}

	public void cQueen() {

		// col same row++, row same col++, col and row -1,-1,and +1,+1 and -1, +1 and +1, -1;
		int[][] directions = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 }, { -1, -1 }, { 1, 1 }, { -1, 1 }, { 1, -1 } };

		for (int[] dir : directions) {

			int colDelta = dir[0];
			int rowDelta = dir[1];

			int col = cActiveP.col + colDelta;
			int row = cActiveP.row + rowDelta;

			while (cActiveP.canMove(col, row)) {

				validMovesCount++;

				isValid = true;

				bestMoves p = new bestMoves(cActiveP.getIndex(), col, row, cActiveP.col, cActiveP.row, moveRating,
						cActiveP);

				setMoveRating(p);

				validMoves.add(p);

				col += colDelta;
				row += rowDelta;
			}

		}

	}

	public void cBishop() {

		// Define deltas for movement directions
		int[][] directions = { { 1, 1 }, { -1, -1 }, { 1, -1 }, { -1, 1 } };

		for (int[] dir : directions) {
			int colDelta = dir[0];
			int rowDelta = dir[1];

			int col = cActiveP.col + colDelta;
			int row = cActiveP.row + rowDelta;

			while (cActiveP.canMove(col, row)) {
				validMovesCount++;

				isValid = true;

				maxCol = col;
				maxRow = row;

				setMax(moveRating, col, row);
				bestMoves p = new bestMoves(cActiveP.getIndex(), maxCol, maxRow, cActiveP.col, cActiveP.row,
						maxMoveRating, cActiveP);

				setMoveRating(p);

				validMoves.add(p);

				col += colDelta;
				row += rowDelta;
			}
		}
	}

	public void cRook() {

		int directions[][] = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };

		for (int[] dir : directions) {

			int colDelta = dir[0];
			int rowDelta = dir[1];

			int col = cActiveP.col + colDelta;
			int row = cActiveP.row + rowDelta;

			while (cActiveP.canMove(col, row)) {

				validMovesCount++;

				maxCol = col;
				maxRow = row;

				setMax(moveRating, col, row);

				bestMoves p = new bestMoves(cActiveP.getIndex(), maxCol, maxRow, cActiveP.col, cActiveP.row,
						maxMoveRating, cActiveP);

				setMoveRating(p);

				validMoves.add(p);

				isValid = true;

				col += colDelta;
				row += rowDelta;

			}
		}
	}

	public void cKnight() {

		int[][] directions = { { 1, -2 }, { 1, 2 }, { -1, -2 }, { -1, 2 }, { 2, 1 }, { 2, -1 }, { -2, 1 }, { -2, -1 } };

		for (int dir[] : directions) {

			int col = cActiveP.col + dir[0];
			int row = cActiveP.row + dir[1];
			while (cActiveP.canMove(col, row)) {

				validMovesCount++;

				maxCol = col;
				maxRow = row;

				setMax(moveRating, maxCol, maxRow);
				// object of type best move is stored and added to valid moves
				bestMoves p = new bestMoves(cActiveP.getIndex(), maxCol, maxRow, cActiveP.col, cActiveP.row,
						maxMoveRating, cActiveP);

				setMoveRating(p);

				// isValid is also set to true to make a move
				isValid = true;

				validMoves.add(p);
				col++;
				row++;
			}
		}
	}

	// method to set the max move rating
	// this method is called after every valid move is found with it's move rating
	public void setMax(double moveRating, int maxCol, int maxRow) {

		// if the received moverating is greater than the previously set move Rating
		// then it is set max
		if (moveRating > this.maxMoveRating) {

			this.maxMoveRating = moveRating;
			this.maxCol = maxCol; // the col and row for the move is also saved as max col and row
			this.maxRow = maxRow;

		}
	}

	// function to clear all the implementations and objects created after a move
	// because the positions and standings all change after a move
	// so they must re-calculated according to the new positions
	public void reset() {
		validMoves.clear();
		checkMoves.clear();
		checkedPieces.clear();
	}

	public boolean checkKingInCheck() { // method to check whether a king is in check or not

		// first the row and col of the king must be found
		setKingCoordinates();

		for (ChessPieces p : PanelGame.simpieces) {

			// we check if an opponent piece can move to the king's col and row
			// this is the condition for check
			if (p.color != PanelGame.compColor && p.canMove(kingCol, kingRow)) {
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

		for (ChessPieces p : PanelGame.simpieces) {
			if (p.color == PanelGame.compColor && p instanceof King) { // matching the color and checking if it is a
																		// king
				kingCol = p.col;
				kingRow = p.row;
			}
		}
	}

	// method to find the hitting piece, the piece which the computer is able to
	// capture
	public boolean hittingPiece(bestMoves Bpiece) {

		// hitting piece is found so that the trade value can be found
		// capturing all the available pieces is not a good strategy
		// thus the trade value needs to be positive

		for (ChessPieces P : PanelGame.simpieces) {
			if ( P.color != Bpiece.piece.color && P.col == Bpiece.bestCol && P.row == Bpiece.bestRow) {
					if(!(P instanceof King)) {
				hitPiece = P; // hit Piece is the being captured by the computer

				return true;
				}
					if(P instanceof King) {
						Bpiece.moveRating = 1.0;
						hitPiece = P;
						return true;
					}
			}
			
		}

		return false;
	}

	// returns a double value, which is the difference between the computer piece
	// value and opponent piece value
	private boolean kingCheckAfter(bestMoves Piece) {
		
		for (ChessPieces P : PanelGame.simpieces) {
			if (P.color == PanelGame.compColor && P == Piece.piece) { // check if the piece is of computer and is the																		// piece for which shield should be determined
				for (ChessPieces P2 : PanelGame.simpieces) {
					// Find if any opponent piece can capture the activeP of comp
					if (P2.color != PanelGame.compColor && P2.canMove(Piece.activeCol, Piece.activeRow)) { 						
						for (ChessPieces P3 : PanelGame.simpieces) {
							// Find if the opponent piece can capture any other except activeP
							if ((P3.color == PanelGame.compColor) && (P3 instanceof King)
									&& (P2.canMove(P3.col, P3.row, true)) && hitPiece != P2) {

								if (getAttackDirection(P3.col, P3.row, P2) == getAttackDirection(Piece.activeCol,
										Piece.activeRow, P2)) { // if both are in same attack direction
									return true; 
								}

							}
						}

					}
				}
			}
		}
		return false;
	}
	
	
	
	public double tradeValue(bestMoves Piece) {
		
		System.out.println("");// to create a new line

		boolean selfCaptured = false; // check if computers piece is being captured
		boolean underAttack = false; // check if computers piece is under threat
		boolean capturePlayerPiece = false; // check is can capture players piece
		
		double compPieceValue = getPieceValue(Piece.piece);
		double oppPieceValue = getPieceValue(hitPiece);
		double tradeValue = 0;

		// when checking if a piece can move to that position after we capture a piece
		// we must check for all pieces
		// except the piece we assume to be captured
		if(!kingCheckAfter(Piece)) {
		
		if (hittingPiece(Piece)) {
			tradeValue += oppPieceValue;
			capturePlayerPiece = true;
		}

		// receives an object from the class type bestmoves
		// gets the piece from the class and finds the difference

		// in trade value tow things must be checked :
		// 1. if the piece captures an opponent piece and an opponent piece can capture
		// that piece
		// trade value of that must be also considered

		// 2. if the piece moves on to capture an opponent piece and was defending one
		// of its important piece than
		// that trade value must be also considered

		// case 1
		System.out.println("Checking " + Piece.piece + " At: " + Piece.activeCol + " " + Piece.activeRow);
		System.out.println("To: "+ Piece.bestCol +" "+ Piece.bestRow);

		for (ChessPieces P : PanelGame.simpieces) {

			if (P.color != PanelGame.compColor) {

				if ((P instanceof Pawn)) {
					if (((Pawn) P).canMove(Piece.bestCol, Piece.bestRow, 1)) {
						System.out.println("Pawn Can Capture !");
						tradeValue -= compPieceValue;
						selfCaptured = true;
						System.out.println("Trade Value: "+tradeValue);
					}
				} else if (!(P instanceof Pawn) && P.canMove(Piece.bestCol, Piece.bestRow, 1)) {
					System.out.println( P+ " Can Capture !");
					tradeValue -= compPieceValue;
					selfCaptured = true;
					System.out.println("Trade Value: "+tradeValue);
				}

			}
		}
		// Case 2
				for (ChessPieces P2 : PanelGame.simpieces) {

					// Find if any opponent piece can capture the activeP of comp
					if (P2.color != PanelGame.compColor && P2.canMove(Piece.activeCol, Piece.activeRow)) { 
						for (ChessPieces P3 : PanelGame.simpieces) {
							// Find if the opponent piece can capture any other except activeP
							if ((P3.color == PanelGame.compColor) && (P3 != Piece.piece)
									&& (P2.canMove(P3.col, P3.row, true))) {
								if (getAttackDirection(P3.col, P3.row, P2) == getAttackDirection(Piece.activeCol,
										Piece.activeRow, P2)) { // if both are in same attack direction
								System.out.println(P3 + " is in Shield !");
									tradeValue -= getPieceValue(P3);
									System.out.println("Trade Value: "+tradeValue);
									break;
								}

							}
						}

					}
				}
		// check if a piece is under attack.. then the piece's move rating is increased
		// by its piece value so that the move is favored to save the piece if only the
		// target square is safe
		for (ChessPieces pi : PanelGame.simpieces) {
			if (pi.color != PanelGame.compColor) {
				if (pi.canMove(Piece.activeCol, Piece.activeRow)) {
					for (ChessPieces pi2 : PanelGame.simpieces) {
						if (pi2.color != PanelGame.compColor) {
							if (!pi2.canMove(Piece.bestCol, Piece.bestRow,true)) {
								System.out.println("Self Under Attack: ");
								tradeValue += getPieceValue(Piece.piece);
								underAttack = true;
								System.out.println("Trade Value: "+tradeValue);
								break;
							}
						}
					}
				}
			}
		}
		
		if(canCheckKing(Piece.piece,Piece.bestCol, Piece.bestRow) && !selfCaptured &&
				!underAttack && !capturePlayerPiece && !anyPieceInThreat()){

			System.out.println("Can Check King !");
			tradeValue += 5;
			System.out.println("Trade Value: "+tradeValue);
		}
		try {
		// check for repeating moves
		if(history.get(0).piece == Piece.piece && !underAttack && !capturePlayerPiece) {
			System.out.println("Repeating Move: ");
			tradeValue -= 3;
			System.out.println("Trade Value: "+tradeValue);
		}
		}catch(Exception e) {}

		return tradeValue;
		}else
			return -1000;
	}

	// method to set the move rating if it captures or is captured by some other
	// piece
	public void setMoveRating(bestMoves Piece) {

		hittingPiece(Piece); // Check for hitting piece
		Piece.moveRating += tradeValue(Piece);
	}

	// method to return the value of the piece

	public int getAttackDirection(int targetCol, int targetRow, ChessPieces P) {
		
		// ChessPieces P is the piece that is making an attack
		// target row and col is the position where the attack is being made
		// which is the row and col of the piece being attacked 
		
		if (targetCol - P.col == 0) {
			if (targetRow - P.row < 0)
				return UP;
			else
				return DOWN;
		} else if (targetRow - P.row == 0) {
			if (targetCol - P.col < 0)
				return LEFT ;
			else
				return RIGHT;
		} else if (targetRow - P.row > 0) {
			if (targetCol - P.col > 0)
				return DOWNRIGHT;
			else
				return UPRIGHT;
		} else if (targetRow - P.row < 0) {
			if (targetCol - P.col > 0)
				return DOWNLEFT;
			else
				return UPLEFT;
		}

		return 0;
	}

	
	// method to get the players king
	private ChessPieces getPlayerKing() {
	
		// getting the player's king can help in determining if the comp piece is in a position to make a check
	
		for(ChessPieces P: PanelGame.simpieces) {
			
			if(P.color != PanelGame.compColor && (P instanceof King)) {
				return P;
			}
		}
		
		return null;
	}
	
	private boolean canCheckKing(ChessPieces piece, int targetCol, int targetRow) {
		
		// this method will be used to make the piece make a move closer to the king to make a check

		int tempRow = piece.prerow;
		int tempCol = piece.precol;
		// to check for if the piece can check a king we need look for the piece's attacking line if
		// the king comes under the attacking line of the piece after making the move then
		// canCheckKing must return true
		
		// we will update the position of the piece temporarily and then reset after verifying check
		ChessPieces playerKing =  getPlayerKing();
		if(playerKing != null) {
			piece.precol = targetCol;
			piece.prerow = targetRow;
			if(piece.canMove(playerKing.col, playerKing.row) && !(piece instanceof  Pawn)) {
				piece.precol = tempCol; // resetting the col and row of the piece
				piece.prerow = tempRow;
				return true;
			}
				
		}
		piece.precol = tempCol; // resetting the col and row before returning
		piece.prerow = tempRow;
		return false;
	}
	
	
	// method to return the piece value 
	public int getPieceValue(ChessPieces Piece) {
		// the value of piece hold high importance in out algorithm because all the moves are determined by it
		// the algorithm encourages positive tradeValue moves which are determined using piece value
		
		int pieceValue = 0;

		if (Piece instanceof Pawn) {
			pieceValue = 1;
		} else if (Piece instanceof Bishop || Piece instanceof Knight) {
			pieceValue = 3;
		} else if (Piece instanceof Rook) {
			pieceValue = 5;
		} else if (Piece instanceof Queen) {
			pieceValue = 9;
		} else if(Piece instanceof King)
			pieceValue = 100;

		return pieceValue;
	}

	private boolean anyPieceInThreat() {
		// Check if any piece is under threat and if the piece has any legal moves to make return true
		// if the piece doesn't have any legal moves then the piece is trapped and if it is under threat then surely its gonna be captured
		for(ChessPieces P: PanelGame.simpieces) {
			if(P.color != PanelGame.compColor) {
				for(ChessPieces PP: PanelGame.simpieces) {
					if(PP.color == PanelGame.compColor) {
						if(P.canMove(PP.col, PP.row))
							for(bestMoves B: validMoves) 
								if(B.piece == PP)
									return true;
					}

				}
			}
		}
		return false;
	}
	
	public void Pause(){
		try {
			Thread.sleep(500); // Pause for 1 second
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
