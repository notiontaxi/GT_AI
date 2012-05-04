package gui;

import astar.AstarNode;
import java.awt.Dimension;

import javax.swing.JFrame;

import org.apache.commons.collections15.Transformer;

import network.Link;
import network.Node;
import network.NetworkGraph;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.Stack;
import spatial.BoundingBox;

public class JUNG {

	private Layout<Node, Link> layout;
	private double width = 0;
	private double height = 0;
	private double minX = 0;
	private double minY = 0;
	private int frameSizeX = 900;
	private int frameSizeY = 900;
	private Stack<AstarNode> astarPath;
	private int startID;
	private int endID;

	public void setAstarPath(Stack<AstarNode> astarPath, int startID, int endID) {
		this.astarPath = astarPath;
		this.startID = startID;
		this.endID = endID;
	}

	public void createLayout(NetworkGraph networkGraph) {

		BoundingBox boundingBox = networkGraph.getBoundingBox();
		width = boundingBox.getBottomRight().getX()
				- boundingBox.getTopLeft().getX();
		height = boundingBox.getBottomRight().getY()
				- boundingBox.getTopLeft().getY();

		minX = boundingBox.getTopLeft().getX();
		minY = boundingBox.getTopLeft().getY();

		layout = new StaticLayout<Node, Link>(networkGraph.getGraph());

		for (Node node : networkGraph.getNodes()) {
			layout.setLocation(node, scaleToFrame(node.getX(), node.getY()));
		}
		// The Layout<V, E> is parameterized by the vertex and edge types
		layout.setSize(new Dimension(frameSizeX, frameSizeY)); // sets the initial size of the space		
	}

	private Point2D.Double scaleToFrame(double x, double y) {
		return new Point2D.Double((x - minX) / width * frameSizeX + 25, ((y - minY) / height * frameSizeY - frameSizeY) * (-1) + 25);
	}

	private boolean astarPathContains(int id) {
		for (Iterator<AstarNode> it = astarPath.iterator(); it.hasNext();) {
			AstarNode astarNode = it.next();
			if (astarNode.getId() == id) {
				return true;
			}
		}
		return false;
	}

	public void draw() {
		// The BasicVisualizationServer<V,E> is parameterized by the edge types
		BasicVisualizationServer<Node, Link> vv =
				new BasicVisualizationServer<Node, Link>(layout);
		vv.setPreferredSize(new Dimension(frameSizeX + 50, frameSizeY + 50)); //Sets the viewing area size

		Transformer<Node, Paint> vertexPaint = new Transformer<Node, Paint>() {

			public Paint transform(Node node) {
				if (node.getId() == startID) {
					return Color.WHITE;
				} else if (node.getId() == endID) {
					return Color.BLACK;
				} else if (astarPath != null && astarPathContains(node.getId())) {
					return Color.GREEN;
				} else {
					return Color.RED;
				}
			}
		};


		Transformer<Node, Shape> vertexShape = new Transformer<Node, Shape>() {
			private double size = 10;
			
			public Shape transform(Node node) {
				return new Ellipse2D.Double(size*(-0.5),size*(-0.5),size,size);
			}
		};


		// Set up a new stroke Transformer for the edges
		float dash[] = {10.0f};
		final Stroke edgeStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
		Transformer<Link, Stroke> edgeStrokeTransformer =
				new Transformer<Link, Stroke>() {

					public Stroke transform(Link link) {
						return edgeStroke;
					}
				};
		vv.getRenderContext().setVertexShapeTransformer(vertexShape);
		vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
		//vv.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);

		JFrame frame = new JFrame("Simple Graph View");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(vv);
		frame.pack();
		frame.setVisible(true);
	}
}
