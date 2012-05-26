package logic;

import java.util.List;
import java.util.Vector;

public class MinMax {

	private Logic logicClone = null;
	private int activePlayer = -1; 
	private Integer[][] currFields;
	private int xSize;
	private int ySize;
	
	public MinMax(Logic logic) throws CloneNotSupportedException {
		this.activePlayer = logic.getActivePlayer();
		this.logicClone = (Logic) logic.clone();
		this.xSize = logicClone.getBoard().getFields()[0].length;
		this.ySize = logicClone.getBoard().getFields()[0].length;
	}

	
	
	public Coordinate minmaxDecision() {
		int bestUtility = -999999;
		Coordinate bestAction = null;
		long startTime = System.currentTimeMillis();

		currFields = this.logicClone.getBoardFields();

		for(int x = 0; x < xSize; x++) {
			for(int y = 0; y < ySize; y++) {
				currFields = this.logicClone.getBoard().getFields();
				System.out.println("(" + x + "," + y +")");
				if(this.logicClone.performMove(x, y)) {
					int utility = minValue();
					if(utility > bestUtility) {
						bestUtility = utility;
						System.out.println(utility);
						bestAction = new Coordinate(x, y);
					}
					this.logicClone.undoMove(x, y);
				}
				System.out.println("Duration: " + (System.currentTimeMillis() - startTime));
				startTime = System.currentTimeMillis();
			}
		}
		return bestAction;
	}
	
	public int minValue() {
		int utility = 999999;
		
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