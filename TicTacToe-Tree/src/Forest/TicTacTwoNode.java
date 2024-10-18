package Forest;
import TicTacToe.Board;
/*
 * This class 
 */
public class TicTacTwoNode extends Node {

    // Instance fields should store information about the board,
    // wins, losses, draws and which move is the best
    // TODO: add instance fields here
    private int xWins;
    private int oWins;
    private int draws;
    public Board board;


    // Create Node that represents the board at this stage
    public TicTacTwoNode(Board board) {
        
        // Must call super() first!
        super();
        // TODO: set instance fields as necessary
        this.board = board;
        // set wins, losses, draws later
    }
    
    public TicTacTwoNode() {
		// TODO Auto-generated constructor stub
    	super();
	}

	// Update the signature of this API to return the best Move
    // as you've designed your classes. Perhaps an int?
    public int[] getBestMove() {
    	
        // greatest winPath
        // TODO: return the instance field for the best move
        double maxScore = 0;
        TicTacTwoNode bestChild = null;
  
        int[] winningMove = board.findWinningMove(true);
        System.out.println(winningMove);
        if(winningMove != null) {
        	System.out.println("Winning move exists");
        	return winningMove;
        }
        
        int[] savingMove = board.findWinningMove(false);
        if(savingMove != null) {
        	System.out.println("Saving move exists");
        	return savingMove;
        }
        
        for(Node child : super.getChildren()) {
        	System.out.println(child);
        	
            TicTacTwoNode ticTacTwoChild = (TicTacTwoNode) child;
            if(ticTacTwoChild.board.checkForWin()) {
                return board.findWinningMove(ticTacTwoChild.board);
            }
            char player = board.getPlayer();
            double totalChildCount = ticTacTwoChild.getDraws() + ticTacTwoChild.getXWins() + ticTacTwoChild.getOWins();
            double score = (ticTacTwoChild.getDraws() * 0.5 + player == 'X' ? ticTacTwoChild.getOWins() : ticTacTwoChild.getXWins()) / totalChildCount;
            System.out.println("score: " + score);
            
            if(score >= maxScore) {
                bestChild = ticTacTwoChild;
                maxScore = score;
            } 
        }
        	System.out.println("Max score " + maxScore);
            return board.findWinningMove(bestChild.board);
    }

    public String toString() {
        // TODO: Update this to nicely present this Node
        //return String.format("TODO: %s", "Update me!!!");
        countResults();
        return String.format("x Wins: %d\no Wins: %d\ndraws: %d\n%s", xWins, oWins, draws, board.toString());

    }

    public String getBoardString() {
        return board.toString();
    }

    public void countResults() {
        if (this.getChildren().size() == 0) {
            switch(board.getWinner()) {
                case 'X':
                    xWins = 1;
                    break;
                case 'O':
                    oWins = 1;
                    break;
                default:
                    draws = 1;
            }
            return;
        }
        xWins = 0;
        oWins = 0;
        draws = 0;
        for(Node child : super.getChildren()) {
            TicTacTwoNode ticTacTwoChild = (TicTacTwoNode) child;
            ticTacTwoChild.countResults();
            xWins += ticTacTwoChild.getXWins();
            oWins += ticTacTwoChild.getOWins();
            draws += ticTacTwoChild.getDraws();
        }
    }

    public int getXWins() {
        return this.xWins;
    }

    public int getOWins() {
        return this.oWins;
    }

    public int getDraws() {
        return this.draws;
    }

    public boolean checkForWin() {
        return board.checkForWin();
    }

    public boolean isBoardFilled() {
        return board.isFilled();
    }

    public int getBoardSize() {
        return board.getSize();
    }

    public boolean isValidMove(int row, int col) {
        return board.isValidMove(row, col);
    }

    public void makeMove(int row, int col) {
        board.makeMove(row, col, true);
    }

    public TicTacTwoNode clone() {
        return new TicTacTwoNode(new Board(this.board));
    }
}
