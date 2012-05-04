package network;

public class Link {

	private int id;

	private Node from;
	private Node to;

	private double capacity;
	private double length;

	public Link(int id, Node from, Node to, double capacity, double length) {
		this.id = id;
		this.from = from;
		this.to = to;
		this.capacity = capacity;
		this.length = length;
		
		this.from.addNeighbor(this);
		this.to.addNeighbor(this);
		
	}
	
	public int getId() {
		return id;
	}
	public Node getFrom() {
		return from;
	}
	public void setFrom(Node from) {
		this.from = from;
	}
	public Node getTo() {
		return to;
	}
	public void setTo(Node to) {
		this.to = to;
	}
	public double getCapacity() {
		return capacity;
	}
	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}
	public double getLength() {
		return length;
	}
	public void setLength(double length) {
		this.length = length;
	}
}