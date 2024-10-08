public class Board {
	private String[][] squares;
	private Python jack;
	private Fruit currFruit;
	private int size;
	public String direction;
	public Point lastPoint;
	
	
	public Board(int size, int x, int y) {
		this.size = size;
		squares = new String[this.size][this.size];
		jack = new Python();
		addFruit();
		
		// starts jack at 4. 7
		for (int i = 0; i < 3; i++) {
			squares[x-1][y-1-i] = "s";
		}
		
		
		
	}
	
	public void initaiteMovement() {
		
	}
	
	public Python getPython() {
		return this.jack;
	}
	public Fruit getFruit() {
		return this.currFruit;
	}
	public String[][] getSquares() {
		return this.squares;
	}
	
	public void addFruit() {
		this.currFruit = new Fruit();
		Point fruitLoc = this.currFruit.getFruitLoc();
		
		int x = fruitLoc.getX() + 1;
		int y = fruitLoc.getY();
    	
    	if (y == 2 || y == 0) {
    		y += 1;
    	}else if (y >= 1) {
    		y -= 1;
    	}
		
		
		for (Point point : jack.getPoints()) {
			if ((point.getX() == x) && (point.getY() == y)) {
				addFruit();
			}
		}
	}
	
public void updateSnakePos() {
		
		jack.setCurrDirection(direction);
		switch(direction) {
			case "Right":
			//case "→":
				moveRight();
				break;
			case "Left":
			//case "←":
				moveLeft();
				break;
			case "Up":
			//case "↑":
				moveUp();	
				break;
			case "Down":
			//case "↓":
				moveDown();
				break;
			default:
				System.out.println("IDK");
				break;
		}
		
	
	
	}
	
	private void moveDown() {
		if(!this.jack.isPoint(this.jack.currentHeadX, this.jack.currentHeadY + 1)) {
			//System.out.println("Old Head: " + this.jack.currentHeadX + ", " +  this.jack.currentHeadY);
			this.jack.addPoint(this.jack.currentHeadX, this.jack.currentHeadY + 1);
			this.jack.currentHeadY++;
			//System.out.println("New Head: " + this.jack.currentHeadX + ", " +  this.jack.currentHeadY);
			this.lastPoint = this.jack.removePoint();
			
		}
	}
	
	private void moveUp() {
		if(!this.jack.isPoint(this.jack.currentHeadX, this.jack.currentHeadY - 1)) {
			this.jack.addPoint(this.jack.currentHeadX, this.jack.currentHeadY - 1);
			this.jack.currentHeadY--;
			this.lastPoint = this.jack.removePoint();
			//System.out.println("New Head: " + this.jack.currentHeadX + ", " +  this.jack.currentHeadY);
			
		}
	}
	private void moveLeft() {
		if(!this.jack.isPoint(this.jack.currentHeadX - 1, this.jack.currentHeadY)) {
			this.jack.addPoint(this.jack.currentHeadX - 1, this.jack.currentHeadY);
			this.jack.currentHeadX--;
			this.lastPoint = this.jack.removePoint();
			//System.out.println("New Head: " + this.jack.currentHeadX + ", " +  this.jack.currentHeadY);
			
		}
	}
	private void moveRight() {
		if(!this.jack.isPoint(this.jack.currentHeadX + 1, this.jack.currentHeadY)) {
			//System.out.println("Old Head: " + this.jack.currentHeadX + ", " +  this.jack.currentHeadY);
			this.jack.addPoint(this.jack.currentHeadX + 1, this.jack.currentHeadY);
			this.jack.currentHeadX++;
			//System.out.println("New Head: " + this.jack.currentHeadX + ", " +  this.jack.currentHeadY);
			this.lastPoint = this.jack.removePoint();
			//System.out.println(jack.getPoints().toString());
			
			if(currFruit.getFruitLoc().getY() == this.jack.currentHeadY && currFruit.getFruitLoc().getX() == this.jack.currentHeadX) {
				System.out.println("EATEN");
			}
			//System.out.println("New Head: " + this.jack.currentHeadX + ", " +  this.jack.currentHeadY);
		}
		
		
		
	}

	

	
	public boolean moveSnake() {
		return false;
	}

	public void addanotherPoint() {
		this.jack.addPoint(this.lastPoint.getX(), this.lastPoint.getY(), 0);
		
	}
	
	
}