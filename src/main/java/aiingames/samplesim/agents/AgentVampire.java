package aiingames.samplesim.agents;

import physics.PhysicsBox;
import aiingames.samplesim.Config;
import aiingames.samplesim.spatial.Object2D;
import aiingames.samplesim.spatial.PointLight;
import aiingames.samplesim.spatial.Vector2D;



public class AgentVampire implements Moveable {

	
	private final String id;
	private double desiredVx;
	private double desiredVy;
	
	
	public AgentVampire(String id) {
		this.id = id;
		this.desiredVx = -0.5;
		this.desiredVy = -0.5;
	}
	
	public void move(PhysicsBox e) {

		Object2D my2Drep = e.getAgent(this.id);
		
		Vector2D newDir = new Vector2D(0.0, 0.0);
		
		for( PointLight light : e.getLights().values()){

			Vector2D v1,v2;
			v1 = (new Vector2D(light.getPosition().getX() - my2Drep.getPosition().getX(), my2Drep.getPosition().getY() - light.getPosition().getY())).normalize();
			v2 = my2Drep.getDirection().normalize();
			v2.setY(v2.getPosition().getY() * (-1));
			
			double angle = v1.dot(v2); 
			light.unmark();
			if(angle <= 1.0 && angle >= 0.31){
				double distance = light.getPosition().getDistanceTo(my2Drep.getPosition());
				if(distance < 1){
					v1 = v1.mult(12/distance);
					newDir.plusEquals(v1);
					light.mark();
				}
					
			}
		}
		newDir = newDir.normalize();
		this.desiredVx = -newDir.getX();
		this.desiredVy = newDir.getY();
		
		
		
		double vx = my2Drep.getDirection().getX();
		double vy = my2Drep.getDirection().getY();
		
		if (vx == 0 && vy == 0) {
			this.desiredVx = Config.MAX_V * 2 * (Math.random()-0.5);
			this.desiredVy = Config.MAX_V * 2* (Math.random()-0.5);
		}

	}

	public String getId() {
		return this.id;
	}

	public double getDesiredVx() {
		return this.desiredVx;
	}

	public double getDesiredVy() {
		return this.desiredVy;
	}


}