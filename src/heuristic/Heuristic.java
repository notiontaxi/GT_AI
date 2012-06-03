/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package heuristic;

/**
 *
 * @author Alex
 */
public interface Heuristic {
	
	public int calcHeuristic(int[][] field);
	public int calcHeuristicForColumn(int[][] field, int col);
	
}
