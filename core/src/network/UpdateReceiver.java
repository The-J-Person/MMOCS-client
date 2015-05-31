package network;

import handlers.GameMap;

import java.io.ObjectInputStream;
import java.util.Hashtable;
import java.util.LinkedList;

import common.Acknowledgement;
import common.Coordinate;
import common.Equipment;
import common.Request;
import common.RequestType;
import common.Resource;
import common.Tile;
import common.Update;

import entities.Player;

public class UpdateReceiver extends Thread {
	private static volatile boolean stop;
	private ObjectInputStream stream;
	private GameMap map;
	private Player player;
	private RequestSender sender;
	
	public UpdateReceiver(ObjectInputStream stream, GameMap map , Player player , RequestSender sender){
		this.stream = stream;
		this.map = map;
		this.player = player;
		this.sender = sender;
		stop = false;
	}
	
	public static void setStop(boolean state){ stop = state; }
	
	public void run(){
		Update up;
		while(!stop){
			try{
				up = (Update)stream.readObject();
				switch(up.getType()){
				case TILE: 
					map.update((Tile)up.getData());
					break;
				case ACKNOWLEDGEMENT:
					handleAck((Acknowledgement)up.getData());
					break;
				case HIT_POINTS: 
					player.setCurrentHp((Integer)up.getData());
					break;
				case RESOURCES: 
					player.addResource((Resource)up.getData());
					break;
				case INVENTORY: 
					player.setInventory((Hashtable<Resource,Integer>) up.getData());
					break;
				case EQUIPMENT: 
					player.addEquipment((Equipment) up.getData());
					break;
				case EQUIPMENT_INVENTORY: 
					player.setEquipments((LinkedList<Equipment>) up.getData());
					break;
				case COORDINATE: 
					//right only one coordinate is meant to initialize the center of the map
					map.setCenter((Coordinate)up.getData());
					break;
				default: break;
				
				}
			}
			catch(Exception e){
				System.out.println(e.getMessage());
			}
		}
	}
	
	private void handleAck(Acknowledgement ack){
		Request req = sender.requestToAck();
		switch(ack.getRequestType()){
		case MOVE:
			//send request for previous and current tiles, also maybe cooldown on move
			sender.sendRequest(new Request(RequestType.TILE , map.getCenter()));
			sender.sendRequest(new Request(RequestType.TILE , req.getData()));
			map.MoveCenter(map.getDirection((Coordinate)req.getData()));
			for(Coordinate cor : map.missingTiles()){
				sender.sendRequest(new Request(RequestType.TILE , cor));
			}
			break;
		case ATTACK:
			//maybe visuals for attacks, also maybe cooldown on attack
			break;
		case HARVEST:
			//maybe visuals for attacks, also maybe cooldown on attack
			break;
		case LOG_IN:
			//allow entering the Play state
			break;
		case CONFIRM:
			//display success message or failure
			break;
		case REGISTER:
			//display success message or failure
			break;
		case CRAFT:
			break;
		default : break;
		}
		sender.setProcessing(false);
	}
}
