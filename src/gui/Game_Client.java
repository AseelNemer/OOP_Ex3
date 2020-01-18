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
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

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
import sun.awt.RepaintArea;
import utils.Point3D;
import utils.StdDraw;
import dataStructure.*;
import java.awt.event.MouseAdapter;

public class Game_Client extends JFrame implements ActionListener, MouseListener  {

	private MyGame game ;
	private static DGraph graph;
	private LinkedList<Fruit> fruits=new LinkedList<Fruit>();
	private LinkedList<Robot> robots=new LinkedList<Robot>();
	private static double[] scale_x;
	private static double[] scale_y;
	 public long time ;
	
	
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
			System.out.println(time);
		}
		catch(Exception e)
			{
				System.out.println(e.getMessage());
			}
		
		
		
		this.addMouseListener(this);
		this.setVisible(true);
	}
	public void run()
	{
		try {
			startgame();
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	
	public void startgame()
	{
		game.getgame().startGame();
		while (game.getgame().isRunning())
		{
			repaint();
			long t = game.getgame().timeToEnd();
			String TimeLeft = "Time to end : " + t;
			System.out.println(t/1000);
			moveRobots(game.getgame(), graph);
			repaint();
		}		
		repaint();
		
		
	}
	
	public void paint(Graphics g )
	{
		 BufferedImage bufferedImage = new BufferedImage(900, 900, BufferedImage.TYPE_INT_ARGB);
         Graphics2D g2d = bufferedImage.createGraphics();
         g2d.setBackground(new Color(240, 240, 240));
        // g2d.clearRect(0, 0, 900, 900);
        
         
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
					
					//g.fillOval(x, y, width, height);
				}
				
				}
			
			 g.setColor(Color.BLACK);
		        g.setFont(new Font("Arial", Font.BOLD, 20));
		        g.drawString("Time left: " + (game.getgame().timeToEnd() / 1000), 70, 70);
			paintRobot(g);
			paintfruit(g);
			
			
			 Graphics2D g2dComponent = (Graphics2D) g;
	         g2dComponent.drawImage(bufferedImage, null, 0, 0);
			
			//paintrobot(g);
	}
	public  void paintRobot(Graphics g)
	{
		LinkedList<Robot> robots=game.get_robot();
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
	public void paintfruit( Graphics g)
	{
		this.fruits=game.get_fruits();
		for (int i=0 ;i<this.fruits.size();i++)
			{
				Fruit fr=this.fruits.get(i);
				if (fr.getType()==1)
				{
					g.setColor(Color.CYAN);
					int x=(int)(scale(fr.getPOS().x(),scale_x[0],scale_x[1],50,850));
					int y=(int)(scale(fr.getPOS().y(),scale_y[0],scale_y[1],200,700));
					g.fillOval(x-7,y-7, 20, 20);
				}
				if(fr.getType()==-1)
				{
					g.setColor(Color.GREEN);
					int x=(int)(scale(fr.getPOS().x(),scale_x[0],scale_x[1],50,850));
					int y=(int)(scale(fr.getPOS().y(),scale_y[0],scale_y[1],200,700));
					g.fillOval(x-7,y-7, 20, 20);
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
			}
		 
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
			repaint();
		*/
		 for (int i = 0; i < game.get_robot_num(); i++) {
	            Robot robot = game.get_robot().get(i);
	            if (robot.getdest() == -1) {
	                String dst_str = JOptionPane.showInputDialog(this, "Please insert robot " + robot.getid() + " next node destination");
	                try {
	                    int dest = Integer.parseInt(dst_str);
	                    this.game.getgame().chooseNextEdge(robot.getid(), dest);
	                } catch (Exception ex) {
	                    JOptionPane.showMessageDialog(this, "ERROR", "ERROR", JOptionPane.ERROR_MESSAGE);
	                }
	            }
	        }
		 repaint();
	}
	private static void moveRobots(game_service game, DGraph gg) {
		List<String> log = game.move();
		if(log!=null) {
			long t = game.timeToEnd();
			for(int i=0;i<log.size();i++) {
				String robot_json = log.get(i);
				Robot robot = null;
				JSONObject line;
				try {
					line = new JSONObject(robot_json);
				
					JSONObject ttt = line.getJSONObject("Robot");
					int rid = ttt.getInt("id");
					int src = ttt.getInt("src");
					int dest = ttt.getInt("dest");
					
					if(dest==-1) {
						dest = nextNode(gg, src);
						game.chooseNextEdge(rid, dest);
						
						System.out.println("Turn to node: "+dest+"  time to end:"+(t/1000));
						
					}
				}
		
			 catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	}
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
		//gg.run();
	}
	
}
