// You must KEEP a few methods as commented below.
// Otherwise, add methods to implement a GUI version of TicTacToe

/*
 This class is responsible for:
    - drawing the TicTacToe board.
    - receiving user clicks and interaction (user events)
    - forwarding all user events appropriately

 All user interactions should be abstracted and forwarded to TicTacToe classes.
*/

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.Timer;

public class GamePanel extends AnimatedPanel implements ActionListener {
	private int cellWidth;
	private int cellHeight;
	private Board board;
	private BufferedImage image;
	private int score;
	private Point snakeHead;
	private Point fruitCoord;
	private MainFrame mainFrame;
	private String lastDirection;
	private String currDirection;
	private Timer timer;
	private Clip sound;
	private boolean noKeyPressed;
	private boolean gameRunning;
	String direction = "";

	// TODO: have instance fields for the current turn and what the AI is (X or O)

	// Keep this instance field

	public GamePanel(MainFrame main) {
		gameRunning = true;
		noKeyPressed = true;
		mainFrame = main;
		board = new Board(17, 7, 4);

		this.setFocusable(true);
		this.score = 0;

		int delay = 1000; // milliseconds

		URL resource;
		resource = getClass().getResource("Fruit/grass.png");

		try {
			image = ImageIO.read(resource);
		} catch (IOException e) {
			e.printStackTrace();
		}
		snakeHead = new Point(0, 0);
		fruitCoord = new Point(0, 0);
		try {
			File file = new File("src/8-bit-jump-001-171817.wav");
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(file));
			this.sound = clip;
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.addKeyListener(new MyKeyAdapter());
		timer = new Timer(500, this);
		timer.start();
	}

	// Keep this method!

	@Override
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		this.setBackground(Color.WHITE);
		g.setColor(new Color(106, 211, 87));
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		int cellWidth = this.getWidth() / 17;
		int cellHeight = (this.getHeight() - this.getHeight() / 9) / 17;

		this.cellWidth = cellWidth;
		this.cellHeight = cellHeight;

		g.setColor(Color.BLACK);
		for (int i = 1; i < 17; i++) {
			g.drawLine(cellWidth * i, this.getHeight() / 9, cellWidth * i, this.getHeight());
			g.drawLine(0, this.getHeight() / 9 + cellHeight * i, this.getWidth(),
					this.getHeight() / 9 + cellHeight * i);
		}

		g.drawImage(image, 0, 0, this.getWidth(),this.getHeight(), this);
		g.setColor(new Color(179, 185, 178));
		g.fillRect(0, 0, this.getWidth(), this.getHeight() / 9);
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", Font.BOLD, 28));
		g.drawString("" + score, 50, 50);

		drawSnake(g);
		drawFruit(g);

	}

	private void drawSnake(Graphics g) {
		Python jack = this.board.getPython();
		ArrayList<Point> points = jack.getPoints();

		for (Point point : points) {
			g.setColor(jack.getColor());
			g.fillRect(point.getX() * this.cellWidth, point.getY() * this.cellHeight + (this.getHeight() / 9),
					this.cellWidth, this.cellHeight);

		}

		// int row = snakeHead.getRow();
		// snakeHead.setRow(snakeHead.getCol() * this.cellWidth );
		// snakeHead.setCol(row * this.cellHeight + (this.getHeight() / 9));
		// snakePoints = point.get(points.size-1);
		// g.setColor(Color.BLACK);
		// g.fillOval(points.get(points.size() - 1).getCol() * this.cellWidth,
		// points.get(points.size() - 1).getRow() * this.cellHeight, cellHeight/5,
		// cellHeight/5);
	}

	private void drawFruit(Graphics g) {
		Fruit fruit = board.getFruit();
		Point fruitLoc = fruit.getFruitLoc();
		// System.out.println(fruitLoc);

		BufferedImage image = null;
		URL resource;
		resource = getClass().getResource(fruit.getFruitPath());

		try {
			image = ImageIO.read(resource);
		} catch (IOException e) {
			e.printStackTrace();
		}

		int xCoord = (fruitLoc.getX() * this.cellWidth);

		int yCoord = (fruitLoc.getY() * this.cellHeight); // - (cellHeight/2);
		// prevents fruit from being on the grey rectangle
		if (yCoord <= (this.getHeight() / 9)) {
			// yCoord += (this.getHeight() / 9) * 2;
			yCoord += this.cellHeight * 2;
		}

		int y = fruitLoc.getY();

		if (y == 2 || y == 0 || y == 1) {
			y += 1;
		} else if (y >= 1) {
			y -= 1;
		}

		System.out.println("fruit instance field: " + fruitLoc.getX() + " , " + fruitLoc.getY());
		fruitCoord = new Point(fruitLoc.getX() + 1, y);
		System.out.println("fruitCoord: " + fruitCoord);
		// System.out.printf("(%d, %d)\n", fruitLoc.getCol() * this.cellWidth, yCoord);
		g.drawImage(image, xCoord, yCoord, cellWidth * 3, cellHeight * 3, new Color(0.0f, 0.0f, 0.0f, 0.5f), this);
		// g.drawImage(image, xCoord, yCoord, cellWidth, cellHeight, new Color(0.0f,
		// 0.0f, 0.0f, 0.5f), this);
	}

	private boolean checkForDeath(String direction) {
		if (this.board.getPython().getCurrentHeadX() == 17 || this.board.getPython().getCurrentHeadY() == 17) {
			System.out.println("dead");
			gameRunning = false;
			return true;
		} else if (this.board.getPython().getCurrentHeadX() == 0 || this.board.getPython().getCurrentHeadY() == 0) {
			gameRunning = false;
			return true;
			// verify that this works with the rectangle
		} else {
			System.out.println("right if");
			System.out.println(checkSelfCollision(direction));
			if (checkSelfCollision(direction)) {
				gameRunning = false;
			}
			return checkSelfCollision(direction);
		}
	}

	private boolean checkSelfCollision(String direction) {
		ArrayList<Point> points = this.board.getPython().getPoints();
		Point head = points.get(points.size() - 1);
		switch (direction) {
		case "Right":
		case "→":

			return points.contains(new Point(head.getX() + 1, head.getY()));

		case "Left":
		case "←":
			System.out.println("head coords: " + head.getX() + ", " + head.getY());
			System.out.println("looking for: " + (head.getX() - 1) + ", " + head.getY());
			System.out.println(points);
			return points.contains(new Point(head.getX() - 1, head.getY()));
		case "Up":
		case "↑":
			return points.contains(new Point(head.getX(), head.getY() - 1));
		case "Down":
		case "↓":
			return points.contains(new Point(head.getX(), head.getY() + 1));
		default:
			// System.out.println("reached default");
			return false;
		}
	}

	/**
	 * This allows this dialog to be drawn at a good size.
	 */
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(MainFrame.WIDTH, MainFrame.HEIGHT);
	}

	private void fruitEaten() {
		if (board.getPython().ateFruit(fruitCoord)) {
			try {
				File file = new File("src/8-bit-jump-001-171817.wav");
				Clip clip = AudioSystem.getClip();
				clip.open(AudioSystem.getAudioInputStream(file));
				clip.start();
			} catch (Exception e) {
				e.printStackTrace();
			}

			System.out.println("nom");
			board.addFruit();
			board.addanotherPoint();
			score++;
			System.out.println("new fruit");

			// GamePanel.this.repaint();
		}
	}

	private void gameEnded() {
		if (checkForDeath(board.direction)) {

			try {
				File file = new File("src/youlose.wav");
				Clip clip = AudioSystem.getClip();
				clip.open(AudioSystem.getAudioInputStream(file));
				clip.start();
			} catch (Exception e) {
				e.printStackTrace();
			}

			mainFrame.showPanel(3);
			gameRunning = false;
			timer.stop();
		}

	}

	public void makeRepaint() {
		GamePanel.this.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (gameRunning) {
			if (!noKeyPressed) {
				board.updateSnakePos();
				fruitEaten();

				gameEnded();

			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e1) {

				e1.printStackTrace();
			}
		}
		repaint();
	}

	public class MyKeyAdapter extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			if (board.direction == null) {
				board.direction = "";
			}
			noKeyPressed = false;
			switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if (!board.direction.equals("Right")) {
					board.direction = "Left";
				}
				break;
			case KeyEvent.VK_RIGHT:
				if (!board.direction.equals("Left")) {
					board.direction = "Right";
				}
				break;
			case KeyEvent.VK_UP:
				if (!board.direction.equals("Down")) {
					board.direction = "Up";
				}
				break;
			case KeyEvent.VK_DOWN:
				if (!board.direction.equals("Up")) {
					board.direction = "Down";
				}
				break;
			}
		}

	}

}
