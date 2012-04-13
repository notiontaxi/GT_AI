package aiingames.samplesim.spatial;

public class Coordinate {

	
	private double x;
	private double y;

	public Coordinate(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return this.x;
	}
	public double getY() {
		return this.y;
	}
	public void setX(double _x) {
		this.x = _x;
	}
	public void setY(double _y) {
		this.y = _y;
	}	
	
	public void setPosition(double _x, double _y){
		this.x = _x;
		this.y = _y;
	}
	
	@Override
	public String toString(){
		return "X: "+this.x+"   Y: "+this.y;
	}

	public double getDistanceTo(Coordinate _coord) {
			
		double xDist = _coord.x -  this.x;
		double yDist = _coord.y -  this.y;
		
		
		return Math.sqrt(xDist*xDist + yDist*yDist);
	}


}
