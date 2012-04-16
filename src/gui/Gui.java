package gui;



import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import main.Config;

import physics.PhysicObject2D;
import spatial.Object2D;
import spatial.PointLight;

public class Gui {
	private JFrame f;
	private DrawArea area;

	private long lastUpdate = 0;
	private double scale;
	private double minX;
	private double minY;
	private double maxX;
	private double maxY;

	public Gui() {

		this.f = new JFrame("Swing Paint Demo");
		this.f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.area = new DrawArea();
		this.f.add(area);
		this.f.setSize(250,250);
		this.f.setVisible(true);
	}

	public void setEnvironmentSize(double minX, double minY, double maxX, double maxY) {
		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;
		resize();
	}

	private void resize() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = this.maxX - this.minX;
		double height = this.maxY - this.minY;

		this.scale = screenSize.getHeight() / height;
		if (width * scale > screenSize.width) {
			scale = screenSize.width / width;
		}

		this.f.setSize((int)(width*scale), (int)(height * scale));
		this.area.setTransformation(scale, minX, minY);

	}


	public void update(Map<String, PhysicObject2D> map, Map<String, PointLight> map2) {
		
		for (Object2D agent : map.values()) {
			this.area.updateAgent(agent);
		}
		
		for(PointLight light : map2.values()){
			this.area.updateLight(light);

		}

		
        long current = System.currentTimeMillis();
        long diff = current - this.lastUpdate;
        long wait = (long) (Config.getSimStepSize() * 1000) -diff;
        if (wait > 0) {
                try {
                        Thread.sleep(wait);
                } catch (InterruptedException ee) {
                       ee.printStackTrace();
                }
        }
        this.lastUpdate = System.currentTimeMillis();
        this.area.repaint();
	}


	private static class DrawArea extends JPanel {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private Map<Object2D,DrawAgent> agents = new HashMap<Object2D,DrawAgent>();
		private Map<Object2D,DrawLight> lights = new HashMap<Object2D,DrawLight>();
		private double scale;
		private double minX;
		private double minY;
		
		public DrawArea(){
			setBorder(BorderFactory.createLineBorder(Color.black));
		}


		public void setTransformation(double scale, double minX, double minY) {
			this.scale = scale;
			this.minX = minX;
			this.minY = minY;
		}
		
		public void updateAgent(Object2D agent) {
			DrawAgent da = this.agents.get(agent);
			if (da == null) {
				da = new DrawAgent(Color.BLUE);
				da.setRadius((int)(0.5+.3*this.scale));
				this.agents.put(agent, da);
			}
			
            int x = (int) (0.5+(agent.getPosition().getX() - this.minX) * this.scale);
            int y = (int) (0.5+(agent.getPosition().getY() - this.minY) * this.scale);
            da.setLocation(x,y);
            
            da.setDirection((agent.getDirection().getX()), (agent.getDirection().getY()));
		}
		
		public void updateLight(PointLight light) {
			DrawLight dl = this.lights.get(light);
			//System.out.println(light);
			if (dl == null) {
				//System.out.println(light);
				dl = new DrawLight(Color.YELLOW);
				dl.setRadius((int)(0.5+.2*this.scale));
				this.lights.put(light, dl);
			}
			
			if(light.isMarked())
				dl.color = Color.CYAN;
			else
				dl.color = Color.YELLOW;
			
            int x = (int) (0.5+(light.getPosition().getX() - this.minX) * this.scale);
            int y = (int) (0.5+(light.getPosition().getY() - this.minY) * this.scale);
            dl.setLocation(x,y);
		}		
		
        @Override
        public void paint(Graphics arg0) {
                super.paint(arg0);
                
                for (DrawLight light : this.lights.values()) {
                    light.paint(arg0);
                }
                
                for (DrawAgent agent : this.agents.values()) {
                    agent.paintAgent(arg0);
                }
        }


	}

	private static class DrawAgent {
		private int radius = 0;
		private Color color;
		private int y;
		private int x;
		private double dx;
		private double dy;

		public DrawAgent(Color color) {
			this.color = color;
		}


		public void paintAgent(Graphics g) {
			g.setColor(this.color);
			g.fillOval(this.x-this.radius, this.y-this.radius, 2*this.radius, 2*this.radius);
			g.setColor(Color.BLACK);
			g.drawLine(this.x, this.y, this.x + (int)(this.dx*(this.radius)), this.y + (int)(this.dy*(this.radius)));
			//System.out.println(dx);
		}


		public void setRadius(int d) {
			this.radius = d;
		}


		public void setLocation(int x, int y) {
			this.x = x;
			this.y = y;

		}

		public void setDirection(double d, double e) {
			this.dx = d;
			this.dy = e;
		}
	}
	
	
// --- ADDED	
	private static class DrawLight {
		private int radius;
		private Color color;
		private int y;
		private int x;

		public DrawLight(Color color) {
			this.color = color;
			this.radius = 1;
		}


		public void setLocation(int x2, int y2) {
			this.x = x2;
			this.y = y2;
			
		}


		public void setRadius(int i) {
			this.radius = i;
			
		}


		public void paint(Graphics g) {
			g.setColor(this.color);
			g.fillOval(this.x-this.radius, this.y-this.radius, 2*this.radius, 2*this.radius);	
//			System.out.println(this.x + " " + this.color);
		}





	}	
// --- ADDED		

}
