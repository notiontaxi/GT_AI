package logic;

import heuristic.Heuristic;
import heuristic.BoardHeuristic;
import org.omg.CORBA.TIMEOUT;

public class AlphaBeta {
	private Logic logicClone = null;
	private int activePlayer = -1; 
	
	private BoardHeuristic heuristic = null;

	private boolean isDone = false;
	private int maxDepth = 0;
	private int iteration = 0;

	/**
	 * Constructor - init AlphaBeta with logic
	 * @param logic
	 */
	public AlphaBeta(Logic logic) {
		this.logicClone = logic.clone();
		this.heuristic = new BoardHeuristic();
		activePlayer = logic.getActivePlayer();
		maxDepth = logic.getConfig().getDepth();
	}
	
	public Coordinate smallAlphaBetaSearch(int[] topFields) {
		int bestAction = heuristic.getBestColumn(logicClone.getBoard(),  this.activePlayer);
		this.logicClone.performMove(bestAction);
		return new Coordinate(bestAction, topFields[bestAction]);
	}
	
	/**
	 * st41at alphaBetaSearch and find next possible best move
	 * @param todoCoordinates - list with all coordinates of possible moves (x moves)
	 * @return Coordinate giving best move
	 */
	public Coordinate alphaBetaSearch(int[] topFields) {
		this.isDone = false;
		this.iteration = 0;
		
		long start = System.currentTimeMillis();
		
		double bestUtility = -999999;
		int bestAction = -1;
		
		for (int x = 0; x < topFields.length; ++x){
			if(topFields[x] >= 0 && this.logicClone.performMove(x)) {
				this.iteration++;
				double utility = minValue(this.logicClone.getTopFields(), this.logicClone.getConfig().getDepth(), bestUtility, 9999999);
				System.out.println("utility(" + x + ")" + utility);
				if(utility > bestUtility) {
					bestUtility = utility;
					bestAction = x;
				}
				this.logicClone.undoMove(x);
			}
		}
		this.isDone = true;
		
		System.out.println("time:" + (System.currentTimeMillis() - start));
		
		return new Coordinate(bestAction, topFields[bestAction]);
	}
	
	/**
	 * playing max player
	 * @param depth - how deep to go
	 * @param alhpa
	 * @param beta
	 * @return calculated utility
	 */
	private double maxValue(int[] topFields, int depth, double alhpa, double beta) {
		double utility = -999999;
		
		if (this.logicClone.isGameOver()){
			utility = finalUtility() * (1 - ((maxDepth - depth) * 0.1));
		} else if (depth < 0){
			utility = (double) this.heuristic.getBoardUtility(this.logicClone.getBoard(), this.activePlayer) * (1 - ((maxDepth - depth) * 0.1));
		} else {
			for(int x = 0; x < topFields.length; x++) {
				if(topFields[x] >= 0 && this.logicClone.performMove(x)) {
					this.iteration++;
					double tmp = minValue(this.logicClone.getTopFields(), depth-1, alhpa, beta);
					utility = Math.max(tmp, utility);
					this.logicClone.undoMove(x);
					if(utility >= beta) {
						return utility;
					}
					alhpa = Math.max(alhpa, utility);
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
	private double minValue(int[] topFields, int depth, double alhpa, double beta) {
		double utility = 999999;

		if (this.logicClone.isGameOver()){
			utility = finalUtility() * (1 - ((maxDepth - depth) * 0.1));
		} else if (depth < 0){
			utility = (double) this.heuristic.getBoardUtility(this.logicClone.getBoard(), this.activePlayer) * (1 - ((maxDepth - depth) * 0.1));
		} else {
			for(int x = 0; x < topFields.length; x++) {
				if(topFields[x] >= 0 && this.logicClone.performMove(x)) {
					this.iteration++;
					double tmp = maxValue(this.logicClone.getTopFields() , depth-1, alhpa, beta);
					utility = Math.min(tmp, utility);
					this.logicClone.undoMove(x);
					if(utility <= alhpa) {
						return utility;
					}
					beta = Math.min(beta, utility);
				}
			}
		}
		return utility;
	}
	
	/**
	 * if game is over, calculate simple utility (-1, 0, 1)
	 * @return
	 */
	private double finalUtility() {
		double utility = 0;
		int winnerID = this.logicClone.getWinnerID();
		if(winnerID != -1) {
			if(winnerID == this.activePlayer) {
				utility = 800;
			} else {
				utility = -800;
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
		return this.logicClone.isGameOver() || depth < 0;
	}
	
	/**
	 * sets heuristic - if no heuristic is set, simple utility (Min(a,b)/Max(a,b)) is calculated
	 * @param heuristic
	 */
	public void setHeuristic(BoardHeuristic heuristic) {
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
	 * what iteration are we currently in?
	 * @return
	 */
	public int getIteration() {
		return iteration;
	}
}
