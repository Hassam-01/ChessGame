package piece;
import main.ChessPieces;
import main.PanelGame;
import main.Type;


public class Pawn extends ChessPieces{
    public Pawn(int color, int row, int col){
        super(color, row, col);
        
     // Assigning ID
        type = Type.PAWN;
        
        if (color == PanelGame.WHITE){
            image = getImage("/piece/w-pawn");
        }else{
            image = getImage("/piece/b-pawn");            
        }
    }
    public boolean canMove(int targetCol, int targetRow) {
    	if(withInBoard(targetCol, targetRow) && !ownSquare(targetCol, targetRow)) {
    		
    		int moveValue = 0;
    		
    		if(color == PanelGame.WHITE)
    			moveValue = -1;
    		if(color == PanelGame.BLACK)
    			moveValue = 1;
    		
    		
    		hittingPiece = detectPiece(targetCol, targetRow);
    		
    		// single step movement of pawn
    		if(targetRow == prerow + moveValue && targetCol == precol && hittingPiece == null)
    			return true;
    		
    		
    		// 2 step movement of pawn
    		if(targetCol == precol && targetRow == prerow + moveValue*2 && hittingPiece == null && !moved
    				&& !pieceOnStraightLine(targetCol, targetRow))
    			return true;
    		
    		// capturing and diagonal movement of pawn
    		if(Math.abs(targetCol-precol) == 1 && targetRow == prerow + moveValue && hittingPiece != null && hittingPiece.color != this.color)
    			return true;
    	
    		// En passant
    		if(Math.abs(targetCol-precol) == 1 && targetRow == prerow + moveValue ) {
    			for(ChessPieces P: PanelGame.simpieces) {
    				if(P.col == targetCol && P.row == prerow && P.twostepped == true) {
    					hittingPiece = P;
    					return true;
    				}
    			}
    			
    		}
    	}
    	
    	return false;
    }
    
}
