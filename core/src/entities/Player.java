package entities;

import handlers.GameMap;

import java.util.Hashtable;

import network.Connection;
import network.RequestSender;

import common.Coordinate;
import common.Request;
import common.RequestType;
import common.Resource;
import common.Tile;

public class Player{
	private int currentHp;
	private int maxHp;
	private Hashtable<Resource,Integer> inventory;
	private GameMap map;
	private Connection con;
	private Coordinate loc;
	
	public Player(Connection con, GameMap map){
		this.map = map;
		this.con = con;
		inventory = null;
		maxHp = 10;
		currentHp = -1;
	}
	
	public int getMaxHp(){ return maxHp;}
	
	public boolean isInitialized(){
		return   currentHp != -1 && inventory != null &&  loc != null;
	}
	
	public int getCurrentHp(){ return currentHp; }
	public void setCurrentHp(int value){ currentHp = value; }
	public void setLocation(Coordinate cor){ loc = cor; }
	public Coordinate getLocation() {return loc;}
	
	public boolean act(Tile tile){
		if(move(tile)){
			return true;
		}
		if(harvest(tile)){
			return true;
		}
		return false;
	}
	
	public boolean move(Tile tile){
		if(tile != null){
			if(map.isNearby(tile.getCoordinate())){
				if(tile.canMoveOn()){
					Request req = new Request(RequestType.MOVE , tile.getCoordinate());
					con.sendRequest(req);
					return true;
				}
			}
		}	
		return false;
	}
	
	public boolean harvest(Tile tile){
		if(tile != null){
			if(tile.canAttack()){
				Request req = new Request(RequestType.ATTACK , tile.getCoordinate());
				con.sendRequest(req);
				return true;
			}
			else if (tile.canHarvest()){
				Request req = new Request(RequestType.HARVEST , tile.getCoordinate());
				con.sendRequest(req);
				return true;
			}
		}	
		return false;
	}
	
	//TODO
	public void addResource(Resource res){
		if(inventory.get(res) != null){
			inventory.put(res, inventory.get(res) + 1);
		}
		inventory.put(res, 1);
	}
	//TODO
	public void setInventory(Hashtable<Resource,Integer> inven){ this.inventory = inven;}


}
