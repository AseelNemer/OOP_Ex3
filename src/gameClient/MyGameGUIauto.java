package gameClient;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.json.JSONException;
import org.json.JSONObject;

import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.EdgeData;
import dataStructure.Fruit;
import dataStructure.Robot;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node;
import dataStructure.node_data;
import oop_dataStructure.OOP_DGraph;
import oop_dataStructure.oop_edge_data;
import oop_dataStructure.oop_graph;
import utils.Point3D;

public class MyGameGUIauto  extends JFrame implements MouseListener,ActionListener{
public static ArrayList<Fruit> Fruit=new ArrayList<Fruit>(); //seder
public static ArrayList<Robot> Robots=new ArrayList<Robot>();
public static game_service game;
public static DGraph graph;
public static Graph_Algo gg ;
double maxX=0,maxY=0,minX=0,minY=0;
public static final double Epsilon=0.0001;
private Logger_KML kml;
private int scenario;

	
	public MyGameGUIauto()  {
		this.setTitle("The Maza Of Waze");
		this.setSize(900, 900);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		String level= JOptionPane.showInputDialog(this,"Please insert Level between [0,23]");
		scenario =Integer.parseInt(level);
		//int scenario=23;
		if (scenario<=23&&scenario>=0)
			 game = Game_Server.getServer(scenario); // you have [0,23] games
		else 
			game=Game_Server.getServer(0);
		kml=new Logger_KML(scenario);
		String g = game.getGraph();
		gg = new Graph_Algo();
		graph=new DGraph();
		graph.init(g);
		gg.init(g);
		min_max();
		JSONObject line;
		String info = game.toString();
		

		try {
			line = new JSONObject(info);
			JSONObject ttt = line.getJSONObject("GameServer");
			int rs = ttt.getInt("robots");
			System.out.println(info);
			System.out.println(g);						
		     	
			// the list of fruits should be considered in your solution
			Iterator<String> f_iter = game.getFruits().iterator();
			while(f_iter.hasNext()) {
				Fruit f=new Fruit();
				String s=f_iter.next();
					f.init(s);
						Fruit.add(f);
							//System.out.println(s);
						}
			

			
			int src_node = 0;  // arbitrary node, you should start at one of the fruits
			int c= 0;
			Robot b ;
			int af=0;
			for(int a = 0;a<rs ;a++) {
				if(Fruit.get(c)!=null  && c<Fruit.size()){	
					EdgeData edge=GetFE(graph,Fruit.get(c));
							src_node= edge.getSrc();
							b=new Robot(src_node,a,0,Fruit.get(c));
							Robots.add(b);
								game.addRobot(src_node);	
								
						System.out.println(src_node+":"+a);
								//System.out.println(b.getid()+" : "+a +" : "+ src_node);
				}else {
					af=(int)Math.random()*(graph.getV().size()-1);
					b=new Robot(af,a,0,null);
				//	System.out.println(b.getid()+" : "+a);
					Robots.add(b);
				game.addRobot(af);
					}
				c++;
				}
			}
		
		catch (JSONException e) {e.printStackTrace();}
		
		this.addMouseListener(this);
		this.setVisible(true);
	}
		public void paint(Graphics g ) {
				super.paint(g);
		        Graphics2D g2d = (Graphics2D) g;
		        g2d.setBackground(new Color(240, 240, 240));
		      
		        
				Collection<node_data> node=graph.getV();
				Iterator<node_data> nodes=node.iterator();

				node=graph.getV();
				nodes=node.iterator();
					while(nodes.hasNext()) 
					{
						node_data n=nodes.next();
						g2d.setColor(Color.BLUE);
						double x_=scale(n.getLocation().x(),minX,maxX,50,850);
						double y_=scale(n.getLocation().y(),minY,maxY,200,700); 
						Point3D p_=new Point3D(x_, y_);
						g2d.fillOval(p_.ix(), p_.iy(), 10, 10);
						g2d.setFont(new Font("deafult", Font.BOLD,14));	
						g2d.setColor(Color.BLUE);
						String key=n.getKey()+"";
						
						g2d.drawString(key, p_.ix(), p_.iy());
						Collection<edge_data> edg=graph.getE(n.getKey());	


						Iterator<edge_data> itr=edg.iterator();
						while(itr.hasNext()) {

							edge_data s=itr.next();
							double x=scale(n.getLocation().x(),minX,maxX,50,850);
							double y=scale(n.getLocation().y(),minY,maxY,200,700); 
							Point3D p=new Point3D(x, y);
							double x1=scale(graph.getNode(s.getDest()).getLocation().x(),minX,maxX,50,850);
							double y1=scale(graph.getNode(s.getDest()).getLocation().y(),minY,maxY,200,700); 
							Point3D p2=new Point3D(x1, y1);
							g2d.setColor(Color.RED);
							g2d.setFont(new Font("deafult", Font.BOLD,14));
							String weight = s.getWeight() + "";
							
							g2d.drawLine(p.ix(), p.iy(), p2.ix(), p2.iy());
													
							g2d.setColor(Color.YELLOW);
							int x2=(int) ((0.8*p2.ix())+ (0.2*p.ix()));
							int y2 =(int)((0.8*p2.iy())+ (0.2*p.iy()));
							g2d.fillOval(x2-5,y2-5,10,10);
							
							
						}
						
						}
					
					
					paintRobot(getGraphics());
					paintFruit(getGraphics());
					
					
			      
		}
		public void paintRobot(Graphics g)
		{
			ArrayList<Robot> robots=this.Robots;
			for (int i=0 ;i<robots.size();i++)
			{
				Robot r=robots.get(i);
				int x=(int)(scale(r.getpos().x(),minX,maxX,50,850));
				int y=(int)(scale(r.getpos().y(),minY,maxY,200,700));
				g.setColor(Color.BLACK);
				g.drawOval(x-15,y-15, 30, 30);
				g.setFont(new Font("Arial", Font.BOLD, 15));
				kml.placemark(r.getpos(), r.getid());
			}
		}
		public void paintFruit(Graphics g2d)
		{
			for (int i=0 ;i<this.Fruit.size();i++)
			{
				Fruit fr=this.Fruit.get(i);
				if (fr.getType()==1)
				{
					g2d.setColor(Color.CYAN);
					int x=(int)(scale(fr.getPOS().x(),minX,maxX,50,850));
					int y=(int)(scale(fr.getPOS().y(),minY,maxY,200,700));
					g2d.fillOval(x-7,y-7, 20, 20);
				}
				if(fr.getType()==-1)
				{
					g2d.setColor(Color.GREEN);
					int x=(int)(scale(fr.getPOS().x(),minX,maxX,50,850));
					int y=(int)(scale(fr.getPOS().y(),minY,maxY,200,700));
					g2d.fillOval(x-7,y-7, 20, 20);
				}
			}
		}
		private void min_max()
		{
			node_data n;
			Iterator<node_data> nodes=graph.getV().iterator();
			if (nodes.hasNext())
			{
				n=nodes.next();
				maxX=n.getLocation().x();
				maxY=n.getLocation().y();
				minX=n.getLocation().x();
				minY=n.getLocation().y();
			}
			while (nodes.hasNext())
			{
				n=nodes.next();
				Point3D p=n.getLocation();
				if (p.x()<minX)
					minX=p.x();
				if(p.x()>maxX)
					maxX=p.x();
				if (p.y()<minY)
					minY=p.y();
				if(p.y()>maxY)
					maxY=p.y();
			}
		}
		
		public void run()
		{
			game.startGame();
			int index=0;
			long time=50;
			while(game.isRunning()) {
				
				moveRobots(game, gg,graph);
				
				try {
					if(index%2==0) {
						repaint();
						//paintFruit(getGraphics());
						//paintRobot(this.getGraphics());
					}
						Thread.sleep(time);
						index++;				
			} 
				catch (InterruptedException e) {e.printStackTrace();}	
			}
			kml.endKml(scenario);
			String results = game.toString();
			System.out.println("Game Over: "+results);
		}
		
		
	
	private double scale(double data, double r_min, double r_max, 
			double t_min, double t_max)
	{
		
		double res = ((data - r_min) / (r_max-r_min)) * (t_max - t_min) + t_min;
		return res;
	}
	/** 
	 * Moves each of the robots along the edge, 
	 * in case the robot is on a node the next destination (next edge) is chosen (randomly).
	 * @param game
	 * @param gg
	 * @param log
	 */
	
	private static void moveRobots(game_service game,  Graph_Algo gg ,DGraph graph) {
		List<String> log = game.move();
		if(log!=null) {
			long t = game.timeToEnd();
			for(int i=0;i<log.size();i++) {
				String robot_json = log.get(i);
				Robot demo=new Robot(robot_json);
				for (Robot r : Robots) {
					if(r.getid()==demo.getid()) {
			Robots.get(r.getid()).init(robot_json);
					}
			
		
				}
				
			}
			
			
			
			Iterator<String> f_iter = game.getFruits().iterator();
			
			Fruit=new ArrayList<Fruit>();
			while(f_iter.hasNext()) {
				Fruit Ban=new Fruit();
				String s=f_iter.next();
					Ban.init(s);
						Fruit.add(Ban);
							
						}
			int dest=0;
			for(Robot r:Robots) {
				if(r.getdest()==-1) {
					dest=nextNode(graph,gg,r);
					
					//System.out.println(dest);
					game.chooseNextEdge(r.getid(), dest);
					
					System.out.println("Turn to node: "+dest+"  time to end:"+(t/1000));
					
					
				}
			}
				
			
		}
	}
	//not finished yet 
	private static EdgeData GetFE(DGraph graph,Fruit f) {
		double  x1,x2,x3,y1,y2,y3;
		x3=f.getPOS().x();
		y3=f.getPOS().y();
		Iterator<node_data> itr=graph.getV().iterator();
		if (f==null) return null;
		while(itr.hasNext()) {
			node_data node=itr.next();
			Iterator<edge_data> edg=graph.getE(node.getKey()).iterator();
			while(edg.hasNext() ) {
				edge_data edge=edg.next();
				Point3D s=graph.getNode(edge.getSrc()).getLocation();
				Point3D d=graph.getNode(edge.getDest()).getLocation();
				Point3D ff=f.getPOS();
				double d1=Math.sqrt((Math.pow(s.x()-ff.x(), 2))+(Math.pow(s.y()-ff.y(), 2)));
				double d2=Math.sqrt((Math.pow(ff.x()-d.x(), 2))+(Math.pow(ff.y()-d.y(), 2)));
				double d3=Math.sqrt((Math.pow(s.x()-d.x(), 2))+(Math.pow(s.y()-d.y(), 2)));
				if(d1+d2<=d3+Epsilon && d3-Epsilon<=d1+d2) {
					if(f.getType()== -1) {
						if(edge.getSrc()>edge.getDest()) return (EdgeData) edge;
						else return (EdgeData) graph.getEdge(edge.getDest(), edge.getSrc() );
					}else {
						if(edge.getSrc()<edge.getDest()) return (EdgeData) edge;
						else return (EdgeData) graph.getEdge(edge.getSrc(), edge.getDest());
						
					}
					
				}
			}
		}
		
		return null;
		
	}
	/**
	 * a very simple random walk implementation!
	 * @param g
	 * @param src
	 * @return
	 */
	private static int nextNode(DGraph g, Graph_Algo gg,Robot robot) {
		int ans = -1;
		List<node_data> path=new LinkedList<node_data>();
		EdgeData fruitedg=null;
		if(robot.getFruit()!=null && Fruit.contains(robot.getFruit()))// and fruits contat robot fruit 
			{
			fruitedg=GetFE(g,robot.getFruit());
			//System.out.println(" src : "+fruitedg.getSrc()+" dest: "+fruitedg.getDest());
		if(robot.getsrc()==fruitedg.getSrc() ) {
			
			if(g.getEdge(robot.getsrc(),fruitedg.getDest())==null) throw new RuntimeException ("there is no edge between src and dest"+robot.getsrc()+" "+fruitedg.getDest());
	//	System.out.println(robot.getsrc()+","+ fruitedg.getSrc());
			robot.SetFruit(null);
			return fruitedg.getDest();
		}else {
			path=gg.shortestPath(robot.getsrc(), fruitedg.getSrc());
		//	System.out.println("path from secon if "+path.get(1).getKey());
		return path.get(1).getKey();
		}
		}
		
	//	int y=(int) Math.random()*(Fruit.size()-1);
		// yeta5en she y get(y) return null
	//	robot.SetFruit(Fruit.get(y));
		double cmp=Double.MAX_VALUE;
		double pathcm = 0;
		EdgeData FG =null ;
		for(Fruit f: Fruit) {
			fruitedg=GetFE(g,f);
			System.out.println(robot.getsrc());


			if(pathcm<cmp){
				Fruit ag=new Fruit(f);
				cmp=pathcm;
				robot.SetFruit(ag);
				 FG=new EdgeData(fruitedg);
			}
			
		}		
		//System.out.println("fg dest:"+FG.getDest());

		
		if(robot.getsrc()== FG.getSrc()) return FG.getDest();
		path=gg.shortestPath(robot.getsrc(), FG.getSrc());
		System.out.println(path.size() +" : " + robot.getsrc() + " : " + FG.getSrc() );
		if(path.size()!=0)
			return path.get(1).getKey();
		return FG.getSrc();

	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	public static void main(String[] args) {
		MyGameGUIauto gg=new MyGameGUIauto();
		gg.run();
	}
}

