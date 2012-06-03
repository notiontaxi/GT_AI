package main;

public class Config {

	private int dimensionX;
	private int dimensionY;
	private int rowLengthToWin;
	private int threadCount;

	private int depth;
	
	public Config(){
		this.dimensionX = 7;
		this.dimensionY = 6;
		this.rowLengthToWin = 4;
		this.threadCount = 7;
		this.depth = 6;
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
	
	public int getThreadCount(){
		return threadCount;
	}

	public int getDepth() {
		return depth;
	}
}
