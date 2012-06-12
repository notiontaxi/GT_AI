package gui;

import heuristic.BlockHeuristic;
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
public class Board extends JPanel implements Runnable, ActionListener,
		ComponentListener, MouseListener {

	private List<Coin> coins = new ArrayList<Coin>();

	private List<Field> fields = new ArrayList<Field>();
	private Map<Integer, Color> colorPlayerMapping;
	private Config config;

	// Animation thread
	private Thread runner = null;
	private boolean running = true;

	private int size_x;

	private int size_y;

	private Dimension dimension = new Dimension(400, 400);

	private int padding = 0;

	private Logic logic;

	public Board(Config config, Logic logic) {

		this.addComponentListener(this);

		// initialize Animation thread
		this.runner = new Thread(this);
		this.runner.start();

		this.logic = logic;
		this.config = config;
		this.colorPlayerMapping = new HashMap<Integer, Color>();
		initColorPlayerMapping();
		resizeComponents(this.config.getDimensionX(), this.config.getDimensionY());

		this.size_x = config.getDimensionX();
		this.size_y = config.getDimensionY();

		this.initListeners();

		this.setPreferredSize(this.dimension);
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		this.setBorder(BorderFactory.createLineBorder(Color.black));
	}

	private void resizeComponents(int width, int height) {
		int wx = (this.getWidth() - (2 * this.padding)) / width;
		int wy = (this.getHeight() - (2 * this.padding)) / height;

		this.fields.clear();

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Field field = new Field(this.padding + x * wx, this.padding + y * wy, wx, wy);
				this.fields.add(field);
			}
		}

		for (Coin coin : this.coins) {
			coin.setWidth(wx);
			coin.setHeight(wy);
		}
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);

		Graphics2D graphics2d = (Graphics2D) g;

		for (Coin coin : this.coins) {
			coin.draw(graphics2d);
		}

		graphics2d.setColor(Color.BLACK);
		int wx = (this.getWidth() - (2 * this.padding)) / this.size_x;
		int wy = (this.getHeight() - (2 * this.padding)) / this.size_y;
		for (int x = 0; x <= this.size_x; x++) {
			graphics2d.setStroke(new BasicStroke(2.0f));
			graphics2d.drawLine(this.padding + x * wx, this.padding,
					this.padding + x * wx, this.getHeight() - this.padding);
		}
		for (int y = 0; y <= this.size_y; y++) {
			graphics2d.setStroke(new BasicStroke(2.0f));
			graphics2d.drawLine(this.padding, this.padding + y * wy,
					this.getWidth() - this.padding, this.padding + y * wy);
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
		while (running) {
			super.repaint();
			try {
				Thread.sleep(40);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void initColorPlayerMapping() {
		colorPlayerMapping.put(0, Color.blue);
		colorPlayerMapping.put(1, Color.red);
	}

	private void initListeners() {
		this.addMouseListener(this);
	}
	
	private void paintCoin(int x, int y){
		int wx = (this.getWidth() - (2 * padding)) / size_x;
		int wy = (this.getHeight() - (2 * padding)) / size_y;
		Coin coin = new Coin(x, y, wx, wy, colorPlayerMapping.get(logic.getActivePlayer()));
		coins.add(coin);
		
		Player winner = logic.getWinner();
		if (winner != null) {
			System.out.println("Winner!!!!!! Congratulations " + winner.getName() + ".");
		} else if (logic.isGameOver()) {
			System.out.println("Game Over. No Winner.");
		}

		repaint();
	}

	/*
	 * Performed actions (buttons)
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		AlphaBeta alphaBeta = new AlphaBeta(logic);
		Coordinate c = null;
		if (arg0.getActionCommand() == "move"){
			c = alphaBeta.alphaBetaSearch(logic.getBoard().getTopFields());
			System.out.println("\n---------------\nAlphaBtea: " + c.getX() + "," + c.getY()+ "\n---------------\n");
		} else if (arg0.getActionCommand() == "smallMove"){
			c = alphaBeta.smallAlphaBetaSearch(logic.getBoard().getTopFields());
			System.out.println("\n---------------\nAlphaBtea: " + c.getX() + "," + c.getY()+ "\n---------------\n");
		}
		logic.performMove(c.getX());
		paintCoin(c.getX(), c.getY());
	}

	@Override
	public void componentHidden(ComponentEvent e) {
	}

	@Override
	public void componentMoved(ComponentEvent e) {
	}

	@Override
	public void componentResized(ComponentEvent e) {
		this.resizeComponents(this.config.getDimensionX(),
				this.config.getDimensionY());
	}

	@Override
	public void componentShown(ComponentEvent e) {
	}

	/*
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent me) {
		if (logic.getWinner() == null && !logic.isGameOver()) {

			if (me.getX() > padding
					&& me.getX() < this.getWidth() - padding
					&& me.getY() > padding
					&& me.getY() < this.getHeight() - padding) {

				int wx = (this.getWidth() - (2 * padding)) / size_x;
				int x = (int) (me.getX() - padding) / wx;

				if (logic.performMove(x)) {
					paintCoin(x, logic.getTopField(x) + 1);
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
}