package Forest;

/*
 * This class:
 *   - IS-A BSTTree
 *       : Each node HAS-A value
 *       : has a max of two children
 *       : is "sorted" as a Binary Search Tree is sorted
 *   - assures that upon creation that we count all the children nodes
 */
public class BSTExtraTree extends BSTTree {
	
	public BSTExtraTree() {
		super();
		this.setRoot(createNewNode((int) (Math.random() * 100) + 1));
	}

    /**
    * Create a new node of the correct Base Type for this Tree.
    */
    @Override
    public BSTExtraNode createNewNode(int value) {
        return new BSTExtraNode(value);
    }

    public static Tree createSomeTree() {
        // TODO: Implement this to create a BSTExtraTree
        //       Create an empty tree
        //       Add 10-30 nodes with random values
        BSTExtraTree tree = new BSTExtraTree();
        int nodeNumber = (int) (Math.random() * 21) + 10;
     
        BSTExtraNode root = (BSTExtraNode) tree.getRoot();
        for (int num = 0; num <= nodeNumber; num++) {
        	//tree.add(tree.createNewNode((int) (Math.random() * 100) + 1), root);
        	tree.add();
        }
      
        root.countChildren();
        root.countHeight();
        root.sumTotal();
        // This is a placeholder that will be replaced and return the Tree
        // you just created.
        return tree;
	}
    
    
}