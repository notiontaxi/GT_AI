package spatial;

public class BoundingBox {

	private Coordinate topLeft ;
	private Coordinate bottomRight ;
	
	public BoundingBox(Coordinate topLeft, Coordinate bottomRight) {
		this.topLeft = topLeft;
		this.bottomRight = bottomRight;
	}
	
	public Coordinate getBottomRight() {
		return bottomRight;
	}
	public void setBottomRight(Coordinate bottomRight) {
		this.bottomRight = bottomRight;
	}
	public Coordinate getTopLeft() {
		return topLeft;
	}
	public void setTopLeft(Coordinate topLeft) {
		this.topLeft = topLeft;
	}
}
