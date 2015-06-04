package utility;

import common.Resource;

//this class is purely because of limitations...
public class Resources {
	private Resource res;
	private int amount;
	
	public Resources(Resource res,int amount){
		this.res = res;
		this.amount = amount;
	}
	
	public Resource getResource(){return res;}
	public int getAmount(){return amount;}
	
	public String toString(){
		String str;
		str = res.name();
		str = str.toLowerCase() + ": " + amount;
		return str;
	}
	
	
	
	
}
