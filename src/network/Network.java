package network;

import java.util.HashMap;
import java.util.Map;

import spatial.BoundingBox;
import spatial.Coordinate;

public class Network {

	private Map<Integer, Node> nodes;
	private Map<Integer, Link> links;
	
	public Network() {
		this.nodes = new HashMap<Integer, Node>();
		this.links = new HashMap<Integer, Link>();
	}
}
