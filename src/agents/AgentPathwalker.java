package agents;

import java.util.List;
import java.util.Stack;

import astar.AstarNode;

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
	
	double vx;
	double vy;
	
	public AgentPathwalker(Stack<AstarNode> path, Node start) {
		this.path = path;
		this.startNode = start;
		nextNode = path.pop();
		
		this.position = new Coordinate(startNode.getX(), startNode.getY());
		this.lastPosition = new Coordinate(startNode.getX(), startNode.getY());
		this.nextPosition = new Coordinate(nextNode.getX(),  nextNode.getY());
		
		this.direction = new Vector2D(10.0,10.0);
		
		vx = 0.01;
		vy = 0.01;
		
	}


	public void update() {
		if(!path.empty()){
			// check for new direction
			if(updateDirection()){
				deltaX = direction.getX() * this.vx;
				deltaY = direction.getY() * this.vy;
			}			
			// position += (normalized direction vector * v)
			this.position.setX(this.position.getX() + deltaX);
			this.position.setY(this.position.getY() + deltaY);
		}				
	}


	private boolean updateDirection() {
		if(nextPositionReached()){
			double xDir = this.position.getX() - this.nextPosition.getX();
			double yDir = this.position.getY() - this.nextPosition.getY();
 		
			this.direction.setX(xDir); 
			this.direction.setY(yDir); 
			 
			return true;
		}
		return false;
	}
	
	private boolean nextPositionReached(){
		if(this.position == this.nextPosition){
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
