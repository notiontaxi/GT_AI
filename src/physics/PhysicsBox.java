package physics;



import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import main.Config;

import agents.Moveable;

import spatial.Coordinate;
import spatial.Object2D;
import spatial.PointLight;
import spatial.Vector2D;




public class PhysicsBox {
	
	private double minX;
	private double minY;
	private double maxX;
	private double maxY;

	Map<String, PhysicObject2D> physicalAgents;
	Map<String, PointLight> physicalLights;
	
	ArrayList<BoundingBoxAABB> boundingboxes;
	
	public PhysicsBox(){
		minX = 0;
		minY = 0;
		maxX = 12;
		maxY = 12;

		physicalAgents = new HashMap<String, PhysicObject2D>();
		physicalLights = new HashMap<String, PointLight>();
		
		boundingboxes = new ArrayList<BoundingBoxAABB>();

	}
	
	
	
	public void createAndAddPhysicalAgentRepresentation(Moveable agent,Coordinate c) {
		PhysicObject2D pa = new PhysicObject2D(c, new Vector2D(2.0, 2.0));
		this.physicalAgents.put(agent.getId(), pa);
		
	}
	
// --- ADDED	
	public void createAndAddPhysicalLightRepresentation(PointLight light) {
		this.physicalLights.put(light.getId(),light);		
	}	
	




	public void moveAgent(Moveable a) {
		PhysicObject2D pa = this.physicalAgents.get(a.getId());
		
		double dx = Config.getSimStepSize()*(pa.getDirection().getX() + a.getDesiredVx())/Config.TAU;
		double dy = Config.getSimStepSize()*(pa.getDirection().getY() + a.getDesiredVy())/Config.TAU;
		double nVx = pa.getDirection().getX() + dx;
		double nVy = pa.getDirection().getY() + dy;
		
		double scale = validateV(nVx, nVy); // value between 0 and 1
		
		double nx = (pa.getPosition().getX() + nVx*scale* Config.getSimStepSize() );
		double ny = (pa.getPosition().getY() + nVy*scale* Config.getSimStepSize() );
		
		Coordinate oc = pa.getPosition();
		Coordinate nc = new Coordinate(nx, ny);
		Vector2D vec = new Vector2D(nVx,nVy).normalize();
		
		pa.setPosition(nc);
		if (!checkCollision(pa)) {
			pa.setDirection(vec);
		} else {
			pa.setPosition(oc);
			pa.setDirection(vec.multEquals(-1.0));
			
			System.out.println("Collision!");
		}
		
		
	}


	private double validateV(double nVx, double nVy) {

		double v = Math.hypot(nVx, nVy);
		if (v > Config.MAX_V) {
			return Config.MAX_V / v;
		}
		return 1;
	}


	private boolean checkCollision(PhysicObject2D _obj) {
		
		for(PhysicObject2D obj : this.physicalAgents.values())
			if(_obj.collidesWith(obj))
				return true;
		
		if (_obj.getPosition().getX() < this.getMinX()) {
			return true;
		}
		if (_obj.getPosition().getY() < this.getMinY()) {
			return true;
		}
		if (_obj.getPosition().getX() > this.getMaxX()) {
			return true;
		}
		if (_obj.getPosition().getY() > this.getMaxY()) {
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
