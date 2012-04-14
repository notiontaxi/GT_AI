/*
APE (Actionscript Physics Engine) is an AS3 open source 2D physics engine
Copyright 2006, Alec Cove 

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA

Contact: ape@cove.org

Converted to Java by Theo Galanakis theo.galanakis@hotmail.com
Modified by Florian Wokurka
(src: http://www.hackchina.com/en/r/113220/Vector.java__html)
*/
package aiingames.samplesim.spatial;


public class Vector2D {
	
	
	private Coordinate c;


	public Vector2D(double _x, double _y) {
		c =  new Coordinate(_x, _y);
	}
	
	
	public double getX(){
		return c.getX();
	}
	
	public double getY(){
		return c.getY();
	}
	
	public void setTo(double _x, double _y) {
		c.setX(_x);
		c.setY(_y);
	}
	
	public void setX(double px) {
		c.setX(px);
	}
	public void setY(double py) {
		c.setY(py);
	}	
	
	public Coordinate getPosition(){
		return this.c;
	}
	
	public void copy(Vector2D v) {
		c.setX(v.getPosition().getX());
		c.setY(v.getPosition().getY());
	}


	public double dot(Vector2D v) {
		return c.getX() * v.getPosition().getX() + c.getY() * v.getPosition().getY();
//		return x * v.x + y * v.y;
	}
	
	
	public double cross(Vector2D v) {
		return c.getX() * v.getPosition().getY() - c.getY() * v.getPosition().getX();
//		return x * v.y - y * v.x;
	}
	

	public Vector2D plus(Vector2D v) {
		return new Vector2D(c.getX() + v.getPosition().getX(), c.getY() + v.getPosition().getY()); 
//		return new Vector2D(x + v.x, y + v.y); 
	}

	
	public Vector2D plusEquals(Vector2D v) {
		c.setPosition(c.getX() + v.getPosition().getX(), c.getY() + v.getPosition().getY());
		return this;
	}
	
	
	public Vector2D minus(Vector2D v) {
		return new Vector2D(c.getX() - v.getPosition().getX(), c.getY() - v.getPosition().getY());    
	}


	public Vector2D minusEquals(Vector2D v) {
		c.setPosition(c.getX() - v.getPosition().getX(), c.getY() - v.getPosition().getY());
		return this;
	}


	public Vector2D mult(double s) {
		return new Vector2D(c.getX() * s, c.getY() * s);
	}


	public Vector2D multEquals(double s) {
		c.setPosition(c.getX() * s, c.getY() * s);
		return this;
	}


//	public Vector2D times(Vector2D v) {
//		return new Vector2D(x * v.x, y * v.y);
//	}
	
	
	public Vector2D divEquals(double s) {
		if (s == 0) s = 0.0001;
		this.c.setX(this.c.getX() / s);
		this.c.setY(this.c.getY() / s);
		return this;
	}
	
	
	public double length() {
		return Math.sqrt(c.getX() * c.getX() + c.getY() * c.getY());
	}

	
	public double distance(Vector2D v) {
		Vector2D delta = this.minus(v);
		return delta.length();
	}

	public double distance(Coordinate _c) {
		return this.c.getDistanceTo(_c);
	}


	public Vector2D normalize() {
		 double m = length();
		 if (m == 0) m = 0.0001;
		 return mult(1 / m);
	}
	
			
	public String toString() {
		return (c.getX() + " : " + c.getY());
	}
	


}
