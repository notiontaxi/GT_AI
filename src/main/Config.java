package main;

public class Config {

	private int dimensionX;
	private int dimensionY;
	
	public Config(){
		this.dimensionX = 4;
		this.dimensionY = 4;
	}
	
	public Config(String path){
		
	}

	public int getDimensionX() {
		return dimensionX;
	}

	public int getDimensionY() {
		return dimensionY;
	}
	
	
}
