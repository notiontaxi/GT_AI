package physics;
import spatial.Coordinate;






public class BoundingBoxAABB {

	    private Coordinate pivot;
	    private double size;
	    
	    public String toString() {
	    	String s = "Pivot: "+this.pivot+" size: "+this.size;
	    	return s;
	    }

	    public BoundingBoxAABB(Coordinate _pivot, double _size) {
	    	this.pivot = _pivot;
	    	this.size = _size;
	    }



	    public boolean testOverlap(BoundingBoxAABB box) {
	    	if(box != this ){
	    		
		        Coordinate d1 = box.getLowerBound().sub(this.getUpperBound());
		        Coordinate d2 = this.getLowerBound().sub(box.getUpperBound());
	  
		        if (d1.getX() > 0.0f || d1.getY() > 0.0f || d2.getX() > 0.0f || d2.getY() > 0.0f) {
		            return false;
		        }
		        else {
		            return true;
		        }
	    	}
	    	return false;
	    }
	    
	    
	    
	    public Coordinate getLowerBound(){
	    	return this.pivot.sub(this.size, this.size);
	    }
	    public Coordinate getUpperBound(){
	    	return this.pivot.sub(-this.size, -this.size);
	    }	    
	    
}
	
	
	
	

