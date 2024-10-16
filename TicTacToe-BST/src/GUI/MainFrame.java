// DO NOT CHANGE

package GUI;

/*
 * There are only two things the student will change in this file.
 * These will be changed only when starting on TicTacToe.
 *
 * 1) private Node getTreeToDisplay() { ... }
 * 2) The imports to allow proper creation of the TicTacToe Tree.
 */
import Forest.*;
import TicTacToe.GameTree;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

public class MainFrame extends JFrame {

	public static final int WIDTH = 400;
	public static final int HEIGHT = 400;
	
	private static final int TREE = 0;
	private static final int GAME = 1;

    private TreePanel treePanel = null;
    private GamePanel gamePanel = null;
	private JScrollPane scroll = null;

    enum TreeType {
        BASE,
        BST,
        BST_EXTRA,
        TICTACTWO_TREE,
        GAME_TREE
    };
    
	public static void startGUI() throws InterruptedException {
		MainFrame theGUI = new MainFrame();
		
		// Starts the UI Thread and creates the the UI in that thread.
		// Uses a Lambda Expression to call the createFrame method.
		// Use theGUI as the semaphore object
		SwingUtilities.invokeLater( () -> theGUI.createFrame(theGUI) );

		// Have the main thread wait for the GUI Thread to be done
		// creating the frame and all panels.
		// Q: What happens if the wait is deleted?
		// A: 
		synchronized (theGUI ) {
			theGUI.wait();
		}
	}
	
	/**
	 * Create the main JFrame and all animation JPanels.
	 * 
	 * @param semaphore The object to notify when complete
	 */
	public void createFrame(Object semaphore) {
		// Sets the title found in the Title Bar of the JFrame
		this.setTitle("Tree");
		// Sets the size of the main Window
		this.setSize(WIDTH, HEIGHT);
		// Allows the application to properly close when the
		// user clicks on the Red-X.
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		addMenuBar();
		
		// Add the Tree panel first
		treePanel= new TreePanel();
		// Set the size depending on the supposed Tree size
		treePanel.setSize(WIDTH*2, HEIGHT*4);
		// allow scroll panel to manage visibility of the Tree panel
		treePanel.setVisible(true);
		scroll = new JScrollPane(treePanel);
		scroll.setBounds(0,0,WIDTH,HEIGHT);
		scroll.setVisible(true);
		add(scroll);
	
		// create the game panel, but don't add it to the JFrame, yet.
        // The constructor will create the GameTree and learn.
		gamePanel = new GamePanel();
		gamePanel.setVisible(false);
		gamePanel.setSize(WIDTH, HEIGHT);
		gamePanel.setBounds(0,0,WIDTH,HEIGHT);
		
		// don't pack. Leave the main frame at its original size.
		// Packing causes the main JFrame to be larger/smaller depending
		// on the size of its children components.
		//this.pack();
		
		// create a Tree to show in the Tree Panel by
        // setting the root of the treePanel
        setTree(TreeType.BASE);
		
		// Set the current frame and this JFrame to be visible
		this.setVisible(true);
		
		// tell the main thread that we are done creating our stuff
		synchronized (semaphore) {
			semaphore.notify();
		}
	}
	
	/**
	 * Show either the Game Panel or the Tree Panel
	 * @param panelIndex Either Main.GAME or Main.TREE
	 */
	private void setVisible(int panelIndex) {
		// The drawing of the scroll bar and management of the layout
		// gets messed up if we simply show/hide the panel.
		// Instead. Remove & Add the panels while also showing/hiding.
		if (panelIndex == TREE) {
			this.remove(gamePanel);
			this.add(scroll);
		} else {
			this.remove(scroll);
			this.add(gamePanel);
		}
		scroll.setVisible(panelIndex == TREE);
		gamePanel.setVisible(panelIndex == GAME);
	}

	private void setTree(TreeType type) {
        Tree result = null;
        switch (type) {
            case BASE:
            default:
                result = Tree.createSomeTree();
                break;
            case BST:
                result = BSTTree.createSomeTree();
                break;
            case BST_EXTRA:
                result = BSTExtraTree.createSomeTree();
                break;
            case GAME_TREE:
                //result = gamePanel.getGameTree();
            	result = GameTree.createGameTree(new GameTree());
                break;
            case TICTACTWO_TREE:
                result = TicTacTwoTree.createGameTree();
                break;
        }
        treePanel.setRoot(result.getRoot());
	}
	
	/**
	 * Add some menu options to control the experience.
	 */
	private void addMenuBar() {
		
		JMenuBar bar = new JMenuBar();
		// Add the menu bar to the JFrame
		this.setJMenuBar(bar);
		
		// Add more top-level menu options for the specific animation panel
		JMenu menu = new JMenu("View");
		menu.setMnemonic('V');

        JMenu submenu = new JMenu("Tree Source");
        menu.setMnemonic('R');

        ButtonGroup treeGroup = new ButtonGroup();
        JMenuItem item = new JRadioButtonMenuItem("Base", true);
        item.setMnemonic('B');
        item.addActionListener(e -> setTree(TreeType.BASE));
        treeGroup.add(item);
		submenu.add(item);
        item = new JRadioButtonMenuItem("BST");
        item.setMnemonic('S');
        item.addActionListener(e -> setTree(TreeType.BST));
		treeGroup.add(item);
        submenu.add(item);
        item = new JRadioButtonMenuItem("BST Extra");
        item.setMnemonic('E');
        item.addActionListener(e -> setTree(TreeType.BST_EXTRA));
		treeGroup.add(item);
        submenu.add(item);
        item = new JRadioButtonMenuItem("TicTacTWO");
        item.setMnemonic('W');
        item.addActionListener(e -> setTree(TreeType.TICTACTWO_TREE));
		treeGroup.add(item);
        submenu.add(item);
        item = new JRadioButtonMenuItem("TicTacToe");
        item.setMnemonic('T');
        item.addActionListener(e -> setTree(TreeType.GAME_TREE));
		treeGroup.add(item);
        submenu.add(item);
        menu.add(submenu);

        ButtonGroup showGroup = new ButtonGroup();
		item = new JRadioButtonMenuItem("Show Tree", true);
        item.setMnemonic('S');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.CTRL_DOWN_MASK));
		item.addActionListener(e -> setVisible(TREE));
        showGroup.add(item);
		menu.add(item);
		item = new JRadioButtonMenuItem("Show TicTacToe");
        item.setMnemonic('T');
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.CTRL_DOWN_MASK));
		item.addActionListener(e -> setVisible(GAME));
        showGroup.add(item);
		menu.add(item);
		
		item = new JMenuItem("Increase Tree Canvas", 'I');
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, InputEvent.CTRL_DOWN_MASK));
		item.addActionListener(e -> treePanel.changeSize(1));
		menu.add(item);

		item = new JMenuItem("Decrease Tree Canvas", 'd');
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, InputEvent.CTRL_DOWN_MASK));
		item.addActionListener(e -> treePanel.changeSize(-1));
		menu.add(item);

		bar.add(menu);

        menu = new JMenu("TicTacToe");
        menu.setMnemonic('T');
		item = new JMenuItem("AI Starts", 'A');
		item.addActionListener(e -> gamePanel.setAIStarts(true));
		System.out.println("Hello sir how is your day");
		menu.add(item);
		item = new JMenuItem("Human Starts", 'H');
		item.addActionListener(e -> gamePanel.setAIStarts(false));
		menu.add(item);
        item = new JMenuItem("Restart");
		item.addActionListener(e -> gamePanel.resetBoard());
        menu.add(item);
        
        bar.add(menu);
	}
}
