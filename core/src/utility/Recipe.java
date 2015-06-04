package utility;

import java.util.Hashtable;
import java.util.Iterator;

import common.Resource;

public class Recipe {
	private Hashtable<Resource, Integer> recipe;
	private Resource product;
	
	public Recipe(Resource res){
		this.product = res;
		this.recipe = res.recipe();	
	}
	
	public Resource getProduct(){return product;}
	public Hashtable<Resource, Integer> getRecipe(){return recipe;}
	
	public String toString(){
		String str = "";
		Resource res;
		Iterator<Resource> iter = recipe.keySet().iterator();
		while(iter.hasNext()){
			res = iter.next();
			str = str + recipe.get(res) +" " + res.name();
			if(iter.hasNext())
				str += ", ";
		}
		str = str.toLowerCase();
		str = product.name() + ": " + str;
		return str;
	}
	
	
}
