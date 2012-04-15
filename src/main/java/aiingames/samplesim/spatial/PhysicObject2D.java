package aiingames.samplesim.spatial;

public class PhysicObject2D extends Object2D{

	
	float speed;
	
	public PhysicObject2D(Coordinate _position, Vector2D _direction) {
		super(_position, _direction);
		init();
		// TODO Auto-generated constructor stub
	}

	private void init(){
		this.speed = 0;
	}
	
//	changeDirection(){
//		
//		
//	}
	

}
