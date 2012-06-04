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
public interface Heuristic {
	
	public int getBestColumn(Board board, int playerID);
	public int calcColumnScore(Board board, int x, int playerID);
	
}
