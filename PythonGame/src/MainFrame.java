// DO NOT CHANGE

/*
 * There are only two things the student will change in this file.
 * These will be changed only when starting on TicTacToe.
 *
 * 1) private Node getTreeToDisplay() { ... }
 * 2) The imports to allow proper creation of the TicTacToe Tree.
 */

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class MainFrame extends JFrame {

	public static final int WIDTH = 400;
	public static final int HEIGHT = 400;

	private static final int GAME = 1;
	private static final int START_PANEL = 0;
	private static final int HELP_PANEL = 2;
	private static final int END_PANEL = 3;

	private static AnimatedPanel[] panels;

	private JScrollPane scroll = null;
	private JScrollPane scroll2 = null;

	private GamePanel gamePanel;
	private StartPanel startPanel;
	private HelpPanel helpPanel;
	private EndPanel endPanel;
	private int currentPanel = -1;

	public static void startGUI() throws InterruptedException {
		MainFrame theGUI = new MainFrame();

		// Starts the UI Thread and creates the the UI in that thread.
		// Uses a Lambda Expression to call the createFrame method.
		// Use theGUI as the semaphore object
		SwingUtilities.invokeLater(() -> theGUI.createFrame(theGUI));

		synchronized (theGUI) {
			theGUI.wait();
		}
	}

	public void showPanel(int index) {
		System.out.printf("Show Panel. Thread is: %s\n", Thread.currentThread().getName());

		// stop the current animation
		// MainFrame.done = true;

		// hide the current panel
		panels[currentPanel].setVisible(false);

		// show the correct panel
		currentPanel = index;
		panels[currentPanel].setVisible(true);
		panels[currentPanel].setFocusable(true);
		panels[currentPanel].setRequestFocusEnabled(true);
		panels[currentPanel].requestFocus();
	}

	public void updateScroll(int panel) {
		if (panel == 2) {
			scroll2 = new JScrollPane(helpPanel);
			scroll2.setBounds(0, 0, WIDTH, HEIGHT);
			scroll2.setVisible(true);
			add(scroll2);
		}
	}

	public void createFrame(Object semaphore) {
		this.panels = new AnimatedPanel[4];

		// Sets the title found in the Title Bar of the JFrame
		this.setTitle("Python");
		// Sets the size of the main Window
		this.setSize(WIDTH, HEIGHT);
		// Allows the application to properly close when the
		// user clicks on the Red-X.
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// addMenuBar();

		// Set the size depending on the supposed Tree size

		// allow scroll panel to manage visibility of the Tree panel

		// create the game panel, but don't add it to the JFrame, yet.
		// The constructor will create the GameTree and learn.
		gamePanel = new GamePanel(this);
		startPanel = new StartPanel(this);
		helpPanel = new HelpPanel(this);
		endPanel = new EndPanel(this);
		panels[GAME] = gamePanel;
		panels[START_PANEL] = startPanel;
		panels[HELP_PANEL] = helpPanel;
		panels[END_PANEL] = endPanel;

		for (AnimatedPanel panel : panels) {
			panel.setBounds(0, 0, WIDTH, HEIGHT);
			this.add(panel);
			panel.setVisible(false);
		}
		this.setSize(WIDTH, HEIGHT);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.currentPanel = START_PANEL;
		panels[currentPanel].setVisible(true);
		this.setVisible(true);

		// gamePanel.setBounds(0,0,WIDTH,HEIGHT);
		if (currentPanel == 0) {
			scroll = new JScrollPane(gamePanel);
			scroll.setBounds(0, 0, WIDTH, HEIGHT);
			scroll.setVisible(true);
			add(scroll);
		}

		this.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent componentEvent) {
				JFrame frame = (JFrame) componentEvent.getSource();
				int w = frame.getWidth();
				int h = frame.getHeight();
				for (AnimatedPanel panel : panels) {
					panel.setBounds(0, 0, w, h);
				}
				if (scroll != null) {
					scroll.setBounds(0, 0, w, h);
				}
				if (scroll2 != null) {
					scroll2.setBounds(0, 0, w, h);
				}
			}
		});

		// don't pack. Leave the main frame at its original size.
		// Packing causes the main JFrame to be larger/smaller depending
		// on the size of its children components.
		// this.pack();

		// create a Tree to show in the Tree Panel by
		// setting the root of the treePanel

		// Set the current frame and this JFrame to be visible
		this.setVisible(true);

		// tell the main thread that we are done creating our stuff
		synchronized (semaphore) {
			semaphore.notify();
		}
	}

}
