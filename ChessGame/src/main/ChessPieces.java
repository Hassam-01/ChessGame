package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ChessPieces {
    
    public BufferedImage image; // ! Buffered image is used to import images to be used in a java project
    public int x,y;
    public int row, col, precol, prerow;
    public int color;
    public boolean twostepped;
    
    // Enum, ID for each type
    public Type type;
    
    public boolean moved;
    public ChessPieces hittingPiece;

    
    // Constructor
    public ChessPieces(int color, int row, int col){
        this.color = color ;
        this.row = row;
        this.col = col;

        x = getX(col);
        y = getY(row);
        
        precol = col;
        prerow = row;
    }

    public int getX(int col){
        return col*Board.SQUARE_SIZE;
    }
    public int getY(int row){
        return row*Board.SQUARE_SIZE;
    }
    
    public int getRow(int y){
    	return (y+Board.HALF_SQUARE_SIZE)/Board.SQUARE_SIZE;
    	
    }

    public int getCol(int x){
    	return (x+Board.HALF_SQUARE_SIZE)/Board.SQUARE_SIZE;
    	
    }
    
    public int getIndex(){
    	for(int index = 0; index < PanelGame.simpieces.size(); index++){
    		if(PanelGame.simpieces.get(index) == this) {
    			return index;
    		} 
    	}return 0;
    }
    
    //  * used to check if the piece is picked and dropped at the same location
    // help to draw the red or white square
    public boolean ownSquare(int targetCol, int targetRow) {
    	if (targetRow == prerow && targetCol == precol) {
    		return true;
    	}
    	else 
    		return false;
    }
    public boolean canMove(int tragetCol, int targetRow) {
    	return false;
    }
    
    public void resetPosition() {
    	col = precol;
    	row = prerow;
    	
    	x = getX(col);
    	y = getY(row);
    	
    }
    public boolean withInBoard(int tragetCol, int targetRow) {
    	if(tragetCol >= 0 && tragetCol <= 7 && targetRow >= 0 && targetRow <= 7) {
    		return true;
    	}
    else
    	return false;
    }
    
    public void updatePosition(){
    	
    	
    	// En passant check
    	if(type == Type.PAWN) {
    		if(Math.abs(row - prerow) == 2) {
    			twostepped = true;
    		}
    	}
    	x = getX(col);
    	y= getY(row);
    	moved = true; 
    	
    	precol = getCol(x);
    	prerow = getRow(y);
    	
    }

    public BufferedImage getImage(String imagePath){
    BufferedImage image = null;
    
    try{
        image = ImageIO.read(getClass().getResourceAsStream(imagePath+".png"));
    }catch(IOException e){
        e.printStackTrace();
    }
        return image;
    }
    
    public void draw(Graphics2D g2){
        
        // ! drawing the image of pieces with height and widht of square size
        g2.drawImage(image, x,y,Board.SQUARE_SIZE, Board.SQUARE_SIZE, null);
    }
    
    
    // Detect if the square is occupied by anypiece the activePiece is trying to move
    public ChessPieces detectPiece(int targetCol, int targetRow) {
    	for(ChessPieces P: PanelGame.simpieces) {
    		if (P.col == targetCol && P.row == targetRow && P != this) {
    			return P;
    		}
    		
    	}
    	return null;
    }
    
    public boolean isValidSquare(int targetCol, int targetRow) {
    	hittingPiece = detectPiece(targetCol, targetRow);
    	
    	if(hittingPiece == null) {
    		return true;
    	}else {
    		if (hittingPiece.color != this.color) {
    			return true;
    		}else {
    			hittingPiece = null;
    		}	
    	}
    	return false;
    }
    
    public boolean pieceOnStraightLine(int targetCol, int targetRow) {
    	
    	// When the piece move to the right
    	for(int c = precol + 1; c < targetCol; c++) {
    		for(ChessPieces P: PanelGame.simpieces) {
    			if(P.col == c  && P.row == targetRow) {
    				hittingPiece = P;
    				return true;
    			}
    		}
    	}
    	
    	// Move to the left
    	for(int c = precol - 1; c > targetCol; c--) {
    		for(ChessPieces P: PanelGame.simpieces) {
    			if(P.col == c  && P.row == targetRow) {
    				hittingPiece = P;
    				return true;
    			}
    		}
    	}
    	
    	// move up
    	for(int c = prerow - 1; c > targetRow; c--) {
    		for(ChessPieces P: PanelGame.simpieces) {
    			if(P.col == targetCol  && P.row == c) {
    				hittingPiece = P;
    				return true;
    			}
    		}
    	}
    	
    	// move down
    	
    	for(int c = prerow + 1; c < targetRow; c++) {
    		for(ChessPieces P: PanelGame.simpieces) {
    			if(P.col == targetCol  && P.row == c) {
    				hittingPiece = P;
    				return true;
    			}
    		}
    	}
    	
    	return false;
    }
    
    public boolean pieceOnDiognal(int targetCol, int targetRow) {
    	
    	
    	if(targetRow < prerow) {
  
    		//  up left
    		for(int c = prerow - 1; c > targetCol; c--) {
    			int diff = Math.abs(c - precol);
    			for(ChessPieces P: PanelGame.simpieces) {
    				if(P.col == c && P.row == prerow - diff) {
    					hittingPiece = P;
    					return true;
    				}
    			}
    		}
    	
    		// up right
    		for(int c = prerow + 1; c < targetCol; c++) {
    			int diff = Math.abs(c - precol);
    			for(ChessPieces P: PanelGame.simpieces) {
    				if(P.col == c && P.row == prerow - diff) {
    					hittingPiece = P;
    					return true;
    				}
    			}
    		}
    	}
    	
    	
    	if(targetRow > prerow) {
    	
    		// down left
    		if(targetRow < prerow) {
        		for(int c = prerow - 1; c > targetCol; c--) {
        			int diff = Math.abs(c - precol);
        			for(ChessPieces P: PanelGame.simpieces) {
        				if(P.col == c && P.row == prerow + diff) {
        					hittingPiece = P;
        					return true;
        				}
        			}
        		}
    		}
    		// down right

        		if(targetRow < prerow) {
            		for(int c = prerow + 1; c < targetCol; c++) {
            			int diff = Math.abs(c - precol);
            			for(ChessPieces P: PanelGame.simpieces) {
            				if(P.col == c && P.row == prerow + diff) {
            					hittingPiece = P;
            					return true;
            				}
            			}
            		}
    	
    	}
    		}
    	return false;
    }
}
    


