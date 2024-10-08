public class Fruit {
	private String fruitPath;
	private Point fruitLoc;
	
	
	public Fruit() {
		//this.fruit = //Image of apple
		this.fruitPath = "Fruit/Apple.png";
		
		// 0 to 15
		this.fruitLoc = new Point(((int) (Math.random() * 16)), ((int) (Math.random() * 16)));
		
		// prevent fruit from appearing ON jack
		
          
	}


	public String getFruitPath() {
		return fruitPath;
	}


	public void setFruitPath(String fruitPath) {
		this.fruitPath = fruitPath;
	}


	public Point getFruitLoc() {
		return fruitLoc;
	}


	public void setFruitLoc(Point fruitLoc) {
		this.fruitLoc = fruitLoc;
	}
	
}
