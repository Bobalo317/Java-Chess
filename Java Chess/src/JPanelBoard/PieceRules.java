package JPanelBoard;

import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.ImageIcon;

public class PieceRules {
	
	public static final int BLACK = 0, WHITE = 1, EMPTY = -1, ROOK = 0, BISHOP = 1, QUEEN = 2, KING = 3, KNIGHT = 4, PAWN = 5; //outlining the positioning of the color/pieces of the png
	
	public static void normMove(sButton CS, sButton holdSquare) {
		CS.pColor = holdSquare.pColor;
		CS.pType = holdSquare.pType;
		CS.setIcon(holdSquare.getIcon());
		CS.pieceMoveCounter++;
	}
	
	public static final boolean pRule(sButton CS, sButton holdSquare, sButton[][] board) {
		final int c = CS.column, r = CS.row;
		int[] check = {holdSquare.column, holdSquare.row},  w1 = {c+1,r+1}, w2 = {c-1,r+1}, w3 = {c,r+2}, w4 = {c,r+1}, 
				b1 = {c+1,r-1}, b2 = {c-1,r-1}, b3 = {c,r-2}, b4 = {c,r-1};
		
		if(holdSquare.pColor == WHITE) {//for white pawns
			if(CS.pColor != EMPTY && CS.pColor!= WHITE && (Arrays.equals(check, w1) || Arrays.equals(check, w2))) {
				if(CS.row == 0) {//
					promotion(CS, holdSquare);
				}
				else {
					normMove(CS, holdSquare);
				}
				return true;
			}
			else if((Arrays.equals(check, w3) && holdSquare.pieceMoveCounter == 0 && board[w3[0]][w3[1]].pColor == EMPTY && board[w4[0]][w4[1]].pColor == EMPTY) || 
						(Arrays.equals(check, w4) && board[CS.column][CS.row].pColor == EMPTY)) {
				if(CS.row == 0) {
					promotion(CS, holdSquare);
				}
				else {
					normMove(CS, holdSquare);
				}
				return true;
			}
			return false;
		}
		else {//for black pawns
			if(CS.pColor != EMPTY && CS.pColor!= BLACK && (Arrays.equals(check, b1) || Arrays.equals(check, b2))) {
				if(CS.row == 7) {
					promotion(CS, holdSquare);
				}
				else {
					normMove(CS, holdSquare);
				}
				return true;
			}
			else if((Arrays.equals(check, b3) && holdSquare.pieceMoveCounter == 0 && board[b3[0]][b3[1]].pColor == EMPTY && board[b4[0]][b4[1]].pColor == EMPTY) || 
						(Arrays.equals(check, b4) && board[CS.column][CS.row].pColor == EMPTY)) {
				if(CS.row == 7) {
					promotion(CS, holdSquare);
				}
				else {
					normMove(CS, holdSquare);
				}
				return true;
			}
			return false;
		}
	
	}
	
	public static void promotion(sButton CS, sButton holdSquare) {
		if(holdSquare.pColor == WHITE)
		{
			CS.pColor = holdSquare.pColor;
			CS.pType = QUEEN;
			ImageIcon qi = new ImageIcon(GUIboard.getPieceImage(WHITE, QUEEN));
			CS.setIcon(qi);
			CS.pieceMoveCounter++;
		}
		else {
			CS.pColor = holdSquare.pColor;
			CS.pType = QUEEN;
			ImageIcon qi = new ImageIcon(GUIboard.getPieceImage(BLACK, QUEEN));
			CS.setIcon(qi);
			CS.pieceMoveCounter++;
		}
	}
	
	public static final boolean nRule(sButton CS, sButton holdSquare) {
		final int c = CS.column, r = CS.row;
		int[] check = {holdSquare.column, holdSquare.row}, p1 = {c-1, r-2}, p2 = {c+1, r-2}, p3 = {c+2, r-1}, p4 = {c+2, r+1}, p5 = {c+1, r+2}, 
				p6 = {c-1, r+2}, p7 = {c-2, r+1}, p8 = {c-2, r-1};
		int[][] hold = {p1,p2,p3,p4,p5,p6,p7,p8};
		
		for(int i = 0; i < 8; i++) {
			if(Arrays.equals(check, hold[i]) && CS.pColor != holdSquare.pColor) {
				normMove(CS, holdSquare);
				return true;
			}
		}
		
		return false;

	}
	
	public static boolean minMax(int num) {
		if(num > 7 || num < 0) {
			return true;
		}
		return false;
	}
	
	public static final boolean bRule(sButton CS, sButton holdSquare, sButton[][] board) {
		final int c = CS.column, r = CS.row;
		int[] check = {holdSquare.column, holdSquare.row};
		ArrayList<int[]> hold = new ArrayList<int[]>();
		
		if(CS.pColor == holdSquare.pColor) {
			return false;
		}
		boolean flag = true;
		for(int i = 1; (i < 8) && (flag == true); i++) {
			int[] square = {c+i,r+i};
			hold.add(square);
			if(minMax(c+i) || minMax(r+i)) {
				hold.remove(hold.size()-1);
				flag = false;
			}
			else if(board[c+i][r+i].pColor != EMPTY) {
				if(board[c+i][r+i].pColor == holdSquare.pColor) {
					hold.remove(hold.size()-1);
				}
				flag = false;
			}
		}
		flag = true;
		for(int i = 1; (i < 8) && (flag == true); i++) {
			int[] square = {c-i,r-i};
			hold.add(square);
			if(minMax(c-i) || minMax(r-i)) {
				hold.remove(hold.size()-1);
				flag = false;
			}
			else if(board[c-i][r-i].pColor != EMPTY) {
				if(board[c-i][r-i].pColor == holdSquare.pColor) {
					hold.remove(hold.size()-1);
				}
				flag = false;
			}
		}
		flag = true;
		for(int i = 1; (i < 8) && (flag == true); i++) {
			int[] square = {c-i,r+i};
			hold.add(square);
			if(minMax(c-i) || minMax(r+i)) {
				hold.remove(hold.size()-1);
				flag = false;
			}
			else if(board[c-i][r+i].pColor != EMPTY) {
				if(board[c-i][r+i].pColor == holdSquare.pColor) {
					hold.remove(hold.size()-1);
				}
				flag = false;
			}
		}
		flag = true;
		for(int i = 1; (i < 8) && (flag == true); i++) {
			int[] square = {c+i,r-i};
			hold.add(square);
			if(minMax(c+i) || minMax(r-i)) {
				hold.remove(hold.size()-1);
				flag = false;
			}
			else if(board[c+i][r-i].pColor != EMPTY) {
				if(board[c+i][r-i].pColor == holdSquare.pColor) {
					hold.remove(hold.size()-1);
				}
				flag = false;
			}
		}
		
		for(int i = 0; i < hold.size(); i++) {
			if(Arrays.equals(check, hold.get(i))) {
				normMove(CS, holdSquare);
				return true;
			}
		}
		return false;
	}
	
	public static final boolean rRule(sButton CS, sButton holdSquare, sButton[][] board) {
		final int c = CS.column, r = CS.row;
		int[] check = {holdSquare.column, holdSquare.row};
		ArrayList<int[]> hold = new ArrayList<int[]>();
		
		if(CS.pColor == holdSquare.pColor) {
			return false;
		}
		boolean flag = true;
		for(int i = 1; (i < 8) && (flag == true); i++) {
			int[] square = {c+i,r};
			hold.add(square);
			if(minMax(c+i) || minMax(r)) {
				hold.remove(hold.size()-1);
				flag = false;
			}
			else if(board[c+i][r].pColor != EMPTY) {
				if(board[c+i][r].pColor == holdSquare.pColor) {
					hold.remove(hold.size()-1);
				}
				flag = false;
			}
		}
		flag = true;
		for(int i = 1; (i < 8) && (flag == true); i++) {
			int[] square = {c-i,r};
			hold.add(square);
			if(minMax(c-i) || minMax(r)) {
				hold.remove(hold.size()-1);
				flag = false;
			}
			else if(board[c-i][r].pColor != EMPTY) {
				if(board[c-i][r].pColor == holdSquare.pColor) {
					hold.remove(hold.size()-1);
				}
				flag = false;
			}
		}
		flag = true;
		for(int i = 1; (i < 8) && (flag == true); i++) {
			int[] square = {c,r+i};
			hold.add(square);
			if(minMax(c) || minMax(r+i)) {
				hold.remove(hold.size()-1);
				flag = false;
			}
			else if(board[c][r+i].pColor != EMPTY) {
				if(board[c][r+i].pColor == holdSquare.pColor) {
					hold.remove(hold.size()-1);
				}
				flag = false;
			}
		}
		flag = true;
		for(int i = 1; (i < 8) && (flag == true); i++) {
			int[] square = {c,r-i};
			hold.add(square);
			if(minMax(c) || minMax(r-i)) {
				hold.remove(hold.size()-1);
				flag = false;
			}
			else if(board[c][r-i].pColor != EMPTY) {
				if(board[c][r-i].pColor == holdSquare.pColor) {
					hold.remove(hold.size()-1);
				}
				flag = false;
			}
		}
		
		for(int i = 0; i < hold.size(); i++) {
			if(Arrays.equals(check, hold.get(i))) {
				normMove(CS, holdSquare);
				return true;
			}
		}
		return false;
	}
	
	public static final boolean qRule(sButton CS, sButton holdSquare, sButton[][] board) {
		if(bRule(CS, holdSquare, board)) {
			return true;
		}
		else if(rRule(CS, holdSquare, board)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public static final boolean kRule(sButton CS, sButton holdSquare) {
		final int c = CS.column, r = CS.row;
		int[] check = {holdSquare.column, holdSquare.row};
		int[][] hold = {{c-1,r-1},{c-1,r},{c-1,r+1},{c,r+1},{c,r-1},{c+1,r+1},{c+1,r},{c+1,r-1}};
		for(int i = 0; i < 8; i++) {
			if(Arrays.equals(check, hold[i])) {
				normMove(CS, holdSquare);
				return true;
			}
		}
		return false;
	}
	
	public static final boolean pieceMoves(sButton CS, sButton holdSquare, sButton[][] board) {
		switch(holdSquare.pType) {
			case PAWN: 
				return(pRule(CS, holdSquare, board));
			case KNIGHT: 
				return(nRule(CS, holdSquare));
			case BISHOP: 
				return(bRule(CS, holdSquare, board));
			case ROOK: 
				return(rRule(CS, holdSquare, board));
			case QUEEN: 
				return(qRule(CS, holdSquare, board));
			case KING: 
				return(kRule(CS, holdSquare));
			default:
				return false;
		}
	}
}
