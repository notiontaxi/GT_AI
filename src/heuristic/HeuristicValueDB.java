/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package heuristic;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Alex
 */
public class HeuristicValueDB {
	
	Map<BoardStatus, Double> values;
	
	public HeuristicValueDB(){
		values = new HashMap<BoardStatus,Double>();
	}
	
	public void insert(BoardStatus key, double value){
		values.put(key, value);
	}
	
	public double get(BoardStatus key) throws IllegalArgumentException{
		Double res = values.get(key);
		if (res == null) throw new IllegalArgumentException("Key not found.");
		return res.doubleValue();
	}
}
