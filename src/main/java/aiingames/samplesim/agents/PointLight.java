package aiingames.samplesim.agents;

import aiingames.samplesim.simulation.Environment;
import aiingames.samplesim.spatial.Coordinate;

public class PointLight implements Moveable {
	
	final private String id;
	
	public PointLight(String _id){
		this.id = _id;
	}
	

	public void move(Environment e) {
		// TODO Auto-generated method stub		
	}

	public String getId() {
		return this.id;
	}

	public double getDesiredVx() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getDesiredVy() {
		// TODO Auto-generated method stub
		return 0;
	}


	public Coordinate getPosition() {
		// TODO Auto-generated method stub
		return null;
	}

}
