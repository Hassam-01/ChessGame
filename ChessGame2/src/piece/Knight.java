package piece;
import main.ChessPieces;
import main.PanelGame;
import main.Type;

public class Knight extends ChessPieces{
    
    public Knight(int color, int row, int col){
        super(color, row, col);
        
     // Assigning ID
        type = Type.KNIGHT;
        
        
        if(color == PanelGame.WHITE){
            image = getImage("/piece/w-knight");
        }else
            image = getImage("/piece/b-knight");
    }
    public boolean canMove(int targetCol, int targetRow){
    	if(withInBoard(targetCol, targetRow)) {
    		if(Math.abs(targetCol - precol) * (Math.abs(targetRow- prerow)) == 2) {
    			if (isValidSquare(targetCol, targetRow))
    				return true;
    		}
    	}
    	
    	return false;
    }
    
    public boolean canMove(int targetCol, int targetRow, int a){
    	if(withInBoard(targetCol, targetRow) && !ownSquare(targetCol, targetRow)) {
    		if(Math.abs(targetCol - precol) * (Math.abs(targetRow- prerow)) == 2) {
    				return true;
    		}
    	}
    	
    	return false;
    }
}
