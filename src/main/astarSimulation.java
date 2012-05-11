package main;


import gui.Gui;
import gui.JUNG;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import agents.AgentPathwalker;
import agents.Moveable;

import physics.PhysicsBox;
import spatial.Coordinate;
import spatial.PointLight;



public class astarSimulation {
	
	AgentPathwalker agent;
	long lastUpdate;
	JUNG gui;
	


	
	public astarSimulation(AgentPathwalker _agent, JUNG gui) {
		this.agent = _agent;
	}
	
	public void run() {	
		double time = 0;
		while (time < Config.getStopTime()) {
			doSimStep(time);
			time += Config.getSimStepSize();
		}
	}

	private void doSimStep(double time) {
        long current = System.currentTimeMillis();
        long diff = current - this.lastUpdate;
        long wait = (long) (Config.getSimStepSize() * 1000) -diff;
        if (wait > 0) {
                try {
                        Thread.sleep(wait);
                } catch (InterruptedException ee) {
                       ee.printStackTrace();
                }
        }
        this.lastUpdate = System.currentTimeMillis();
		
        // UPDATES
		this.agent.update();
		gui.updateAgent(this.agent.getX(), this.agent.getY());
	}


	

}
