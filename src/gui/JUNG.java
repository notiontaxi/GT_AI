package gui;

import agents.AgentPathwalker;
import astar.Astar;
import astar.AstarNode;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import org.apache.commons.collections15.Transformer;

import main.astarSimulation;
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
import edu.uci.ics.jung.visualization.annotations.AnnotationControls;
import edu.uci.ics.jung.visualization.control.*;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.PickableVertexPaintTransformer;
import edu.uci.ics.jung.visualization.layout.LayoutTransition;
import edu.uci.ics.jung.visualization.util.Animator;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import javax.swing.*;
import spatial.BoundingBox;

public class JUNG {

	private static boolean paintPath = false;
	private Layout<Node, Link> layout;
	private double width = 0;
	private double height = 0;
	private double minX = 0;
	private double minY = 0;
	private int frameSizeX = 900;
	private int frameSizeY = 700;
	private Stack<AstarNode> astarPath;
	private List<Link> dijkstraPath;
	private int startID;
	private int endID;

	private Node agent;
	private Astar astar;
	private JPanel jp;
	private VisualizationViewer<Node, Link> vv;
	private NetworkGraph networkGraph;
	
	
	// call before createLayout!!
	public void addAgent(Node agent){
		this.agent = agent;
	}
	public void updateAgent(double _x, double _y){
		if(this.agent == null)
			System.err.println("no agent was added until now");
		else{
			int radius = 5;
			this.agent.setX(_x);
			this.agent.setY(_y);
			Point2D p = scaleToFrame(_x, _y);
			
			layout.setLocation(this.agent, p);
			
			if(JUNG.paintPath)			
				jp.getComponents()[0].getGraphics().fillOval((int)p.getX()-radius, (int)p.getY()-radius, 2*radius, 2*radius);
			else
				jp.getComponents()[0].repaint();		
		}
	}

	public VisualizationViewer getVV(){
		return vv;
	}
	
	public NetworkGraph getNetworkGraph(){
		return networkGraph;
	}
	
	public void setDijkstra(List<Link> dijkstraPath){
		this.dijkstraPath = dijkstraPath;
	}

	public void addAstar(Astar astar){
		this.astar = astar;
	}
	
	public void setAstarPath(Stack<AstarNode> astarPath, int startID, int endID) {
		this.astarPath = astarPath;
		this.startID = startID;
		this.endID = endID;
	}

	public void createLayout(NetworkGraph networkGraph) {
		this.networkGraph = networkGraph;
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
		
		// -- ---------------
		if(this.agent == null)
			System.err.println("no agent was added until now");
		else
			networkGraph.getGraph().addVertex(this.agent);
		// -- ---------------		
		
		// The Layout<V, E> is parameterized by the vertex and edge types
		layout.setSize(new Dimension(frameSizeX, frameSizeY)); // sets the initial size of the space		
	}

	private Point2D.Double scaleToFrame(double x, double y) {
		return new Point2D.Double((x - minX) / width * frameSizeX + 25, ((y - minY) / height * frameSizeY - frameSizeY) * (-1) + 25);
	}
	
	private boolean dijkstraPathContains(int id){
		for (Iterator<Link> it = dijkstraPath.iterator(); it.hasNext();) {
			Link link = it.next();
			if (link.getFrom().getId() == id || link.getTo().getId() ==id) return true;
		}
		return false;
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

	private JPanel getGraphPanel(Layout<Node, Link> _layout) {
		// The BasicVisualizationServer<V,E> is parameterized by the edge types
		/*
		 * BasicVisualizationServer<Node, Link> vv = new
		 * BasicVisualizationServer<Node, Link>(layout);
		 */
		
		float scale = 1;

		vv = new VisualizationViewer<Node, Link>(_layout);
		

		vv.setPreferredSize(new Dimension(frameSizeX + 50, frameSizeY + 50)); //Sets the viewing area size

		Transformer<Node, Paint> vertexPaint = new Transformer<Node, Paint>() {

			public Paint transform(Node node) {
				if (node.getId() == startID) {
					return Color.WHITE;
				} else if (node.getId() == endID) {
					return Color.BLACK;
				} else if(node.getId() == 4711){
					return Color.BLUE;
				} else if (astarPath != null &&  dijkstraPath != null && astarPathContains(node.getId()) && dijkstraPathContains(node.getId())) {
					return Color.MAGENTA;
				} else if (astarPath != null && astarPathContains(node.getId())) {
					return Color.GREEN;
				} else if (astarPath != null && dijkstraPathContains(node.getId())) {
					return Color.PINK;
				} else {
					return Color.RED;
				}
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

		//vv.getRenderContext().setVertexFillPaintTransformer(new PickableVertexPaintTransformer<Node>(vv.getPickedVertexState(), Color.red, Color.yellow));
		vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
		vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<Node, Link>());
		
		vv.getRenderContext().getVertexFillPaintTransformer().transform(agent);
		
//		final DefaultModalGraphMouse<Node, Link> graphMouse = new DefaultModalGraphMouse<Node, Link>();
//		vv.setGraphMouse(graphMouse);
//		vv.addKeyListener(graphMouse.getModeKeyListener());
		
		PluggableGraphMouse gm = new PluggableGraphMouse(); 				
		gm.add(new OwnPickingGraphMousePlugin(MouseEvent.BUTTON1_MASK, MouseEvent.BUTTON1_DOWN_MASK, this.astar, this));
		gm.add(new TranslatingGraphMousePlugin(MouseEvent.BUTTON3_MASK));
		gm.add(new ScalingGraphMousePlugin(new CrossoverScalingControl(), 0, 1.1f, 0.9f));
		vv.setGraphMouse(gm);
		
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

		JButton goToStart = new JButton("Go to start");
		goToStart.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				AgentPathwalker.reset = true;
				JUNG.paintPath = false;
			}
		});		
		
		JButton togglePause = new JButton("Toggle pause");
		togglePause.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				astarSimulation.pause = !astarSimulation.pause;
			}
		});			
		
		JButton togglePath = new JButton("Toggle path paint");
		togglePath.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JUNG.paintPath = !JUNG.paintPath;
			}
		});		
		
		
		
		//JComboBox modeBox = graphMouse.getModeComboBox();
		//modeBox.addItemListener(((DefaultModalGraphMouse<Node, Link>) vv.getGraphMouse()).getModeListener());

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
		//bottomControls.add(modeBox);
		bottomControls.add(goToStart);
		bottomControls.add(togglePause);
		bottomControls.add(togglePath);
		
		
		return jp;
	}
	
	public void repaint(){
		jp.repaint();
	}
	
	public void draw() {
		jp = getGraphPanel(this.layout);
		jp.setOpaque(false);	
		
		JFrame jf = new JFrame();		
		
//		jf.getContentPane().add(area);
		jf.getContentPane().add(jp);
		
		

		
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.pack();
		jf.setVisible(true);
		
		
		Graphics2D g = (Graphics2D)jp.getComponents()[0].getGraphics();
		System.out.println(g);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);		
		
		
	}
}
