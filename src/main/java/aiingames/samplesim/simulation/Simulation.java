package aiingames.samplesim.simulation;

import java.util.ArrayList;
import java.util.List;

import aiingames.samplesim.Config;
import aiingames.samplesim.agents.Agent;
import aiingames.samplesim.gui.Gui;
import aiingames.samplesim.spatial.Coordinate;

public class Simulation {
	
	List<Agent> agents = new ArrayList<Agent>();
	
	Environment e;

	private Gui gui;
	
	public Simulation(Environment e) {
		this.e = e;
	}
	
	public void run() {
		
		double time = 0;
		while (time < Config.getStopTime()) {
			doSimStep(time);
			
			
			time += Config.getSimStepSize();;
		}
		
		
	}

	private void doSimStep(double time) {
		updateAgents();
		moveAgents();
		updateGui();
	}

	private void updateGui() {
		if (this.gui != null) {
			this.gui.update(this.agents, this.e);
		}
	}

	private void moveAgents() {
		for (Agent agent : this.agents) {
			this.e.moveAgent(agent);
		}
		
	}

	private void updateAgents() {
		for(Agent agent : this.agents){
			agent.update(e);
		}
		
	}
	
	public void addAgent(Agent agent,Coordinate c){
		this.agents.add(agent);
		this.e.createAndAddPhysicalAgentRepresentation(agent,c);
	}

	public void setGui(Gui gui) {
		this.gui = gui;
		
	}

	
}
