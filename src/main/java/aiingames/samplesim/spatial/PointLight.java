package aiingames.samplesim.spatial;

import physics.PhysicsBox;
import aiingames.samplesim.agents.Moveable;
import aiingames.samplesim.gui.Markable;

public class PointLight extends Object2D implements Moveable, Markable {
	
	final private String id;
	private boolean marked;
	
	public PointLight(String _id, Coordinate _c){
		super(_c);
		this.id = _id;
	}
	

	public void move(PhysicsBox e) {
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