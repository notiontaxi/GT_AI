/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import java.util.HashMap;
import java.util.Map;
import main.Config;

/**
 *
 * @author Alex
 */
public class Logic {
	private Map<Integer, Player> players;
	private Field field;
	private int activePlayerID;
	
	public Logic(Config config){
		players = new HashMap<Integer, Player>();
		players.put(0, new Player(0, "Max"));
		players.put(1, new Player(1, "Min"));
		activePlayerID = players.get(0).getId();
		field = new Field(config.getDimensionX(), config.getDimensionY());
	}	
	
	public boolean performMove(int x, int y) {
		return true;
	}

	public boolean undoMove(int x, int y) {
		return true;
	}

	public boolean isMovePossible(int x, int y) {
		return true;
	}

	public Player getWinner() {
		return null;
	}

	public int getActivePlayer() {
		return activePlayerID;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}	
	
}
