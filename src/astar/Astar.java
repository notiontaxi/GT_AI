package astar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import network.Link;
import network.NetworkGraph;
import network.Node;

/**
 * finds the shortest way in a network from a start to the end
 */
public class Astar {

	private Map<Integer, AstarNode> available;
	private Map<Integer, AstarNode> closed;
	
	private NetworkGraph networkGraph;
	private AstarNode start;
	private AstarNode target;
	
	public Astar(){
		
	}
	
	/**
	 * 
	 * constructor
	 * @param network - containing all links and nodes
	 * @param start - where to start
	 * @param target - where to end
	 */
	public Astar(NetworkGraph networkGraph, Node start, Node target) {
		this.networkGraph = networkGraph;
		this.start = new AstarNode(start);
		this.target = new AstarNode(target);
		calculate();
	}
	
	public void calculate(){
		if (networkGraph != null){
			this.initAstar();
			this.initAstarNodes();
			this.findRoute();
		}
	}
	
	private void initAstar(){
		// all available nodes in the network
		this.available = new HashMap<Integer, AstarNode>();
		// all finished nodes
		this.closed = new HashMap<Integer, AstarNode>();
		// possible next nodes

		System.out.println(this.start.getId() +" -> "+this.target.getId());
	}
	
	private void findRoute() {
		AstarNode currentNode = this.getMinAvailableNode();
		
		int i = 0;
		while(currentNode != null && currentNode != this.target && this.available.size() != 0) {
			this.available.remove(currentNode.getId());
			this.expandNode(currentNode);
			this.closed.put(currentNode.getId(), currentNode);
			
			currentNode = this.getMinAvailableNode();
		}
	}
	
	/**
	 * get node with min. distance from Available list
	 * @return
	 */
	private AstarNode getMinAvailableNode() {
		AstarNode minNode = null;
		for(AstarNode availableNode : this.available.values()) {
			if(availableNode.getTotalDistance() != null && (minNode == null || minNode.getTotalDistance().compareTo(availableNode.getTotalDistance()) > 0)) {
				minNode = availableNode;
			}
		}
		return minNode;
	}
	
	/**
	 * finds the next closest node (expandNode)
	 * 
	 * @return Node - closest Node
	 */
	private void expandNode(AstarNode currentNode) {
		Double newDistance;
		Double euclideanDistance;
		int nodeId;
		for (Link link : networkGraph.getOutLinks(currentNode.getOrigNode())) {
			nodeId = link.getTo().getId();
			if (this.closed.containsKey(nodeId)){
				continue;
			}
			
			AstarNode neighbour = this.available.get(link.getTo().getId());
			
			euclideanDistance = neighbour.getEuclideanDistanceToTarget();
			newDistance = link.getLength() + euclideanDistance + currentNode.getTotalDistance();
			
			if(neighbour.getTotalDistance() != null && (this.available.containsKey(nodeId) && newDistance.compareTo(neighbour.getTotalDistance() + euclideanDistance) >= 0.0)) {
				continue;
			}
			
			neighbour.setTotalDistance(newDistance - euclideanDistance);
			neighbour.setPrevious(currentNode);
			if(!this.available.containsKey(neighbour.getId())) {
				this.available.put(neighbour.getId(), neighbour);
			}
		}
	}
	
	/**
	 * get a stack with all nodes from start to target (pop = start)
	 * @return Stack<AstarNode>
	 */
	public Stack<AstarNode> getPath() {
		AstarNode currentNode = this.target;
		Stack<AstarNode> result = new Stack<AstarNode>();
		
		while(currentNode.getPrevious() != null) {
			result.push(currentNode);
			currentNode = currentNode.getPrevious();
//			System.out.println(currentNode.getId());
		}
		return result;
	}
	
	/***
	 * initialize astar and calculate the euclidean distance to the target
	 */
	private void initAstarNodes() {
		for(Node node : networkGraph.getNodes()) {
			AstarNode astarNode = new AstarNode(node);
			astarNode.setTarget(this.target);
			this.available.put(astarNode.getId(), astarNode);
		}
				// get start and set distance to 0
		this.start = this.available.get(start.getId());
		this.start.setTotalDistance(0.0);
		// remove start from available
		
		this.target = this.available.get(target.getId());
	}
	
	public AstarNode getStart(){
		return start;
	}
	
	public AstarNode getTarget(){
		return target;
	}
	
}
