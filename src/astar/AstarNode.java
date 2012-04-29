package astar;

import network.Node;

public class AstarNode extends Node {

	private Node previous;
	private double totalDistance;
	
	private double euclideanDistanceToTarget;
	
	public AstarNode(int id, float x, float y) {
		super(id, x, y);
	}
	
	public void setTarget(Node target) {
		this.euclideanDistanceToTarget = this.getDistanceTo(target);
	}
	
	public double getEuclideanDistanceToTarget() {
		return euclideanDistanceToTarget;
	}
	
	public Node getPrevious() {
		return previous;
	}
	
	public double getTotalDistance() {
		return totalDistance;
	}
}
