package Forest;

/*
 * This class:
 *    - is a node for a Binary Search Tree... a BSTNode.
 *    - inherits from Node so that the BST can be drawn.
 *    - IS-A Node that HAS-A integer
 * 
 * This class will:
 *   - provide an abstraction for Left and Right children.
 *   - have getters/setters for Left/Right children.
 *   - have a default constructor to hold a simple zero integer value.
 *   - have a constructor to set this node to a specific integer value.
 *   - implement Comparable<> for convenient comparisons.
 *   - override toString() to display information nicely.
 *
 */
public class BSTNode extends Node implements Comparable<BSTNode> {
	
    // TODO: add instance fields as needed
	private int value;
	
	public BSTNode() {
		this(0);
	}
	
	public BSTNode(int info) {
		super();
		
		// TODO: Add two null children nodes
		// always gonna have children, but one or both may be null
		// emphasizes absence
		this.getChildren().add(null);
		this.getChildren().add(null);
        
        // TODO: add info to this node's state
		this.value = info;
	}
	
	public void setLeft(BSTNode node) {
		this.getChildren().set(0, node);
	}
	
	public void setRight(BSTNode node) {
		this.getChildren().set(1, node);
	}
	
	
	public BSTNode getLeft() {
		
		// dog in pet basket
        return(BSTNode) this.getChildren().get(0);
	}
	
	public BSTNode getRight() {
		
		return(BSTNode) this.getChildren().get(1); 
	}
	
	public int getInfo() {
		// Not yet implemented
        return this.value;
	}

    @Override
    public void clickEvent() {
        // TODO: print this sub-tree in-order
    	//processInOrder(this);
    	System.out.println(nodeHeight(this));
    	
    }
    
    public int processPostOrder(BSTNode node) {
    	if (node == null) {
    		
    		// if null, return 0-doesn't add to child count
    		return 0;
    	}else {
    
    		// return 1 + nmbr of left children + nmbr of right children
    		return 1 + processPostOrder(node.getLeft()) 
    		+ processPostOrder(node.getRight());
    	}
    	
    }
    
    // total value
    public int totalValue(BSTNode node) {
    	// exit case!
    	if (node == null) {
    		
    		// if null, return 0
    		return 0;
    	}else {
    
    		// return 1 + nmbr of left children + nmbr of right children
    		// has to be node
    		// if u had this.getInfo(), it would add node's value * childrenCount
    		// look for patterns
    		return node.getInfo() + totalValue(node.getLeft()) 
    		+ totalValue(node.getRight());
    	}
    }
    
    public int nodeHeight(BSTNode node) {
    	if (node == null) {
    		return 0;
    	}else {
    		return 1 + Math.max(nodeHeight(node.getLeft()), nodeHeight(node.getRight()));
    	}
    }
    
    public void processInOrder(BSTNode node) {
    	if (node == null) {
    		return;
    	}
    	
    	processInOrder(node.getLeft());
    	System.out.println(node.getInfo() + " ");
    	processInOrder(node.getRight());
    }
    
    // TODO: override toString()
    @Override
    public String toString() {
    	return "     " + value;
    }

	@Override
	public int compareTo(BSTNode o) {
		// Not yet implemented
        //return 0;
        
        return this.getInfo() - o.getInfo();
	}
}
