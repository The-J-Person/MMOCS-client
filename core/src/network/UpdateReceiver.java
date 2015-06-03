package network;

import handlers.GameMap;
import handlers.GameStateManager;
import handlers.MyDialog;

import java.io.ObjectInputStream;
import java.util.Hashtable;
import java.util.LinkedList;

import MMOCS.game.MMOCSClient;

import common.Acknowledgement;
import common.Coordinate;
import common.Request;
import common.RequestType;
import common.Resource;
import common.Tile;
import common.Update;
import common.UpdateType;

import entities.Player;

public class UpdateReceiver extends Thread {
	private static volatile boolean stop;
	LinkedList<Update> updates;
	private ObjectInputStream stream;
//	private GameMap map;
//	private Player player;
//	private GameStateManager gsm;
//	private RequestSender sender;
//	private Connection con;
	
	public UpdateReceiver(ObjectInputStream stream, LinkedList<Update> updates){
//		this.con = con;
		this.stream = stream;
//		this.map = game.getMap();
//		this.player = game.getPlayer();
//		this.sender = con.getRequestSender();
//		this.gsm = game.getGSM();
		this.updates = updates;
		stop = false;
	}
	
	public static void setStop(boolean state){ stop = state; }
	
	public void run(){
		Update up;
		while(!stop){
			try{
				up = (Update)stream.readObject();
				System.out.println(up.getType());
				if(up.getType() == UpdateType.TILE){
					Tile tile = (Tile)up.getData();
					System.out.println(tile.getCoordinate().X()+","+tile.getCoordinate().Y() + ":" +tile.getFloorType() + "," + tile.getMapObjectType());

				}
				
//				Thread.sleep(5000); // ONLY MEANT FOR TESTING MENUS!!!
				
				synchronized(updates){
					updates.addLast(up);
				}
//				switch(up.getType()){
//				case TILE: 
//					map.update((Tile)up.getData());
//					break;
//				case ACKNOWLEDGMENT:
//					handleAck((Acknowledgement)up.getData());
//					break;
//				case HIT_POINTS: 
//					player.setCurrentHp((Integer)up.getData());
//					break;
//				case RESOURCES: 
//					player.addResource((Resource)up.getData());
//					break;
//				case INVENTORY: 
//					player.setInventory((Hashtable<Resource,Integer>) up.getData());
//					break;
//				case EQUIPMENT: 
//					player.addEquipment((Equipment) up.getData());
//					break;
//				case EQUIPMENT_INVENTORY: 
//					player.setEquipments((LinkedList<Equipment>) up.getData());
//					break;
//				case COORDINATE: 
//					//right only one coordinate is meant to initialize the center of the map
//					map.setCenter((Coordinate)up.getData());
//					for(Coordinate cor : map.missingTiles()){
//						sender.sendRequest(new Request(RequestType.TILE , cor));
//					}
//					break;
//				default: break;
//				
//				}
			}
			catch(Exception e){
				System.out.println("i died");
				System.out.println(e.getMessage());
				break;
				
			}
		}
		System.out.println("Thread stopped");
	}
	
//	private void handleAck(Acknowledgement ack){
//		Request req = sender.requestToAck();
//		switch(ack.getRequestType()){
//		case MOVE:
//			//send request for previous and current tiles, also maybe cooldown on move
//			map.getTile(map.getMiddleX(), map.getMiddleY()).setMapObjectType(null);
//			sender.sendRequest(new Request(RequestType.TILE , map.getCenter()));
//			sender.sendRequest(new Request(RequestType.TILE , req.getData()));
//			map.MoveCenter(map.getDirection((Coordinate)req.getData()));
//			for(Coordinate cor : map.missingTiles()){
//				sender.sendRequest(new Request(RequestType.TILE , cor));
//			}
//			break;
//		case ATTACK:
//			//maybe visuals for attacks, also maybe cooldown on attack
//			break;
//		case HARVEST:
//			//maybe visuals for attacks, also maybe cooldown on attack
//			break;
//		case LOG_IN:
//			gsm.pushState(GameStateManager.PLAY);
//			break;
//		case CONFIRM:
//			if(ack.getAck()){
//				System.out.println("you confirmed successfully");
//			}
//			else{
//				System.out.println("you sent it good and got 'no' answer ");
//			}
//			con.close();
//			break;
//		case REGISTER:
//			if(ack.getAck()){
//				MyDialog dia = new MyDialog("Success" , "You registered sucessfully please check your E-mail to confirm your account");
//				dia.show(gsm.peek().getInputProcessor());
//			}
//			else{
//				MyDialog dia = new MyDialog("Failure" , "Failed to register, please check the details you entered");
//				dia.show(gsm.peek().getInputProcessor());
//			}
//			con.close();
//			break;
//		case CRAFT:
//			break;
//		default : break;
//		}
//		sender.setProcessing(false);
//	}
}
