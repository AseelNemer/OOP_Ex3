package dataStructure;

import utils.Point3D;

public class node implements node_data{
 private  int key;
 private Point3D location;
 private double weight;
 private String info;
 private int tag;
 public static int id = 0;
 public node()
 {
	 this.key = id+1;
	 setLocation (new Point3D((double)this.getKey(),(double)this.getKey()));
	 this.weight=(Double.MAX_VALUE);
	 this.info=null;
	 this.tag=0;
	 id++;
 }
 public node(node_data n)
 {
	 this.key=n.getKey();
     setWeight(n.getWeight());
     setTag(n.getTag());
     setInfo(n.getInfo());
     
 }
 public node(Point3D p) {
	 	this.key = id+1;
		this.weight = 0;
		this.location = p;
		this.info = "";
		this.tag = 0;
		id=id+1;
	}
 public node(int key,Point3D location) {
		this.key = key;
		if(location != null)
			this.location = new Point3D(location);
		else
			this.location = null;
		this.weight = 0;
		this.info= "";
		this.tag = 0;
	}
 
 
 public node(int key,Point3D location,double weight,int tag) {
		this.key = key;
		if(location != null)
			this.location = new Point3D(location);
		else
			this.location = null;
	this.weight = weight;
	this.tag = tag;
	this.info = "";
}
	@Override
	public int getKey() {
		// TODO Auto-generated method stub
		return this.key;
	}

	@Override
	public Point3D getLocation() {
		// TODO Auto-generated method stub
		return this.location;
	}

	@Override
	public void setLocation(Point3D p) {
		// TODO Auto-generated method stub
		this.location=p;
	}

	@Override
	public double getWeight() {
		// TODO Auto-generated method stub
		return this.weight;
	}

	@Override
	public void setWeight(double w) {
		// TODO Auto-generated method stub
		this.weight=w;
	}

	@Override
	public String getInfo() {
		// TODO Auto-generated method stub
		return this.info;
	}

	@Override
	public void setInfo(String s) {
		// TODO Auto-generated method stub
		if (s==null)
			return;
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
	@Override
	public String toString() {
		if(this.getLocation() != null)
			return ("[Key: " + key + ", Location: (" + location.toString() + ")]");
		else
			return ("[Key: " + key + ", Location: null]");
	}
public static void main(String[] args) {
	Point3D p=new Point3D(2,3);
	node a=new node(3,p);
	System.out.println(a);
	
}
}
