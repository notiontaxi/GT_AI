package network;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;

import main.astarSimulation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import agents.AgentPathwalker;
import astar.Astar;
import astar.AstarNode;
import gui.JUNG;
import java.util.List;


public class JSONImport {

	/**
	 * parse file and return a Network object
	 * @return Network
	 * @throws IOException
	 */
	public NetworkGraph parseFile(String path) throws IOException {
		NetworkGraph networkGraph = new NetworkGraph();
		
		JSONObject json;
		try {
			json = new JSONObject(this.readFile(path));

		    JSONArray jsonNodes = json.getJSONObject("network").getJSONArray("nodes");
		    JSONArray jsonLinks = json.getJSONObject("network").getJSONArray("links");

		    for (int i = 0; i < jsonNodes.length(); i++) {
		    	JSONObject jsonNode = jsonNodes.getJSONObject(i).getJSONObject("node");
		    	
		    	Node node = new Node(jsonNode.getInt("id"), jsonNode.getDouble("x"), jsonNode.getDouble("y"));
		    	networkGraph.addNode(node);
		    }

		    for (int i = 0; i < jsonLinks.length(); i++) {
		    	JSONObject jsonLink = jsonLinks.getJSONObject(i).getJSONObject("link");
		    	
		    	int id = jsonLink.getInt("id");
		    	int to = jsonLink.getInt("to");
		    	int from = jsonLink.getInt("from");
		    	double length = jsonLink.getDouble("length");
		    	double capacity = jsonLink.getDouble("capacity");
		    	
		    	Link link= new Link(id, networkGraph.getNode(from), networkGraph.getNode(to), capacity, length);
		    	networkGraph.addLink(link);
		    }
		    
		} catch (JSONException e) {
			System.out.println("Error while parsing document!");
			e.printStackTrace();
		}
		return networkGraph;
	  }
	
	/**
	 * read a files content and return it as string
	 * @return String content
	 * @throws IOException
	 */
	private String readFile(String path) throws IOException {
		StringBuilder text = new StringBuilder();
		String NL = System.getProperty("line.separator");
		Scanner scanner = new java.util.Scanner(new FileInputStream(path));
		try {
			while (scanner.hasNextLine()) {
				text.append(scanner.nextLine() + NL);
			}
		} finally {
			scanner.close();
		}
		return text.toString();
	}
	
	public static void main(String[] args) {
		JSONImport jsonImport = new JSONImport();
        JUNG jung = new JUNG();
		try {
			NetworkGraph networkGraph = jsonImport.parseFile(args[0]);
			Node start = networkGraph.getNode(1322811485);
			Node destination = networkGraph.getNode(1328453500);
			
			Astar astar = new Astar(networkGraph, start, destination);
			Stack<AstarNode> path = astar.getPath();
			Stack<AstarNode> pathdummy = astar.getPath();
			
			networkGraph.calcDijkstra();
			
			
			List<Link> dijList = networkGraph.getDijkstraPath(start, destination);
			
			// ---
			
			AgentPathwalker agent = new AgentPathwalker(path);
			astarSimulation astarSimulation = new astarSimulation(agent, jung);
			
			// ---
			
			jung.addAgent(new Node(4711, start.getX(), start.getY()));
			jung.createLayout(networkGraph);
			jung.setDijkstra(dijList);
			jung.setAstarPath(path, 1322811485, 1328453500);
			
			jung.draw();
			
			
			
			/*if(args.length > 1 && args[1].equals("3d")) {
				new gui.Network(network);
			}*/
			double dist = 0;
			int ownListSize = 0;
			while (!pathdummy.empty()){
				AstarNode anode1 = pathdummy.pop();
				AstarNode anode2 = anode1.getPrevious();
				dist += anode1.getDistanceTo(anode2);
				ownListSize++;
			}	
			System.out.println("dist: " + dist + " count: " +ownListSize);
			System.out.println("dij dist: " + networkGraph.getDijkstraLength(networkGraph.getNode(1322811485), networkGraph.getNode(1328453500)) + " count:" + dijList.size());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
