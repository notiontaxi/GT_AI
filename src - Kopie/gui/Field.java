package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;

public class Field extends Polygon {

	private static final long serialVersionUID = 1L;

	public Field(int x, int y, int width, int height) {

		int radius = (width-10)/2;
		int c_x = x + width / 2;
		int c_y = y + height / 2;
		
		int corners = 16;
		
		for (int i = 0; i < corners+1; i++) {
		    this.addPoint( (int) (c_x + radius * Math.cos( i * 2 * Math.PI / corners )),
	                  (int) (c_y + radius * Math.sin( i * 2 * Math.PI / corners )) );
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
