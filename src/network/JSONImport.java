package network;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JSONImport {

	/**
	 * parse file and return a Network object
	 * @return Network
	 * @throws IOException
	 */
	public Network parseFile(String path) throws IOException {
		Network network = new Network();
		
		JSONObject json;
		try {
			json = new JSONObject(this.readFile(path));

		    JSONArray jsonNodes = json.getJSONObject("network").getJSONArray("nodes");
		    JSONArray jsonLinks = json.getJSONObject("network").getJSONArray("links");

		    for (int i = 0; i < jsonNodes.length(); i++) {
		    	JSONObject jsonNode = jsonNodes.getJSONObject(i).getJSONObject("node");
		    	
		    	Node node = new Node(jsonNode.getInt("id"), jsonNode.getDouble("x"), jsonNode.getDouble("y"));
		    	
		    	network.addNode(node);
		    }

		    for (int i = 0; i < jsonLinks.length(); i++) {
		    	JSONObject jsonLink = jsonLinks.getJSONObject(i).getJSONObject("link");
		    	
		    	int id = jsonLink.getInt("id");
		    	int to = jsonLink.getInt("to");
		    	int from = jsonLink.getInt("from");
		    	double length = jsonLink.getDouble("length");
		    	double capacity = jsonLink.getDouble("capacity");
		    	
		    	Link link= new Link(id, network.getNode(from), network.getNode(to), capacity, length);
		    	network.addLink(link);
		    }
		    
		} catch (JSONException e) {
			System.out.println("Error while parsing document!");
			e.printStackTrace();
		}
		return network;
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
		try {
			Network network = jsonImport.parseFile(args[0]);
			
			if(args.length > 1 && args[1].equals("3d")) {
				new gui.Network(network);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
