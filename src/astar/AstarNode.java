package astar;

import network.Node;

public class AstarNode extends Node {

	private AstarNode previous;
	private Node origNode;
	private Double totalDistance = null;
	
	private double euclideanDistanceToTarget;
	
	public AstarNode(int id, float x, float y) {
		super(id, x, y);
		this.origNode = null;
	}
	
	public AstarNode(Node node) {
		super(node.getId(), node.getX(), node.getY());
		this.setNeighbours(node.getNeighbours());
		this.origNode = node;
	}
	
	public Node getOrigNode(){
		return this.origNode;
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
