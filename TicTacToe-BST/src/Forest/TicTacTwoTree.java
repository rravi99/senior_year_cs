package Forest;

import java.util.Arrays;

import TicTacToe.Board;

public class TicTacTwoTree extends Tree {

    
    public TicTacTwoTree() {
        super();
        this.setRoot(new TicTacTwoNode(new Board(2)));
    }

    
    /**
    * Create a Game Tree for TicTacTwo... where there is a
    * very simple 2x2 Board that is similar to TicTacToe.
    * In this 2x2 Board we don't care about wins or loses.
    * We only want to demonstrate that we can create a Tree
    * that represents the board all the moves and configurations.
    */

    public static Tree createGameTree() {
        TicTacTwoTree tree = new TicTacTwoTree();
        TicTacTwoTree.newMove((TicTacTwoNode) tree.getRoot());
        ((TicTacTwoNode) tree.getRoot()).countResults();
        return tree;
        // root is empty board
        // need to cretae every single possible board
        // if rue - x move
        // if false - o move
        // if a node has an empty board, create a new child node with
        // every empty spot filled w/ the appropriate character
        // don't create any more children for a node with a full board
        
    }


    public static void newMove(TicTacTwoNode node) {
        // base case
        if(node.checkForWin() || node.isBoardFilled()) {
            return;
        }
        int size = node.getBoardSize();
        for(int row = 0; row < size; row++) {
            for(int col = 0; col < size; col++) {
                TicTacTwoNode child = node.clone();
                if (child.isValidMove(row, col)) {
                    child.makeMove(row, col);
            
                    node.addChild(child);
                    TicTacTwoTree.newMove(child);
                }
            }
        }
    } 
}
