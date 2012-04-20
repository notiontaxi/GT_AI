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
	public void createAndAddPhysicalLightRepresentation(PointLight light) {
		this.physicalLights.put(light.getId(),light);		
	}	
	




	public void moveAgent(Moveable a) {
		PhysicObject2D physicalObject = this.physicalAgents.get(a.getId());
		Coordinate oldCoordinate = physicalObject.getPosition(); // remember old position
		
		// compute new direction vector
		double dx = Config.getSimStepSize()*(physicalObject.getDirection().getX() + a.getDesiredVx())/Config.TAU;
		double dy = Config.getSimStepSize()*(physicalObject.getDirection().getY() + a.getDesiredVy())/Config.TAU;
		double nVx = physicalObject.getDirection().getX() + dx;
		double nVy = physicalObject.getDirection().getY() + dy;
		Vector2D newDirection = new Vector2D(nVx,nVy).normalize();
		
		
		// add delta to old position => new position 
		double nx = (physicalObject.getPosition().getX() + newDirection.getX()* Config.getSimStepSize() );
		double ny = (physicalObject.getPosition().getY() - newDirection.getY()* Config.getSimStepSize() );
		Coordinate newCoordinate = new Coordinate(nx, ny);				

		System.out.println(newDirection);
		// set to new position and check for collisions
		physicalObject.setPosition(newCoordinate);
		if (!checkCollision(physicalObject)) {
			physicalObject.setDirection(newDirection);
		} else {
			physicalObject.setPosition(oldCoordinate);
			physicalObject.setDirection(newDirection.multEquals(-1.0));;
			System.out.println("Collision!");
		}		
	}


//	private double validateV(double nVx, double nVy) {
//
//		double v = Math.hypot(nVx, nVy);
//		if (v > Config.MAX_V) {
//			return Config.MAX_V / v;
//		}
//		return 1;
//	}


	private boolean checkCollision(PhysicObject2D _obj) {
		
		// check for collision with other PhysicObject2Ds
		for(PhysicObject2D obj : this.physicalAgents.values())
			if(_obj.collidesWith(obj))
				return true;
		
		// check for sollision with borders
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
