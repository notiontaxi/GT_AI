/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package heuristic;

/**
 *
 * @author Alex
 */
public class BoardStatus{
	
	private long player0;
	private long player1;

	public BoardStatus(long player0, long player1) {
		this.player0 = player0;
		this.player1 = player1;
	}
	
	public long getPlayer0() {
		return player0;
	}

	public long getPlayer1() {
		return player1;
	}
	
	public boolean equals(BoardStatus other){
		return other.getPlayer0() == player0 && other.getPlayer1() == player1; 
	}
}
