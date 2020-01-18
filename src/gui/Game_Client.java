package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.peer.MouseInfoPeer;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.event.MouseInputListener;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.corba.se.impl.orbutil.graph.Node;
import com.sun.javafx.scene.paint.GradientUtils.Point;

import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import dataStructure.EdgeData;
import dataStructure.Fruit;
import dataStructure.MyGame;
import dataStructure.Robot;
import dataStructure.edge_data;
import dataStructure.node;
import dataStructure.node_data;
import javafx.scene.input.MouseButton;
import sun.awt.RepaintArea;
import sun.security.action.GetLongAction;
import utils.Point3D;
import utils.StdDraw;
import dataStructure.*;
import java.awt.event.MouseAdapter;

public class Game_Client extends JFrame implements ActionListener, MouseListener  {

	private MyGame game ;
	private static DGraph graph;
	private static LinkedList<Fruit> fruits=new LinkedList<Fruit>();
	private static LinkedList<Robot> robots=new LinkedList<Robot>();
	private static double[] scale_x;
	private static double[] scale_y;
	 public static long time ;
	
	
	public Game_Client()
	{
		this.setSize(900,900);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		try 
		{
			String level= JOptionPane.showInputDialog(this,"Please insert Level between [0,23]");
			int scenario =Integer.parseInt(level);
			
			if (scenario>23||scenario<0)
				game =new MyGame(0);
				 //throw new IllegalArgumentException("please inter a number between [0,23]");
			else
				game =new MyGame(scenario);
			this.graph=game.get_graph();
			this.fruits=game.get_fruits();
			this.robots=game.get_robot();
			this.scale_x=game.scale_x();
			this.scale_y=game.scale_y();
			game.getgame().startGame();
			time=game.getgame().timeToEnd();
			
		}
		catch(Exception e)
			{
				System.out.println(e.getMessage());
			}
		
		
		
		this.addMouseListener(this);
		this.setVisible(true);
	}
	public void update(Graphics g)
	{
	  paint(g);
	}
	public void run()
	{
		game.getgame().startGame();
		int index=0;
		long time=50;
		while(game.getgame().isRunning()) {
			
			
				game.getgame().move();
				game.update();
			try {
				Thread.sleep(time);
					index++;
					
				if(index%1==0) {
						paintfruit(getGraphics());
						paintRobot(getGraphics());
					}
			}
			catch (InterruptedException e) {e.printStackTrace();}	
		}
	
		String results = game.toString();
		System.out.println("Game Over: "+results);
	
	
		this.setVisible(false);
		System.exit(0);
	}

	
	
	public void paint(Graphics g2d)
	{
			
			super.paint(g2d);;
			/**Graphics2D g2d = (Graphics2D) g;
		    g2d.setBackground(new Color(240, 240, 240));
		    */
		        Collection<node_data> node=graph.getV();
		        Iterator<node_data> nodes=node.iterator();
		
		        node=graph.getV();
		        nodes=node.iterator();
			while(nodes.hasNext()) 
			{
				node_data n=nodes.next();
				g2d.setColor(Color.BLUE);
				double x_=scale(n.getLocation().x(),scale_x[0],scale_x[1],50,850);
				double y_=scale(n.getLocation().y(),scale_y[0],scale_y[1],200,700); 
				Point3D p_=new Point3D(x_, y_);
				//Image img=new ImageIcon(this.getClass().getResource("/apple.png")).getImage();
				g2d.fillOval(p_.ix(), p_.iy(), 10, 10);
				g2d.setFont(new Font("deafult", Font.BOLD,14));	
				g2d.setColor(Color.BLUE);
				String key=n.getKey()+"";
				
				g2d.drawString(key, p_.ix(), p_.iy());
				Collection<edge_data> edg=graph.getE(n.getKey());	


				Iterator<edge_data> itr=edg.iterator();
				while(itr.hasNext()) {

					edge_data s=itr.next();
					double x=scale(n.getLocation().x(),scale_x[0],scale_x[1],50,850);
					double y=scale(n.getLocation().y(),scale_y[0],scale_y[1],200,700); 
					Point3D p=new Point3D(x, y);
					double x1=scale(graph.getNode(s.getDest()).getLocation().x(),scale_x[0],scale_x[1],50,850);
					double y1=scale(graph.getNode(s.getDest()).getLocation().y(),scale_y[0],scale_y[1],200,700); 
					Point3D p2=new Point3D(x1, y1);
					g2d.setColor(Color.RED);
					g2d.setFont(new Font("deafult", Font.BOLD,14));
					String weight = s.getWeight() + "";
					
					g2d.drawLine(p.ix(), p.iy(), p2.ix(), p2.iy());
					
					//g.drawString(Double.toString(s.getWeight()), (int)((p.x()+(int)p2.x())/2),	(int)((p.y()+(int)p2.y())/2));
				

					g2d.setColor(Color.YELLOW);
					int x2=(int) ((0.8*p2.ix())+ (0.2*p.ix()));
					int y2 =(int)((0.8*p2.iy())+ (0.2*p.iy()));
					g2d.fillOval(x2-5,y2-5,10,10);
					
				}
				
				}
			
				this.time=game.getgame().timeToEnd()/1000;
				g2d.setColor(Color.BLACK);
		        g2d.setFont(new Font("Arial", Font.BOLD, 20));
		        g2d.drawString("Time left: " + (this.time ), 70, 70);
		        
		        
			paintRobot(g2d);
			paintfruit(g2d);
			
	}
	public  void paintRobot(Graphics g)
	{
		
        //g.setBackground(new Color(240, 240, 240));
		this.robots=game.get_robot();
		LinkedList<Robot> robots=this.robots;
		for (int i=0 ;i<robots.size();i++)
		{
			Robot r=robots.get(i);
			int x=(int)(scale(r.getpos().x(),scale_x[0],scale_x[1],50,850));
			int y=(int)(scale(r.getpos().y(),scale_y[0],scale_y[1],200,700));
			g.setColor(Color.BLACK);
			g.drawOval(x-7,y-7, 30, 30);
			g.setFont(new Font("Arial", Font.BOLD, 15));
		}
		
	}
	public void paintfruit( Graphics g2d)
	{
		
       // g2d.setBackground(new Color(240, 240, 240));
		this.fruits=game.get_fruits();
		for (int i=0 ;i<this.fruits.size();i++)
			{
				Fruit fr=this.fruits.get(i);
				if (fr.getType()==1)
				{
					g2d.setColor(Color.CYAN);
					int x=(int)(scale(fr.getPOS().x(),scale_x[0],scale_x[1],50,850));
					int y=(int)(scale(fr.getPOS().y(),scale_y[0],scale_y[1],200,700));
					g2d.fillOval(x-7,y-7, 20, 20);
				}
				if(fr.getType()==-1)
				{
					g2d.setColor(Color.GREEN);
					int x=(int)(scale(fr.getPOS().x(),scale_x[0],scale_x[1],50,850));
					int y=(int)(scale(fr.getPOS().y(),scale_y[0],scale_y[1],200,700));
					g2d.fillOval(x-7,y-7, 20, 20);
				}
			}
		 
	}
	private static double scale(double data, double r_min, double r_max, 
			double t_min, double t_max)
	{
		
		double res = ((data - r_min) / (r_max-r_min)) * (t_max - t_min) + t_min;
		return res;
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		
		/** JOptionPane.showMessageDialog(this, "press on the robot you want to move" , "MOVE", JOptionPane.INFORMATION_MESSAGE);
			
			int x = arg0.getX();
			int y = arg0.getY();
			System.out.println(x);
			System.out.println(y);
			Robot temp=null;
			node_data s=new node();
			for (int i=0;i<game.get_robot_num();i++)
			{
				Robot r=this.robots.get(i);
				node_data src=graph.getNode(r.getsrc());
				Point3D p=src.getLocation();
				double px=(scale(p.x(),scale_x[0],scale_x[1],50,850));
				double py=(scale(p.y(),scale_y[0],scale_y[1],200,700));
				if((x<=px+10&&x>=px-20)&&(y<=py+20&&y>=py-20))
				{
					s=src;
					temp=r;	
				}
			}*/
		 
			String str=JOptionPane.showInputDialog("please inter a robot id you want to move");
			int id=Integer.valueOf(str);
			Robot temp=null;
			temp.init(str);
			node_data src=graph.getNode(temp.getsrc());
			JOptionPane.showMessageDialog(this, "press on the next node for this robot"+temp.getid() , "MOVE", JOptionPane.INFORMATION_MESSAGE);
			
			
			int x = arg0.getX();
			 int  y = arg0.getY();
			if(temp!=null)
			{
				  JOptionPane.showMessageDialog(this, "press on the next node for this robot"+temp.getid() , "MOVE", JOptionPane.INFORMATION_MESSAGE);
				 
				 System.out.println(arg0);
				 Iterator<edge_data> neighbors =graph.getE(src.getKey()).iterator();
				 while (neighbors.hasNext())
				 {
					 edge_data edge=neighbors.next();
					 Point3D p=graph.getNode(edge.getDest()).getLocation();
					 double px=(scale(p.x(),scale_x[0],scale_x[1],50,850));
					double py=(scale(p.y(),scale_y[0],scale_y[1],200,700));
					if((x<=px+10&&x>=px-10)&&(y<=py+10&&y>=py-10))
					{
						game.getgame().chooseNextEdge(temp.getid(), edge.getDest());
						System.out.println("Turn to node: "+edge.getDest()+"  time to end:"+(time/1000));
						break;
					}
				 }
			}
		
		
		/** for (int i = 0; i < game.get_robot_num(); i++) {
	            Robot robot = game.get_robot().get(i);
	            if (robot.getdest() == -1) {
	                String dst_str = JOptionPane.showInputDialog(this, "Please insert robot " + robot.getid() + " next node destination");
	                try {
	                    int dest = Integer.parseInt(dst_str);
	                    this.game.getgame().chooseNextEdge(robot.getid(), dest);
						System.out.println("Turn to node: "+dest+"  time to end:"+(time/1000));
						System.out.println(game.getgame().getRobots().get(i));
	                } catch (Exception ex) {
	                    JOptionPane.showMessageDialog(this, "ERROR", "ERROR", JOptionPane.ERROR_MESSAGE);
	                }
	            }
	        }*/
	}
	private static void moveRobots(game_service game, DGraph gg) {
		/**
		 * List<String> log = game.move();
		if(log!=null) {
			long t = game.timeToEnd();
			for(int i=0;i<log.size();i++) {
				String robot_json = log.get(i);
				Robot robot = new Robot(robot_json);
				try {
					Point3D pr=robot.getpos();
					int rx=(int)pr.x();
					int ry=(int)pr.y();
					
					Point2D p = MouseInfo.getPointerInfo().getLocation();
					int x=(int)p.getX();
					int y=(int)p.getY();
					System.out.println(x);
					
					if((x<=rx+10&&x>=rx-10)&&(y<=ry+10&&y>=ry-10))
					{
						int dest=robot.getdest();
						int src=robot.getsrc();
						int rid=robot.getid();
						if(dest==-1) {
						dest = nextNode(gg, src);
						game.chooseNextEdge(rid, dest);
						System.out.println("Turn to node: "+dest+"  time to end:"+(t/1000));
					}
					}
				}
		
			 catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	}
		
		
		
		
		for (int i = 0; i < game.getRobots().size(); i++) {
            String r = game.getRobots().get(i);
            Robot robot=new Robot(r);
            if (robot.getdest() == -1) {
               // String dst_str = JOptionPane.showInputDialog( "Please insert robot " + robot.getid() + " next node destination");
                
                try {
					Point3D pr=robot.getpos();
					int rx=(int)pr.x();
					int ry=(int)pr.y();
					Point2D p = MouseInfo.getPointerInfo().getLocation();
					int x=(int)p.getX();
					int y=(int)p.getY();
					System.out.println(x);
					
					if((x<=rx+10&&x>=rx-10)&&(y<=ry+10&&y>=ry-10))
					{
						int dest=robot.getdest();
						int src=robot.getsrc();
						int rid=robot.getid();
						if(dest==-1) {
						dest = nextNode(gg, src);
						game.chooseNextEdge(rid, dest);
						System.out.println("Turn to node: "+dest+"  time to end:"+(time/1000));
					}
					}
                }catch (Exception e) {
						
					}
            }
        }*/
	}
		
		

	private static  int nextNode(DGraph gg, int src) {
		
		
		
		Point2D p = MouseInfo.getPointerInfo().getLocation();
		
		int x=(int)p.getX();
		int y=(int)p.getY();
		
		Iterator<edge_data> neighbors =gg.getE(src).iterator();
		 while (neighbors.hasNext())
		 {
			 edge_data edge=neighbors.next();
			 Point3D p1=graph.getNode(edge.getDest()).getLocation();
			 double px=(scale(p1.x(),scale_x[0],scale_x[1],50,850));
			double py=(scale(p1.y(),scale_y[0],scale_y[1],200,700));
			if((x<=px+10&&x>=px-10)&&(y<=py+10&&y>=py-10))
			{
				return edge.getDest();
			}
		 }
		
		return src;
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

		 /**JOptionPane.showMessageDialog(this, "press on the robot you want to move" , "MOVE", JOptionPane.INFORMATION_MESSAGE);
			
			int x = arg0.getX();
			int y = arg0.getY();
			System.out.println(x);
			System.out.println(y);
			Robot temp=null;
			node_data s=new node();
			for (int i=0;i<game.get_robot_num();i++)
			{
				Robot r=this.robots.get(i);
				node_data src=graph.getNode(r.getsrc());
				Point3D p=src.getLocation();
				double px=p.x();
				double py=p.y();
				if((x<=px+10&&x>=px-20)&&(y<=py+20&&y>=py-20))
				{
					s=src;
					temp=r;	
				}
			}
			if(temp!=null)
			{
				  JOptionPane.showMessageDialog(this, "press on the next node for this robot"+temp.getid() , "MOVE", JOptionPane.INFORMATION_MESSAGE);
				 x = arg0.getX();
				 y = arg0.getY();
				 Iterator<edge_data> neighbors =graph.getE(s.getKey()).iterator();
				 while (neighbors.hasNext())
				 {
					 edge_data edge=neighbors.next();
					 Point3D p=graph.getNode(edge.getDest()).getLocation();
					 double px=p.x();
					double py=p.y();
					if((x<=px+10&&x>=px-10)&&(y<=py+10&&y>=py-10))
					{
						game.getgame().chooseNextEdge(temp.getid(), edge.getDest());
						break;
					}
				 }
			}
			repaint();*/
			
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
		Game_Client gg=new Game_Client();
		gg.run();
	}
	
}
