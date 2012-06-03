package main;

public class Config {

	private int dimensionX;
	private int dimensionY;
	private int rowLengthToWin;
	private int threadCount;

	private int depth;
	
	public Config(){
		this.dimensionX = 3;
		this.dimensionY = 3;
		this.rowLengthToWin = 3;
		this.threadCount = 1;
		this.depth = 20;
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
