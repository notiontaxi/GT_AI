/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Alex
 */
public class Board {

	private Integer[][] fields;
	private final int emptyValue = -1;
	private int count0 = 0;
	private int count1 = 0;
	private int count2 = 0;
	
	public Board(int x, int y){
		fields = new Integer[x][y];
		initFields();
	}
	
	private Board() {
		
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		Board board = new Board();
		board.fields = this.fields.clone();
		return board;
	}
	
	public void performMove(int playerID, int x, int y) throws IllegalAccessException{
		count0 += 1;
		if (count0 % 100000 == 0){
			count0 = 0;
			count1++;
			if (count1 % 1000 == 0){
				count1 = 0;
				count2++;
				System.err.println(count2);
			}
		}
		if (fields[x][y] == emptyValue){
			fields[x][y] = playerID;
			//emptyFields.removeElement(coordinate.getX() * fields[0].length + coordinate.getY());
		} else {
			throw new IllegalAccessException("Field is not empty.");
		}
	}
	
	public void undoMove(int x,  int y) {
		fields[x][y] = emptyValue;
	}

	public Integer[][] getFields() {
		return fields;
	}
	
	public int getFieldValue(int x, int y) throws IllegalAccessException{
		if (isCoordinateValid(x,y)){
			return fields[x][y];
		} else {
			throw new IllegalAccessException("Coordinate is invalid");
		}
	}
	
	public int unsafeGetFieldValue(int x, int y) {
		return fields[x][y];
	}
	
	public int getEmptyValue(){
		return emptyValue;
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
				//emptyFields.add(x * fields[0].length + y);
			}
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
