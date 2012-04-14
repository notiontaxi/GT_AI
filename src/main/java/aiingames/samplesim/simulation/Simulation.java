package aiingames.samplesim.simulation;

import java.util.ArrayList;
import java.util.List;

import aiingames.samplesim.Config;
import aiingames.samplesim.agents.Moveable;
import aiingames.samplesim.agents.PointLight;
import aiingames.samplesim.gui.Gui;
import aiingames.samplesim.spatial.Coordinate;

public class Simulation {
	
	List<Moveable> agents = new ArrayList<Moveable>();
	List<Moveable> lights = new ArrayList<Moveable>();
	
	Environment e;

	private Gui gui;
	
	public Simulation(Environment e) {
		this.e = e;
	}
	
	public void run() {
		
		double time = 0;
		while (time < Config.getStopTime()) {
			doSimStep(time);

			time += Config.getSimStepSize();
		}
		
		
	}

	private void doSimStep(double time) {
		updateAgents();
		moveAgents();
		updateGui();
	}

	private void updateGui() {
		if (this.gui != null) {
			this.gui.update(e.getAgents(), e.getLights());
		}
	}

	private void moveAgents() {
		for (Moveable agent : this.agents) {
			this.e.moveAgent(agent);
		}
		
	}

	private void updateAgents() {
		for(Moveable agent : this.agents){
			agent.update(e);
		}
		
	}
	
	public void addAgent(Moveable agent,Coordinate c){
		this.agents.add(agent);
		this.e.createAndAddPhysicalAgentRepresentation(agent,c);
	}
	
// --- ADDED
	public void addLight(Moveable light, Coordinate c) {
		this.lights.add(light);
		this.e.createAndAddPhysicalLightRepresentation(light, c);
	}
// --- ADDED END	

	public void setGui(Gui gui) {
		this.gui = gui;
		
	}



	
}
