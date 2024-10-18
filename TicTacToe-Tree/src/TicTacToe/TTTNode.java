package TicTacToe;

import Forest.Node;
import Forest.TicTacTwoNode;

/*
 * This class 
 */
public class TTTNode extends Forest.TicTacTwoNode {

    // Instance fields should store information about the board,
    // wins, losses, draws and which move is the best
	// Add instance fields here

    // Create Node that represents the board at this stage
	public TTTNode() {
        // Must call super() first!
        super(new Board(3));
	}
	
	public TTTNode(Board board) {
        
        // Must call super() first!
        super(board);
    }
    
	
	public TTTNode clone() {
        return new TTTNode(new Board(this.board));
    }
    
}