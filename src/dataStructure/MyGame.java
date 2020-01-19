package dataStructure;

import java.util.Iterator;
import java.util.LinkedList;

import org.json.JSONException;
import org.json.JSONObject;

import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo;
import oop_dataStructure.OOP_DGraph;
import utils.Point3D;

public class MyGame {
	private game_service game;
	public DGraph graph=new DGraph();
	private int robots_num;
	public LinkedList<Fruit> fruits=new LinkedList<Fruit>();
	private LinkedList<Robot> robots=new LinkedList<Robot>();
	private int scenario;
	 double maxX=0 ,maxY=0,minX=0,minY=0;
	
	
	public MyGame( int scenario)
	{
		if (scenario>23||scenario<0)
			throw new RuntimeException("ERR : this scenario isn't find");
		 game = Game_Server.getServer(scenario);
		 String g = game.getGraph();
		 String info = game.toString();
		 graph.init(g);

		 JSONObject line;
		 
			 try {
					line = new JSONObject(info);
					JSONObject ttt = line.getJSONObject("GameServer");
					robots_num = ttt.getInt("robots");
					System.out.println(info);
					System.out.println(g);
				     
				     find_scale();
				     setFruits();
				     setR();
				     setRobots(robots_num);
		 }
			 catch (JSONException e) 
				{
					e.printStackTrace();
				}
	    
	}
	
	
	
	
	
	public void find_scale()
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
	
	
	
	
	
	public void setFruits()
		{
		this.fruits=new LinkedList<Fruit>();
			Iterator<String> f_iter = game.getFruits().iterator();
			while (f_iter.hasNext())
			{
				Fruit f=new Fruit();
				f.init(f_iter.next());
				this.fruits.add(f);
			}
			Fruit_Comperator c=new Fruit_Comperator();
			this.fruits.sort(c);
		}
		
		
		public void setR()
		{JSONObject robots;
		try {
			   robots = new JSONObject(game.toString());
               robots = robots.getJSONObject("GameServer");
               int rs = robots.getInt("robots");
			
			    int src_node = 0; 
			  for(int a = 0;a<rs;a++) {
				   game.addRobot(src_node+a);
			  }
			}
		catch (JSONException e) {
			e.printStackTrace();
		}
		}
		public void setRobots(int robots_num)
		{
			this.robots=new LinkedList<Robot>();
			try {
		            	 for (String robot : game.getRobots()) 
		            	 {
		                     Robot robot_tmp = new Robot(robot);
		                     game.addRobot(robot_tmp.getid());
		                     
		                     this.robots.add( robot_tmp);
		            	 }
		            
			}catch (Exception e) {
				
			}
		}
		public double[] scale_x()
		{
			double[] x= {this.maxX,this.minX};
			return x;
		}
		
		public double[] scale_y()
		{
			double[] y= {this.maxY,this.minY};
			return y;
		}
		
		
		public LinkedList<Fruit> get_fruits()
		{
			return this.fruits;
		}
		
		public LinkedList<Robot> get_robot()
		{
			return this.robots;
		}
		public int get_robot_num()
		{
			return this.robots_num;
		}
		
		public DGraph get_graph()
		{
			return graph;
		}
		public game_service getgame()
		{
			return this.game;
		}
		public void update()
		{
			
		     setFruits();
		     setRobots(robots_num);
		}
	
	
}
