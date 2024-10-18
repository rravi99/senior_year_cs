package Forest;

/*
 * This class:
 *   - IS-A BSTNode
 *       : Each node HAS-A value
 *       : is "sorted" as a Binary Search Tree is sorted
 *   - HAS-A count of how many total children (all descendants)
 */
public class BSTExtraNode extends BSTNode {
    // TODO:
    //   - add children count instance field
	private int count;
	private int total;
	private int height;

    public BSTExtraNode(int value) {
        super(value);
    }
    
    public String toString() {
    	this.countChildren();
    	this.countHeight();
    	this.sumTotal();
    	return super.toString() + "\ncount: " + this.count + "\ntotal: " + this.total +  "\nheight: " + this.height;
	}

    public void countChildren() {
        this.count = super.processPostOrder(this);
    }

    public void countHeight() {
        this.height = super.nodeHeight(this);
    }

    public void sumTotal() {
        this.total = super.totalValue(this);
    }
    
    public int getCount() {
    	return this.count;
    }
    
    public int getHeight() {
    	return this.height;
    }
    
    public int getTotal() {
    	return this.total;
    }
    
    
}