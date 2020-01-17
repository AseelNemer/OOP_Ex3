package dataStructure;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import utils.Point3D;

	public class Fruit 
	{
		private double value ;
		private int type;
		private Point3D p;
	public Fruit() 
	{
			this.value=0.0;
			this.type=1;
			this.p=new Point3D(0,0,0);
	}
	
	public Fruit(double v,int type,Point3D p)
	{
		this.value=v;
		this.p=p;
		this.type=type;
	}
	
	
	public void init(String JSONfile) {

		InputStream fis;
		JSONObject read;
		try {

			 read=new JSONObject(JSONfile);
			 JSONObject ttt = read.getJSONObject("Fruit");
			this.value=(double) ttt.get("value");
			this.type=(int) ttt.get("type");
			String s= (String) ttt.get("pos");
			this.p=new Point3D(s);
			
		}catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	
	public double getValue() 
	{
		return (double)this.value;
	}
	
	
	public int getType() 
	{
		return this.type;
	}
	
	
	public Point3D getPOS() 
	{
		return this.p;
	}
	
	
}