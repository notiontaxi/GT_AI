package gui;

import com.sun.j3d.utils.geometry.*;

import com.sun.j3d.utils.universe.*;

import javax.media.j3d.*;

import javax.vecmath.*;

import spatial.BoundingBox;

public class Network {

	public Network(network.Network network) {

		BoundingBox boundingBox = network.getBoundingBox();
		double width = boundingBox.getBottomRight().getX()
				- boundingBox.getTopLeft().getX();
		double height = boundingBox.getBottomRight().getY()
				- boundingBox.getTopLeft().getY();

		double minX = boundingBox.getTopLeft().getX();
		double minY = boundingBox.getTopLeft().getY();

		System.out.println(width + " -> " + height);

		SimpleUniverse universe = new SimpleUniverse();

		BranchGroup group = new BranchGroup();

		// X axis made of spheres

		for (network.Node node : network.getNodes().values()) {
			float x = new Float(((node.getX() - minX) * 2 / width) - 1);
			float y = new Float(((node.getY() - minY) * 2 / height) - 1);

			Sphere sphere = new Sphere(0.01f);
			TransformGroup tg = new TransformGroup();
			Transform3D transform = new Transform3D();
			Vector3f vector = new Vector3f(x, y, .0f);
			transform.setTranslation(vector);
			tg.setTransform(transform);
			tg.addChild(sphere);
			group.addChild(tg);
		}

		// for (float x = -1.0f; x <= 1.0f; x = x + 0.1f)
		// {
		// Sphere sphere = new Sphere(0.05f);
		// TransformGroup tg = new TransformGroup();
		// Transform3D transform = new Transform3D();
		// Vector3f vector = new Vector3f(x, .0f, .0f);
		// transform.setTranslation(vector);
		// tg.setTransform(transform);
		// tg.addChild(sphere);
		// group.addChild(tg);
		// }

		Color3f light1Color = new Color3f(.1f, 1.4f, .1f); // green light
		BoundingSphere bounds =	new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
		Vector3f light1Direction = new Vector3f(4.0f, -7.0f, -12.0f);
		DirectionalLight light1	= new DirectionalLight(light1Color, light1Direction);
		light1.setInfluencingBounds(bounds);

		group.addChild(light1);
		universe.getViewingPlatform().setNominalViewingTransform();
		// add the group of objects to the Universe
		universe.addBranchGraph(group);
	}
}