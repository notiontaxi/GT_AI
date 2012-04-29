package network;

import java.util.HashMap;
import java.util.Map;

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
}
