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
	Coordinate lastPosition;
	Coordinate nextPosition;
	
	Node startNode;
	Node nextNode;
	
	public AgentPathwalker(Stack<AstarNode> path, Node start) {
		this.path = path;
		this.startNode = start;
		nextNode = path.pop();
		
		this.position = new Coordinate(startNode.getX(), startNode.getY());
		this.lastPosition = new Coordinate(startNode.getX(), startNode.getY());
		this.nextPosition = new Coordinate(nextNode.getX(),  nextNode.getY());
	}


	public void update() {
		if(!path.empty()){
		nextNode = path.pop();
		
		this.lastPosition.setX(this.position.getX());
		this.lastPosition.setY(this.position.getY());
				
	//	this.nextPosition = new Coordinate(nextNode.getX(),  nextNode.getY());
		this.position = new Coordinate(nextNode.getX(), nextNode.getY());
		}
				
	}


	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}


	public double getX() {
		return this.position.getX();
	}


	public double getY() {
		return this.position.getY();
	}

}
