package aiingames.samplesim;

//import org.apache.log4j.Logger;

import aiingames.samplesim.agents.Moveable;
import aiingames.samplesim.agents.PointLight;
import aiingames.samplesim.agents.MothAgent;
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
		 sim.addAgent( new MothAgent("moth1"), new Coordinate(6,6));
//		 sim.addAgent( new MothAgent("moth2"), new Coordinate(2,6));
		 
		 sim.addLight(new PointLight("pointl1"), new Coordinate(2,3));
		 sim.addLight(new PointLight("pointl2"), new Coordinate(6,1));
		 sim.addLight(new PointLight("pointl3"), new Coordinate(8,3));
		 sim.addLight(new PointLight("pointl4"), new Coordinate(4,7));
		 sim.addLight(new PointLight("pointl5"), new Coordinate(4,1));
		 sim.addLight(new PointLight("pointl6"), new Coordinate(2,5));
		 sim.addLight(new PointLight("pointl7"), new Coordinate(6,7));
		 sim.addLight(new PointLight("pointl8"), new Coordinate(8,5));		 
		 
		 
//		 log.info("starting sim ...");
		 sim.run();
		 
//		 log.info("done.");
		 
	 }
	 
	 public static void main(String [] args) {
		 new Controller().run();
	 }
	
}
