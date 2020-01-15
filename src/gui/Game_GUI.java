package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.corba.se.internal.Interceptors.PIORB;
import com.sun.prism.Image;

import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo;
import algorithms.graph_algorithms;
import dataStructure.DGraph;


import dataStructure.Fruit;
import dataStructure.Robot;
import dataStructure.edge_data;
import dataStructure.node;
import dataStructure.node_data;
import utils.Point3D;

public class Game_GUI extends JFrame implements ActionListener, MouseListener {

		DGraph graph;
		game_service game ;
		graph_algorithms g_algo;
		LinkedList<Fruit> fruits=new LinkedList<Fruit>();
		LinkedList<Robot> robots=new LinkedList<Robot>();
		double minX=0,maxX,minY=0,maxY;
		public Game_GUI() 
		{
			this.setSize(900,900);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.graph=new DGraph();
			game=Level();
			String g=game.getGraph();
			graph.init(g);
			String info = game.toString();
			JSONObject line;
			this.g_algo=new Graph_Algo();
			g_algo.init(g);

			try {
				line = new JSONObject(info);
				JSONObject ttt = line.getJSONObject("GameServer");
				int rs = ttt.getInt("robots");
				System.out.println(info);
				System.out.println(g);					
				Iterator<String> f_iter = game.getFruits().iterator();
				while(f_iter.hasNext()) 
				{
					Fruit f=new Fruit();
					f.init(f_iter.next());
					this.fruits.add(f);
				}
				AddRobot(rs);
				min_max();
				}
			catch (JSONException e) 
			{
				e.printStackTrace();
			}
			
			this.addMouseListener(this);
			this.setVisible(true);
		}
		private void  AddRobot(int rs)
		{
			//JSONObject rlist=(JSONObject) this.game.getRobots();
			for (int i=0;i<rs;i++)
			{	
				String robot_id= JOptionPane.showInputDialog(this,"Please insert node key to add the robot id ");
				int r_id=Integer.valueOf(robot_id);
				this.game.addRobot(r_id);
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
		private game_service Level()
		{
			
			try 
			{
				String level= JOptionPane.showInputDialog(this,"Please insert Level between [0,23]");
				int scenario =Integer.parseInt(level);
				
				if (scenario>23||scenario<0)
					  throw new IllegalArgumentException("please inter a number between [0,23]");
				return Game_Server.getServer(scenario);
			}
			catch(Exception e)
				{
					System.out.println(e.getMessage());
				}
			return null;
		}
		public void paint(Graphics g )
		{
			Collection<node_data> node=graph.getV();
			Iterator<node_data> nodes=node.iterator();

			 node=graph.getV();
			nodes=node.iterator();
				while(nodes.hasNext()) 
				{
					node_data n=nodes.next();
					g.setColor(Color.BLUE);
					double x_=scale(n.getLocation().x(),minX,maxX,180,720);
					double y_=scale(n.getLocation().y(),minY,maxY,180,720); 
					Point3D p_=new Point3D(x_, y_);
					//Image img=new ImageIcon(this.getClass().getResource("/apple.png")).getImage();
					g.fillOval(p_.ix(), p_.iy(), 10, 10);
					g.setFont(new Font("deafult", Font.BOLD,14));	
					g.setColor(Color.BLUE);
					String key=n.getKey()+"";
					
					g.drawString(key, p_.ix(), p_.iy());
					Collection<edge_data> edg=graph.getE(n.getKey());	


					Iterator<edge_data> itr=edg.iterator();
					while(itr.hasNext()) {

						edge_data s=itr.next();
						double x=scale(n.getLocation().x(),minX,maxX,180,720);
						double y=scale(n.getLocation().y(),minY,maxY,180,720); 
						Point3D p=new Point3D(x, y);
						double x1=scale(graph.getNode(s.getDest()).getLocation().x(),minX,maxX,180,720);
						double y1=scale(graph.getNode(s.getDest()).getLocation().y(),minY,maxY,180,720); 
						Point3D p2=new Point3D(x1, y1);
						g.setColor(Color.RED);
						g.setFont(new Font("deafult", Font.BOLD,14));
						String weight = s.getWeight() + "";
						
						g.drawLine(p.ix(), p.iy(), p2.ix(), p2.iy());
						
						//g.drawString(Double.toString(s.getWeight()), (int)((p.x()+(int)p2.x())/2),	(int)((p.y()+(int)p2.y())/2));
					

						g.setColor(Color.YELLOW);
						int x2=(int) ((0.8*p2.ix())+ (0.2*p.ix()));
						int y2 =(int)((0.8*p2.iy())+ (0.2*p.iy()));
						g.fillOval(x2-5,y2-5,10,10);
						
						//g.fillOval(x, y, width, height);
					}
					}
		}
		private double scale(double data, double r_min, double r_max, 
				double t_min, double t_max)
		{
			
			double res = ((data - r_min) / (r_max-r_min)) * (t_max - t_min) + t_min;
			return res;
		}
		@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	public static void main(String[] args) {
		Game_GUI gg=new Game_GUI();
		
	}

}
