package logic;

public class MinMax {

	private Logic logic = null;
	private Logic logicClone = null;
	
	
	public MinMax(Logic logic) throws CloneNotSupportedException {
		this.logic = logic;
		this.logicClone = (Logic) logic.clone();
	}

	
	
	public Coordinate minmaxDecision(int state) {
		int bestUtility = -999999;
		Coordinate bestAction = null;
		

		Integer[][] fields = new Integer[4][4]; // this.logic.getFields();
		
		for(int x = 0; x < fields.length; x++) {
			
			for(int y = 0; y < fields[x].length; y++) {
			
				if(this.logicClone.performMove(x, y)) {
					int utility = minValue(state);
					if(utility > bestUtility) {
						bestUtility = utility;
						bestAction = new Coordinate(x, y);
					}
				}
			}
		}
		return bestAction;
	}
	
	public int minValue(int state) {
		int utility = 999999;
		
		if(this.logicClone.isGameOver()) {
			utility = this.utility();
		} else {

			Integer[][] fields = new Integer[4][4]; // this.logic.getFields();
			for(int x = 0; x < fields.length; x++) {
				
				for(int y = 0; y < fields[x].length; y++) {
				
					if(this.logicClone.performMove(x, y)) {
						int tmp = maxValue(state);
						utility = Math.min(tmp, utility);
					}
				}
			}
		}
		return utility;
	}
	

	public int maxValue(int state) {
		int utility = -999999;
		
		if(this.logicClone.isGameOver()) {
			utility = this.utility();
		} else {

			Integer[][] fields = new Integer[4][4]; // this.logic.getFields();
			for(int x = 0; x < fields.length; x++) {
				for(int y = 0; y < fields[x].length; y++) {
					if(this.logicClone.performMove(x, y)) {
						int tmp = minValue(state);
						utility = Math.max(tmp, utility);
					}
				}
			}
		}
		return utility;
	}
	
	
	private int utility() {

		int utility = 0;
		Player winner = this.logicClone.getWinner();
		if(this.logicClone.getWinner() != null) {
			if(winner.getId() == this.logic.getActivePlayer()) {
				utility = 1;
			} else {
				utility = -1;
			}
		}
		return utility;
	}
}