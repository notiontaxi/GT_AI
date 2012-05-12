package agents;

import java.util.List;
import java.util.Stack;

import astar.AstarNode;

import main.Config;
import network.Link;
import network.Node;
import physics.PhysicsBox;
import spatial.Coordinate;
import spatial.Vector2D;

public class AgentPathwalker {


	Stack<AstarNode> path;
	Coordinate position;
	Coordinate lastPosition;
	Coordinate nextPosition;
	
	double deltaX;
	double deltaY;
	
	Vector2D direction;
	
	Node startNode;
	Node nextNode;
	
	public AgentPathwalker(Stack<AstarNode> path, Node start) {
		this.path = path;
		this.startNode = start;
		nextNode = path.pop();
		
		this.position = new Coordinate(startNode.getX(), startNode.getY());
		this.lastPosition = new Coordinate(startNode.getX(), startNode.getY());
		this.nextPosition = new Coordinate(nextNode.getX(),  nextNode.getY());
		
		this.direction = new Vector2D(0.0,0.0);

		calcDirection();
		setDelta();
		
	}


	public void update() {
		if(!path.empty()){
			// check for new direction
			if(updateDirection()){
				setDelta();
			}						
			this.position.setX(this.position.getX() + deltaX);
			this.position.setY(this.position.getY() + deltaY);
		}				
	}


	private void setDelta() {
		deltaX = direction.getX() * Config.MAX_V;
		deltaY = direction.getY() * Config.MAX_V;
		System.out.println("direction changed");
	}


	private boolean updateDirection() {
		if(nextPositionReached()){
			calcDirection();
			return true;
		}
		return false;
	}
	
	private void calcDirection() {
		double xDir = this.nextPosition.getX() - this.position.getX();
		double yDir = this.nextPosition.getY() - this.position.getY();
		
		this.direction.setX(xDir); 
		this.direction.setY(yDir); 
		this.direction = this.direction.normalize();
		
		System.out.println("new direction: " + this.direction);
	}


	private boolean nextPositionReached(){
		if((this.position.equals(this.nextPosition))){
			this.nextNode = this.path.pop();
			this.nextPosition.setX(this.nextNode.getX());
			this.nextPosition.setY(this.nextNode.getY());
			return true;
		}
		return false;
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
