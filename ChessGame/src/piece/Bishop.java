package piece;
import main.ChessPieces;
import main.PanelGame;
import main.Type;


public class Bishop extends ChessPieces{

    public Bishop(int color, int row, int col){
        super(color, row, col);
        
     // Assigning ID
        type = Type.BISHOP;
        
        if(color == PanelGame.WHITE){
            image = getImage("/piece/w-bishop");
        }else
            image = getImage("/piece/b-bishop");
    }
    
    public boolean canMove(int targetCol, int targetRow){
    	if(withInBoard(targetCol, targetRow) && !ownSquare(targetCol, targetRow)) {
    		
    		if(Math.abs(targetCol - precol) == Math.abs(targetRow - prerow)) {
    			if(isValidSquare(targetCol, targetRow) && !pieceOnDiognal(targetCol, targetRow))
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    public boolean canMove(int targetCol, int targetRow, int a){
    	if(withInBoard(targetCol, targetRow) && !ownSquare(targetCol, targetRow)) {
    		
    		if(Math.abs(targetCol - precol) == Math.abs(targetRow - prerow)) {
    			if(!pieceOnDiognal(targetCol, targetRow))
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    public boolean canMove(int targetCol, int targetRow, boolean value){
    	if(withInBoard(targetCol, targetRow) && !ownSquare(targetCol, targetRow)) {
    		
    		if(Math.abs(targetCol - precol) == Math.abs(targetRow - prerow)) {
//    			if(!pieceOnDiognal(targetCol, targetRow))
    			return true;
    		}
    	}
    	
    	return false;
    }
}
