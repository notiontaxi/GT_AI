package aiingames.samplesim.simulation;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import aiingames.samplesim.Config;
import aiingames.samplesim.agents.Agent;
import aiingames.samplesim.spatial.Coordinate;

public class Environment {
	
	private double minX = 0;
	private double minY = 0;
	private double maxX = 12;
	private double maxY = 12;

	Map<String,PhysicalAgent> physicalAgents = new HashMap<String,PhysicalAgent>();
	
	protected void createAndAddPhysicalAgentRepresentation(Agent agent,Coordinate c) {
		PhysicalAgent pa = new PhysicalAgent();
		pa.c = c;
		pa.vx = 0;
		pa.vy = 0;
		
		this.physicalAgents.put(agent.getId(), pa);
		
	}
	
	
	private static class PhysicalAgent {
		Coordinate c;
		double vx;
		double vy;
		
	}


	public void moveAgent(Agent a) {
		PhysicalAgent pa = this.physicalAgents.get(a.getId());
		
		double dx = Config.getSimStepSize()*(pa.vx - a.getDesiredVx())/Config.TAU;
		double dy = Config.getSimStepSize()*(pa.vy - a.getDesiredVy())/Config.TAU;
		double nVx = pa.vx + dx;
		double nVy = pa.vy + dy;
		
		double scale = validateV(nVx, nVy);
		
		double mvX = scale * nVx * Config.getSimStepSize();
		double mvY = scale * nVy * Config.getSimStepSize();
		
		double nx = pa.c.getX() + mvX;
		double ny = pa.c.getY() + mvY;
		Coordinate nc = new Coordinate(nx, ny);
		
		if (!checkCollision(pa.c,nc)) {
			pa.c = nc;
			pa.vx = nVx;
			pa.vy = nVy;
		} else {
			pa.vx = 0;
			pa.vy = 0;
		}
		
		
	}


	private double validateV(double nVx, double nVy) {

		double v = Math.hypot(nVx, nVy);
		if (v > Config.MAX_V) {
			return Config.MAX_V / v;
		}
		return 1;
	}


	private boolean checkCollision(Coordinate c, Coordinate nc) {
		if (nc.getX() < this.getMinX()) {
			return true;
		}
		if (nc.getY() < this.getMinY()) {
			return true;
		}
		if (nc.getX() > this.getMaxX()) {
			return true;
		}
		if (nc.getY() > this.getMaxY()) {
			return true;
		}
		return false;
	}


	public Coordinate getAgentLocation(String id) {
		return this.physicalAgents.get(id).c;
	}
	
	public double getVx(String id) {
		return this.physicalAgents.get(id).vx;
	}
	public double getVy(String id) {
		return this.physicalAgents.get(id).vy;
	}


	public double getMinX() {
		return minX;
	}


	public void setMinX(double minX) {
		this.minX = minX;
	}


	public double getMinY() {
		return minY;
	}


	public void setMinY(double minY) {
		this.minY = minY;
	}


	public double getMaxX() {
		return maxX;
	}


	public void setMaxX(double maxX) {
		this.maxX = maxX;
	}


	public double getMaxY() {
		return maxY;
	}


	public void setMaxY(double maxY) {
		this.maxY = maxY;
	}
	

}
