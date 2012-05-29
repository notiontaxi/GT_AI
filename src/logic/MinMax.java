package logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Vector;

public class MinMax{

	protected Logic logicClone = null;
	protected int activePlayer = -1; 
	protected int[][] currFields;
	protected List<Integer> emptyList;
	protected int xSize;
	protected int ySize;
	protected int finalUtility;
	private int c0 = 0;
	private int c2 = 0;
	private int c1 = 0;
	
	public MinMax(Logic logic) throws CloneNotSupportedException {
		this.activePlayer = logic.getActivePlayer();
		this.logicClone = (Logic) logic.clone();
		this.xSize = logicClone.getBoard().getFields().length;
		this.ySize = logicClone.getBoard().getFields()[0].length;
	}

	
	public Coordinate minmaxDecision(List<Coordinate> todoCoordinates) {
		int bestUtility = -999999;
		Coordinate bestAction = null;

		if (todoCoordinates == null){
			todoCoordinates = this.logicClone.getBoard().getEmptyFields();
		}
		
		//emptyList = this.logicClone.getBoard().getEmptyFieldsCoded();
		
		for (Coordinate coordinate : todoCoordinates){
			int x = coordinate.getX();
			int y = coordinate.getY();
			currFields = this.logicClone.getBoardFields();
			System.out.println("Start (" + x + "," + y +")");
			if(this.logicClone.performMove(x, y)) {
				System.out.println("Performing (" + x + "," + y +")");
				int utility = minValue();
				if(utility > bestUtility) {
					bestUtility = utility;
					bestAction = coordinate;
				}
				this.logicClone.undoMove(x, y);
			}
			System.out.println("End (" + x + "," + y +")");
		}
		finalUtility = bestUtility;
		return bestAction;
	}
	
	/*public int minValue() {
		int utility = 999999;
		c1++;
		if(this.logicClone.isGameOver()) {
			utility = this.utility();
		} else {

			emptyList = this.logicClone.getBoard().getEmptyFieldsCoded();
			int x,y;
			for (Integer val : emptyList){
				x = (int) (val / ySize);
				y = val % xSize;
						if(this.logicClone.performMove(x, y)) {
							currFields = this.logicClone.getBoardFields();
							int tmp = maxValue();
							utility = Math.min(tmp, utility);
							this.logicClone.undoMove(x, y);
						}
			}
		}
		return utility;
	}
	

	public int maxValue() {
		int utility = -999999;
		c2++;
		if(this.logicClone.isGameOver()) {
			utility = this.utility();
		} else {
			
			emptyList = this.logicClone.getBoard().getEmptyFieldsCoded();
			int x,y;
			for (Integer val : emptyList){
				x = (int) (val / ySize);
				y = val % xSize;
						if(this.logicClone.performMove(x, y)) {
							currFields = this.logicClone.getBoardFields();
							int tmp = minValue();
							utility = Math.max(tmp, utility);
							this.logicClone.undoMove(x, y);
						}
			}
		}
		return utility;
	}*/
	
	public int minValue() {
		int utility = 999999;
		c1++;
		if(this.logicClone.isGameOver()) {
			utility = this.utility();
		} else {

			
			for(int x = 0; x < xSize; x++) {
				for(int y = 0; y < ySize; y++) {
					if (currFields[x][y] == -1){
						if(this.logicClone.performMove(x, y)) {
							currFields = this.logicClone.getBoardFields();
							int tmp = maxValue();
							utility = Math.min(tmp, utility);
							this.logicClone.undoMove(x, y);
						}
					}
				}
			}
		}
		return utility;
	}
	

	public int maxValue() {
		int utility = -999999;
		c2++;
		if(this.logicClone.isGameOver()) {
			utility = this.utility();
		} else {
			for(int x = 0; x < xSize; x++) {
				for(int y = 0; y < ySize; y++) {
					if (currFields[x][y] == -1){
						if(this.logicClone.performMove(x, y)) {
							currFields = this.logicClone.getBoardFields();
							int tmp = minValue();
							utility = Math.max(tmp, utility);
							this.logicClone.undoMove(x, y);
						}
					}
				}
			}
		}
		return utility;
	}
	
	
	private int utility() {

		int utility = 0;
		int winnerID = this.logicClone.getWinnerID();
		if(winnerID != -1) {
			if(winnerID == this.activePlayer) {
				utility = 1;
			} else {
				utility = -1;
			}
		}
		return utility;
	}
}