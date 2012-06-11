/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package heuristic;

import logic.Board;

/**
 *
 * @author Alex
 */
public class BlockHeuristic implements Heuristic{
	
	private int[] lineArrayX;
	private int[] lineArrayY;
	private Board board;
	private int rowLengthToWin;
	
	public BlockHeuristic(int rowLengthToWin){
		initLineArray();
		this.rowLengthToWin = rowLengthToWin;
	}

	@Override
	public int getBestColumn(Board board, int playerID) {
		this.board = board;
		
		int res = 0;
		int tmpCount = 0;
		int maxCount = -1;
		int[] topFields = board.getTopFields();
		
		for (int x = 0; x < topFields.length; ++x) {
			tmpCount = calcAdjacentCoins(x, topFields[x], playerID, 2);
			if (tmpCount > maxCount){
				maxCount = tmpCount;
				res = x;
			}
		}
		return res;
	}

	@Override
	public int calcColumnScore(Board board, int x, int playerID) {
		this.board = board;
		return calcAdjacentCoins(x, board.getTopField(x), playerID, 2);
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param playerID
	 * @param modifier  modifier == 1 ? count : heuristic value;
	 * @return 
	 */
	private int calcAdjacentCoins(int x, int y, int playerID, int modifier){
		int res = 0;
		int posInArray = 0;
		int fieldsInRow = 1;
		int directionSwitches = 0;
		int fieldValue;
		int steps;
		int xNew, xCurr, xCurrDelta;
		int yNew, yCurr, yCurrDelta;
		if (y >= 0){
			while (posInArray <= 3){
				xCurr = x;
				yCurr = y; 
				xCurrDelta = lineArrayX[posInArray];
				yCurrDelta = lineArrayY[posInArray];
				directionSwitches = 0;
				fieldsInRow = 0;
				steps = 0;

				while(directionSwitches < 2){
					xNew = xCurr + xCurrDelta;
					yNew = yCurr + yCurrDelta;
					try{
						fieldValue = board.getFieldValue(xNew, yNew);
						steps++;
						if (fieldValue != playerID && fieldValue != board.getEmptyValue()){
							fieldsInRow = (fieldsInRow + 1) * 2;
							xCurr = xNew;
							yCurr = yNew;
						} else {
							xCurrDelta *= (-1);
							yCurrDelta *= (-1);
							directionSwitches++;
							res += fieldsInRow;
							fieldsInRow = 0;
							steps = 0;
							xCurr = x;
							yCurr = y;
						}
						if (steps == rowLengthToWin){
							throw new IllegalAccessException("");
						}
					} catch (IllegalAccessException e){
						xCurrDelta *= (-1);
						yCurrDelta *= (-1);
						directionSwitches++;
						res += fieldsInRow;
						fieldsInRow = 0;
						steps = 0;
						xCurr = x;
						yCurr = y;
					}
				}
				posInArray++;
			}
		}
			
		return res;
	}
	
	private void initLineArray(){
		lineArrayX = new int[4];
		lineArrayX[0] = 1;
		lineArrayX[1] = 0;
		lineArrayX[2] = 1;
		lineArrayX[3] = -1;
		
		lineArrayY = new int[4];
		lineArrayY[0] = 0;
		lineArrayY[1] = 1;
		lineArrayY[2] = 1;
		lineArrayY[3] = 1;
	}
	
}
