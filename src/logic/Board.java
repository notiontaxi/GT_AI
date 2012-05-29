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

	private int[][] fields;
	//private ArrayList<Integer> emptyList;
	private final int emptyValue = -1;
	private int count0 = 0;
	private int count1 = 0;
	private int count2 = 0;
	
	public Board(int x, int y){
		fields = new int[x][y];
		//emptyList = new ArrayList<Integer>();
		initFields();
	}
	
	@Override
	protected Board clone() {
		Board board = new Board(fields.length,fields[0].length);
		//board.emptyList = this.emptyList.subList(0, emptyList.size());
		//board.emptyList.clone();
		for(int x = 0; x < fields.length; x++) {
			for(int y = 0; y < fields[0].length; y++) {
				board.setFieldValue(x,y, this.fields[x][y]);
			}
		}
		return board;
	}
	
	private void setFieldValue(int x, int y, int value){
		fields[x][y] = value;
	}
	
	public void performMove(int playerID, int x, int y) throws IllegalAccessException{
		/*count0 += 1;
		if (count0 % 100000 == 0){
			count0 = 0;
			count1++;
			if (count1 % 1000 == 0){
				count1 = 0;
				count2++;
				System.err.println(count2);
			}
		}*/
		if (fields[x][y] == emptyValue){
			fields[x][y] = playerID;
			//emptyList.remove((Integer) (x * fields[0].length + y));
		} else {
			throw new IllegalAccessException("Field is not empty.");
		}
	}
	
	public void undoMove(int x,  int y) {
		//emptyList.add(x * fields[0].length + y);
		fields[x][y] = emptyValue;
	}

	public int[][] getFields() {
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
				//emptyList.add(x * fields[0].length + y);
			}
		}
	}
	
	/*public List<Integer> getEmptyFieldsCoded(){
		return this.emptyList;
	}*/
	
	public ArrayList<Coordinate> getEmptyFields(){
		ArrayList<Coordinate> emptyFields = new ArrayList<Coordinate>();
		/*for (Integer val : emptyList){
			emptyFields.add(new Coordinate((int) val / fields[0].length, val % fields.length));
		}*/
		
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
