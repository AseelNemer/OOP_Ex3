package algorithms;

import java.util.*;
import java.util.Stack;
import java.util.concurrent.TimeoutException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import dataStructure.DGraph;
import dataStructure.EdgeData;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node;
import dataStructure.node_data;
import gui.Graph_GUI;
import jdk.nashorn.internal.parser.JSONParser;
import utils.Point3D;
/**
 * This empty class represents the set of graph-theory algorithms
 * which should be implemented as part of Ex2 - Do edit this class.
 * @author 
 *
 */
public class Graph_Algo implements graph_algorithms,Serializable{
private graph D=new DGraph();

public Graph_Algo() 
{
	D=new DGraph();

}
	public Graph_Algo(graph g) 
	{
		D=new DGraph();
		this.init(g);
	}
	
	
	@Override
	public void init(graph g) {
		// TODO Auto-generated method stub
		 D=new DGraph();
		Iterator<node_data> itr=g.getV().iterator();
		node_data x=new node();
		while(itr.hasNext()) 
		{
			x=itr.next();
			D.addNode(x);
			
		}
		 itr=g.getV().iterator();
		 x=new node();
		while(itr.hasNext()) 
		{
			x=itr.next();
			
				Iterator<edge_data> etr=g.getE(x.getKey()).iterator();
				
				while(etr.hasNext())
				{
					edge_data r=etr.next();
					D.connect(r.getSrc(), r.getDest(), r.getWeight());
				}
		}
	}
	
	@Override
	public void init(String JSONfile) {
	/*
		try 
		{
			FileInputStream file =new FileInputStream(file_name);
			ObjectInputStream object=new ObjectInputStream(file);
			this.D= (graph) object.readObject();
			
			object.close();
			file.close();
		}
        catch(IOException ex) 
        { 
            System.out.println("IOException is caught"); 
        } 
          
        catch(ClassNotFoundException ex) 
        { 
            System.out.println("ClassNotFoundException is caught"); 
        } 
        */

		try 
		{	
	
		JSONObject read=new JSONObject (JSONfile);
			JSONArray edges=(JSONArray) read.get("Edges");
			JSONArray nodes=(JSONArray) read.get("Nodes");
			for(int i=0;i<nodes.length();i++) {
				JSONObject jsonobject=(JSONObject) nodes.get(i);
				
				int id=(int) jsonobject.get("id");
				String S=jsonobject.getString("pos");
				Point3D p=new Point3D(S);
				node n=new node(id,p);
				D.addNode(n);
			}
			for(int i=0;i<edges.length();i++) {
				JSONObject jsonobject=(JSONObject) edges.get(i);
			int src,dest;
			double w;
			src=(int) jsonobject.get("src");
       dest=(int) jsonobject.get("dest");
       w=(double) jsonobject.getDouble("w");
       D.connect(src, dest, w);
			}
			
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
	}

	@Override
	public void save(String file_name) {
		// TODO Auto-generated method stub
		try 
		{
			FileOutputStream file =new FileOutputStream(file_name);
			ObjectOutputStream object=new ObjectOutputStream(file);
			object.writeObject(D);
			object.close();
			file.close();
			System.out.println("Graph has been serialized"); 
			
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
  
	}

	@Override
	public boolean isConnected() {
		if(this.D==null)
			return false;
		boolean flag=true;
		int nodesize =0;
		setTag(D);
		Iterator<node_data> nodes=D.getV().iterator();
		while(nodes.hasNext())
		{
			node_data node=nodes.next();
			Iterator<edge_data> edges=D.getE(node.getKey()).iterator();
		
			while(edges.hasNext())
			{
				edge_data edge=edges.next();
				node_data src=D.getNode(edge.getSrc());
				node_data dest=D.getNode(edge.getDest());
				
				if(dest.getTag()==0)
				{
					nodesize++;
					dest.setTag(1);
				}
				
			}
		
		
		}
		if(nodesize!=D.nodeSize())
			flag=false;
		return flag;
	}

	@Override
	public double shortestPathDist(int src, int dest) {

		//check if this src and this dest is in the graph
		if(D.getNode(src)==null)  throw new IllegalArgumentException("the src you entered doesn't exist in this graph " +src);
		if(D.getNode(dest)==null) throw new IllegalArgumentException("the dest you entered doesn't exist in this graph" +dest);
		
		//set all the nodes tag to 0
		setTag(D);
		
		//set all the node weight to infinity
		set_weight_inf(D,src);
	
		RecursiveShortPath(src);
		return D.getNode(dest).getWeight();

	}

	@Override
	public List<node_data> shortestPath(int src, int dest) {
	LinkedList<node_data> list=new LinkedList<node_data>();
		try {
	
		node_data node=new node();
		if(shortestPathDist( src, dest)>=0)
		{
			node=D.getNode(dest);
			int temp;
			while (node!=D.getNode(src))
			{
				list.addFirst(node);
				temp= Integer.parseInt(node.getInfo());
				node=D.getNode(temp);
			}
		}
		list.addFirst(D.getNode(src));
		
		return list;

	}catch(IllegalArgumentException e) {
		System.out.println(e.getMessage());
	}
return list ;
	}

	@Override
	public List<node_data> TSP(List<Integer> targets) {
		// TODO Auto-generated method stub
		List<node_data> ans=new LinkedList<node_data>();
        List<Integer> target =new ArrayList<Integer>(targets);
        for(int i: target) {
        	if(shortestPath(i, i+1)==null) throw new RuntimeException ("The Graph isnotconnected there is two targets without road");
        	if(target.size()==1) break;
        	ans.addAll(shortestPath(i,i+1));
        	Iterator<node_data> itr=ans.iterator();
        	while(itr.hasNext()) {
        		node_data node=itr.next();
        		if(target.contains(node.getKey()) && target.get(i+1)!=node.getKey()) {
        			target.remove(node.getKey());
        		};
        	}
        }
		return ans;
	}

	@Override
	public graph copy() {
		graph g=new DGraph();
		Iterator<node_data> nodes=D.getV().iterator();
		while(nodes.hasNext())
		{
			node_data node=nodes.next();
			Collection<edge_data> edges=D.getE(node.getKey());
			Iterator<edge_data> edgeitr=edges.iterator();
			if(!g.getV().contains(node))
				g.addNode(node);
			while(edgeitr.hasNext())
			{
				edge_data e=edgeitr.next();
				if(!g.getV().contains(D.getNode(e.getDest())))
					g.addNode(D.getNode(e.getDest()));
				g.connect(node.getKey(), e.getDest(),e.getWeight() );
			}
			
		}
		return g;
	}
	private void setTag(graph g)
	{
		node_data n=new node();
		Iterator<node_data> itr=g.getV().iterator();
		
		while(itr.hasNext())
		{
			
			n=itr.next();
			
			g.getNode(n.getKey()).setTag(0);
		}
	}
	private void set_weight_inf(graph g,int src)
	{
		node_data n=new node();
		Iterator<node_data> itr=g.getV().iterator();
		
		while(itr.hasNext())
		{
			n=itr.next();
			g.getNode(n.getKey()).setWeight(Double.MAX_VALUE);
			g.getNode(n.getKey()).setInfo("");
		}
		g.getNode(src).setWeight(0);
	}
	
	
private void RecursiveShortPath(int src) {
		Iterator<edge_data> itr=D.getE(src).iterator();
		
		while(itr.hasNext())
		{
				edge_data ed=itr.next();
				double v=D.getNode(src).getWeight()+ed.getWeight();
			
				if(D.getNode(ed.getDest()).getTag()==0) 
				{
					D.getNode(ed.getDest()).setWeight(v);
					D.getNode(ed.getDest()).setTag(1);
					RecursiveShortPath(ed.getDest());
					D.getNode(ed.getDest()).setInfo(Integer.toString(ed.getSrc()));
			
				}
				else 
				{
					if(v<D.getNode(ed.getDest()).getWeight()) {
						D.getNode(ed.getDest()).setWeight(v);
						RecursiveShortPath(ed.getDest());
						D.getNode(ed.getDest()).setInfo(Integer.toString(ed.getSrc()));
				
					}
		
				}
			}
		}
	
	



	

	
		



	
}