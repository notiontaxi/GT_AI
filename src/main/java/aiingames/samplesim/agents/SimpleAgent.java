package aiingames.samplesim.agents;


import aiingames.samplesim.simulation.Environment;

public class SimpleAgent implements Moveable {

	
	private final String id;
	private double desiredVx;
	private double desiredVy;

	public SimpleAgent(String id) {
		this.id = id;
		
	}
	
	public void update(Environment e) {
//		double vx = e.getVx(id);
//		double vy = e.getVy(id);
//		
//		if (vx == 0 && vy == 0) {
//			this.desiredVx = Config.MAX_V * 2 * (Math.random()-0.5);
//			this.desiredVy = Config.MAX_V * 2* (Math.random()-0.5);
//		}

	}

	public String getId() {
		return this.id;
	}

	public double getDesiredVx() {
		return this.desiredVx;
	}

	public double getDesiredVy() {
		return this.desiredVy;
	}

}
