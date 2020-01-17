package dataStructure;

import java.util.Comparator;


import dataStructure.Fruit;
public  class Fruit_Comperator implements Comparator<Fruit> {
	public Fruit_Comperator() {;}
	
	
	@Override
	public int compare(Fruit f1, Fruit f2) {
		int res=0;
    	if(f1.getValue()-f2.getValue()<0) return 1;
    		else if(f1.getValue()-f2.getValue()>0) return -1;
    	return 0;//there value are equal****
	}
}
