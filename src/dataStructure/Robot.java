package dataStructure;

import org.json.JSONException;
import org.json.JSONObject;

import utils.Point3D;

public class Robot {
	
	int src;
	Point3D pos;
	int id;
	int dest;
	double value;
	double speed;
	
	
	public Robot(int src, int id, Point3D pos, int dest, double value) 
	{
		this.src = src;
		this.id = id;
		this.pos = pos;
		this.dest = dest;
		this.value = value;
	}
	public void init(String JASON) {
		 try {
			JSONObject line = new JSONObject(JASON);
			JSONObject ttt = line.getJSONObject("Robot");
			this.id = ttt.getInt("id");
			this.src = ttt.getInt("src");
			this.dest = ttt.getInt("dest");
			Point3D p=new Point3D(ttt.getString("pos"));
			this.pos=p;
		 }catch(JSONException e) {e.printStackTrace();
		 
		 }
	}
	/**
	 * return the source of this robot 
	 * @return
	 */
	public int getsrc()
	{
		return this.src;
	}
	
	/**
	 * 	 return the dest of this robot 
	 * @return
	 */
	public int getdest()
	{
		return this.dest;
	}
	
	/**
	 * 	 return the ID of this robot 
	 * @return
	 */
	public int getid()
	{
		return this.id;
	}
	
	/**
	 * 	 return the Positions of this robot 
	 * @return
	 */
	public Point3D getpos()
	{
		return this.pos;
	}
	
	/**
	 * 	 return the value of this robot 
	 * @return
	 */
	public double getvalue()
	{
		return this.value;
	}
}
