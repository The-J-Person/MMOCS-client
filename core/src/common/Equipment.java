package common;

import java.io.Serializable;

public class Equipment implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 64453;
	int ID;
	String Name;
	String Image;
	
	public String toString()
	{
		return Integer.toString(ID) + " " + Name + " " + Image;
	}
	
	public Equipment(int ID, String Name, String Image)
	{
		this.ID = ID;
		this.Name = Name;
		this.Image = Image;
	}
	
	public int getID()
	{
		return this.ID;
	}
	
	public String getName()
	{
		return this.Name;
	}
	
	public String getImage()
	{
		return this.Image;
	}
	
	public void setID(int ID)
	{
		this.ID = ID;
	}
	
	public void setName(String Name)
	{
		this.Name = Name;
	}
	
	public void setImage(String Image)
	{
		this.Image = Image;
	}
}
