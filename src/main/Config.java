package main;

public class Config {

	public static final int dimensionX = 7;
	public static final int dimensionY = 6;

	public static final int PLAYER1 = 0;
	public static final int PLAYER2 = 1;	
	
	private int rowLengthToWin;
	private int threadCount;

	private int depth;
	
	public Config(){
		this.rowLengthToWin = 4;
		this.threadCount = 7;
		this.depth = 8;
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
