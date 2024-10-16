package Forest;

/*
 * This is the Base Node class.
 * This class has a list of children.
 * This class prints its adress since it has no information.
 *
 * Derived classes should:
 *   - have some information.
 *   - override toString()
 */
import java.util.ArrayList;
import java.util.List;

public class Node {

    // Activity #1: Code along
    // Add the following:
    //   - List of Nodes for the children
    //   - constructor to create empty List
    //   - addChild(node)
    //   - getChildren();
    //   - toString()
	private List<Node> children;

	public Node() {
		
		//empty list of children
		children = new ArrayList<>();
	}
	
	public void addChild(Node child) {
		this.children.add(child);
	}
	
	public List<Node> getChildren() {
		return this.children;
	}
	
	// returns a random child.
	// if no children, returns self.
	public Node getRandomChild() {
		if (hasChildren()) {
			int index = (int) (Math.random() * children.size());
			return children.get(index);
		}else {
			return this;
		}
	}
	
	

	public boolean hasChildren() {
    	return this.children.size() > 0;
    }
    public void clickEvent() {
        System.out.println("That tickled!");
    }
}
