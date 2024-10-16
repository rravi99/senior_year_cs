package Forest;

/*
 * This class implements a BST (Binary Search Tree)
 * All children on the Left are < self.
 * All children on the Right are >= self.
 *
 * The contents of each node is a simple primitive integer.
 *
 * This class:
 *   - provides the ability to add a value to the tree.
 *   - encapsulates the root node of the tree
 */ 

public class BSTTree extends Tree {
	
	public BSTTree() {
		super();
		this.setRoot(createNewNode((int) (Math.random() * 100)));
	}

    // Our parent adds a node randomly to the tree.
    // This class creates a random number and adds it
    // to the tree to the right location for that number.
    @Override
    public void add() {
        int num = (int) (Math.random()*100) + 1;
        add(num);
    }

    /**
    * Create a new node of the correct Base Type for this Tree.
    * Derived classes should override.
    * This allows us to reuse the add(value) method.
    */
    public BSTNode createNewNode(int value) {
        return new BSTNode(value);
    }
    
    /**
    * This adds a value to the BST Tree so that we preserve the properties
    * of a Binary Search Tree.
    *
    * We create a Node and either:
    *   a) Make it our root because this is the first node
    *   b) Add it to our root node by calling our helper method
    *
    * @param value The integer value to add to the tree
    */
	public void add(int value) {
        // Not yet implemented
        // Tip: use createNewNode() instead of "new BSTNode(value)"
		BSTNode next = this.createNewNode(value);
		BSTNode current = (BSTNode) getRoot();
		add(next, current);

	}
		
	

    /*
    * Create a private helper method will add a Node to the Tree starting
    * at some parent node.
    * This method uses recursion to add the node to the correct child. 
    */
    // TODO: Create a private helper method to add a node via recursion
	public void add(BSTNode next, BSTNode current) {
		if (current.compareTo(next) == 0) {
			return;
		}
		
		if (current.compareTo(next) > 0) {
			if (current.getLeft() == null) {
				current.setLeft(next);
				return;
			}
			current = current.getLeft();
		}else {
			// go right
			if (current.getRight() == null) {
				current.setRight(next);
				return;
			} 
			current = current.getRight();
		}
		add(next, current);
	}

    /*
    * This static method creates a tree for graphing & inspection.
    */
    public static Tree createSomeTree() {
		// TODO: update this with code to generate a more interesting Tree
        // that is of type BSTTree with BSTNodes.
    	BSTTree tree = new BSTTree();
        // randomly add some number of nodes to this tree
        int number  = (int) (Math.random() * 20) + 1;
        //tree.setRoot(new BSTNode(number));
        
        for (int index = 0; index < number; index++) {
        	tree.add();
        }
        //static so have to ref instance methods through the instance
        return tree;
	}

    public void printTreeInOrder() {
        // Not Yet Implemented
    }

}

