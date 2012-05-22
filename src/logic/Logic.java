/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Alex
 */
public class Logic {
	private Map<Integer, Player> players;
	private Player activePlayer;
	
	public Logic(){
		players = new HashMap<Integer, Player>();
		players.put(0, new Player(0, "Max"));
		players.put(1, new Player(1, "Min"));
		activePlayer = players.get(0);
	}
	
}
