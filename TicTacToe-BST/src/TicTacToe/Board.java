package TicTacToe;

/**
 * This class is for consideration only. Mr. Stride had this class
 * in his implementation. Perhaps you have something similar.
 * 
 * Here is a description of Mr. Stride's Board class...
 *
 * This class:
 *   - HAS-A array of the pieces on the board
 *   - makes changes to the board by doing a Move
 *   - can undo a Move, which is helpful in creating a Tree recursively
 *   - can determine if a move is valid
 *   - HAS-A list of moves one can make
 *   - HAS-A list of moves already made
 *   - can determine if there is a winner
 *   - can determine if the game is over (board is full, possible draw)
 *   - provides a nice String representation of itself (i.e. toString())
 * 
 *
 */

import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;

/**
 * Represents a Tic-Tac-Toe game board.
 * Manages the game board, player moves, and checks for a winner.
 * 
 * @author Advaith Vijayakumar
 */
public class Board implements Cloneable {
    // Students: Use your implementation from before and update as necessary.

    private char[][] grid;
    private int size;
    private char winner;

    /**
     * Constructs a new Tic-Tac-Toe game board with initial square positions.
     */
    public Board(int size) {
        /*
         * board = new Square[][] {{new Square('1'), new Square('2'), new Square('3')},
         * {new Square('4'), new Square('5'), new Square('6')},
         * {new Square('7'), new Square('8'), new Square('9')}};
         */
        grid = new char[size][size];
        this.size = size;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                grid[row][col] = ' ';
            }
        }
    }

    public Board(Board board) {
        size = board.getSize();
        this.grid = new char[size][size];
        for (int i = 0; i < size; i++) {
            grid[i] = Arrays.copyOf(board.grid[i], size);
        }
    }

    /**
     * Prints the current state of the board in game mode.
     * Squares will either be empty or filled with 'X' or 'O'.
     */
    public String toString() {
        String boardString = "\n";
        for (int row = 0; row < this.size; row++) {
            for (int col = 0; col < this.size; col++) {
                boardString += " " + grid[row][col] + " ";
                if (col < this.size - 1) {
                    boardString += "|";
                }
            }
            boardString += "\n";
            if (row < this.size - 1) {
                for (int i = 0; i < size * 3 + size - 1; i++) {
                    boardString += "-";
                }

            }
            boardString += "\n";
        }
        return boardString;
    }

    public int getSize() {
        return this.size;
    }
    
    public boolean gameOver() {
    	return this.checkForWin() || this.isFilled();
    }
    
    public int[] findWinningMove(boolean useCurrentPlayer) {
    	for(int row = 0; row < size; row ++) {
    		for(int col = 0; col < size; col ++) {
    			Board newBoard = new Board(this);
    			newBoard.makeMove(row, col, useCurrentPlayer);
    			if(newBoard.checkForWin()) {
    				System.out.println("new board has a win");
    				return new int[] {row, col};
    			}
    		}
    	}
    	return null;
    }

    /*
     * Prints the current state of the board in coordinate mode.
     * Squares will either show their coordinate or an asterisk if filled.
     * 
     * public void printCoordinateMode() {
     * System.out.println("\n");
     * for (int row = 0; row < this.board.length; row++) {
     * for (int col = 0; col < this.board.length; col++) {
     * System.out.print(" " + board[row][col].coordinateBoardContent()
     * + " ");
     * if (col < this.board.length - 1) {
     * System.out.print("|");
     * }
     * }
     * System.out.println();
     * if (row < this.board.length - 1) {
     * System.out.println("-----------");
     * }
     * }
     * }
     */

    // irrelevant for tictactwo
    /*
     * Checks if there is a decisive winner on the board (three in a row).
     *
     * @return True if there is a decisive winner, false otherwise.
     * public boolean decisiveWinner() {
     * for(int group = 0; group < 3; group ++) {
     * if((board[group][0].getContent() == board[group][1].getContent()
     * && board[group][1].getContent() == board[group][2].getContent())
     * || (board[0][group].getContent() == board[1][group].getContent()
     * && board[1][group].getContent() == board[2][group].getContent())) {
     * return true;
     * }
     * }
     * return (board[0][0].getContent() == board[1][1].getContent()
     * && board[1][1].getContent() == board[2][2].getContent())
     * || (board[0][2].getContent() == board[1][1].getContent()
     * && board[1][1].getContent() == board[2][0].getContent());
     * }
     */

    /**
     * Checks if the game board is fully filled.
     *
     * @return True if the board is filled, false otherwise.
     */
    public boolean isFilled() {
        for (char[] row : grid) {
            for (char content : row) {
                // System.out.println(content);
                if (content == ' ') {
                    // System.out.println(Arrays.deepToString(board));
                    // System.out.println("in content if statement");
                    return false;
                }
            }
        }
        // System.out.println("reaching true");
        return true;
    }

    /**
     * Attempts to make a move on the game board.
     *
     * @param move The move entered by the player.
     * @return True if the move is valid and made, false otherwise.
     */
    // deleted Scanner console argument
    // prviacy issues; board should know whose move it is (x or o)

    // delegate more authority to smaller objects to simplify command
    public void makeMove(int row, int col, boolean useCurrentPlayer) {
        if (!isValidMove(row, col)) {
            return;
        }

        char player = getPlayer();
        if(!useCurrentPlayer) {
        	switch(player) {
        		case 'X': 
        			player = 'O';
        			break;
        		case 'O':
        			player = 'X';
        			break;
        	}
        }
        grid[row][col] = player;
    }

    public char getPlayer() {
        String boardString = Arrays.deepToString(grid);
        // System.out.println(boardString);
        int xCounts = 0;
        int oCounts = 0;
        for (char content : boardString.toCharArray()) {
            if (content == 'X') {
                xCounts++;
            } else if (content == 'O') {
                oCounts++;
            }
        }
        return xCounts == oCounts ? 'X' : 'O';
    }

    public boolean isValidMove(int row, int col) {
        return grid[row][col] == ' ';
    }
    
    /*
    public void setBoard(char[][] grid) {
        this.grid = grid;
    }

    @Override
    // https://www.digitalocean.com/community/tutorials/java-clone-object-cloning-java
    // Cloneable interface, to make a copy
    public Object clone() {

        try {
            Object obj = super.clone();
            Board boardCopy = (Board) obj;

            char[][] newBoard = new char[this.size][this.size];

            for (int row = 0; row < this.size; row++) {
                for (int col = 0; col < this.size; col++) {
                    newBoard[row][col] = this.grid[row][col];
                }
            }

            boardCopy.setBoard(newBoard);
            return boardCopy;
        } catch (CloneNotSupportedException ex) {
            return null;
        }
    }
    */
    
    // Checks for win
    public boolean checkForWin() {
        if (size == 2) {
            // System.out.println("Size is somehow 2...");
            // System.out.println("Size == 2");
            // System.out.println("Is filled: " + isFilled());
            if (isFilled()) {
                // System.out.println("in isFilled");
                return checkForTicTacTwoWin();
            }
            return false;
        }
        for (int group = 0; group < size; group++) {
            if (grid[group][group] != ' '
                    && (((grid[group][0] == grid[group][1]) && (grid[group][1] == grid[group][2]))
                    || ((grid[0][group] == grid[1][group]) && (grid[1][group] == grid[2][group])))) {
                // System.out.println("Row or column winner");
                winner = grid[group][group];
                return true;
            }
        }
        if (grid[1][1] != ' ' && ((grid[0][0] == grid[1][1] && grid[1][1] == grid[2][2])
                || (grid[0][2] == grid[1][1] && grid[1][1] == grid[2][0]))) {
            // System.out.println("Diagonal winner");
            winner = grid[1][1];
            return true;
        }
        return false;
    }

    private boolean checkForTicTacTwoWin() {
        // System.out.println("In TT2 win check method");
        int xScore = 0;
        int[][] xKey = { { 1, 2 }, { 3, 4 } };
        int[][] oKey = { { -4, -2 }, { -3, -1 } };
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                switch (grid[row][col]) {
                    case 'X':
                        xScore += xKey[row][col];
                        break;
                    case 'O':
                        xScore += oKey[row][col];
                }
            }
        }
        // System.out.println("xScore: " + xScore);
        if (xScore == 0) {
            return false;
        } else if (xScore > 0) {
            winner = 'X';
        } else if (xScore < 0) {
            winner = 'O';
        }
        return true;
    }

    public ArrayList<Integer> getMoves(char player) {
        ArrayList<Integer> moves = new ArrayList<>();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (grid[row][col] == player) {
                    moves.add(row * size + col + 1);
                }
            }
        }
        return moves;
    }

    public char getWinner() {
        return winner;
    }

    public char charAt(int row, int col) {
        return grid[row][col];
    }

    // Precondition: Winning position is in fact a winning position
    public int[] findWinningMove(Board newPosition) {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (grid[row][col] == ' ' && newPosition.charAt(row, col) != ' ') {
                    return new int[] { row, col };
                }
            }
        }
        return new int[] {0, 0};
    }
}
