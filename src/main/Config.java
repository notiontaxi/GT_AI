package main;

public class Config {

	private int dimensionX;
	private int dimensionY;
	private int rowLengthToWin;
	
	public Config(){
		this.dimensionX = 4;
		this.dimensionY = 4;
		this.rowLengthToWin = 4;
	}
	
	public Config(String path){
		
	}
	
	public int getRowLengthToWin(){
		return rowLengthToWin;
	}

	public int getDimensionX() {
		return dimensionX;
	}

	public int getDimensionY() {
		return dimensionY;
	}
	
	
}
