package aiingames.samplesim.agents;


import aiingames.samplesim.simulation.Environment;

public interface Moveable {
	
	void update(Environment e);
	
	public String getId();
	
	public double getDesiredVx();
	
	public double getDesiredVy();

}
