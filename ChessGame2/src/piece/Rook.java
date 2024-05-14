package piece;
import main.ChessPieces;
import main.PanelGame;
import main.Type;

public class Rook extends ChessPieces{

    public Rook(int color, int row, int col){
        super(color, row, col);
        
     // Assigning ID
        type = Type.ROOK;
        
        
        if(color == PanelGame.WHITE){
            image = getImage("/piece/w-rook");
        }else
            image = getImage("/piece/b-rook");
    }
    public boolean canMove(int targetCol, int targetRow){
    	
    	if(withInBoard(targetCol, targetRow) && ownSquare(targetCol, targetRow) == false)
    		// can move only in the same row and same column
    		if(targetCol == precol || targetRow == prerow)
    			if(isValidSquare(targetCol, targetRow) && !pieceOnStraightLine(targetCol, targetRow))
    				return true;
    	
    	return false;
    }
    public boolean canMove(int targetCol, int targetRow, int a){
    	
    	if(withInBoard(targetCol, targetRow) && ownSquare(targetCol, targetRow) == false)
    		// can move only in the same row and same column
    		if(targetCol == precol || targetRow == prerow)
    			if(!pieceOnStraightLine(targetCol, targetRow))
    				return true;
    	
    	return false;
    }
    
 public boolean canMove(int targetCol, int targetRow, boolean a){
    	
    	if(withInBoard(targetCol, targetRow) && ownSquare(targetCol, targetRow) == false)
    		// can move only in the same row and same column
    		if(targetCol == precol || targetRow == prerow)
    				return true;
    	
    	return false;
    }
    
}
    

