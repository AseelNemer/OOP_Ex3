package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.sun.corba.se.impl.orbutil.graph.Node;

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
import utils.Point3D;

public class Game_GUI extends JFrame implements ActionListener, MouseListener  {

	public MyGame game ;
	public DGraph graph;
	public LinkedList<Fruit> fruits=new LinkedList<Fruit>();
	public LinkedList<Robot> robots=new LinkedList<Robot>();
	public double[] scale_x,scale_y;
	
	
	
	
	public Game_GUI()
	{
		this.setSize(900,900);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//try 
		//{
			/**String level= JOptionPane.showInputDialog(this,"Please insert Level between [0,23]");
			int scenario =Integer.parseInt(level);
			
			if (scenario>23||scenario<0)
				
				 throw new IllegalArgumentException("please inter a number between [0,23]");
			*/
			game =new MyGame(1) ;
			this.graph=game.get_graph();
			
		/*}
		catch(Exception e)
			{
				System.out.println(e.getMessage());
			}*/
		this.fruits=game.get_fruits();
		this.robots=game.get_robot();
		this.scale_x=game.scale_x();
		this.scale_y=game.scale_y();
		
		
		this.addMouseListener(this);
		this.setVisible(true);
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
			
			paintfruit(g);
			paintRobot(g);

			 Graphics2D g2dComponent = (Graphics2D) g;
	         g2dComponent.drawImage(bufferedImage, null, 0, 0);
			
			//paintrobot(g);
	}
	public void paintRobot(Graphics g)
	{
		for (int i=0 ;i<this.robots.size();i++)
		{
			Robot r=this.robots.get(i);
			g.setColor(Color.BLACK);
			int x=(int)(scale(r.getpos().x(),scale_x[0],scale_x[1],50,850));
			int y=(int)(scale(r.getpos().y(),scale_y[0],scale_y[1],200,700));
			g.fillOval(x-7,y-7, 30, 30);
		}
	}
	public void paintfruit( Graphics g)
	{
		for (int i=0 ;i<this.fruits.size();i++)
			{
				Fruit fr=this.fruits.get(i);
				g.setColor(Color.CYAN);
				int x=(int)(scale(fr.getPOS().x(),scale_x[0],scale_x[1],50,850));
				int y=(int)(scale(fr.getPOS().y(),scale_y[0],scale_y[1],200,700));
				g.fillOval(x-7,y-7, 20, 20);
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
		String str_key = JOptionPane.showInputDialog(this, "Press on a robot you want to move");
		int robot=Integer.valueOf(str_key);
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
			if((x<=px+10&&x>=px-10)&&(y<=py+10&&y>=py-10))
			{
				s=src;
				temp=r;	
			}
		}
		if(temp!=null)
		{
			String next = JOptionPane.showInputDialog(this, "Press on a next node");
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
					((game_service) game).chooseNextEdge(temp.getid(), edge.getDest());
					break;
				}
			 }
		}
		
		
		
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
