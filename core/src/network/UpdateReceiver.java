package network;


import java.io.ObjectInputStream;
import java.util.LinkedList;
import common.Tile;
import common.Update;
import common.UpdateType;


public class UpdateReceiver extends Thread {
	private static volatile boolean stop;
	LinkedList<Update> updates;
	private ObjectInputStream stream;

	
	public UpdateReceiver(ObjectInputStream stream, LinkedList<Update> updates){
		this.stream = stream;
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
