package gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

@SuppressWarnings("serial")
public class Board extends JPanel implements Runnable {

	private List<Coin> coins = new ArrayList<Coin>();
	private List<Field> fields = new ArrayList<Field>();

	private Thread runner = null;
	
	private boolean running = true;
	
	private int size_x = 4;

	private int size_y = 4;
	
	private Dimension dimension = new Dimension(400, 400);
	
	private int padding = 20;
	
    public Board() {
    	this.runner = new Thread(this);
    	this.runner.start();
    	
    	this.initListeners();
        
        this.setPreferredSize(this.dimension);
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        this.setBorder(BorderFactory.createLineBorder(Color.black));
		
//        JButton startButton = new JButton("Start");        
//        JButton stopButton  = new JButton("Stop");
//        
//        //... Add Listeners
//        startButton.addActionListener(new StartAction());
//        stopButton.addActionListener(new StopAction());
//        
//        //... Layout inner panel with two buttons horizontally
//        JPanel buttonPanel = new JPanel();
//        buttonPanel.setLayout(new FlowLayout());
//        buttonPanel.add(startButton);
//        buttonPanel.add(stopButton);
        
        //... Layout outer panel with button panel above bouncing ball
        this.setLayout(new BorderLayout());
//        this.add(buttonPanel, BorderLayout.NORTH);
        
        this.placeCoin(10, 10);
        

		int wx = (this.dimension.width - (2*this.padding)) / this.size_x;
		int wy = (this.dimension.height - (2*this.padding)) / this.size_y;

		for(int x = 0; x < this.size_x; x++) {
			for(int y = 0; y < this.size_y; y++) {
				this.fields.add(new Field(this.padding+x*wx, this.padding+y*wy, wx, wy));
			}
		}
    }//end constructor
	
	public void placeCoin(int x, int y) {
		Coin coin = new Coin(x, y, 20, 20, Color.YELLOW);
		this.coins.add(coin);
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);

		Graphics2D graphics2d = (Graphics2D) g;
		
		graphics2d.setColor(Color.BLACK);
		

		graphics2d.fill(new Ellipse2D.Double(22,22,22,22));

		for(Coin coin : this.coins) {
			coin.y++;
			coin.draw(graphics2d);
		}
//		for(Field field : this.fields) {
//			field.draw(graphics2d);
//		}
		

		int wx = (this.dimension.width - (2*this.padding)) / this.size_x;
		int wy = (this.dimension.height - (2*this.padding)) / this.size_y;
		for(int x = 0; x <= this.size_x; x++) {
			graphics2d.setStroke(new BasicStroke(2.0f));
			graphics2d.drawLine(this.padding+x*wx, this.padding, this.padding+x*wx, this.dimension.height-this.padding);
		}
		for(int y = 0; y <= this.size_y; y++) {
			graphics2d.setStroke(new BasicStroke(2.0f));
			graphics2d.drawLine(this.padding, this.padding+y*wy, this.dimension.width-this.padding, this.padding+y*wy);
		}
		
	}
	
	@Override
	public void run() {
		while(running) {
			super.repaint();
			try {
				Thread.sleep(40);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	private void initListeners() {
		this.addMouseListener(new MouseAdapter() {          
			public void mousePressed(MouseEvent me) { 
				System.out.println(me); 
          } 
		});
	}
    
    ////////////////////////////////////// inner listener class StartAction
    class StartAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.out.println("Reset");
        }
    }
    
    
    //////////////////////////////////////// inner listener class StopAction
    class StopAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
           System.out.println("Restart");
        }
    }
}