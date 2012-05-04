package astar;

import network.Node;

public class AstarNode extends Node {

	private AstarNode previous;
	private Double totalDistance = null;
	
	private double euclideanDistanceToTarget;
	
	public AstarNode(int id, float x, float y) {
		super(id, x, y);
	}
	
	public AstarNode(Node node) {
		super(node.getId(), node.getX(), node.getY());
		this.setNeighbours(node.getNeighbours());
	}
	
	public void setTarget(Node target) {
		this.euclideanDistanceToTarget = this.getDistanceTo(target);
	}
	
	public double getEuclideanDistanceToTarget() {
		return euclideanDistanceToTarget;
	}
	
	public AstarNode getPrevious() {
		return previous;
	}
	public void setPrevious(AstarNode previous) {
		this.previous = previous;
	}
	
	public Double getTotalDistance() {
		return totalDistance;
	}
	
	public void setTotalDistance(Double totalDistance) {
		this.totalDistance = totalDistance;
	}
	
}
