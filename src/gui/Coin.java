package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;

public class Coin extends Ellipse2D.Double implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	final static BasicStroke stroke = new BasicStroke(1.0f); 
	
	Color color;

	public Coin(int x, int y, double width, double height, Color color) {
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("Coin action");
	}
	
	public void draw(Graphics2D graphics2d) {
		graphics2d.setColor(this.getColor());
		graphics2d.setStroke(Coin.stroke);
		graphics2d.fill(this);
		graphics2d.setColor(Color.BLACK);
		graphics2d.draw(this);
	}
	
}
