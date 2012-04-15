package aiingames.samplesim.spatial;


public class Object2D {
	
	public static int sum = 0;
	
	Coordinate position;
	Vector2D direction;
	int id;
	
	public Object2D (Coordinate _position,	Vector2D _direction) {
		this.position = _position;
		this.direction = _direction;
		this.id = ++Object2D.sum;
	}
	
	public Object2D (Coordinate _position) {
		this.position = _position;
		this.direction = new Vector2D(0.0, 0.0);
		this.id = ++Object2D.sum;
	}
	
	public Coordinate getPosition() {
		return this.position;
	}
	
	public Vector2D getDirection() {
		return this.direction;
	}
	
	public int getID(){
		return this.id;
	}

	public void setPosition(Coordinate _nc) {
		this.position.setX(_nc.getX());
		this.position.setY(_nc.getY());
	}

	public void setDirection(Vector2D _dir) {
		this.direction.setX(_dir.getX());
		this.direction.setY(_dir.getY());
	}

}
