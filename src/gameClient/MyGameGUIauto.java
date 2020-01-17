package gameClient;

import java.util.List;
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

public class MyGameGUIauto {
public static ArrayList<Fruit> Fruit=new ArrayList<Fruit>(); //seder
public static ArrayList<Robot> Robots=new ArrayList<Robot>();
public static final double Epsilon=0.0001;
public static void main(String[] a) {
	test1();
	}
	
	public static void test1()  {
		int scenario_num = 2;
		game_service game = Game_Server.getServer(2); // you have [0,23] games
		String g = game.getGraph();
		Graph_Algo gg = new Graph_Algo();
		DGraph graph=new DGraph();
		graph.init(g);
		gg.init(g);
		JSONObject line;
		String info = game.toString();
		try {
			line = new JSONObject(info);
			JSONObject ttt = line.getJSONObject("GameServer");
			int rs = ttt.getInt("robots");
			System.out.println(info);
			System.out.println(g);						
		     Fruit f=new Fruit();	
			// the list of fruits should be considered in your solution
			Iterator<String> f_iter = game.getFruits().iterator();
			while(f_iter.hasNext()) {
				String s=f_iter.next();
					f.init(s);
						Fruit.add(f);
							System.out.println(s);
						}
			

			
			int src_node = 0;  // arbitrary node, you should start at one of the fruits
			int c= (int) Math.random()*(Fruit.size()-1);
			Robot b ;
			
			for(int a = 0;a<rs ;a++) {
				if(Fruit.get(c)!=null  && c<Fruit.size()){	
					EdgeData edge=GetFE(graph,Fruit.get(c));
							src_node= edge.getSrc();
							b=new Robot(src_node,a,0,Fruit.get(c));
							Robots.add(b);
								game.addRobot(src_node);	
								c= (int) Math.random()*(Fruit.size()-1);
								System.out.println(b.getid()+" : "+a +" : "+ src_node);
				}else {
					
					b=new Robot(5,a,0,null);
					System.out.println(b.getid()+" : "+a);
					Robots.add(b);
				game.addRobot(5);
					}
				}
			}
		catch (JSONException e) {e.printStackTrace();}
		game.startGame();
		// should be a Thread!!!
		int index=0;
		long time=50;
		while(game.isRunning()) {
			moveRobots(game, gg,graph);
								try {
						if(index%2==0) {;
						}
				else {
			Thread.sleep(time);
			index++;
			}
					} catch (InterruptedException e) {e.printStackTrace();}	
		}
		String results = game.toString();
		System.out.println("Game Over: "+results);
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
				Robot demo =new Robot(robot_json);
				for (Robot r : Robots) {
			Robots.get(r.getid()).init(robot_json);
			
				}
				
			}
			
			
			
			Iterator<String> f_iter = game.getFruits().iterator();
			Fruit Ban=new Fruit();
			Fruit=new ArrayList<Fruit>();
			while(f_iter.hasNext()) {
				String s=f_iter.next();
					Ban.init(s);
						Fruit.add(Ban);
							System.out.println(s);
						}
			int dest=0;
			for(Robot r:Robots) {
				if(r.getdest()==-1) {
					dest=nextNode(graph,gg,r);
					game.chooseNextEdge(r.getsrc(), dest);
					
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
		if(robot.getFruit()!=null )// and fruits contat robot fruit 
			{
			fruitedg=GetFE(g,robot.getFruit());
		if(robot.getsrc()==fruitedg.getSrc() ) {
			if(g.getEdge(robot.getsrc(),fruitedg.getDest())==null) throw new RuntimeException ("there is no edge between src and dest"+robot.getsrc()+" "+fruitedg.getDest());
			Fruit.remove(robot.getFruit());
		robot.SetFruit(null);
			return fruitedg.getDest();
		}else {
			path=gg.shortestPath(robot.getsrc(), fruitedg.getSrc());
		return path.get(1).getKey();
		}
		}
		
	//	int y=(int) Math.random()*(Fruit.size()-1);
		// yeta5en she y get(y) return null
	//	robot.SetFruit(Fruit.get(y));
		double cmp=Double.MAX_VALUE;
		double pathcm;
		EdgeData FG =null ;
		for(Fruit f: Fruit) {
			fruitedg=GetFE(g,f);
			pathcm=gg.shortestPathDist(robot.getsrc(), fruitedg.getSrc());
			if(pathcm<cmp){
				Fruit ag=new Fruit(f);
				cmp=pathcm;
				robot.SetFruit(ag);
				 FG=new EdgeData(fruitedg);
			}
			
		}
		if(robot.getsrc()== FG.getSrc()) return FG.getDest();
		path=gg.shortestPath(robot.getsrc(), fruitedg.getSrc());
		System.out.println(path.size() +" : " + robot.getsrc() + " : " + fruitedg.getSrc() );
		return path.get(1).getKey();
	}
}

