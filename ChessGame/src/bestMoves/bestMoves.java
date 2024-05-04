package bestMoves;

import main.ChessPieces;

public class bestMoves {

	public int bestCol, bestRow;
	public ChessPieces piece;
	public double moveRating;
	public int activeRow, activeCol;
	public int index;
	
	public bestMoves(int index,int bestCol, int bestRow, int activeCol, int activeRow,double moveRating, ChessPieces piece) {
		this.index = index;
		this.activeCol = activeCol;
		this.activeRow = activeRow;
		this.bestCol = bestCol;
		this.bestRow = bestRow;
		this.moveRating = moveRating;
		this.piece = piece;
	}
	
	public bestMoves(int activecol, int activerow) {
		this.activeRow = activerow;
		this.activeCol = activecol;
	}
}
