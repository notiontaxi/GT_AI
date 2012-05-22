/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import java.util.List;

/**
 *
 * @author Alex
 */
public class Field {

	private Integer[][] fields;
	private final int emptyValue = -1;
	
	public Field(int x, int y){
		fields = new Integer[x][y];
		initFields();
	}
	
	public void setPlayerMove(int playerID, Coordinate coordinate) throws IllegalAccessException{
		if (fields[coordinate.getX()][coordinate.getY()] == emptyValue){
			fields[coordinate.getX()][coordinate.getY()] = playerID;
		} else {
			throw new IllegalAccessException("Field is not empty.");
		}
	}

	public Integer[][] getFields() {
		return fields;
	}
	
	public int getEmptyValue(){
		return emptyValue;
	}
	
	private void initFields(){
		for(int x = 0; x < fields.length; ++x){
			for (int y = 0; y < fields[0].length; ++y){
				fields[x][y] = emptyValue;
			}
		}
	}
}
