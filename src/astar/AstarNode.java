package astar;

import java.util.HashMap;
import java.util.Map;

public class AstarNode extends Node2D {

	private Node2D previous;
	private double totalDistance;
	
	private double euclideanDistanceToTarget;
	
	public AstarNode(int id, float x, float y) {
		super(id, x, y);
	}
	
	public void setTarget(Node2D target) {
		this.euclideanDistanceToTarget = this.getDistanceTo(target);
	}
	
	public double getEuclideanDistanceToTarget() {
		return euclideanDistanceToTarget;
	}
}
