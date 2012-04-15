package aiingames.samplesim.agents;


import physics.PhysicsBox;

public interface Moveable {
	
	void move(PhysicsBox e);
	
	public String getId();
	
	public double getDesiredVx();
	
	public double getDesiredVy();

}
