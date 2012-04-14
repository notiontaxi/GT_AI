package aiingames.samplesim.agents;

import aiingames.samplesim.Config;
import aiingames.samplesim.simulation.Environment;
import aiingames.samplesim.spatial.Object2D;
import aiingames.samplesim.spatial.Vector2D;
import aiingames.samplesim.spatial.Coordinate;

import java.util.*;

public class MothAgent implements Moveable {

	
	private final String id;
	private double desiredVx;
	private double desiredVy;
	
	
	public MothAgent(String id) {
		this.id = id;
		this.desiredVx = -0.5;
		this.desiredVy = -0.5;
	}
	
	public void move(Environment e) {
		
//		a = (x, y)
//		|a| = sqrt( x*x + y*y ) 
		
		Object2D my2Drep = e.getAgent(this.id);
		
		for( Object2D light : e.getLights().values()){
			
//			System.out.println("Distance to the light: \t"			+ coord.getDistanceTo(myCoord));

			
			//v1: agent to light
			//v2: agent direction
			
			Vector2D v1,v2;
			v1 = (new Vector2D(light.getPosition().getX() - my2Drep.getPosition().getX(), my2Drep.getPosition().getY() - light.getPosition().getY())).normalize();
			v2 = my2Drep.getDirection().normalize();
			v2.setY(v2.getPosition().getY() * (-1));
			
			double angle = v1.dot(v2); 
//			System.out.println("Angle between agent and light: \t"	+ angle);
//			System.out.println("Angle between agent and light: \t"	+ angle);
			if(angle <= 1.0 && angle >= 0.31){
				//System.out.println("See ya! "+angle);
//				System.out.println("desired VX: " + this.desiredVx+ "   desired VY: " + this.desiredVy);
				this.desiredVx = v1.getX() * -Config.MAX_V;
				this.desiredVy = v1.getY() * Config.MAX_V;
			}
		}
		
		
		
//		ArrayList<Coordinate> lights =  e.getLightCoordinates();
//				
//		for(Coordinate lc : lights)
//			System.out.println(lc.getX());
		
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