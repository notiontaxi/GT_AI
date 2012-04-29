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
	
	public Map<Integer, Link> getLinks() {
		return links;
	}
	public Map<Integer, Node> getNodes() {
		return nodes;
	}
	
	public void addNode(Node node) {
		this.nodes.put(node.getId(), node);
	}
	
	public void addLink(Link link) {
		this.links.put(link.getId(), link);
	}

	public Node getNode(int id) {
		return this.nodes.get(id);
	}

	public Link getLink(int id) {
		return this.links.get(id);
	}
	
	public BoundingBox getBoundingBox() {
		Double minX = null;
		Double minY = null;
		Double maxX = null;
		Double maxY = null;
		
		for (Node node : this.nodes.values()) {
			if(minX == null || node.getX() < minX) {
				minX = node.getX();
			}
			
			if(maxX == null || node.getX() > maxX) {
				maxX = node.getX();
			}

			if(minY == null || node.getY() < minY) {
				minY = node.getY();
			}
			
			if(maxY == null || node.getY() > maxY) {
				maxY = node.getY();
			}
		}
		
		BoundingBox boundingBox = new BoundingBox(new Coordinate(minX, minY), new Coordinate(maxX, maxY));
		return boundingBox;
	}
}
