package network;

import java.util.HashMap;
import java.util.Map;

import spatial.Coordinate;

public class Node extends Coordinate {

	private int id;

	private Map<Integer, Link> neighbours = new HashMap<Integer, Link>();

	public Node(int id, double x, double y) {
		super(x, y);
		this.id = id;
	}

	public int getId() {
		return id;
	}
	public Map<Integer, Link> getNeighbours() {
		return neighbours;
	}
	public void setNeighbours(Map<Integer, Link> neighbours) {
		this.neighbours = neighbours;
	}
	public void addNeighbor(Link link) {
		this.neighbours.put(link.getId(), link);
	}
	public Link getNeighbour(int id) {
		return this.neighbours.get(id);
	}
}