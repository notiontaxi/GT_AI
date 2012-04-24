package astar;


public class Link2D {

	private int id;
	
	private Node2D from;
	private Node2D to;
	
	private float capacity;
	private float length; 
	
	public Link2D(int id, Node2D from, Node2D to, float capacity, float length ) {
		this.id = id;
		this.from = from;
		this.to = to;
		this.capacity = capacity;
		this.length = length;
	}	
}
