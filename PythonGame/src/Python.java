import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;

public class Python {

	private ArrayList<Point> points;
	private boolean alive;
	private boolean ateFruit;
	private Color color;
	public int currentHeadY;
	public int currentHeadX;
	private Point currentTail;
	private String currDirection;
	
	public String getCurrDirection() {
		return currDirection;
	}

	public void setCurrDirection(String direction) {
		this.currDirection = direction;
	}

	public Python() {
		alive = true;
		color = Color.BLUE;
		
		points = new ArrayList<>();
		points.add(new Point(1, 6));
		points.add(new Point(2, 6));
		points.add(new Point(3, 6));
		
		// might not work for checking pos-only calculated at beginning?
		currentHeadX = points.get(2).getX();
		currentHeadY = points.get(2).getY();
	}
	
	public void handleLength() {
		// in future: type of fruit
	}
	
	public void eatFruit() {
		this.ateFruit = true;
	}
	
	public void moveRight() {
		
	}
	
	public boolean ateFruit(Point fruitCoord) {
		return (currentHeadX == fruitCoord.getX()) && (currentHeadY == fruitCoord.getY());
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public ArrayList<Point> getPoints() {
		return points;
	}

	public void setPoints(ArrayList<Point> points) {
		this.points = points;
	}
	
	
	public int getCurrentHeadY() {
		return currentHeadY;
	}

	public void setCurrentHeadY(int currentHeadY) {
		this.currentHeadY = currentHeadY;
	}

	public int getCurrentHeadX() {
		return currentHeadX;
	}

	public void setCurrentHeadX(int currentHeadX) {
		this.currentHeadX = currentHeadX;
	}

	// swapped x and y, not sure where used: check on that
	public void addPoint(int x, int y) {
		points.add(new Point(x, y));
	}
	public void addPoint(int x, int y, int index) {
		points.add(index, new Point(x,y));
	}
	
	public Point removePoint() {
		return points.remove(0);
	}
	
	// checks if Jack is going in impossible direction (left directly over himself)
	public boolean isPoint(int x, int y) {
		Point nextTo = points.get(points.size() - 2);
		return (nextTo.getX() == x && nextTo.getY() == y);
	}
	
}