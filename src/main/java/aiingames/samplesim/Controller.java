package aiingames.samplesim;

//import org.apache.log4j.Logger;

import physics.PhysicsBox;
import aiingames.samplesim.agents.AgentVampire;
import aiingames.samplesim.agents.Moveable;
import aiingames.samplesim.agents.AgentMoth;
import aiingames.samplesim.gui.Gui;
import aiingames.samplesim.simulation.Simulation;
import aiingames.samplesim.spatial.Coordinate;
import aiingames.samplesim.spatial.PointLight;

public class Controller {

//	 private static final Logger log = Logger.getLogger(Controller.class);
	 
	 public void run() {
//		 log.info("creating GUI...");
		 Gui gui = new Gui();
		 
//		 log.info("creating environment ...");
		 PhysicsBox e = new PhysicsBox();
		 gui.setEnvironmentSize(e.getMinX(), e.getMinY(), e.getMaxX(), e.getMaxY());
		 
//		 log.info("creating sim ...");
		 Simulation sim = new Simulation(e);
		 sim.setGui(gui);
		 
//		 log.info("creating agents ...");
		 sim.addAgent( new AgentMoth("moth1"), new Coordinate(3,3));
		 sim.addAgent( new AgentVampire("moth2"), new Coordinate(2,6));
//		 sim.addAgent( new AgentMoth("moth3"), new Coordinate(3,5));
		 sim.addAgent( new AgentMoth("moth4"), new Coordinate(2,1));
		 
		 sim.addLight(new PointLight("pointl_01", new Coordinate(2,3)));
		 sim.addLight(new PointLight("pointl_06", new Coordinate(2,5)));
		 sim.addLight(new PointLight("pointl_04", new Coordinate(4,7)));
		 sim.addLight(new PointLight("pointl_05", new Coordinate(4,1)));
		 sim.addLight(new PointLight("pointl_02", new Coordinate(6,1)));
		 sim.addLight(new PointLight("pointl_07", new Coordinate(6,7)));
		 sim.addLight(new PointLight("pointl_03", new Coordinate(8,3)));
		 sim.addLight(new PointLight("pointl_08", new Coordinate(8,5)));	
		 
		 sim.addLight(new PointLight("pointl_09", new Coordinate(3,2)));
		 sim.addLight(new PointLight("pointl_10", new Coordinate(6,2)));	
		 
//		 log.info("starting sim ...");
		 sim.run();
		 
//		 log.info("done.");
		 
	 }
	 
	 public static void main(String [] args) {
		 new Controller().run();
	 }
	
}
