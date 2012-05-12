package agents;

import java.util.Iterator;
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


	public static boolean reset;
	
	Stack<AstarNode> path;
	Stack<AstarNode> pathBackup;
	
	Coordinate position;
	Coordinate lastPosition;
	Coordinate nextPosition;
	boolean destinationNotReached;
	
	double deltaX;
	double deltaY;
	
	Vector2D direction;
	
	Node startNode;
	Node nextNode;
	
	public AgentPathwalker(Stack<AstarNode> path, Node start) {
		destinationNotReached = true;
		pathBackup = path;		
		this.path = new Stack<AstarNode>();				
		this.startNode = start;		
		this.direction = new Vector2D(0.0,0.0);
		
		init();
	}


	private void init() {
		
		Iterator<AstarNode> iter = pathBackup.listIterator();
		
		while(iter.hasNext()){
			AstarNode next = iter.next();
			path.push(next);
		}	
		
		nextNode = path.pop();		
		this.position = new Coordinate(startNode.getX(), startNode.getY());
		this.lastPosition = new Coordinate(startNode.getX(), startNode.getY());
		this.nextPosition = new Coordinate(nextNode.getX(),  nextNode.getY());

		calcDirection();
		setDelta();
	}


	public void update() {
		if(destinationNotReached){
			// check for new direction
			if(updateDirection()){
				if(AgentPathwalker.reset){
					init();
					AgentPathwalker.reset = false;
				}else	
				setDelta();
			}						
			this.position.setX(this.position.getX() + deltaX);
			this.position.setY(this.position.getY() + deltaY);
		}				
	}


	private void setDelta() {
		deltaX = direction.getX() * Config.MAX_V;
		deltaY = direction.getY() * Config.MAX_V;
		//System.out.println("direction changed");
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
		
		//System.out.println("new direction: " + this.direction);
	}


	private boolean nextPositionReached(){
		if((this.position.equals(this.nextPosition))){
			if(this.path.isEmpty()){
				this.destinationNotReached = false;
			}else{
			this.nextNode = this.path.pop();
			this.nextPosition.setX(this.nextNode.getX());
			this.nextPosition.setY(this.nextNode.getY());
			return true;
			}
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
