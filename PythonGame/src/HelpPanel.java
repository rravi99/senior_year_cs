import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JButton;
public class HelpPanel extends AnimatedPanel implements ActionListener{
	


		private MainFrame mainFrame;
		private URL resource;
		private JButton home;

		private BufferedImage image;
		
		public HelpPanel(MainFrame main) {
			this.repaint();
			mainFrame = main;
			this.setBackground(new Color(33, 145, 10));
			try {
			      image = null;
			      resource = getClass().getResource("/Fruit/HelpScreen.png");
			      image = ImageIO.read(resource);
			    } catch (IOException e1) {
			      e1.printStackTrace();
			    }
			createComponents();
		}
		private void createComponents() {
			home = new JButton("HOME");
			this.setLayout(null);
			
			home.setBounds(820,20,300,100);
			home.addActionListener(this);
			this.add(home);
		}
		@Override
	    public Dimension getPreferredSize() {
	        return new Dimension(MainFrame.WIDTH, MainFrame.HEIGHT);
	    }

		@Override
		public void actionPerformed(ActionEvent e) {
			String s = e.getActionCommand();
			
			if(s.equals("HOME")){
				mainFrame.showPanel(0);
			} 
			
		}
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setColor(Color.GREEN);
			//g.drawImage(image,0,-100,this);
			g.drawImage(image,getWidth()/2 - image.getWidth() / 2,getHeight()/10 - 50,this);
		}

		
	}


