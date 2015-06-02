package entities;

import handlers.GameMap;

import java.util.Hashtable;
import java.util.LinkedList;

import network.RequestSender;

import common.Coordinate;
import common.Equipment;
import common.Request;
import common.RequestType;
import common.Resource;
import common.Tile;

public class Player{
	private int currentHp;
	private int maxHp;
	private Hashtable<Resource,Integer> inventory;
	private LinkedList<Equipment> eq;
	private GameMap map;
	private RequestSender sender;
	private Coordinate loc;
	
	public Player(GameMap map){
		this(null ,map);
	}
	
	public Player(RequestSender sender, GameMap map){
		this.map = map;
		this.sender = sender;
		inventory = null;
		eq = null;
		maxHp = 10;
		currentHp = -1;
	}
	
	public int getMaxHp(){ return maxHp;}
	
	public boolean isInitialized(){
		return   currentHp != -1 && inventory != null && eq != null && loc != null;
	}
	
	public void setMap(GameMap map){ this.map = map;}
	public void setRequestSender(RequestSender sender){ this.sender = sender;}
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
					sender.sendRequest(req);
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
				sender.sendRequest(req);
				return true;
			}
			else if (tile.canHarvest()){
				Request req = new Request(RequestType.HARVEST , tile.getCoordinate());
				sender.sendRequest(req);
				return true;
			}
		}	
		return false;
	}
	
	//TODO
	public void addResource(Resource res){}
	//TODO
	public void setInventory(Hashtable<Resource,Integer> inven){ this.inventory = inven;}
	//TODO
	public void addEquipment(Equipment eq){}
	//TODO
	public void setEquipments(LinkedList<Equipment> eq){ this.eq = eq;}
}
