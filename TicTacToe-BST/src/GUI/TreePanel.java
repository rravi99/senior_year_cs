// DO NOT CHANGE!!

package GUI;

/*
  This class is responsible for drawing a Tree.
  The Tree must have nodes that inherit from Node.
  The Nodes should override toString() to allow for information
  to be displayed nicely.

  If a node is clicked on, the information is printed to the console.
  If a node is moused-over, the information is displayed in a popup window.
*/

import Forest.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

import javax.swing.JPanel;

public class TreePanel extends JPanel {

	private static final long serialVersionUID = 1L;
    public static final int MAX_ROW_COUNT = 40;
    
	private Node root;
    private Node visualRoot;
	private ArrayList<DrawInfo> queue;
	private int maxLevel;
	private int sizeMultiplier;
	private JPanel tipPanel = null;
	public String tipString = "test tool tip";
	private Font font = new Font("Monospaced", Font.PLAIN, 10);

	public TreePanel() {
		root = null;
        visualRoot = null;
		queue = null;
		maxLevel = 0;
		sizeMultiplier = 2;
		addEventHandlers();
		
		final TreePanel self = this;
		
		this.tipPanel = new JPanel() {
			@Override
			public void paintComponent(Graphics g){  
		        super.paintComponent(g);
				this.setBackground(Color.WHITE);
				g.clearRect(0, 0, this.getWidth(), this.getHeight());
				g.setColor(Color.BLACK);
				g.drawRect(0, 0, this.getWidth()-1, this.getHeight()-1);
				g.setFont(font);
                
				// if the tipString has multiple lines, draw on multiple lines
				String[] lines = self.tipString.split("\\n");
				// depending on the font, we have a line height. Just do a const for now.
				final int dy = 12;
				int y = dy + 5;
				for (String line : lines) {
					g.drawString(line, 5, y);
					y += dy;
				}
			}
		};
		
		tipPanel.setVisible(false);
		this.setBounds(0, 0, 200, 150);
		this.add(tipPanel);
	}

	public void setRoot(Node node) {
        root = node;
        setVisualRoot(root);
    }
    
	public void setVisualRoot(Node node) {
		visualRoot = node;
        //System.out.println("Creating drawing information...");
		createDrawInfo();
        //System.out.println("Balancing the drawing...");
		balanceDrawing();
        secondPassBalance();
        //System.out.println("Tree info is ready!");
        this.repaint();
	}

	public void changeSize(int delta) {
		sizeMultiplier += delta;
		sizeMultiplier = Math.max(1, Math.min(20, sizeMultiplier));
		this.setSize(MainFrame.WIDTH*sizeMultiplier, MainFrame.HEIGHT*4);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(MainFrame.WIDTH*sizeMultiplier, MainFrame.HEIGHT*4);
	}
	
	// do NOT override paint(Graphics g). Use paintComponent(Graphics g)
	// because...
	// Paint will draw borders and children
	@Override
	public void paintComponent(Graphics g){  
        super.paintComponent(g);
		this.setBackground(Color.WHITE);
		g.clearRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(Color.BLACK);

        //System.out.printf("Drawing Tree of size %d\n", queue.size());
		if (queue != null) {
			for (DrawInfo node : queue) {
                if (node.rowOrdinal <= MAX_ROW_COUNT) {
				    node.draw(g);
                }
			}
		}
		//System.out.println("Done drawing tree");
	}
	
	private void balanceDrawing() {
		// assure that the first child is at a good starting position
		// relative to the parent
		adjustForLeftChild(queue.get(0));
	}
	
	private void adjustForLeftChild(DrawInfo node) {
		if (node.getChildren().size() > 0) {
			DrawInfo child = (DrawInfo) node.getChildren().get(0);
			int acceptable = node.getChildren().size() / 2;
			if (child.rowOrdinal + acceptable < node.rowOrdinal) {
				// shift over all children at this level and to our right
				int shift = node.rowOrdinal - child.rowOrdinal - acceptable;
				shift(child, shift);
			}
			
			for (Node childNode : node.getChildren()) {
				adjustForLeftChild((DrawInfo) childNode);
			}
		}
	}
	
	private void shift(DrawInfo node, int shift) {
		// find the index of the node in the queue
		int index = 1;
		int level = node.level;
		node.setLocation(level, node.rowOrdinal + shift);
		while (queue.get(index) != node) {
			index++;
		}
		// shift until the node is not at the starting level
		boolean done = (index + 1 >= queue.size());
		while (!done) {
			index++;
			node = queue.get(index);
			if (node.level == level) {
				node.setLocation(level, node.rowOrdinal + shift);
				done = (index + 1 >= queue.size());
			} else {
				done = true;
			}
		} 
	}

	private void createDrawInfo() {
		queue = new ArrayList<DrawInfo>();
		DrawInfo diNode = new DrawInfo(visualRoot);
            
		int currentIndex = 0;
		maxLevel = 0;
		// add the root node to the queue directly
		diNode.setLocation(0, 0);
		queue.add(diNode);
		
		// while we have nodes left in the queue, get current (front)
		// and add all of its children to our queue and tree-mirror
		while (currentIndex < queue.size()) {
			diNode = queue.get(currentIndex);
			
			maxLevel = diNode.level;
			if (diNode.node != null) {
				if (diNode.node.getChildren().size() > 0) {
					maxLevel++;
				}
                // keep on adding all children... but only if we have space
                if (diNode.rowOrdinal <= MAX_ROW_COUNT) {
				    for (Node child : diNode.node.getChildren()) {
    					DrawInfo childInfo = new DrawInfo(child);
    					// build our tree-mirror
    					diNode.addChild(childInfo);
    					addNodeToQueue(childInfo, maxLevel);
    				}
                }
			}
			currentIndex++;
		}
		
	}
	
	private void addNodeToQueue(DrawInfo node, int level) {
		
		DrawInfo lastNode = queue.get(queue.size()-1);
		int rowOrdinal = lastNode.rowOrdinal + 1;
		if (lastNode.level != level) {
			// starting a new row
			rowOrdinal = 0;
		}
		node.setLocation(level, rowOrdinal);
		queue.add(node);
	}
	
	private void addEventHandlers() {
		// a mouse listener requires a full interface with lots of methods.
		// to get around having implement all, we use the MouseAdapter class 
		// and override just the one method we're interested in.
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent me) { 
				onMouseClicked(me); 
			}
		});
		
		this.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent me) {
				onMouseMoved(me);
			}
		});
		
	}    
	/**
	 * The method that gets called when the user moves the mouse.
	 * This will find the node moved over and print information on the node.
	 * @param me The MouseEvent data structure provided by the event.
	 */
	private void onMouseMoved(MouseEvent me) {
		// get the coordinates.
		int x = me.getX();
		int y = me.getY();
		// System.out.printf("Mouse Moved to (%d, %d)\n", x, y);
		if (queue != null && queue.size() > 0) {
			for (DrawInfo node : queue) {
				if (node.inside(x, y)) {
					if (node.node == null) {
						// inside an X... empty node. show nothing
						return;
					}
					// show the informative text for this node
					this.tipString = node.toString();
					this.tipPanel.setBounds(x, y, 100, 130);
					this.tipPanel.setVisible(true);
					this.tipPanel.repaint();
					return;
				}
			}
		}
		// not over a node. hide the tooltip.
		this.tipPanel.setVisible(false);
	}
	
	/**
	 * The method that gets called when the user clicks the mouse.
	 * This will find the node clicked on and print information on the node.
	 * @param me The MouseEvent data structure provided by the event.
	 */
	private void onMouseClicked(MouseEvent me) {
        boolean singleClick = (me.getClickCount() < 2);
        if (singleClick && me.getButton() == MouseEvent.BUTTON3) {
            System.out.println("Resetting to top root");
            setVisualRoot(root);
            this.repaint();
            return;
        }
		// get the coordinates.
		int x = me.getX();
		int y = me.getY();
		//System.out.printf("Mouse Clicked at (%d, %d)\n", x, y);
		if (queue != null && queue.size() > 0) {
			for (DrawInfo node : queue) {
				if (node.inside(x, y)) {
                    if (singleClick) {
                        node.node.clickEvent();
                    } else {
                        System.out.println("Set new visual Root");
                        setVisualRoot(node.node);
                        this.repaint();
                    }
                    // break out of loop since we found our Node
                    break;
				}
			}
		}
	}

    // Second Pass balancing will start near the bottom of the tree (2nd to last row).
    // Start at the right and make the parent be the average rowOrdinal of its children.
    private void secondPassBalance() {
        // Only do this if we have fewer than ~75 nodes
        final int max = 75;
        int nodeCount = countNodes(this.queue.get(0), max);
        if (nodeCount > max) {
            System.out.println("Too many nodes to do second pass balance");
            // do nothing!
            return;
        }
        Map<Integer, List<DrawInfo>> map = createLevelMap();

        // sort each level by rowOrdinal and calc averages
        for (int level = this.maxLevel - 1; level >=0; level--) {
            List<DrawInfo> listAtLevel = map.get(level);
            Collections.sort(listAtLevel);

            int lastRowOrdinal = -1;
            // Calc the average ordinal of node's children 
            // for every node in this level
            for (DrawInfo node : listAtLevel) {
                int rowOrdinalSum = 0;
                for (Node child : node.getChildren()) {
                    rowOrdinalSum += ((DrawInfo)(child)).rowOrdinal;
                }
                // set rowOrdinal to the new average (default to node's rowOrdinal)
                // rowOrdinal must be greater than left-sibling's rowOrdinal
                int rowOrdinal = node.rowOrdinal;
                if (node.getChildren().size() > 0) {
                    rowOrdinal = rowOrdinalSum / node.getChildren().size();
                }
                rowOrdinal = Math.max(lastRowOrdinal+1, rowOrdinal);
                lastRowOrdinal = rowOrdinal;
                node.setLocation(level, rowOrdinal);
            }
        }
    }

    private int countNodes(Node node, int max) {
        // 1 for ourself
        int count = 1;
        for (Node child : node.getChildren()) {
            count += countNodes(child, max);
            if (count > max) {
                break;
            }
        }
        return count;
    }
    
    // Create a map of List<DrawInfo>. Don't add the last level.
    //     recur through the tree to get all nodes
    //        if new level, add List<DrawInfo> to the level
    //        Add node to List<DrawInfo> at this level
    private Map<Integer, List<DrawInfo>> createLevelMap() {
        Map<Integer, List<DrawInfo>> map = new HashMap<>();
        // intialize with lists
        for (int level = 0; level <= this.maxLevel; level++) {
            map.put(level, new ArrayList<DrawInfo>());
        }

        // recursively add all nodes to the map
        addNodeToLevelMap((DrawInfo) queue.get(0), map);
        return map;
    } 

    private void addNodeToLevelMap(DrawInfo node, Map<Integer, List<DrawInfo>> map) {
        // add self to the List at the node's level
        List<DrawInfo> nodes = map.get(node.level);
        nodes.add(node);

        // recursively add all children
        for (Node child : node.getChildren()) {
            // if child is null, create a placeholder DrawInfo node
            if (node.node == null) {
                System.out.printf("X DI at Level:%d ordinal:%d\n",
                                 node.level, node.rowOrdinal);
            }
            addNodeToLevelMap((DrawInfo) child, map);
        }
    }
}
