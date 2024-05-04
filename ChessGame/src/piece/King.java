package piece;
import main.ChessPieces;
import main.PanelGame;
import main.Type;

public class King extends ChessPieces{

    public King(int color, int row, int col){
        super(color, row, col);
        
     // Assigning ID
        type = Type.KING;
        
        
        if(color == PanelGame.WHITE){
            image = getImage("/piece/w-king");
        }else
            image = getImage("/piece/b-king");
    }
    
    
//    public boolean ownSquare(int targetCol, int targetRow) {
//    	if (targetRow == prerow && targetCol == precol) {
//    		return true;
//    	}
//    	else 
//    		return false;
//    }
    
    public boolean canMove(int targetCol, int targetRow) {
    	
    	if(withInBoard(targetCol, targetRow)){
    		
    		if((Math.abs(targetRow - prerow) + (Math.abs(targetCol - precol)) == 1) ||
    				(Math.abs(targetRow - prerow) * (Math.abs(targetCol - precol)) == 1)){
    			if (isValidSquare(targetCol, targetRow))
    					return true;
    		}
    	
    	// Castling
    		if (!moved) {
    			// Right Castling
    			if(targetCol == precol + 2 && targetRow == prerow && !pieceOnStraightLine(targetCol, targetRow)) {
    				for(ChessPieces P: PanelGame.simpieces) {
    					if(P.col == precol + 3 && P.row == prerow && P.moved == false) {
    						PanelGame.castlingPiece = P;
    						return true;
    					}
    				}
    			}
    		
    		// left castling
    			if(targetCol == precol - 2 && targetRow == prerow && !pieceOnStraightLine(targetCol, targetRow)) {
    				ChessPieces P[] = new ChessPieces[2];
    				for(ChessPieces piece: PanelGame.simpieces) {
    					if(piece.col == precol - 3 && piece.row == targetRow) {
    						P[0] = piece;
    						
    					}
    					if(piece.col == precol - 4 && piece.row == targetRow) {
    						P[1] = piece;
    						
    					}
    					if(P[0] == null && P[1] != null && P[1].moved == false) {
    						PanelGame.castlingPiece = P[1];
    						return true;
    					}
    				}
    			}

    		}
    	}
    	return false;
    }
    
    
public boolean canMove(int targetCol, int targetRow, int a) {
    	
    	if(withInBoard(targetCol, targetRow)){
    		
    		if((Math.abs(targetRow - prerow) + (Math.abs(targetCol - precol)) == 1) ||
    				(Math.abs(targetRow - prerow) * (Math.abs(targetCol - precol)) == 1)){
    					return true;
    		}
    	}
    	return false;
    }
}
    
    
    

