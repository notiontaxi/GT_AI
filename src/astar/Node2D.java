package astar;

import java.util.HashMap;
import java.util.Map;

import spatial.Coordinate;

public class Node2D extends Coordinate {

	private int id;

	private Map<Integer, Link2D> neighbours = new HashMap<Integer, Link2D>();
	
	public Node2D(int id, double x, double y) {
		super(x, y);
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
}
