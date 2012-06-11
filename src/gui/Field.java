package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;

public class Field extends Polygon {

	private static final long serialVersionUID = 1L;

	public Field(int x, int y, int width, int height) {
		this.initFieldValues(x, y, width, height);
	}
	
	public void initFieldValues(int x, int y, int width, int height) {
		this.reset();
		
		int radius = (Math.min(width, height)-10)/2;
		int c_x = x + width / 2;
		int c_y = y + height / 2;
		
		int corners = 16;

		int currx = 0;
		int curry = 0;
		for (int i = 0; i < corners+1; i++) {
			currx = (int) (c_x + radius * Math.cos( i * 2 * Math.PI / corners ));
			curry = (int) (c_y + radius * Math.sin( i * 2 * Math.PI / corners ));
		    this.addPoint(currx, curry);
		}

		this.addPoint(x+width, y+height/2);
		this.addPoint(x+width, y+height);
		this.addPoint(x+width/2, y+height);
		this.addPoint(x, y+height);
		this.addPoint(x, y+height/2);
		this.addPoint(x, y);
		this.addPoint(x+width/2, y);
		this.addPoint(x+width, y);
		this.addPoint(x+width, y+height/2);
		this.addPoint(currx, curry);
	}

	public void draw(Graphics2D graphics2d) {
		this.draw(graphics2d, Color.BLACK);
	}

	public void draw(Graphics2D graphics2d, Color color) {
		graphics2d.setColor(color);
		graphics2d.setStroke(new BasicStroke(2.0f));
		graphics2d.fillPolygon(this);
	}
}
