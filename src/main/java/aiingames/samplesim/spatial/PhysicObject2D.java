package aiingames.samplesim.spatial;

public class PhysicObject2D extends Object2D{

	
	float speed;
	BoundingBoxAABB boundingBox;
	
	
	public PhysicObject2D(Coordinate _position, Vector2D _direction) {
		super(_position, _direction);
		init();
		this.boundingBox = new BoundingBoxAABB(	new Coordinate(_position.getX()-1, _position.getY()-1),
												new Coordinate(_position.getX()+1, _position.getY()+1));

	}

	private void init(){
		this.speed = 0;
	}
	
	

	

}
