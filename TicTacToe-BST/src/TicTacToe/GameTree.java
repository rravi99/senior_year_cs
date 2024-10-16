package TicTacToe;

import Forest.Node;
import Forest.TicTacTwoNode;
import Forest.TicTacTwoTree;
import Forest.Tree;
import java.util.*;

/*
 * This class will:
 *   - create a Tree of Nodes that represent the TicTacToe Board
 *   - find a Node for a given Board and ask it to provide the best
 *     move to make for either X or O.
 */

public class GameTree extends Forest.TicTacTwoTree {

    // A very easy way to find a Node for a Board is to have
    // a HashMap of nodes that are indexed by a Boards.toString()
	private Map<String, TTTNode> treeNodes = new HashMap<>();
	
	public GameTree() {
		super();
		this.setRoot(new TTTNode());
	}
	/**
    * This method will create the full GameTree
    */
	public void learn() {
		System.out.println("learning...");
        // create an initial Board with no moves completed
        // create an empty Tree
        // recursively add nodes to the tree
		GameTree.createGameTree(this);
		System.out.println("Once a tree is created, I'm Smart!");
	}

    /**
    * A helper method to create a GameTree recursively.
    * This will add a child node for every possible move a user could
    * make. 
    * @param node The parent node for the given Board
    * @param board The current board.
    */ 
	public static GameTree createGameTree(GameTree tree) {
		TTTNode root = (TTTNode) tree.getRoot();
        tree.newMove(root);
        (root).countResults();
        tree.createBoardMap(root);
        //root.addToBoardMap();
        return tree;

	}
	
 

    private void createBoardMap(TTTNode node) {
        if (node.getChildren().size() == 0) {
            treeNodes.put(node.getBoardString(), node);
            return;
        }

        for (Node child : node.getChildren()) {
            TTTNode tttNodeChild = (TTTNode) child;
            createBoardMap(tttNodeChild);
        }

        treeNodes.put(node.getBoardString(), node);
    }

    public TTTNode getMapNode(String gameBoard) {
    	return treeNodes.get(gameBoard);
    }
    /**
    * Find the node that matches the provided board and return the best
    * move for the correct piece that should move next.
    * 
    * STUDENT: You may choose to not use the Move abstraction. That's is fine.
    *          Update this method as you see fit.
    *
    * @param Board the current state of the board
    * @return the best move to make.
    */
    /*
	public Move getBestMove(Board board) {
        // TODO: implement this.
		return null;
	}
	*/

    public static void newMove(TTTNode node) {
        // System.out.println(node.checkForWin() + node.getBoardString());
        // base case
        //System.out.println("Check for win: " + node.checkForWin());
        //System.out.println("Board filled: " + node.isBoardFilled());
      
        if(node.checkForWin() || node.isBoardFilled()) {
            return;
        }
        int size = node.getBoardSize();
        for(int row = 0; row < size; row++) {
            for(int col = 0; col < size; col++) {
                TTTNode child = node.clone();
                //System.out.println(child.board + " valid move: " + child.isValidMove(row, col));
                if (child.isValidMove(row, col)) {
                    child.makeMove(row, col);

                    node.addChild(child);
                    GameTree.newMove(child);
                }
            }
        }
    } 
}