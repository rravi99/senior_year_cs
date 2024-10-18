// You must KEEP a few methods as commented below.
// Otherwise, add methods to implement a GUI version of TicTacToe

package GUI;
// started this
/*
 This class is responsible for:
    - drawing the TicTacToe board.
    - receiving user clicks and interaction (user events)
    - forwarding all user events appropriately

 All user interactions should be abstracted and forwarded to TicTacToe classes.
*/

import Forest.Tree;
import TicTacToe.Board;
import TicTacToe.TTTNode;
import TicTacToe.GameTree;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;


public class GamePanel extends JPanel {
	
	// TODO: have instance fields for the current turn and what the AI is (X or O)
	private boolean aiTurn;
	private char aiSymbol;

    // Keep this instance field
    private GameTree ai = null;

    private int playerWins;
    private int aiWins;
    private int draws;
    
    // Consider using a monospaced, bold font to draw X's and O's
	private Font font = new Font("Monospaced", Font.BOLD, 130);

    // Have a board or something like it
	private Board board;

    public GamePanel() {
        ai = new GameTree();
        ai.learn();

        board = new Board(3);
        createEventHandlers();
    }

    // Keep this method!
    public Tree getGameTree() {
        return ai;
    }

    // Keep this method!
    public void setAIStarts(boolean aiMovesFirst) {
        aiTurn = aiMovesFirst;
        aiSymbol = aiMovesFirst ? 'X' : 'O';
        resetBoard();
        repaint();
    }
    
	@Override
	public void paintComponent(Graphics g) {
		System.out.println("In Repaint");
        super.paintComponent(g);
		this.setBackground(Color.WHITE);
		g.clearRect(0, 0, this.getWidth(), this.getHeight());
		
		// draw the Game board hash marks and X's and O's
		// for now, just draw some text
        g.setColor(Color.BLACK);
		//g.drawString("Game Board", 100,100);
		drawBoard(g);
		if (board.gameOver()) {
			System.out.println(board);
			SwingUtilities.invokeLater(() -> {
				String message = "";
				if (board.checkForWin()) {
					char winner = board.getWinner();
					if(winner == aiSymbol) {
						playerWins ++;
					} else {
						aiWins ++;
					}
					message = winner + " won!";
				} else {
					message = "You're both losers";
					draws ++;
				} int result = JOptionPane.showConfirmDialog(GamePanel.this, message);
				if (result == JOptionPane.YES_OPTION || result == JOptionPane.NO_OPTION || result == JOptionPane.CANCEL_OPTION) {
					result = JOptionPane.showConfirmDialog(GamePanel.this, "Player Wins: " + playerWins + "\nAI Wins: " + aiWins + "\nDraws: " + draws);
					if (result == JOptionPane.YES_OPTION || result == JOptionPane.NO_OPTION || result == JOptionPane.CANCEL_OPTION)
						GamePanel.this.resetBoard();
					
				}});
				aiSymbol = aiSymbol == 'X' ? 'O' : 'X';
 		} else if (aiTurn) {
			TTTNode gameNode = ai.getMapNode(this.board.toString());
			int[] move = gameNode.getBestMove();
			System.out.println("Move Received:  " + Arrays.toString(move));
			this.board.makeMove(move[0], move[1], true);
			aiTurn = false;
			repaint();
		}
	}
	
	public void clearBoard(Graphics g) {
        super.paintComponent(g);
		this.setBackground(Color.WHITE);
		g.clearRect(0, 0, this.getWidth(), this.getHeight());
		
		// draw the Game board hash marks and X's and O's
		// for now, just draw some text
        g.setColor(Color.BLACK);
	}

    /**
	 * This allows this dialog to be drawn at a good size.
	 */
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(MainFrame.WIDTH, MainFrame.HEIGHT);
	}
	
	/**
	 * This is called when a new game is started. This will
	 * reset our state back to a new game and redraw the board.
	 */
	public void resetBoard() {
		this.board = new Board(3);
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.repaint();
	}
	
	/**
     * Recommended method, but not required.
     *
	 * This is called by the Swing Framework when the dialog needs
	 * to be (re)drawn. We need to draw the full TicTacToe board
	 * with slashes and X's and O's.
	 */
	public void drawBoard(Graphics g) {
		try {
			File file = new File("src/8-bit-jump-001-171817.wav");
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(file));
			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		g.drawLine(this.getWidth() / 3, 0, this.getWidth() / 3, this.getHeight());
		g.drawLine(2 * this.getWidth() / 3, 0, 2 * this.getWidth() / 3, this.getHeight());
		g.drawLine(0, this.getHeight() / 3, this.getWidth(), this.getHeight() / 3);
		g.drawLine(0, 2 * this.getHeight() / 3, this.getWidth(), 2 * this.getHeight() / 3);
		for(int row = 0; row < board.getSize(); row ++) {
			for(int col = 0; col < board.getSize(); col ++) {
				g.setFont(font);
				Canvas c = new Canvas();
				FontMetrics fm = c.getFontMetrics(font);
				char symbol = board.charAt(row, col);
				g.setColor(symbol == 'X' ? Color.BLUE : Color.RED);
				g.drawString("" + symbol, col * this.getWidth() / 3 + this.getWidth() / 6 - fm.getWidths()[88] / 2, row * this.getHeight() / 3 + this.getHeight() / 6 + fm.getHeight() / 4);
			}
		}
    }
	
	/**
	 * Set up all the event handlers for our components.
	 */
	private void createEventHandlers() {
		this.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent me) {
				int row = (int)((double)me.getY() / GamePanel.this.getHeight() * 3);
				System.out.println("Row: " + row);
				int col = (int)((double)me.getX() / GamePanel.this.getWidth() * 3);
				System.out.println("Col: " + col);
				if(GamePanel.this.board.isValidMove(row, col)) {
					GamePanel.this.board.makeMove(row, col, true);
					GamePanel.this.aiTurn = true;
					GamePanel.this.repaint();
				}
			}
		});
	}    
	

}
