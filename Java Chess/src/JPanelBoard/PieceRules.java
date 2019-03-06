package JPanelBoard;

import java.util.Arrays;

public class PieceRules {
	public final boolean pRule(sButton CS, sButton holderSquare) {
		
	}
	
	public static final boolean nRule(sButton CS, sButton holdSquare) {
		final int c = CS.column, r = CS.row;
		int[] check = {holdSquare.column, holdSquare.row}, p1 = {c-1, r-2}, p2 = {c+1, r-2}, p3 = {c+2, r-1}, p4 = {c+2, r+1}, p5 = {c+1, r+2}, 
				p6 = {c-1, r+2}, p7 = {c-2, r+1}, p8 = {c-2, r-1};
		int[][] hold = {p1,p2,p3,p4,p5,p6,p7,p8};
		for(int i = 0; i < 8; i++) {
			if(Arrays.equals(check, hold[i])) {
				CS.pColor = holdSquare.pColor;
				CS.pType = holdSquare.pType;
				CS.setIcon(holdSquare.getIcon());
				CS.pieceMoveCounter++;
				return true;
			}
		}
		return false;

	}
	
	public final boolean bRule(sButton CS, sButton holderSquare) {
		
	}
	
	public final boolean rRule(sButton CS, sButton holderSquare) {
		
	}
	
	public final boolean qRule(sButton CS, sButton holderSquare) {
		
	}
	
	public final boolean kRule(sButton CS, sButton holderSquare) {
		
	}
}
