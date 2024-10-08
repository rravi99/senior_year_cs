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




public class EndPanel extends AnimatedPanel implements ActionListener{
	private MainFrame mainFrame;
	private URL resource;
	private JButton exit;
	private BufferedImage title;
	private boolean done;
	
	public EndPanel(MainFrame main) {
		this.repaint();
		done = false;
		mainFrame = main;
		//this.setBackground(new Color(33, 145, 10));
		
		try {
		      title = null;
		    
		      resource = getClass().getResource("/Fruit/end.png");
		      title = ImageIO.read(resource);
		    } catch (IOException e1) {
		      e1.printStackTrace();
		    }
		createComponents();
		//keepPainting();
		addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                repaint();
            }
        });
	}

	private void createComponents() {
		exit = new JButton("EXIT");
		this.setLayout(null);
		System.out.print("HEIGHT IS: " + this.getHeight());
		exit.setBounds(670,400,600,150);
		//play.setBounds((title.getWidth() - title.getWidth()/500 - title.getWidth()/2),title.getHeight()/ 10 + (title.getHeight() - title.getHeight()/10),300,100);
		//title,getWidth()/2 - title.getWidth() / 2,getHeight()/10 - 50,this
	
		exit.addActionListener(this);
		this.add(exit);
		
	}
	@Override
    public Dimension getPreferredSize() {
        return new Dimension(MainFrame.WIDTH, MainFrame.HEIGHT);
    }
	

	@Override
	public void actionPerformed(ActionEvent e) {
		String s = e.getActionCommand();
		
		if(s.equals("EXIT")){
			mainFrame.setVisible(false);
		} 
		this.repaint();
		
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.drawImage(title,getWidth()/2 - title.getWidth() / 2,getHeight()/10 - 50,this);
		
	}
	
	public void keepPainting() {
		while(!done) {
			this.repaint();
		}
	}

	/*public void onResize(int w, int h) {
       // try {
           // title = resizeImage(getBackgroundImage(w, h), w, h);
            int buttonWidth = this.getWidth() / 5;
            int buttonHeight = this.getHeight() / 12;
            play.setBounds((this.getWidth() - buttonWidth) / 2, this.getHeight() / 4, buttonWidth, buttonHeight);
            help.setBounds((this.getWidth() - buttonWidth) / 2, this.getHeight() / 3, buttonWidth, buttonHeight);
       
        //} /*catch (IOException e) {
            e.printStackTrace();
      //  } */
		
	//}
	

	
}
