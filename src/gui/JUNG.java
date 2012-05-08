package gui;

import astar.AstarNode;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import org.apache.commons.collections15.Transformer;

import network.Link;
import network.Node;
import network.NetworkGraph;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.algorithms.layout.util.Relaxer;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.samples.ShowLayouts.GraphChooser;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.PickableVertexPaintTransformer;
import edu.uci.ics.jung.visualization.layout.LayoutTransition;
import edu.uci.ics.jung.visualization.util.Animator;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import javax.swing.*;
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
	private List<Link> dijkstraPath;
	private int startID;
	private int endID;

	private static final class LayoutChooser implements ActionListener {

		private final JComboBox jcb;
		private final VisualizationViewer<Node, Link> vv;
		private static Graph<? extends Object, ? extends Object>[] g_array;

		private LayoutChooser(JComboBox jcb, VisualizationViewer<Node, Link> vv) {
			super();
			this.jcb = jcb;
			this.vv = vv;
		}

		public void actionPerformed(ActionEvent arg0) {
			Object[] constructorArgs = {g_array[0]};

			Class<? extends Layout<Node, Link>> layoutC =
					(Class<? extends Layout<Node, Link>>) jcb.getSelectedItem();
//            Class lay = layoutC;
			try {
				Constructor<? extends Layout<Node, Link>> constructor = layoutC.getConstructor(new Class[]{Graph.class});
				Object o = constructor.newInstance(constructorArgs);
				Layout<Node, Link> l = (Layout<Node, Link>) o;
				l.setInitializer(vv.getGraphLayout());
				l.setSize(vv.getSize());

				LayoutTransition<Node, Link> lt =
						new LayoutTransition<Node, Link>(vv, vv.getGraphLayout(), l);
				Animator animator = new Animator(lt);
				animator.start();
				vv.getRenderContext().getMultiLayerTransformer().setToIdentity();
				vv.repaint();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setDijkstra(List<Link> dijkstraPath){
		this.dijkstraPath = dijkstraPath;
	}

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

	private JPanel getGraphPanel() {
		// The BasicVisualizationServer<V,E> is parameterized by the edge types
		/*
		 * BasicVisualizationServer<Node, Link> vv = new
		 * BasicVisualizationServer<Node, Link>(layout);
		 */
		
		float scale = 1;

		final VisualizationViewer<Node, Link> vv =
				new VisualizationViewer<Node, Link>(layout);
		

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

		Transformer<Link, Paint> vertexPaintEdge = new Transformer<Link, Paint>() {

			public Paint transform(Link link) {
				if (dijkstraPath.contains(link)){
					return Color.ORANGE;
				}
				return Color.BLACK;
			}
		};
		

		Transformer<Node, Shape> vertexShape = new Transformer<Node, Shape>() {

			private double size = 10;

			public Shape transform(Node node) {
				return new Ellipse2D.Double(size * (-0.5), size * (-0.5), size, size);
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
		
		//vv.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);

		vv.getRenderContext().setVertexFillPaintTransformer(new PickableVertexPaintTransformer<Node>(vv.getPickedVertexState(), Color.red, Color.yellow));
		vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
		vv.getRenderContext().setEdgeDrawPaintTransformer(vertexPaintEdge);
		
		
		final DefaultModalGraphMouse<Integer, Number> graphMouse = new DefaultModalGraphMouse<Integer, Number>();
		vv.setGraphMouse(graphMouse);

		final ScalingControl scaler = new CrossoverScalingControl();

		JButton plus = new JButton("+");
		plus.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				scaler.scale(vv, 1.1f, vv.getCenter());
			}
		});
		JButton minus = new JButton("-");
		minus.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				scaler.scale(vv, 1 / 1.1f, vv.getCenter());
			}
		});
		JButton reset = new JButton("reset");
		reset.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Layout<Node, Link> layout = vv.getGraphLayout();
				layout.initialize();
				Relaxer relaxer = vv.getModel().getRelaxer();
				if (relaxer != null) {
//				if(layout instanceof IterativeContext) {
					relaxer.stop();
					relaxer.prerelax();
					relaxer.relax();
				}
			}
		});

		JComboBox modeBox = graphMouse.getModeComboBox();
		modeBox.addItemListener(((DefaultModalGraphMouse<Node, Link>) vv.getGraphMouse()).getModeListener());

		JPanel jp = new JPanel();
		jp.setBackground(Color.WHITE);
		jp.setLayout(new BorderLayout());
		jp.add(vv, BorderLayout.CENTER);

		JPanel control_panel = new JPanel(new GridLayout(2, 1));
		JPanel topControls = new JPanel();
		JPanel bottomControls = new JPanel();
		control_panel.add(topControls);
		control_panel.add(bottomControls);
		jp.add(control_panel, BorderLayout.NORTH);


		bottomControls.add(plus);
		bottomControls.add(minus);
		bottomControls.add(modeBox);
		bottomControls.add(reset);
		return jp;
	}
	
	public void draw() {
		JPanel jp = getGraphPanel();

		JFrame jf = new JFrame();
		jf.getContentPane().add(jp);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.pack();
		jf.setVisible(true);
	}
}
