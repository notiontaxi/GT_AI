package spatial;


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
	
	public boolean equals(Coordinate _c){
		double offset = 0.5;
		double deltaX = this.x - _c.getX();
		double deltaY = this.y - _c.getY();
		
		if( (deltaX < offset && deltaX > -offset) &&
		    (deltaY < offset && deltaY > -offset))
			return true;
		else
			return false;
	}


	public double getDistanceTo(Coordinate _coord) {
			
		double xDist = _coord.x -  this.x;
		double yDist = _coord.y -  this.y;
		
		
		return Math.sqrt(xDist*xDist + yDist*yDist);
	}
	
	public Coordinate clone(){
		return new Coordinate(this.x, this.y);
	}
	public Coordinate sub(Coordinate _c){
		return new Coordinate(this.x - _c.getX(), this.y - _c.getY());
	}
	public Coordinate sub(double _x, double _y){
		return new Coordinate(this.x - _x, this.y - _y);
	}	
	
    /** True if the vector represents a pair of valid, non-infinite floating point numbers. */ 
    public boolean isValid() {
        return x != Float.NaN && x != Float.NEGATIVE_INFINITY
                && x != Float.POSITIVE_INFINITY && y != Float.NaN
                && y != Float.NEGATIVE_INFINITY && y != Float.POSITIVE_INFINITY;
    }
    
    
	@Override
	public String toString(){
		return "X: "+this.x+"   Y: "+this.y;
	}
    

}
