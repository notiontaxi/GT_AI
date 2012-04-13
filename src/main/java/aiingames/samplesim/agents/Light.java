package aiingames.samplesim.agents;

import aiingames.samplesim.spatial.Coordinate;

public class Light {
	
	public static int sum = 0;
	
	Coordinate coord;
	int id;
	
	public Light(Coordinate c){
		this.coord = c;
		this.id = ++Light.sum;
	}
	
	public Coordinate getPosition(){
		return this.coord;
	}

}
