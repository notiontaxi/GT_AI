/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import main.Config;

/**
 *
 * @author Alex
 */
public class Logic {
	private Player[] players;
	private int moveCount;
	private Config config;
	private int winnerID;
	private Board board;
	private int activePlayerID;
	private int[] lineArrayX;
	private int[] lineArrayY;
	private int dimXdimY;
	
	public Logic(Config config){
		this.config = config;
		players = new Player[2];
		players[0] = new Player(0,"Max");
		players[1] = new Player(1,"Min");
		
		winnerID = -1;
		activePlayerID = 0;
		dimXdimY = config.getDimensionX() * config.getDimensionY();
		board = new Board(config.getDimensionX(), config.getDimensionY());
		initLineArray();
	}	
	
	public boolean performMove(int x, int y) {
		try {
			//System.out.println("Valid move(" + x + "," + y + ")");
			board.performMove(activePlayerID, x,y);
		} catch (IllegalAccessException e) {
			System.out.println("Invalid move(" + x + "," + y + ").");
			return false;
		}
		moveCount++;
		if (moveCount >= (2 * config.getRowLengthToWin()) - 1){
			calculateWinner(x,y);
		}
		
		switchPlayer();
		
		return true;
	}

	public void undoMove(int x, int y) {
		board.undoMove(x,y);
		this.winnerID = -1;
		moveCount--;
		switchPlayer();
	}

	public boolean isMovePossible(int x, int y) {
		return board.isFieldEmpty(x, y);
	}
	
	public boolean isGameOver(){
		return (moveCount == dimXdimY) || (winnerID != -1);
	}
	
	private void switchPlayer(){
		activePlayerID = (activePlayerID + 1) % 2;
	}

	public Player getWinner() {
		if (winnerID != -1)
			return players[winnerID];
		else
			return null;
	}
	
	public int getWinnerID() {
		return winnerID;
	}

	public int getActivePlayer() {
		return activePlayerID;
	}
	
	public int getPlayerCount(){
		return 2;
	}
	
	public Board getBoard(){
		return this.board;
	}
	
	public Integer[][] getBoardFields(){
		return this.board.getFields();
	}
	
	private void calculateWinner(int x, int y){
		int posInArray = 0;
		int fieldsInRow = 1;
		int directionSwitches = 0;
//		Coordinate currCoordinate;
//		Coordinate backUpCoordinate = new Coordinate(x,y);
		int xNew, xCurr, xCurrDelta;
		int yNew, yCurr, yCurrDelta;
		while (winnerID == -1 && posInArray <= 3){
			fieldsInRow = 1;
			xCurr = x;
			yCurr = y; //currCoordinate = backUpCoordinate;
			xCurrDelta = lineArrayX[posInArray];
			yCurrDelta = lineArrayY[posInArray];
			directionSwitches = 0;
			
			while(winnerID == -1 && directionSwitches < 2){
				xNew = xCurr + xCurrDelta;
				yNew = yCurr + yCurrDelta;
				if (board.isCoordinateValid(xNew, yNew) && board.unsafeGetFieldValue(xNew,yNew) == activePlayerID){
					fieldsInRow++;
					xCurr = xNew;
					yCurr = yNew;
				} else {
					xCurrDelta *= (-1);
					yCurrDelta *= (-1);
					directionSwitches++;
					xCurr = x;
					yCurr = y;
				}
				if (fieldsInRow == config.getRowLengthToWin()){
					winnerID = activePlayerID;
				}
			}
			posInArray++;
		}
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
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		
		Logic logic = new Logic(config);

		logic.players = players.clone();
		logic.moveCount = this.moveCount;
		logic.config = new Config();
		logic.winnerID = this.winnerID;
		
		logic.board = (Board) this.board.clone();
		logic.activePlayerID = this.activePlayerID;
		logic.lineArrayX = this.lineArrayX.clone();
		logic.lineArrayY = this.lineArrayY.clone();
		
		return logic;
	}	
	
}
