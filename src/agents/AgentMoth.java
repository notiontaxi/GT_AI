package agents;

import physics.PhysicsBox;
import spatial.Object2D;
import spatial.PointLight;
import spatial.Vector2D;

/**
 * An Object of this class represents a light-addicted Braitenberg Vehicle
 * 
 * @author notiontaxi (Florian Wokurka)
 */

public class AgentMoth implements Moveable {

	
	private final String id;
	private double desiredVx;
	private double desiredVy;
	
	private Object2D my2DRepresentation;	
	private Vector2D newDirection;
	
	
	public AgentMoth(String id) {
		this.id = id;
		this.desiredVx = -0.5;
		this.desiredVy = -0.5;
	}
	
	public void move(PhysicsBox _box) {

		my2DRepresentation = _box.getAgent(this.id);		
		newDirection = new Vector2D(0.0, 0.0);
		double distance;
		
		// iterate over all existing lights in the scene
		for( PointLight light : _box.getLights().values()){

			Vector2D directionToTheLight = (new Vector2D(	light.getPosition().getX() - my2DRepresentation.getPosition().getX(), 
															my2DRepresentation.getPosition().getY() - light.getPosition().getY())
														);

			double angle = my2DRepresentation.getDirection().dot(directionToTheLight.normalize()); 
			
			light.unmark();
			
			// if angle between -90 and +90 degree
			if(angle <= 1.0 && angle >= 0.31){
				distance = light.getPosition().getDistanceTo(my2DRepresentation.getPosition());
				if(distance < 4){
					directionToTheLight = directionToTheLight.mult(1/(distance*2));
					newDirection.plusEquals(directionToTheLight);
					light.mark();				
				}
					
			}
		}
		
		newDirection = newDirection.normalize();
		this.desiredVx = newDirection.getX();
		this.desiredVy = newDirection.getY();	
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