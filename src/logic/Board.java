/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alex
 */
public class Board {

	private int[][] fields;
	private int[] topFields;
	private final int emptyValue = -1;
	
	private int undocount = 0;
	private int docount = 0;
	
	public Board(int x, int y){
		fields = new int[x][y];
		topFields = new int[x];
		initFields();
	}
	
	@Override
	protected Board clone() {
		Board board = new Board(fields.length,fields[0].length);
		for(int x = 0; x < fields.length; x++) {
			for(int y = 0; y < fields[0].length; y++) {
				//board.unsafePerformMove(this.fields[x][y],x,y);
				board.fields[x][y] = this.fields[x][y];
			}
		}
		for(int x = 0; x < topFields.length; x++) {
			board.topFields[x] = this.topFields[x];
		}
		return board;
	}
	
	private void setFieldValue(int x, int y, int value){
		fields[x][y] = value;
	}
	
	public void performMove(int playerID, int x) throws IllegalAccessException{
		if (isMoveValid(x)){
			setFieldValue(x, topFields[x], playerID);
			docount++;
			topFields[x]--;
		} else {
			throw new IllegalAccessException("Move is invalid.\ndocount:" +  docount + "\nundocount:" +  undocount);
		}
	}
	
	public void unsafePerformMove(int playerID, int x) {
		try {
			//setFieldValue(x,y, playerID);
			//topFields[x]--;
			
			performMove(playerID, x);
		} catch (IllegalAccessException ex) {
			Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public boolean isMoveValid(int x){
		return topFields[x] >= 0;		
	}
	
	public void undoMove(int x) {
		topFields[x]++;
		undocount++;
		fields[x][topFields[x]] = emptyValue;
	}

	public int[][] getFields() {
		return fields;
	}
	
	public int unsafeGetFieldValue(int x, int y) {
		return fields[x][y];
	}
	
	public boolean isFieldEmpty(int x, int y){
		return fields[x][y] == emptyValue;
	}
	
	public boolean isCoordinateValid(int x, int y){
		return x >= 0 && x < fields.length && y >= 0 && y < fields[0].length;
	}
	
	private void initFields(){
		for(int x = 0; x < fields.length; ++x){
			for (int y = 0; y < fields[0].length; ++y){
				fields[x][y] = emptyValue;
			}
			topFields[x] = fields[0].length - 1;
		}
	}
	
	public int getTopField(int x){
		return topFields[x];
	}
	
	public int[] getTopFields(){
		return topFields;
	}	
	
	public int getEmptyValue(){
		return emptyValue;
	}
	
	public int getFieldValue(int x, int y) throws IllegalAccessException{
		if (isCoordinateValid(x,y)){
			return fields[x][y];
		} else {
			throw new IllegalAccessException("Coordinate is invalid");
		}
	}
	
	public ArrayList<Coordinate> getEmptyFields(){
		ArrayList<Coordinate> emptyFields = new ArrayList<Coordinate>();
		
		for (int x = 0; x < fields.length; ++x){
			for (int y = 0; y < fields[0].length; ++y){
				if (fields[x][y] == -1){
					emptyFields.add(new Coordinate(x,y));
				}
			}
		}
		return emptyFields;
	}
}
