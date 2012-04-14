package aiingames.samplesim.agents;


import aiingames.samplesim.simulation.Environment;

public interface Moveable {
	
	void move(Environment e);
	
	public String getId();
	
	public double getDesiredVx();
	
	public double getDesiredVy();

}
