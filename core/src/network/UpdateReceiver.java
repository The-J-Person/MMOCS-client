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
				up = new Update();
				up = (Update)stream.readObject();
				System.out.println(up.getType());
				if(up.getType() == UpdateType.TILE){
					Tile tile = (Tile)up.getData();
					System.out.println(tile.getCoordinate().X()+","+tile.getCoordinate().Y() + ":" +tile.getFloorType() + "," + tile.getMapObjectType());

				}
				
				synchronized(updates){
					updates.addLast(up);
				}
			}
			catch(Exception e){
				System.out.println("i died");
				System.out.println(e.getMessage());
				break;
				
			}
		}
		System.out.println("Thread stopped");
	}
}
