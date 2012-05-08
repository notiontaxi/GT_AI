/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import com.sun.org.apache.bcel.internal.generic.AALOAD;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections15.Transformer;
import spatial.BoundingBox;
import spatial.Coordinate;

/**
 *
 * @author Alex
 */
public class NetworkGraph {
    
    private Graph<Node, Link> graph;
    private Map<Integer, Node> nodes;
    private DijkstraShortestPath<Node,Link> dij;
    
    public NetworkGraph(){
        graph = new DirectedSparseMultigraph(); 
        nodes = new HashMap<Integer, Node>();
    }
	
	public void calcDijkstra(){
		Transformer<Link, Double> wtTransformer = new Transformer<Link,Double>() {
			public Double transform(Link link) {
				return link.getLength();
				}
			};
		dij = new DijkstraShortestPath(graph, wtTransformer);
	}
    
	public List<Link> getDijkstraPath(Node start, Node end){
		return dij.getPath(start, end);
	}
	
	public double getDijkstraLength(Node start, Node end){
		return dij.getDistance(start, end).doubleValue();
	}
		
    public Graph<Node, Link> getGraph(){
		return this.graph;
    }
    
    public Collection<network.Link> getLinks() {
        return graph.getEdges();
    }
    
    public Collection<Node> getNodes() {
        return graph.getVertices();
    }
    
    public Node getNode (int id){
        return nodes.get(id);
    }

    public void addNode(Node node) {
        nodes.put(node.getId(), node);
    }

    public void addLink(Link link) {
        graph.addEdge(link, link.getFrom(), link.getTo());
    }
    
    public BoundingBox getBoundingBox() {
		Double minX = null;
		Double minY = null;
		Double maxX = null;
		Double maxY = null;
		
		for (Node node : graph.getVertices()) {
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
