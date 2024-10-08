import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JButton;

public class StartPanel extends AnimatedPanel implements ActionListener {
	private MainFrame mainFrame;
	private URL resource;
	private JButton play;
	private JButton help;
	private BufferedImage title;
	private boolean done;

	public StartPanel(MainFrame main) {
		this.repaint();
		done = false;
		mainFrame = main;
		// this.setBackground(new Color(33, 145, 10));

		try {
			title = null;

			resource = getClass().getResource("/Fruit/Python.png");
			title = ImageIO.read(resource);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		createComponents();
		// keepPainting();
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				repaint();
			}
		});
	}

	private void createComponents() {
		play = new JButton("PLAY");
		help = new JButton("HELP");
		this.setLayout(null);
		System.out.print("HEIGHT IS: " + this.getHeight());
		play.setBounds(670, 400, 600, 150);

		help.setBounds(800, 600, 330, 100);
		help.addActionListener(this);
		play.addActionListener(this);
		this.add(help);
		this.add(play);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(MainFrame.WIDTH, MainFrame.HEIGHT);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String s = e.getActionCommand();

		if (s.equals("PLAY")) {
			mainFrame.showPanel(1);
		} else if (s.equals("HELP")) {
			// mainFrame.updateScroll(2);
			mainFrame.showPanel(2);
		}
		this.repaint();

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.GREEN);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.drawImage(title, getWidth() / 2 - title.getWidth() / 2, getHeight() / 10 - 50, this);

	}

	public void keepPainting() {
		while (!done) {
			this.repaint();
		}
	}

}
