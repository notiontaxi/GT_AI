package logic;

public class MinMax {

	private Logic logicClone = null;
	private int activePlayer = -1; 
	private int comboMin = 0;
	private int comboMax = 0;
	private int comboMM = 0;
	private Integer[][] currFields;
	
	public MinMax(Logic logic) throws CloneNotSupportedException {
		this.activePlayer = logic.getActivePlayer();
		this.logicClone = (Logic) logic.clone();
	}

	
	
	public Coordinate minmaxDecision() {
		int bestUtility = -999999;
		Coordinate bestAction = null;

		currFields = this.logicClone.getBoard().getFields(); // this.logic.getFields();

		for(int x = 0; x < currFields.length; x++) {
			for(int y = 0; y < currFields[x].length; y++) {
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
			}
		}
		System.out.println("ComboMin: " + comboMin);
		System.out.println("ComboMax: " + comboMax);
		System.out.println("ComboMM: " + comboMM);
		return bestAction;
	}
	
	public int minValue() {
		int utility = 999999;
		if(this.logicClone.isGameOver()) {
			utility = this.utility();
		} else {

			//Integer[][] fields = this.logicClone.getBoard().getFields(); // this.logic.getFields();
			for(int x = 0; x < currFields.length; x++) {
				
				for(int y = 0; y < currFields[x].length; y++) {
					if(this.logicClone.performMove(x, y)) {
						currFields = this.logicClone.getBoard().getFields();
						int tmp = maxValue();
						utility = Math.min(tmp, utility);
						this.logicClone.undoMove(x, y);
					}
				}
			}
		}
		return utility;
	}
	

	public int maxValue() {
		int utility = -999999;
		
		if(this.logicClone.isGameOver()) {
			//System.out.println("Game over");
			utility = this.utility();
		} else {

			//Integer[][] fields = this.logicClone.getBoard().getFields(); // this.logic.getFields();
			for(int x = 0; x < currFields.length; x++) {
				for(int y = 0; y < currFields[x].length; y++) {
					if(this.logicClone.performMove(x, y)) {
						currFields = this.logicClone.getBoard().getFields();
						int tmp = minValue();
						utility = Math.max(tmp, utility);

						this.logicClone.undoMove(x, y);
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
			if(winner.getId() == this.activePlayer) {
				utility = 1;
			} else {
				utility = -1;
			}
		}
		return utility;
	}
}