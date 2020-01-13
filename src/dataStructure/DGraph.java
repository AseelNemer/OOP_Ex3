package dataStructure;
import java.util.HashMap;

import java.util.Iterator;

import gui.Graph_GUI;

import java.util.Collection;

public class DGraph implements graph{
	
private HashMap<Integer ,node_data> nodes=new HashMap<Integer,node_data>();
private HashMap<Integer ,HashMap<Integer,edge_data>> edges=new HashMap<Integer, HashMap<Integer,edge_data>>();
private int MC=0;
private int num_of_edges=0;

public DGraph()
{
	this.nodes=new HashMap<Integer, node_data>();
	this.edges=new HashMap<Integer, HashMap<Integer,edge_data>>();
	this.MC=0;
	this.num_of_edges=0;
}

	@Override
	public node_data getNode(int key) {
		
		return nodes.get(key);
	}

	@Override
	public edge_data getEdge(int src, int dest) {
		
		return edges.get(src).get(dest);
	}

	@Override
	public void addNode(node_data n) {

		if(n==null) throw new RuntimeException("Invalid input");

		if(nodes.containsKey(n.getKey())) {
			throw new RuntimeException ("this node key is already availabe in V "+n.getKey());
		}

		nodes.put(n.getKey(), n);
		edges.put(n.getKey(),new HashMap<Integer,edge_data>());
		MC++;
		
	}

	@Override
	public void connect(int src, int dest, double w) {
		//if edges contain this src
		if(nodes.containsKey(src) && nodes.containsKey(dest) && src!=dest) 
		{
			if(edges.containsKey(src)) {
				if(edges.get(src).containsKey(dest)) {
					
			HashMap<Integer,edge_data> f=new HashMap(edges.get(src));
			edge_data edge=new EdgeData(this.getNode(src),this.getNode(dest),w,0);
			f.replace(dest,edge );
			edges.replace(src, f);
				}
				else {
					HashMap<Integer,edge_data> f=new HashMap(edges.get(src));
					edge_data edge=new EdgeData(this.getNode(src),this.getNode(dest),w,0);
					f.put(dest, edge);
					edges.replace(src, f);
				}
		}	else  //if edges dosn't contain this src
		{
			HashMap<Integer,edge_data> f=new HashMap<Integer,edge_data>();
			f.put(dest,(edge_data) new EdgeData(this.getNode(src),this.getNode(dest),w,0));
			edges.put(src, f);
			
			
		}
			
			MC++;//we made changes
		num_of_edges++;//we added a edge
		}
		
	
		
	}

	@Override
	public Collection<node_data> getV() {
		// TODO Auto-generated method stub
		
		return nodes.values();
	}

	@Override
	public Collection<edge_data> getE(int node_id) {
		// TODO Auto-generated method stub	
		
		return  edges.get(node_id).values();	

	
	}

	@Override
	public node_data removeNode(int key) {
		// TODO Auto-generated method stub
		node_data remove=new node();
		//delete this key from the nodes hashmap
		
		if(nodes.containsKey(key))
		{
			//We made changes
			MC++;
			remove=nodes.get(key);
			this.nodes.remove(key);	
		//delete this key from edges(dest)
		Collection<node_data> n =nodes.values();
		Iterator<node_data> itr = n.iterator();
		while(itr.hasNext()) 
		{
			node_data node=itr.next();
			if (edges.get(node.getKey()).get(key)!=null)
				{
					edges.get(node.getKey()).remove(key);
					num_of_edges--;
				}
		}
		//delete this key from egdes(src)
		if (edges.containsKey(key))
			edges.remove(key);
		}
		else
			throw new IllegalArgumentException("This Key Does  Not Exist In Graph");
		return remove;
	}
	@Override
	public edge_data removeEdge(int src, int dest) {
		// TODO Auto-generated method stub
		
		edge_data remove_edge=edges.get(src).remove(dest);
		if(remove_edge!=null)//check if there dest is in edges hashmap
		{
			num_of_edges--;
			MC++;//We made changes
		}
		return remove_edge;
	}

	@Override
	public int nodeSize() {
		// TODO Auto-generated method stub
		return nodes.size();
	}

	@Override
	public int edgeSize() {
		// TODO Auto-generated method stub
		/**Iterator<Integer> itr=edges.keySet().iterator();
		int V=0;
		int i;
		while(itr.hasNext()) {
			i=itr.next();
			V+=edges.get(i).size();
		}
		return V;*/
		return num_of_edges;
	}

	@Override
	public int getMC() {
		return MC;
	}
	public static void main(String[] args){
//graph D=Graph_GUI.nodesFactory();
		}
}
