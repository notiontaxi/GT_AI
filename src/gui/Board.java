package gui;

import heuristic.BlockHeuristic;
import heuristic.HeuristicNOutOfFour;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;

import logic.*;
import main.Config;

@SuppressWarnings("serial")
public class Board extends JPanel implements Runnable {

	private List<Coin> coins = new ArrayList<Coin>();
	
	private List<Field> fields = new ArrayList<Field>();
	private Map<Integer , Color> colorPlayerMapping;
	private Config config;
	
	// Animation thread
	private Thread runner = null;
	private boolean running = true;
	
	private int size_x;

	private int size_y;
	
	private Dimension dimension = new Dimension(400, 400);
	
	private int padding = 20;
	
	private Logic logic;

	private JLabel itterationCounterLabel;
	
    public Board(Config config, Logic logic) {
    	// initialize Animation thread
    	this.runner = new Thread(this);
    	this.runner.start();
    	
    	this.logic = logic; 
		this.config = config;
		this.colorPlayerMapping = new HashMap<Integer, Color>();
		initColorPlayerMapping();
		initFields(this.config.getDimensionX(), this.config.getDimensionY());
		
		this.size_x = config.getDimensionX();
		this.size_y = config.getDimensionY();
		
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

        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new FlowLayout());
        itterationCounterLabel = new JLabel("000000000000000000");
        JLabel itterationCounter = new JLabel("Generated positions: ");
        labelPanel.add(itterationCounter);
        labelPanel.add(itterationCounterLabel);
        
        //... Layout outer panel with button panel above bouncing ball
        this.setLayout(new BorderLayout());
        this.add(labelPanel);
        
//        this.add(buttonPanel, BorderLayout.NORTH);
        
    }
	
    private void initFields(int width, int height) {	
		int wx = (this.dimension.width - (2*this.padding)) / width;
		int wy = (this.dimension.height - (2*this.padding)) / height;

		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				Field field = new Field(this.padding+x*wx, this.padding+y*wy, wx, wy);
				this.fields.add(field);
			}
		}
    }
    
	@Override
	public void paint(Graphics g) {
		super.paint(g);

		Graphics2D graphics2d = (Graphics2D) g;
		
		graphics2d.setColor(Color.BLACK);
		

		for(Coin coin : this.coins) {
			coin.draw(graphics2d);
		}

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
		
		for (Field field : this.fields) {
			graphics2d.fillPolygon(field);
		}
		
	}
	
	@Override
	/**
	 *  when using animations/implementing runnable
	 */
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
	
	private void initColorPlayerMapping(){
		colorPlayerMapping.put(0, Color.blue);
		colorPlayerMapping.put(1, Color.red);
	}
	
	
	private void initListeners() {
		this.addMouseListener(new MouseAdapter() {          
			public void mousePressed(MouseEvent me) { 
				if (logic.getWinner() == null && !logic.isGameOver()){
					
					BlockHeuristic bH = new BlockHeuristic(config.getRowLengthToWin());
					HeuristicNOutOfFour heuristicNOutOfFour = new HeuristicNOutOfFour();
					
					if(me.getX() > padding && me.getX() < dimension.getWidth() - padding && 
							me.getY() > padding && me.getY() < dimension.getHeight() - padding) {

						int wx = (dimension.width - (2*padding)) / size_x;
						int wy = (dimension.height - (2*padding)) / size_y;

						int xIndex = (int) (me.getX()-padding) / wx;
						int yIndex = (int) (me.getY()-padding) / wy;

						int x = xIndex * wx + padding;
						int y = yIndex * wy + padding;

						if (logic.performMove(xIndex, yIndex)){
							Coin coin = new Coin(x, y, wx, wy, colorPlayerMapping.get(logic.getActivePlayer()));
							coins.add(coin);
							Player winner = logic.getWinner();
							if (winner != null){
								System.out.println("Winner!!!!!! Congratulations " + winner.getName() + ".");
							} else if (logic.isGameOver()){
								System.out.println("Game Over. No Winner.");
							}
							repaint();
						}
						
						System.out.println("BlockHeuristic result (best col): " + bH.getBestColumn(logic.getBoard(), logic.getActivePlayer()));
						System.out.println("HeuristicNOutOfFour result (best col): " + heuristicNOutOfFour.getBestColumn(logic.getBoard(), logic.getActivePlayer()));
						
						

						if (logic.getWinner() == null && !logic.isGameOver()){ 
							//try {
								//MinMax minMax = new MinMax(logic);
								long startTime = System.currentTimeMillis();
								ThreadObsever to = new ThreadObsever(logic, config.getThreadCount());

//								Thread decisionThread = new Thread(to);
//								decisionThread.start();
//								while(!to.isDone()) {
//									NumberFormat f = new DecimalFormat();
//									itterationCounterLabel.setText(f.format(to.getTotalItterations()));
//									itterationCounterLabel.paintImmediately(itterationCounterLabel.getVisibleRect());
//									try {
//										Thread.sleep(100);
//									} catch (InterruptedException e) {
//										e.printStackTrace();
//									}
//								}
//								NumberFormat f = new DecimalFormat();
//								itterationCounterLabel.setText(f.format(to.getTotalItterations()));
//								itterationCounterLabel.paintImmediately(itterationCounterLabel.getVisibleRect());
								
								Coordinate c = to.getCoordinate();
								
								//Coordinate c = minMax.minmaxDecision();
								System.out.println("Final Duration: " + (System.currentTimeMillis() - startTime));
								if(c != null && logic.performMove(c.getX(), c.getY())) {
									Coin coin = new Coin(c.getX(), c.getY(), wx, wy, colorPlayerMapping.get(logic.getActivePlayer()));
									coins.add(coin);
								}

								Player winner = logic.getWinner();
								if (winner != null){
									System.out.println("Winner!!!!!! Congratulations " + winner.getName() + ".");
								} else if (logic.isGameOver()){
									System.out.println("Game Over. No Winner.");
								}

								repaint();
							/*} catch (CloneNotSupportedException e) {
								e.printStackTrace();
							}*/
						}

					}
				}
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