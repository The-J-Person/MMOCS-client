package network;

import handlers.GameMap;

import java.io.ObjectInputStream;

import common.Tile;
import common.Update;

import entities.Player;

public class UpdateReceiver extends Thread {
	private volatile boolean stop;
	private ObjectInputStream stream;
	private GameMap map;
	private Player player;
	//should add some object for acks
	
	public UpdateReceiver(ObjectInputStream stream, GameMap map , Player player){
		this.stream = stream;
		this.map = map;
		this.player = player;
		stop = false;
	}
	
	public void setStop(boolean state){ stop = state; }
	
	public void run(){
		while(!stop){
			try{
				Update up = (Update)stream.readObject();
				switch(up.getType()){
				case TILE: 
					map.update((Tile)up.getData());
					break;
				case ACKNOWLEDGMENT:
					//here i should make some ack queue
					break;
				case HIT_POINTS: 
					//update the hitpoints of the player
					break;
				case RESOURCES: 
					//update the resoureces of the player
					break;
				case INVENTORY: 
					//update the inventory of the player
					break;
				case COORDINATE: 
					//this is pointlesss, it may be used still so i keep it here
					break;
				default: break;
				
				}
			}
			catch(Exception e){
				System.out.println(e.getMessage());
			}
		}
	}
}
