package Forest;

import java.util.List;

// Implements a basic Tree 
public class Tree {

    // Activity #1: Code along
    // Add the following:
    //   - root Node
    //   - 2 constructors: no argument, root argument
    //   - setRoot(node)
    //   - getRoot()
    //   - add(): adds a node to some random location
    //   - createSomeTree()
	
	private Node root;
	public Tree() {
		this(new Node());
	}

    public Tree(Node root) {
        setRoot(root);
    }

    public void setRoot(Node root) {
        this.root = root;
    }
    
    public Node getRoot() {
        // Not Yet Implemented
        return this.root;
    }

    /**
    * add one Node randomly to the Tree
    */
    public void add() {
    	//add(root, new Node());
        Node child = new Node();
        double adder = Math.random();
        Node parent = root;
        while(parent.hasChildren() && Math.random() > 0.1) {
        	parent = parent.getRandomChild();
        }
        
        parent.addChild(child);
    }
    
    public void add(Node parent, Node child) {
    	if (parent.hasChildren() && Math.random() > 0.25) {
    		
    		//recursive
    		
    		add(parent.getRandomChild(), child);
    	} else {
    		parent.addChild(child);
    	}
    }
    
    public static Tree createSomeTree() {
        Tree tree = new Tree();
        // randomly add some number of nodes to this tree
        int number  = (int) (Math.random() * 20) + 1;
        for (int index = 0; index < number; index++) {
        	tree.add();
        }
        //static so have to ref instance methods through the instance
        return tree;
    }
}
