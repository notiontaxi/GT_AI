package logic;

import heuristic.Heuristic;
import heuristic.HeuristicNOutOfFour;
import java.util.List;

public class AlphaBeta {
	private Logic logicClone = null;
	private int activePlayer = -1; 
	private int[][] currFields;
	private int xSize;
	private int ySize;
	private int finalUtility;
	
	private Heuristic heuristic = null;

	private boolean isDone = false;
	private int iteration = 0;

	/**
	 * Constructor - init AlphaBeta with logic
	 * @param logic
	 */
	public AlphaBeta(Logic logic) {
		this.logicClone = logic.clone();
		this.heuristic = new HeuristicNOutOfFour();
	}
	
	/**
	 * stat alphaBetaSearch and find next possible best move
	 * @param todoCoordinates - list with all coordinates of possible moves (x moves)
	 * @return Coordinate giving best move
	 */
	public Coordinate alphaBetaSearch(int[] topFields) {
		this.isDone = false;
		this.iteration = 0;
		
		int bestUtility = -999999;
		int bestAction = -1;
		
		for (int x = 0; x < topFields.length; ++x){
			currFields = this.logicClone.getBoardFields();
			if(topFields[x] >= 0 && this.logicClone.unsafePerformMove(x, topFields[x])) {
				this.iteration++;
				int utility = minValue(this.logicClone.getBoard().getTopFields(), this.logicClone.getConfig().getDepth(), -999999, +999999);
				if(utility > bestUtility) {
					bestUtility = utility;
					bestAction = x;
				}
				this.logicClone.undoMove(x);
			}
		}
		finalUtility = bestUtility;
		this.isDone = true;
		return new Coordinate(bestAction, topFields[bestAction]);
	}
	
	/**
	 * playing max player
	 * @param depth - how deep to go
	 * @param alhpa
	 * @param beta
	 * @return calculated utility
	 */
	private int maxValue(int[] topFields, int depth, int alhpa, int beta) {
		int utility = -999999;
		if(this.isFinal(depth)) {
			utility = this.finalUtility();
		} else {
			for(int x = 0; x < topFields.length; x++) {
				if(topFields[x] >= 0 && this.logicClone.unsafePerformMove(x, topFields[x])) {
					this.iteration++;
					currFields = this.logicClone.getBoardFields();
					int tmp = minValue(this.logicClone.getBoard().getTopFields(), depth-1, alhpa, beta);
					utility = calculateUtility(x, true, tmp, utility);

					if(utility >= beta) {
						return utility;
					}
					alhpa = Math.max(alhpa, utility);
					this.logicClone.undoMove(x);
				}
			}
		}
		return utility;
	}

	/**
	 * playing min player
	 * 
	 * @param depth - how deep to go
	 * @param alhpa
	 * @param beta
	 * @return calculated utility
	 */
	private int minValue(int[] topFields, int depth, int alhpa, int beta) {
		int utility = +999999;
		if(this.isFinal(depth)) {
			utility = this.finalUtility();
		} else {
			for(int x = 0; x < topFields.length; x++) {
				if(topFields[x] >= 0 && this.logicClone.unsafePerformMove(x, topFields[x])) {
					this.iteration++;
					currFields = this.logicClone.getBoardFields();
					int tmp = maxValue(this.logicClone.getBoard().getTopFields() , depth-1, alhpa, beta);
					utility = calculateUtility(x, false, tmp, utility);

					if(utility <= alhpa) {
						return utility;
					}
					beta = Math.min(alhpa, utility);
					this.logicClone.undoMove(x);
				}
			}
		}
		return utility;
	}

	/**
	 * calculate utility, if no heuristic is set, use Min(a,b), Max(a,b), else heuristic
	 * @param x
	 * @param getMax
	 * @param tmpUtility
	 * @param currentUtility
	 * @return utility
	 */
	private int calculateUtility(int x, boolean getMax, int tmpUtility, int currentUtility) {
		int utility = currentUtility;
		System.out.println("calculateUtility");
		if(this.heuristic == null) {
			if(getMax) {
				utility = Math.max(tmpUtility, currentUtility);
			} else {
				utility = Math.min(tmpUtility, currentUtility);
			}
		} else {
			utility = this.heuristic.calcColumnScore(this.logicClone.getBoard(), x, this.activePlayer);
			System.out.println("utility(" + x + "): " + utility);
		}
		return utility;
	}
	
	/**
	 * if game is over, calculate simple utility (-1, 0, 1)
	 * @return
	 */
	private int finalUtility() {
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

	/**
	 * checks if game is over (or max depth is reached)	
	 * @param depth
	 * @return
	 */
	private boolean isFinal(int depth) {
		boolean isFinal = this.logicClone.isGameOver() || depth < 0;
		return isFinal;
	}
	
	/**
	 * sets heuristic - if no heuristic is set, simple utility (Min(a,b)/Max(a,b)) is calculated
	 * @param heuristic
	 */
	public void setHeuristic(Heuristic heuristic) {
		this.heuristic = heuristic;
	}
	
	/**
	 * tells thread keeper that we're done
	 * @return
	 */
	public boolean isDone() {
		return isDone;
	}
	
	/**
	 * -returns final utility
	 * @return
	 */
	public int getFinalUtility() {
		return finalUtility;
	}
	
	/**
	 * what iteration are we currently in?
	 * @return
	 */
	public int getIteration() {
		return iteration;
	}
}
