/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import java.util.ArrayList;

/**
 *
 * @author Alex
 */
public class Board {

	private int[][] fields;
	private int[] topFields;
	private final int emptyValue = -1;
	
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
				board.unsafePerformMove(this.fields[x][y],x,y);
			}
		}
		return board;
	}
	
	private void setFieldValue(int x, int y, int value){
		fields[x][y] = value;
	}
	
	public void performMove(int playerID, int x, int y) throws IllegalAccessException{
		if (isMoveValid(x,y)){
			setFieldValue(x,y, playerID);
			topFields[x]--;
		} else {
			throw new IllegalAccessException("Move is invalid.");
		}
	}
	
	public void unsafePerformMove(int playerID, int x, int y) {
		setFieldValue(x,y, playerID);
		topFields[x]--;
	}
	
	public boolean isMoveValid(int x, int y){
		return topFields[x] == y;		
	}
	
	public void undoMove(int x,  int y) {
		fields[x][y] = emptyValue;
		topFields[x]++;
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
