package aiingames.samplesim;

//import org.apache.log4j.Logger;

import aiingames.samplesim.agents.Agent;
import aiingames.samplesim.agents.SimpleAgent;
import aiingames.samplesim.gui.Gui;
import aiingames.samplesim.simulation.Environment;
import aiingames.samplesim.simulation.Simulation;
import aiingames.samplesim.spatial.Coordinate;

public class Controller {

//	 private static final Logger log = Logger.getLogger(Controller.class);
	 
	 public void run() {
//		 log.info("creating GUI...");
		 Gui gui = new Gui();
		 
//		 log.info("creating environment ...");
		 Environment e = new Environment();
		 gui.setEnvironmentSize(e.getMinX(), e.getMinY(), e.getMaxX(), e.getMaxY());
		 
//		 log.info("creating sim ...");
		 Simulation sim = new Simulation(e);
		 sim.setGui(gui);
		 
//		 log.info("creating agents ...");
		 Agent agent = new SimpleAgent("simple");
		 sim.addAgent(agent, new Coordinate(6,6));
		 
//		 log.info("starting sim ...");
		 sim.run();
		 
//		 log.info("done.");
		 
	 }
	 
	 public static void main(String [] args) {
		 new Controller().run();
	 }
	
}
