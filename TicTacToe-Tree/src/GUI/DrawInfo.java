// DO NOT CHANGE
package GUI;

/*
 * This class is the DrawInfo node of a tree used to draw other trees.
 * 
 * This class:
 *   - HAS-A  node  (of the tree it is actually illustrating/drawing)
 *   - HAS-A  (x,y) and (width, height)
 *   - level : the vertical level or height. Root = 0. 
 *   - rowOrdinal : The position of this node on this level of the drawn tree.
 *
 *   - IS-A  Node.  (This Node both IS-A Node and HAS-A Node)
 *
 * This DrawInfo class is a Node that is used to hold all the illustration
 * information that allows us to draw the Tree nicely. We construct
 * a "mirror tree" which is a Tree with the exact same structure (children,
 * height, etc) of the tree we are illustrating. But, this Tree is using
 * DrawInfo nodes to give us nice draing information.
 */
import Forest.*;
import java.awt.Graphics;


public class DrawInfo extends Node implements Comparable<DrawInfo>{
	
	private static final int WIDTH = 20;
	private static final int HEIGHT = 20;
	
	private int x;
	private int y;
	private int width;
	private int height;
	public int level;
	public int rowOrdinal;
	public Node node;
	
	public DrawInfo(Node node) {
		this.node = node;
	}

    /**
    * Allows the creator of this node to set its physical location
    * in the drawn tree. 
    * @param level The height in the drawn tree
    * @param rowOrdinal The horizontal position in the drawn tree
    */
	public void setLocation(int level, int rowOrdinal) {
		this.level = level;
		this.rowOrdinal = rowOrdinal;
		this.setPixelLocation(rowOrdinal*40, level*60 + 10);
	}

    // Sets the pixel location of where this node would be drawn
    // given the height and rowOrdinal.
	private void setPixelLocation(int x, int y) {
		this.x = x;
		this.y = y;
		this.width = WIDTH;
		this.height = HEIGHT;
	}

    /*
    * Draws a single node and the arrows to it's children.
    * If the arrows is out of scope of our MAX_ROW_COUNT (the limits
    * of what we want to draw) then we don't draw it.
    */
	public void draw(Graphics g) {
		// draw self only if we are not null
		if (this.node == null) {
			return;
		}
		g.drawOval(x, y, WIDTH, HEIGHT);
		// draw arrows to children
		for (Node child : this.getChildren()) {
			DrawInfo diChild = (DrawInfo) child;
            // don't draw arrows to children that are too far out
            if (diChild.rowOrdinal <= TreePanel.MAX_ROW_COUNT) {
			    drawArrow(g, diChild.node, x+WIDTH/2, y+HEIGHT, diChild.x+WIDTH/2, diChild.y-3);
            }
		}
	}

    /** 
    * The string representation of this Node is retrieved from
    * the actual tree we are illustrating.
    */
	public String toString() {
		return this.node == null ? "<null>" : this.node.toString();
	}
	
	public void drawArrow(Graphics g, Node child, int x1, int y1, int x2, int y2) {
		// draw arrow shaft
		g.drawLine(x1, y1, x2, y2-5);
		if (child == null) {
			// draw an X
			g.drawLine(x2-5, y2-5, x2+5, y2);
			g.drawLine(x2-5, y2, x2+5, y2-5);
		} else {
			// draw the arrow head
			g.drawLine(x2-5, y2-5, x2+5, y2-5);
			g.drawLine(x2+5, y2-5, x2, y2);
			g.drawLine(x2-5, y2-5, x2, y2);
		}
	}

    /**
    * Tells us if a point is inside this Node
    * @param x The x position to check
    * @param y The y position to check
    * @return true if the (x,y) point is inside this drawn Node
    */
	public boolean inside(int x, int y) {
		if (x > this.x && y > this.y) {
			return (x <= this.x + this.width && y <= this.y + this.height); 
		}
		return false;
	}

    /**
    * Compares this node to the other node to determine if they
    * are equal. It is the implemenation of Comparable<> interface.
    * We use the (x,y) position of this node to determine equality.
    * REVIEW: Do we even use this?
    * @param o The other object we are comparing against
    * @return 0 iff equal. negative if this < other. positive if this > other.
    */
	@Override
	public int compareTo(DrawInfo o) {
		// use x & y as equality
		if (this.x == o.x && this.y == o.y) {
			return 0;
		}
		if (this.x < o.x) {
			return -1;
		} else if (this.x > o.x) {
			return 1;
		}
		return Integer.compare(this.y, o.y);
	}

}
