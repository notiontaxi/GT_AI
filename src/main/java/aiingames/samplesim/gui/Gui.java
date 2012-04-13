package aiingames.samplesim.gui;

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

import aiingames.samplesim.Config;
import aiingames.samplesim.agents.Moveable;
import aiingames.samplesim.agents.PointLight;
import aiingames.samplesim.simulation.Environment;
import aiingames.samplesim.spatial.Coordinate;
import aiingames.samplesim.spatial.Object2D;

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
	// --- MODIFIED
	public void update(Collection<Moveable> agents, Collection<Moveable> lights, Environment e) {
		for (Moveable agent : agents) {
			this.area.updateAgent(agent,e);
		}
		

		for(Moveable light : lights)
			this.area.updateLight(light, e);
// --- MODIFIED		
		
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
		private Map<Moveable,DrawAgent> agents = new HashMap<Moveable,DrawAgent>();
		private Map<Moveable,DrawLight> lights = new HashMap<Moveable,DrawLight>();
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
		
		public void updateAgent(Moveable agent, Environment e) {
			DrawAgent da = this.agents.get(agent);
			if (da == null) {
				da = new DrawAgent(Color.BLUE);
				da.setRadius((int)(0.5+.5*this.scale));
				this.agents.put(agent, da);
			}
			
			Object2D agent2D = e.getAgent(agent.getId());
			
            int x = (int) (0.5+(agent2D.getPosition().getX() - this.minX) * this.scale);
            int y = (int) (0.5+(agent2D.getPosition().getY() - this.minY) * this.scale);
            da.setLocation(x,y);
            double norm = 2 *Math.sqrt(Math.pow(agent2D.getDirection().getX(), 2)+ Math.pow(agent2D.getDirection().getY(), 2));
            da.setDirection((int)(agent2D.getDirection().getX()*this.scale/norm+0.5),(int) (agent2D.getDirection().getY()*this.scale/norm+0.5));
		}
		
		public void updateLight(Moveable light, Environment e) {
			DrawLight dl = this.lights.get(light);
			if (dl == null) {
				dl = new DrawLight(Color.YELLOW);
				dl.setRadius((int)(0.5+.2*this.scale));
				this.lights.put(light, dl);
			}
			
			Object2D light2D = e.getLight(light.getId());
			
            int x = (int) (0.5+(light2D.getPosition().getX() - this.minX) * this.scale);
            int y = (int) (0.5+(light2D.getPosition().getY() - this.minY) * this.scale);
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
		private int dx;
		private int dy;

		public DrawAgent(Color color) {
			this.color = color;
		}


		public void paintAgent(Graphics g) {
			g.setColor(this.color);
			g.fillOval(this.x-this.radius, this.y-this.radius, 2*this.radius, 2*this.radius);
			g.setColor(Color.BLACK);
			g.drawLine(this.x, this.y, this.x + this.dx, this.y + this.dy);
		}


		public void setRadius(int d) {
			this.radius = d;
		}


		public void setLocation(int x, int y) {
			this.x = x;
			this.y = y;

		}

		public void setDirection(int dx, int dy) {
			this.dx = dx;
			this.dy = dy;
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
