/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.Config;

/**
 *
 * @author Alex
 */
public class Logic {
	private Map<Integer, Player> players;
	private int moveCount;
	private Config config;
	private int winnerID;
	private Board board;
	private int activePlayerID;
	private Coordinate[] lineArray;
	
	public Logic(Config config){
		this.config = config;
		players = new HashMap<Integer, Player>();
		addPlayer("Max");
		addPlayer("Min");
		
		winnerID = -1;
		activePlayerID = 0;
		board = new Board(config.getDimensionX(), config.getDimensionY());
		initLineArray();
	}	
	
	public boolean performMove(int x, int y) {
		try {
			board.performMove(activePlayerID, new Coordinate(x,y));
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return false;
		}
		moveCount++;
		if (moveCount >= (2 * config.getRowLengthToWin()) - 1){
			calculateWinner(x,y);
		}
		switchPlayer();
		return true;
	}

	public void undoMove(int x, int y) throws IllegalAccessException {
		board.undoMove(activePlayerID, new Coordinate(x,y));
	}

	public boolean isMovePossible(int x, int y) {
		return board.isFieldEmpty(new Coordinate(x, y));
	}
	
	public boolean isGameOver(){
		return (moveCount == config.getDimensionX() * config.getDimensionY()) || (winnerID != -1);
	}
	
	private void switchPlayer(){
		activePlayerID = (activePlayerID + 1) % players.size();
	}

	public Player getWinner() {
		return players.get(winnerID);
	}

	public int getActivePlayer() {
		return activePlayerID;
	}
	
	public int getPlayerCount(){
		return players.size();
	}
	
	public Board getBoard(){
		return this.board;
	}
	
	private void addPlayer(String name){
		int playerID = players.size();
		players.put(playerID, new Player(playerID, name));
	}
	
	private void calculateWinner(int x, int y){
		int posInArray = 0;
		int fieldsInRow = 1;
		int directionSwitches = 0;
		Coordinate currCoordinate;
		Coordinate backUpCoordinate = new Coordinate(x,y);
		Coordinate currDeltaCoordinate;
		while (winnerID == -1 && posInArray <= 3){
			fieldsInRow = 1;
			currCoordinate = backUpCoordinate;
			currDeltaCoordinate = lineArray[posInArray];
			directionSwitches = 0;
			
			while(winnerID == -1 && directionSwitches < 2){
				try {
					Coordinate temp = getNeighbour(currCoordinate, currDeltaCoordinate);
					if (temp != null && board.getFieldValue(temp) == activePlayerID){
						fieldsInRow++;
						currCoordinate = temp; 
					} else {
						currDeltaCoordinate = getInverseCoordinate(currDeltaCoordinate);
						directionSwitches++;
						currCoordinate = backUpCoordinate;
					}
				} catch (IllegalAccessException ex) {}
				if (fieldsInRow == config.getRowLengthToWin()){
					winnerID = activePlayerID;
				}
			}
			posInArray++;
		}
	}
	
	private Coordinate getInverseCoordinate(Coordinate coordinate){
		return new Coordinate(coordinate.getX() * (-1),coordinate.getY() * (-1));
	}
	
	private void initLineArray(){
		lineArray = new Coordinate[4];
		lineArray[0] = new Coordinate(-1,1);
		lineArray[1] = new Coordinate(0,1);
		lineArray[2] = new Coordinate(1,1);
		lineArray[3] = new Coordinate(1,0);
	}

	private Coordinate getNeighbour(Coordinate coordinate, Coordinate nextDelta){
		Coordinate temp = new Coordinate(coordinate.getX() + nextDelta.getX(), coordinate.getY() + nextDelta.getY());
		return board.isCoordinateValid(temp) ? temp : null;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}	
	
}
