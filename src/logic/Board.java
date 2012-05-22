/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

/**
 *
 * @author Alex
 */
public class Board {

	private Integer[][] fields;
	private final int emptyValue = -1;
	
	public Board(int x, int y){
		fields = new Integer[x][y];
		initFields();
	}
	
	public void performMove(int playerID, Coordinate coordinate) throws IllegalAccessException{
		if (fields[coordinate.getX()][coordinate.getY()] == emptyValue){
			fields[coordinate.getX()][coordinate.getY()] = playerID;
		} else {
			throw new IllegalAccessException("Field is not empty.");
		}
	}
	
	public void undoMove(int playerID, Coordinate coordinate) throws IllegalAccessException{
		if (fields[coordinate.getX()][coordinate.getY()] == playerID){
			fields[coordinate.getX()][coordinate.getY()] = emptyValue;
		} else {
			throw new IllegalAccessException("Field isn't marked by player[" + playerID + "].");
		}
	}

	public Integer[][] getFields() {
		return fields;
	}
	
	public int getFieldValue(Coordinate coordinate)throws IllegalAccessException{
		if (coordinate != null && isCoordinateValid(coordinate)){
			return fields[coordinate.getX()][coordinate.getY()];
		} else {
			throw new IllegalAccessException("Coordinate is invalid");
		}
	}
	
	public int getEmptyValue(){
		return emptyValue;
	}
	
	public boolean isFieldEmpty(Coordinate coordinate){
		return fields[coordinate.getX()][coordinate.getY()] == emptyValue;
	}
	
	public boolean isCoordinateValid(Coordinate coordinate){
		return coordinate.getX() > 0 && coordinate.getX() < fields.length && coordinate.getY() > 0 && coordinate.getY() < fields[0].length;
	}
	
	private void initFields(){
		for(int x = 0; x < fields.length; ++x){
			for (int y = 0; y < fields[0].length; ++y){
				fields[x][y] = emptyValue;
			}
		}
	}
}
