package physics;
import spatial.Coordinate;
import spatial.Object2D;
import spatial.Vector2D;






public class PhysicObject2D extends Object2D{

	
	private float speed;
	private BoundingBoxAABB boundingBox;
	
	
	public PhysicObject2D(Coordinate _position, Vector2D _direction) {
		super(_position, _direction);
		init();
		this.boundingBox = new BoundingBoxAABB(this.getPosition(), 0.2);
	}

	private void init(){
		this.speed = 0;
	}
	
	public boolean collidesWith(PhysicObject2D _obj){
		return this.boundingBox.testOverlap(_obj.boundingBox);
	}

	public BoundingBoxAABB getBoundingBox(){
		return this.boundingBox;
	}

}
