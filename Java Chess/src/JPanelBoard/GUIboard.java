package JPanelBoard;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;



public class GUIboard {
	
	//elements of the gui and the gui itself. 
	private final JPanel chessGUI = new JPanel(new BorderLayout(3,3));
	private JPanel chessBoard;
	private sButton[][] chessSquares = new sButton[8][8];
	private static Image[][] chessPieces = new Image[2][6];
	
	public static final int BLACK = 0, WHITE = 1, EMPTY = -1, ROOK = 0, BISHOP = 1, QUEEN = 2, KING = 3, KNIGHT = 4, PAWN = 5; //outlining the positioning of the color/pieces of the png
	public static final int[] PIECE_ORDER = {ROOK, KNIGHT, BISHOP, QUEEN, KING, BISHOP, KNIGHT, ROOK}; 
	
	public static final String COLUMNS_LIST = "ABCDEFGH";
	public static final String ROWS_LIST = "12345678";
	
	ImageIcon icon =  new ImageIcon(new BufferedImage(69, 69, BufferedImage.TYPE_INT_ARGB));
	
	public sButton getSquare(int c, int r) {
		return chessSquares[c][r]; 
	}
	
	public static Image getPieceImage(int color, int piece) {
		return chessPieces[color][piece];
	}
	
	

	GUIboard() {
		startGame();
	}
	
	
	
	
	public final void startGame() {
		
		convertImage();
		
		sButton holdSquare = new sButton(); //an empty square to hold information from the buttons
		holdSquare.pColor = EMPTY;
		holdSquare.pType = EMPTY;
		holdSquare.mCounter = 1; //move counter to make sure only the correct side can move a piece
		
		chessGUI.setBorder(new EmptyBorder(5,5,5,5));
		JToolBar tbuttons = new JToolBar(); //making a toolbar for the buttons in the game
		tbuttons.setFloatable(false);
		//tbuttons.setRollover(true);
		chessGUI.add(tbuttons, BorderLayout.PAGE_START);
		
		JButton ngButton = new JButton("NEW GAME");
		ngButton.addActionListener(e -> newGame(holdSquare));
		
		JButton clearButton = new JButton("CLEAR BOARD");
		clearButton.addActionListener(e -> clearBoard(holdSquare));
		
		JButton exitButton = new JButton("EXIT GAME");
		exitButton.addActionListener(e -> exitGame());
		tbuttons.add(ngButton);
		tbuttons.add(clearButton);
		tbuttons.add(exitButton);
		
		chessBoard = new JPanel(new GridLayout(0,10));
		chessBoard.setBorder(new LineBorder(Color.black));
		
		JPanel boardLayout = new JPanel(new GridBagLayout());
		boardLayout.add(chessBoard);
		chessGUI.add(boardLayout);
		
		
		Insets squareMargin = new Insets(0, 0, 0, 0);
		for (int i = 0; i < chessSquares.length; i++ ) {
			for (int j = 0; j < chessSquares[i].length; j++) {
				sButton square = new sButton();
				square.setMargin(squareMargin);
				square.setBorderPainted(false);
				square.setIcon(icon);
				
				if((j % 2 == 1 && i % 2 == 1) || (j % 2 == 0 && i % 2 == 0)) {
					square.setBackground(Color.WHITE);
				}
				else {
					square.setBackground(Color.BLACK);
				}
				
				square.column = j;
				square.row = i;
				square.pColor = EMPTY;
				square.pType = EMPTY;
				
				square.addActionListener(e -> moving(square, holdSquare, chessSquares));
				
				chessSquares[j][i] = square;
			}
		}
		
		chessBoard.add(new JLabel(""));//adds notation to the top of the board
		for (int i = 0; i < 8; i++) {
			chessBoard.add(new JLabel(COLUMNS_LIST.substring(i, i+1), SwingConstants.CENTER));
		}
		chessBoard.add(new JLabel("", SwingConstants.CENTER));
		
		for(int i = 0; i < 8; i++) {//adds notation to the left and right side of the board
			for (int j = 0; j < 8; j++) {
				switch (j) {
					case 0:
						chessBoard.add(new JLabel("" + (9-(i+1)), SwingConstants.CENTER));
					default:
						chessBoard.add(chessSquares[j][i]);
				}
				if(j==7) {
					chessBoard.add(new JLabel("" + (9-(i+1)), SwingConstants.CENTER));
				}
			}
		}
		
		chessBoard.add(new JLabel(""));//adds notation to the bottom of the board
		for (int i = 0; i < 8; i++) {
			chessBoard.add(new JLabel(COLUMNS_LIST.substring(i, i+1), SwingConstants.CENTER));
		}
		chessBoard.add(new JLabel("", SwingConstants.CENTER));
	}
	
	public final void convertImage() { // method when called imports the png of the pieces into the chessPiece element
		try {
			BufferedImage holder = ImageIO.read(new File("E:\\Coding Project\\Java Chess\\src\\JPanelBoard\\clear.png"));
			for(int i = 0; i < 2; i++) {
				for(int j = 0; j < 6; j++) {
					chessPieces[i][j] = holder.getSubimage((j)*52, (i)*69, 52, 69);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public final void moving(sButton CS, sButton holdSquare, sButton[][] board) { //method for the board squares to move the pieces 
		if(CS.pColor != EMPTY && holdSquare.pColor == EMPTY) { //picking up a piece and making sure the correct color can only move on the correct turn
			if((holdSquare.mCounter % 2 == 1 && CS.pColor == WHITE) || (holdSquare.mCounter % 2 == 0 && CS.pColor == BLACK)) {
				holdSquare.pColor = CS.pColor;
				holdSquare.pType = CS.pType;
				holdSquare.row = CS.row;
				holdSquare.column = CS.column;
				holdSquare.setIcon(CS.getIcon());
				holdSquare.pieceMoveCounter = CS.pieceMoveCounter;
			
				CS.pColor = EMPTY;
				CS.pType = EMPTY;
				CS.setIcon(icon);
				CS.pieceMoveCounter = 0;
				
				holdSquare.mCounter++;
			}
		}
		else if(holdSquare.pColor != EMPTY){ //moving piece to an empty square
			if(CS.row == holdSquare.row && CS.column == holdSquare.column) { //checks if player is putting piece back down
				CS.pColor = holdSquare.pColor;
				CS.pType = holdSquare.pType;
				CS.setIcon(holdSquare.getIcon());
				holdSquare.mCounter--;
			}
			else if(castling(CS, holdSquare) != true) {
				/*CS.pColor = holdSquare.pColor;
				CS.pType = holdSquare.pType;
				CS.setIcon(holdSquare.getIcon());
				CS.pieceMoveCounter++;
				/*if(holdSquare.pType == PAWN) {//TEST FOR NIGHT RULE
					System.out.println("t1");
					if(PieceRules.pRule(CS, holdSquare) == true) {
						System.out.println("t2");
					}
					else
						return;
				}*/
				
				if(PieceRules.pieceMoves(CS, holdSquare, board) != true) {
					return;
				}
			}
			holdSquare.pColor = EMPTY;
			holdSquare.pType = EMPTY;
			holdSquare.row = EMPTY;
			holdSquare.column = EMPTY;
			holdSquare.setIcon(icon);
			holdSquare.pieceMoveCounter = 0;
		}
		/*else if(CS.pColor != EMPTY && holdSquare.pColor != EMPTY) { //capturing a piece
			if(CS.pColor != holdSquare.pColor) {
				if(PieceRules.pieceMoves(CS, holdSquare, board) != true) {
					return;
				}
				/*CS.pColor = holdSquare.pColor;
				CS.pType = holdSquare.pType;
				CS.setIcon(holdSquare.getIcon());
				CS.pieceMoveCounter = holdSquare.pieceMoveCounter;
				
				holdSquare.pColor = EMPTY;
				holdSquare.pType = EMPTY;
				holdSquare.row = EMPTY;
				holdSquare.column = EMPTY;
				holdSquare.setIcon(icon);
				holdSquare.pieceMoveCounter = 0;
			}
		}*/
		//else if(CS.pColor == EMPTY && holdSquare.pColor == EMPTY)//do nothing	
	}
	
	public final boolean castling(sButton CS, sButton holdSquare) {// performs castling and if it does returns true, if it doesn't returns false
		if (holdSquare.pieceMoveCounter == 0 && holdSquare.pColor == WHITE && holdSquare.pType == KING && holdSquare.row == 7 && holdSquare.column == 4) { //white
			if (CS.row == 7 && CS.column == 2 && chessSquares[0][7].pieceMoveCounter == 0 && chessSquares[0][7].pType == ROOK && chessSquares[3][7].pType == EMPTY && chessSquares[2][7].pType == EMPTY && chessSquares[1][7].pType == EMPTY) {
				CS.pColor = holdSquare.pColor;
				CS.pType = holdSquare.pType;
				CS.setIcon(holdSquare.getIcon());
				CS.pieceMoveCounter++;
				
				chessSquares[3][7].pColor = chessSquares[0][7].pColor;
				chessSquares[3][7].pType = chessSquares[0][7].pType;
				chessSquares[3][7].setIcon(chessSquares[0][7].getIcon());
				
				chessSquares[0][7].pColor = EMPTY;
				chessSquares[0][7].pType = EMPTY;
				chessSquares[0][7].setIcon(icon);
				
				return true;
			}
			else if(CS.row == 7 && CS.column == 6 && chessSquares[7][7].pieceMoveCounter == 0 && chessSquares[7][7].pType == ROOK && chessSquares[6][7].pType == EMPTY && chessSquares[5][7].pType == EMPTY) {
				CS.pColor = holdSquare.pColor;
				CS.pType = holdSquare.pType;
				CS.setIcon(holdSquare.getIcon());
				CS.pieceMoveCounter++;
				
				chessSquares[5][7].pColor = chessSquares[7][7].pColor;
				chessSquares[5][7].pType = chessSquares[7][7].pType;
				chessSquares[5][7].setIcon(chessSquares[7][7].getIcon());
				
				chessSquares[7][7].pColor = EMPTY;
				chessSquares[7][7].pType = EMPTY;
				chessSquares[7][7].setIcon(icon);
				
				return true;
			}
			else {
				return false;
			}
		}
		else if(holdSquare.pieceMoveCounter == 0 && holdSquare.pColor == BLACK && holdSquare.pType == KING && holdSquare.row == 0 && holdSquare.column == 4){//black
			if (CS.row == 0 && CS.column == 2 && chessSquares[0][0].pieceMoveCounter == 0 && chessSquares[0][0].pType == ROOK && chessSquares[3][0].pType == EMPTY && chessSquares[2][0].pType == EMPTY && chessSquares[1][0].pType == EMPTY) {
				CS.pColor = holdSquare.pColor;
				CS.pType = holdSquare.pType;
				CS.setIcon(holdSquare.getIcon());
				CS.pieceMoveCounter++;
				
				chessSquares[3][0].pColor = chessSquares[0][0].pColor;
				chessSquares[3][0].pType = chessSquares[0][0].pType;
				chessSquares[3][0].setIcon(chessSquares[0][0].getIcon());
				
				chessSquares[0][0].pColor = EMPTY;
				chessSquares[0][0].pType = EMPTY;
				chessSquares[0][0].setIcon(icon);
				
				return true;
			}
			else if(CS.row == 0 && CS.column == 6 && chessSquares[7][0].pieceMoveCounter == 0 && chessSquares[7][0].pType == ROOK && chessSquares[6][0].pType == EMPTY && chessSquares[5][0].pType == EMPTY) {
				CS.pColor = holdSquare.pColor;
				CS.pType = holdSquare.pType;
				CS.setIcon(holdSquare.getIcon());
				CS.pieceMoveCounter++;
				
				chessSquares[5][0].pColor = chessSquares[7][0].pColor;
				chessSquares[5][0].pType = chessSquares[7][0].pType;
				chessSquares[5][0].setIcon(chessSquares[7][0].getIcon());
				
				chessSquares[7][0].pColor = EMPTY;
				chessSquares[7][0].pType = EMPTY;
				chessSquares[7][0].setIcon(icon);
				
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	public final void newGame(sButton holdSquare) {//sets up a new game with all the pieces in starting position
		clearBoard(holdSquare);
		
		for(int i = 0; i < PIECE_ORDER.length; i++) { //sets starting position of the game 
			chessSquares[i][0].setIcon(new ImageIcon(chessPieces[BLACK][PIECE_ORDER[i]])); 
			chessSquares[i][0].pColor = BLACK;
			chessSquares[i][0].pType = PIECE_ORDER[i];
			chessSquares[i][1].setIcon(new ImageIcon(chessPieces[BLACK][PAWN])); 
			chessSquares[i][1].pColor = BLACK;
			chessSquares[i][1].pType = PAWN;
			chessSquares[i][6].setIcon(new ImageIcon(chessPieces[WHITE][PAWN])); 
			chessSquares[i][6].pColor = WHITE;
			chessSquares[i][6].pType = PAWN;
			chessSquares[i][7].setIcon(new ImageIcon(chessPieces[WHITE][PIECE_ORDER[i]])); 
			chessSquares[i][7].pColor = WHITE;
			chessSquares[i][7].pType = PIECE_ORDER[i];
		}
	}
	
	public final void clearBoard(sButton holdSquare) {
		for(int i = 0; i < PIECE_ORDER.length; i++) {  //clears the board and sets all the squares empty
			for(int j = 0; j < PIECE_ORDER.length; j++) {
				chessSquares[i][j].setIcon(icon);
				chessSquares[i][j].revalidate();
				chessSquares[i][j].pColor = EMPTY;
				chessSquares[i][j].pType = EMPTY;
				chessSquares[i][j].pieceMoveCounter = 0;
			}
		}
		holdSquare.pColor = EMPTY;
		holdSquare.pType = EMPTY;
		holdSquare.row = EMPTY;
		holdSquare.column = EMPTY;
		holdSquare.setIcon(icon);
		holdSquare.pieceMoveCounter = 0;
		holdSquare.mCounter = 1;
	}
	
	public final void exitGame() {
		System.exit(0);
	}
	
	public final JComponent getGUI() {
		return chessGUI;
	}
	
	public static void main(String[] args) {
		Runnable r =new Runnable() {

			@Override
			public void run() {
				GUIboard game = new GUIboard();
				JFrame frame = new JFrame("Chess");
				frame.add(game.getGUI());
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setLocationByPlatform(true);
				frame.pack();
				frame.setMinimumSize(frame.getSize());
				frame.setVisible(true);
			}
			
		};
		SwingUtilities.invokeLater(r);
	}
	
}
