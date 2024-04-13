package piece;
import main.ChessPieces;
import main.PanelGame;
import main.Type;

public class Queen extends ChessPieces{
    
	
	
    public Queen(int color, int row, int col){
        super(color, row, col);
        
        // Assigning ID
        type = Type.QUEEN;
        
        if(color == PanelGame.WHITE){
            image = getImage("/piece/w-queen");
        }else
            image = getImage("/piece/b-queen");
    }
    
    public boolean canMove(int targetCol, int targetRow) {
    	if(withInBoard(targetCol, targetRow) && !ownSquare(targetCol, targetRow)) {
    		if(targetCol == precol || targetRow == prerow)
    			if(isValidSquare(targetCol, targetRow) && !pieceOnStraightLine(targetCol, targetRow))
    				return true;
    		if(Math.abs(targetCol - precol) == Math.abs(targetRow - prerow)) {
    			if(isValidSquare(targetCol, targetRow) && !pieceOnDiognal(targetCol, targetRow))
    			return true;
    		}
    	}
    	return false;
    }
}
