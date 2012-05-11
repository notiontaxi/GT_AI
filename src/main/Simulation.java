package main;


import gui.Gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import agents.Moveable;

import physics.PhysicsBox;
import spatial.Coordinate;
import spatial.PointLight;



public class Simulation {
	
	Map<String, Moveable> agents = new HashMap<String, Moveable>();
	List<Moveable> lights = new ArrayList<Moveable>();
	
	PhysicsBox e;

	private Gui gui;
	
	public Simulation(PhysicsBox e) {
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
		updateEnvironment();
		updateGui();
	}

	private void updateGui() {
		if (this.gui != null) {
			this.gui.update(e.getAgents(), e.getLights());
		}
	}

	private void updateEnvironment() {
		for (Moveable agent : this.agents.values()) {
			this.e.moveAgent(agent);
		}	
	}

	private void updateAgents() {
		for(Moveable agent : this.agents.values()){
			agent.move(e);
		}
	}
	
	public void addAgent(Moveable agent,Coordinate c){
		this.agents.put(agent.getId(), agent);
		this.e.createAndAddPhysicalAgentRepresentation(agent,c);
	}
	
// --- ADDED
	public void addLight(PointLight light) {
		this.lights.add(light);
		this.e.createAndAddPhysicalLightRepresentation(light);
	}
// --- ADDED END	

	public void setGui(Gui gui) {
		this.gui = gui;
		
	}




	
}
