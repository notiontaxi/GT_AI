package aiingames.samplesim.agents;


import aiingames.samplesim.simulation.Environment;
import aiingames.samplesim.spatial.Coordinate;

public interface Agent {

	void update(Environment e);
	
	public String getId();
	
	public double getDesiredVx();
	
	public double getDesiredVy();




}
