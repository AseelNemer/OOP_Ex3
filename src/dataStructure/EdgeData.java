package dataStructure;

import utils.Point3D;

public class EdgeData implements edge_data {
	 node_data src;
	 node_data dest;
	 double weight;
	 String info;
	 int tag;
	  
	 public EdgeData()
	 {
		 this.src=new node();
		 this.dest=new node();
		 this.weight=0;
		 this.info="";
		 this.tag=1;
	 }

	 public EdgeData(node_data s,node_data d,double w,int t)
	 {
		 this.src=s;
		 this.dest=d;
		 this.weight=w;
		 this.tag=t;
		 this.info="";
		 
	 }
	 public EdgeData(EdgeData e)
		{
			this(e.src,e.dest,e.weight,e.tag);
			this.info = e.info;
		}
	
	@Override
	public int getSrc() {
		// TODO Auto-generated method stub
		return this.src.getKey();
	}

	@Override
	public int getDest() {
		// TODO Auto-generated method stub
		
		return this.dest.getKey();

		
	}

	@Override
	public double getWeight() {
		// TODO Auto-generated method stub
		return this.weight;
	}

	@Override
	public String getInfo() {
		// TODO Auto-generated method stub
		return this.info;
	}

	@Override
	public void setInfo(String s) {
		// TODO Auto-generated method stub
		this.info=new String(s);
	}

	@Override
	public int getTag() {
		// TODO Auto-generated method stub
		return this.tag;
	}

	@Override
	public void setTag(int t) {
		// TODO Auto-generated method stub
		this.tag=t;
	}
    public String toString() {
    	String ans="("+src+","+dest+")";
    	return ans;
    }

}
