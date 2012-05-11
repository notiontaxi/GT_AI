package agents;

import java.util.List;
import java.util.Stack;

import astar.AstarNode;

import network.Link;
import network.Node;
import physics.PhysicsBox;
import spatial.Coordinate;

public class AgentPathwalker {


	Stack<AstarNode> path;
	Coordinate position;
	Coordinate lastNode;
	Coordinate nextNode;
	
	Node startNode;
	
	
	public AgentPathwalker(Stack<AstarNode> path) {
		this.path = path;

		this.startNode = path.pop();
		Node nextNode = path.pop();
		
		this.position = new Coordinate(startNode.getX(), startNode.getY());
		this.lastNode = new Coordinate(startNode.getX(), startNode.getY());
		this.nextNode = new Coordinate(nextNode.getX(),  nextNode.getY());
	}


	public void update() {
		// TODO Auto-generated method stub
		
	}


	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}


	public double getX() {
		// TODO Auto-generated method stub
		return 0;
	}


	public double getY() {
		// TODO Auto-generated method stub
		return 0;
	}

}
