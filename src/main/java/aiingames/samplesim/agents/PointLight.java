package aiingames.samplesim.agents;

import aiingames.samplesim.simulation.Environment;
import aiingames.samplesim.spatial.Coordinate;
import aiingames.samplesim.spatial.Object2D;

public class PointLight extends Object2D implements Moveable, Markable {
	
	final private String id;
	private boolean marked;
	
	public PointLight(String _id, Coordinate _c){
		super(_c);
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





	public void mark() {
		this.marked = true;
	}


	public void unmark() {
		this.marked = false;
	}


	public boolean isMarked() {
		return this.marked;
	}

}
