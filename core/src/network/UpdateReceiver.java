package network;

import handlers.GameMap;

import java.io.ObjectInputStream;
import java.util.Stack;

import common.Acknowledgement;
import common.Tile;
import common.Update;

import entities.Player;

public class UpdateReceiver extends Thread {
	private static volatile boolean stop;
	private ObjectInputStream stream;
	private GameMap map;
	private Player player;
	private Stack<Acknowledgement> ack;
	//should add some object for acks
	
	public UpdateReceiver(ObjectInputStream stream, GameMap map , Player player){
		this.stream = stream;
		this.map = map;
		this.player = player;
		this.ack = new Stack<Acknowledgement>();
		stop = false;
	}
	
	public static void setStop(boolean state){ stop = state; }
	
	public void run(){
		while(!stop){
			try{
				Update up = (Update)stream.readObject();
				switch(up.getType()){
				case TILE: 
					map.update((Tile)up.getData());
					break;
				case ACKNOWLEDGMENT:
					ack.push((Acknowledgement) up.getData());
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
