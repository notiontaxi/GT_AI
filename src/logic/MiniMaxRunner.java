package logic;

import java.util.List;

public class MiniMaxRunner extends MinMax implements Runnable{

	private List<Coordinate> todoCoordinates;
	private Coordinate finalCoordinate;
	private int id;
	
	public MiniMaxRunner(Logic logic, List<Coordinate> todoCoordinates, int id) throws CloneNotSupportedException{
		super(logic);
		this.id = id;
		int i = 0;
		this.todoCoordinates = todoCoordinates;
		System.out.println("runner ID:" + id);
		for (Coordinate coordinate : todoCoordinates){
			System.out.println("item " + i + " = (" + coordinate.getX() + "," + coordinate.getY() + ")");
			i++;
		}
		
	}

	public int getFinalUtility(){
		return this.finalUtility;
	}
	
	public Coordinate getFinalCoordinate(){
		return this.finalCoordinate;
	}

	@Override
	public void run() {
		this.finalCoordinate = minmaxDecision(todoCoordinates);
	}
	public int getId() {
		return id;
	}
	

}