package aiingames.samplesim.simulation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import aiingames.samplesim.Config;
import aiingames.samplesim.agents.Moveable;
import aiingames.samplesim.agents.PointLight;
import aiingames.samplesim.spatial.Coordinate;
import aiingames.samplesim.spatial.Object2D;
import aiingames.samplesim.spatial.PhysicObject2D;
import aiingames.samplesim.spatial.Vector2D;

public class Environment {
	
	private double minX = 0;
	private double minY = 0;
	private double maxX = 12;
	private double maxY = 12;

	Map<String, PhysicObject2D> physicalAgents = new HashMap<String, PhysicObject2D>();
	Map<String, PointLight> physicalLights = new HashMap<String, PointLight>();
	
	
	protected void createAndAddPhysicalAgentRepresentation(Moveable agent,Coordinate c) {
		PhysicObject2D pa = new PhysicObject2D(c, new Vector2D(2.0, 2.0));

		this.physicalAgents.put(agent.getId(), pa);
		
	}
	

	public void createAndAddPhysicalLightRepresentation(PointLight light) {
		this.physicalLights.put(light.getId(),light);
		
	}	
	



	public void moveAgent(Moveable a) {
		Object2D pa = this.physicalAgents.get(a.getId());
		
		double dx = Config.getSimStepSize()*(pa.getDirection().getX() + a.getDesiredVx())/Config.TAU;
		double dy = Config.getSimStepSize()*(pa.getDirection().getY() + a.getDesiredVy())/Config.TAU;
		double nVx = pa.getDirection().getX() + dx;
		double nVy = pa.getDirection().getY() + dy;
		
		double scale = validateV(nVx, nVy); // value between 0 and 1
		
		double nx = (pa.getPosition().getX() + nVx*scale* Config.getSimStepSize() );
		double ny = (pa.getPosition().getY() + nVy*scale* Config.getSimStepSize() );
		
		Coordinate nc = new Coordinate(nx, ny);
		Vector2D vec = new Vector2D(nVx,nVy).normalize();
		
		if (!checkCollision(pa.getPosition(),nc)) {
			pa.setPosition(nc);
			pa.setDirection(vec);
		} else {
			pa.setDirection(new Vector2D(0.0,0.0));
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



	public Object2D getAgent(String _id){
		
		return this.physicalAgents.get(_id);
	}



	public Map<String, PointLight> getLights() {
		return this.physicalLights;
	}	
	
	public Map<String, PhysicObject2D> getAgents() {
		return this.physicalAgents;
	}

	public void moveLight(Moveable light) {
		// TODO Auto-generated method stub
		
	}

	
	

}
