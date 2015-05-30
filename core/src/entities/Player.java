package entities;

import handlers.GameMap;

import java.util.Hashtable;
import java.util.LinkedList;

import network.RequestSender;

import common.Equipment;
import common.MapObjectType;
import common.Request;
import common.RequestType;
import common.Resource;
import common.Tile;

public class Player{
	private int hp;
	private Hashtable<Resource,Integer> inventory;
	private LinkedList<Equipment> eq;
	private GameMap map;
	private RequestSender sender;
	//....
	
	public Player(GameMap map, RequestSender sender){
		this.map = map;
		this.sender = sender;
		inventory = new Hashtable<Resource, Integer>();
		eq = new LinkedList<Equipment>();
		hp = -1;
	}
	
	public void act(Tile tile){
		
	}
	
	public void moveOrAct(Tile tile){
		
	}
	
	public void dropItem(Tile tile,Resource res){
		
	}
	
	public void Move(Tile tile){
		if(tile == null){
			Request req = new Request(RequestType.TILE , tile);
			sender.sendRequest(req);
			return;
		}
		
	}
	
	private void PickUp(MapObjectType obj){
		Request req = new Request();
		sender.sendRequest(req);
	}
	
	/*
	public void MapAction(Direction dir){
		Tile tile = lookInDirection(dir);
		if(tile == null)
			return;
		if(tile.canMoveOn()){
			if(tile.canPickUp()){
				//here we need to send pick up action
				System.out.println("im picking things up");
				tile.setMapObjectType(null);
			}
			moveCenter(dir);
		}
		else if (tile.canAttack()){
			//here we need to send attack command
			System.out.println("dieeeee you monster");
		}
		else if (tile.canHarvest()){
			//here we need to send harvest command
			System.out.println("im a lousy farmer");
		}
	}
	*/
}
