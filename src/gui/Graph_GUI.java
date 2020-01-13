package gui;

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.List;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FilenameFilter;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import algorithms.Graph_Algo;
import algorithms.graph_algorithms;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

import javax.swing.JFrame;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node;
import dataStructure.node_data;
import utils.Point3D;

public class Graph_GUI extends JFrame implements ActionListener, MouseListener {

	 private graph graph;
	 private Graph_Algo algoG;
	 
public Graph_GUI() 
{
		 this.graph=new DGraph();
	     algoG=new Graph_Algo();
		 initGUI();
}
	
public Graph_GUI(graph D)
{
		 this.graph=D;
	     algoG = new Graph_Algo();
		 initGUI();
}
	
private void initGUI() 
{
	this.setSize(800,800);
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	MenuBar menuBar = new MenuBar();
	
	Menu menu = new Menu("file");
	menuBar.add(menu);
	this.setMenuBar(menuBar);
	
	Menu menu1=new Menu("Algorithms");
	menuBar.add(menu1);
	this.setMenuBar(menuBar);
	
	Menu menu2=new Menu("Add/Delete Node");
	menuBar.add(menu2);
	this.setMenuBar(menuBar);
	
	Menu menu3=new Menu("Add/Delete Edge");
	menuBar.add(menu3);
	this.setMenuBar(menuBar);
	
	
	
	MenuItem item1 = new MenuItem("Save File");
	item1.addActionListener(this);
	menu.add(item1);
	menu.addSeparator();
	
	MenuItem item2 = new MenuItem("Load File");
	item2.addActionListener(this);
	menu.add(item2);
	
	MenuItem item3 = new MenuItem("Is Connected");
	item3.addActionListener(this);
	menu1.add(item3);
	
	menu1.addSeparator();
	
	MenuItem item4 = new MenuItem("Shortest Path Distance");
	item4.addActionListener(this);
	menu1.add(item4);
	
	menu1.addSeparator();
	
	MenuItem item5 = new MenuItem("Shortest Path");
	item5.addActionListener(this);
	menu1.add(item5);
	
	menu1.addSeparator();
	
	MenuItem item10 = new MenuItem("TSP");
	item10.addActionListener(this);
	menu1.add(item10);
	
	MenuItem item6 = new MenuItem("Add Node");
	item6.addActionListener(this);
	menu2.add(item6);
	
	menu2.addSeparator();
	
	MenuItem item7 = new MenuItem("Delete Node");
	item7.addActionListener(this);
	menu2.add(item7);
	
	MenuItem item8 = new MenuItem("Connect");
	item8.addActionListener(this);
	menu3.add(item8);
	
	menu3.addSeparator();
	
	MenuItem item9 = new MenuItem("Delete Edge");
	item9.addActionListener(this);
	menu3.add(item9);
	
	
	this.addMouseListener(this);
	this.setVisible(true);
}

public void paint(Graphics g )
{
	node_data prev=null;
	super.paint(g);
	
	Collection<node_data> node=graph.getV();
	Iterator<node_data> nodes=node.iterator();

	while (nodes.hasNext())
	{
		node_data n=nodes.next();
		int rx = (int)(Math.random()*500+160);
		int ry = (int)(Math.random()*400+150);
		n.setLocation(new Point3D(rx, ry));
	}
	 node=graph.getV();
	nodes=node.iterator();
		while(nodes.hasNext()) 
		{
			node_data n=nodes.next();
			g.setColor(Color.BLUE);
			g.fillOval((int)n.getLocation().x(), (int)n.getLocation().y(), 10, 10);
			g.setFont(new Font("deafult", Font.BOLD,14));	
			g.setColor(Color.BLUE);
			String key=n.getKey()+"";
			g.drawString(key, n.getLocation().ix()+3, n.getLocation().iy());
			Collection<edge_data> edg=graph.getE(n.getKey());	


			Iterator<edge_data> itr=edg.iterator();
			while(itr.hasNext()) {

				edge_data s=itr.next();
				
				Point3D p=n.getLocation();
				Point3D p2=graph.getNode(s.getDest()).getLocation();
			
				g.setColor(Color.RED);
				g.setFont(new Font("deafult", Font.BOLD,14));
				String weight = s.getWeight() + "";
				
				g.drawLine(p.ix(), p.iy(), p2.ix(), p2.iy());
				
				g.drawString(Double.toString(s.getWeight()), (int)((p.x()+(int)p2.x())/2),	(int)((p.y()+(int)p2.y())/2));
			

				g.setColor(Color.YELLOW);
				int x=(int)0.8*p2.ix()+ (int)0.2*p.ix();
				int y =(int)0.8*p2.iy()+ (int)0.2*p.iy();
				g.fillOval(x-5, y-5, 10, 10);
				
				//g.fillOval(x, y, width, height);
			}
			}
		
	}

	
@Override
public void actionPerformed(ActionEvent e) {
	
	String str = e.getActionCommand();
	switch (str) {
	case "Save File":
			savefile();
		 break;
	case "Load File":
		loadfile();
		break;
	case "Is Connected":
			isconnected();	
		break;
		
	case "Shortest Path Distance":
		shortest_path_distance();
		break;
	case "Shortest Path":
		shortest_path();
		break;
	case "TSP":
		TSP();
		break;
	case "Add Node":
		addnode();
		break;
	case "Delete Node":
		deletenode();
		break;
	case "Connect":
		connect();
			break;
	case "Delete Edge":
		delete_edge();
				break;
	default:
		break;
	}
	
}
private void savefile()
{
	/**algoG.init(graph);
	FileDialog fd = new FileDialog(this, "Save graph", FileDialog.SAVE);
	 String filename = fd.getFile();
	 fd.setVisible(true);
	 if (fd!=null)
	 {
		
		algoG.save(fd.getDirectory()+filename +".txt");
		JOptionPane.showMessageDialog(this, "Graph saved to " + fd.getFile(), "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
	 }
	 else
           JOptionPane.showMessageDialog(this, "File did not saved" , "ERROR", JOptionPane.ERROR_MESSAGE);
*/
	algoG.init(graph);
    FileDialog fd = new FileDialog(this, "Save graph", FileDialog.SAVE);
    fd.setFile("*.txt");
    fd.setFilenameFilter(new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(".txt");
        }
    });
    fd.setVisible(true);
    if(fd.getDirectory()!=null&&fd.getFile()!=null) {
        algoG.save(fd.getDirectory() + fd.getFile());
        JOptionPane.showMessageDialog(this, "Graph saved to " + fd.getFile(), "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
    }
    else
    {
        JOptionPane.showMessageDialog(this, "File did not saved" , "ERROR", JOptionPane.ERROR_MESSAGE);

    }
}
private void loadfile()
{
	 FileDialog fd = new FileDialog(this, "Open text file", FileDialog.LOAD);
     fd.setFile("*.txt");
     fd.setFilenameFilter(new FilenameFilter() {
         @Override
         public boolean accept(File dir, String name) {
             return name.endsWith(".txt");
         }
     });
     fd.setVisible(true);
     algoG.init(fd.getDirectory() + fd.getFile());
     this.graph = algoG.copy();
     repaint();
}
private void isconnected()
{
	algoG.init(graph);
	if(algoG.isConnected()==true)
		JOptionPane.showMessageDialog(this, "The Graph Is Connected \n" , "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
	else
		JOptionPane.showMessageDialog(this, "The Graph Is Not Connected \n" , "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
}
private void addnode()
{
	
	try {
		algoG.init(graph);
	JOptionPane.showMessageDialog(this, "Please press on the GUI to set the new node location");
	
	}catch (Exception e) {
    		JOptionPane.showMessageDialog(this, "ERROR: " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
}
private void deletenode()
{
	String str_key= JOptionPane.showInputDialog(this,"Please insert node key to remove");
    try
    {
    	algoG.init(graph);
        int key=Integer.parseInt(str_key);
        node_data n=graph.removeNode(key);
        if(n!=null) {
            JOptionPane.showMessageDialog(this, "Remove node succeeded", "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
        }
        else
        {
            JOptionPane.showMessageDialog(this, "Key does not exist in graph", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        repaint();
    }
    catch (Exception e)
    {
        JOptionPane.showMessageDialog(this, "ERROR: " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
    } 
    	
 }

private void connect()
{
	
	 String str_src= JOptionPane.showInputDialog(this,"Please insert source node");
     String str_dst=JOptionPane.showInputDialog(this,"Please insert destination node");
     String str_w=JOptionPane.showInputDialog(this,"Please insert weight");
     try 
     {
    	 algoG.init(graph);
    	int src=Integer.parseInt(str_src);
    	int dest=Integer.parseInt(str_dst);
    	double w= Double.parseDouble(str_w);
    	graph.connect(src, dest, w);
    	JOptionPane.showMessageDialog(this, "connect succeed");
     }
     catch(Exception e)
     {
         JOptionPane.showMessageDialog(this, "ERROR: " + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
         System.out.println(e.getMessage());
     }
     repaint();
}
private void delete_edge()
{
	 String str_src= JOptionPane.showInputDialog(this,"Please insert source node");
     String str_dst=JOptionPane.showInputDialog(this,"Please insert destination node");
     try 
     {
    	 algoG.init(graph);
    	 int src=Integer.parseInt(str_src);
    	 int dest=Integer.parseInt(str_dst);
    	 graph.removeEdge(src, dest);
    	 JOptionPane.showMessageDialog(this, "The Edge Is Deleted");
    	 repaint();
     }catch (Exception e)
     {
    	 JOptionPane.showMessageDialog(this, "ERR :"+e.getMessage());
     }
     
}
private void TSP()
{
	try {
		algoG.init(graph);
		String targets_Num=JOptionPane.showInputDialog(this, "Please Insert The Number Of Targets");
		int targets_num=Integer.parseInt(targets_Num);
		LinkedList<Integer> targets=new LinkedList<Integer>();
		String keys="";
		int key;
		for (int i=0;i<targets_num;i++)
	{
		  keys= JOptionPane.showInputDialog(this,"Please insert target number");
		  key=Integer.parseInt(keys);
		  targets.add(key);
	}
	LinkedList<node_data> Tsp=(LinkedList<node_data>) algoG.TSP(targets);
	String path="{";
	
	for (int i=0;i<Tsp.size();i++)
	{
		if(i!=Tsp.size()-1) {
			path+=Tsp.get(i).getKey()+",";
			System.out.println(path);
		}
		else {
			path+=Tsp.get(i).getKey();
		path+="}";}
	}
	 JOptionPane.showMessageDialog(this, "The Road  Is:"+path);
	 
	}
	catch(Exception e)
	{
		JOptionPane.showMessageDialog(this, e.getMessage());
	}
}
private void shortest_path_distance()
{
	algoG.init(graph);
	 String str_src= JOptionPane.showInputDialog(this,"Please insert source node");
     String str_dst=JOptionPane.showInputDialog(this,"Please insert destination node");
	try {
		
		int src=Integer.parseInt(str_src);
		int dest=Integer.parseInt(str_dst);
		double shortestpath=algoG.shortestPathDist(src, dest);
		 if(shortestpath==Double.MAX_VALUE)
     
             JOptionPane.showMessageDialog(this, "There Is No Path Between This Two Points ", "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
		 else
			 JOptionPane.showMessageDialog(this, "The Shortest Path Between The Two Points Is:"+shortestpath);
		repaint();
	}
	catch(Exception e)
	{
		JOptionPane.showMessageDialog(this, "ERR :"+e.getMessage());
	}
}
private void shortest_path()
{
	algoG.init(graph);
	 String str_src= JOptionPane.showInputDialog(this,"Please insert source node");
     String str_dst=JOptionPane.showInputDialog(this,"Please insert destination node");
	try {
		algoG.init(graph);
		int src=Integer.parseInt(str_src);
		int dest=Integer.parseInt(str_dst);
		LinkedList<node_data> shortestpath= (LinkedList<node_data>) algoG.shortestPath(src, dest);
		
		 if(shortestpath==null)
     
             JOptionPane.showMessageDialog(this, "There Is No Path Between This Two Points ", "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
		 else {
			 String path="{";
			 for (int i=0;i<shortestpath.size();i++)
				 if (i!=shortestpath.size()-1)
					 path+=shortestpath.get(i).getKey()+",";
				 else
					 path+=shortestpath.get(i).getKey();
			 path+="}";
			 JOptionPane.showMessageDialog(this, "The Shortest Path Between The Two Points Is:"+path);
		repaint();
		 }
	}catch(Exception e)
	{
		JOptionPane.showMessageDialog(this, "ERR :"+e.getMessage());
	}
}

@Override
public void mouseClicked(MouseEvent e) {
	// TODO Auto-generated method stub
	System.out.println("mouseClicked");
}
@Override
public void mouseEntered(MouseEvent e) {
	// TODO Auto-generated method stub
	System.out.println("mouseEntered");	
}
@Override
public void mouseExited(MouseEvent e) {
	// TODO Auto-generated method stub
	System.out.println("mouseExited");
}
@Override

public void mousePressed(MouseEvent e) 
{
	String str_key = JOptionPane.showInputDialog(this, "Please insert node key");
	try 
	{
		int key =Integer.parseInt(str_key);
		int x = e.getX();
		int y = e.getY();
		System.out.println(x);
		System.out.println(y);
		Point3D p = new Point3D(x,y);
		//int key=Integer.parseInt(str_key);
		graph.addNode(new node(key,p));
		JOptionPane.showMessageDialog(this, "Add Node succed");
		repaint();
	}
	catch (Exception e1)
	{
		JOptionPane.showMessageDialog(this," ERR: "+e1.getMessage());
	}


}
/*
public void mousePressed(MouseEvent e) {
	// TODO Auto-generated method stub
	
	int x = e.getX();
	int y = e.getY();
	Point3D p = new Point3D(x,y);
	node_data s=new node();
	s.setLocation(p);
	K.addNode(s);
	
	
	
	
	repaint();
	
>>>>>>> afba0ad1d00687c44621f0e708dc77a0bea49e1f

	System.out.println("mousePressed");
}
*/
@Override
public void mouseReleased(MouseEvent e) {
	// TODO Auto-generated method stub
	System.out.println("mouseReleased");
}

/**public static graph nodesFactory() {
	graph d=new DGraph();
	d.addNode(new node(2,new Point3D(250,100)));
	d.addNode(new node(1,new Point3D(50,250)));
	d.addNode(new node(3,new Point3D(255,280)));
	d.addNode(new node(4,new Point3D(500,500)));
	d.addNode(new node(5,new Point3D(700,450)));
	 d.connect(1, 2, 5);
	 d.connect(1, 3, 4);
	 d.connect(1, 4, 7);
	 d.connect(2, 5, 2);
	 d.connect(4, 5, 3);
	 d.connect(3, 4, 1.5);
<<<<<<< HEAD
	graph_algorithms  g=new Graph_Algo();
	g.init(d);
	//System.out.println(g.isConnected());
	 

=======
	 
>>>>>>> 9acd3493fd39d1d4dacb90970d8eb4f378372abc
	 return d;
}*/
}
