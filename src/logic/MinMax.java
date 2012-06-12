package logic;

import java.util.List;

public class MinMax{

	protected Logic logicClone = null;
	protected int activePlayer = -1; 
	protected int[][] currFields;
	protected List<Integer> emptyList;
	protected int xSize;
	protected int ySize;
	protected int finalUtility;

	private boolean isDone = false;
	private int itteration = 0;
	
	public MinMax(Logic logic) throws CloneNotSupportedException {
		this.activePlayer = logic.getActivePlayer();
		this.logicClone = (Logic) logic.clone();
		this.xSize = logicClone.getBoard().getFields().length;
		this.ySize = logicClone.getBoard().getFields()[0].length;

		this.itteration = 0;
	}

	
	public Coordinate minmaxDecision(List<Coordinate> todoCoordinates) {
		this.isDone = false;
		this.itteration = 0;
		
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
				this.itteration++;
				System.out.println("Performing (" + x + "," + y +")");
				int utility = minValue(this.logicClone.getConfig().getDepth());
				if(utility > bestUtility) {
					bestUtility = utility;
					bestAction = coordinate;
				}
				//this.logicClone.undoMove(x, y);
			}
			System.out.println("End (" + x + "," + y +")");
		}
		finalUtility = bestUtility;
		this.isDone = true;
		return bestAction;
	}
	
	public int minValue(int depth) {
		int utility = 999999;
		if(this.logicClone.isGameOver() || depth < 0) {
			utility = this.utility();
		} else {

			
			for(int x = 0; x < xSize; x++) {
				for(int y = 0; y < ySize; y++) {
					if (currFields[x][y] == -1){
						if(this.logicClone.performMove(x, y)) {
							
							this.itteration++;
							currFields = this.logicClone.getBoardFields();
							int tmp = maxValue(depth-1);
							utility = Math.min(tmp, utility);
							//this.logicClone.undoMove(x, y);
						}
					}
				}
			}
		}
		return utility;
	}
	

	public int maxValue(int depth) {
		int utility = -999999;
		if(this.logicClone.isGameOver() || depth < 0) {
			utility = this.utility();
		} else {
			for(int x = 0; x < xSize; x++) {
				for(int y = 0; y < ySize; y++) {
					if (currFields[x][y] == -1){
						if(this.logicClone.performMove(x, y)) {
							this.itteration++;
							currFields = this.logicClone.getBoardFields();
							int tmp = minValue(depth-1);
							utility = Math.max(tmp, utility);
							//this.logicClone.undoMove(x, y);
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

	public boolean isDone() {
		return isDone;
	}
	
	public int getItteration() {
		return itteration;
	}
}