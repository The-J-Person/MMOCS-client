package network;

import handlers.GameMap;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Stack;

import common.Acknowledgement;

import entities.Player;

public class Connection {

	private Socket skt;
	private RequestSender sender;
	private UpdateReceiver receiver;
	private Stack<Acknowledgement> ack;
	
	public Connection(){
		skt = new Socket();
		sender = null;
		receiver = null;
		ack = new Stack<Acknowledgement>();
	}
	
	public boolean Connect(){
		try{
			skt.connect(new InetSocketAddress("77.125.250.34", 9098), 1000);
			sender = new RequestSender(new ObjectOutputStream(skt.getOutputStream()));
			return true;
		}
		catch(Exception e){
			return false;
		}
	}
	
	public boolean isConnected(){
		return skt.isConnected();
	}
	
	public void Close(){
		if(isConnected())
			try {
				skt.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	public void StartReceiver(GameMap map, Player player){
		try {
			receiver = new UpdateReceiver(
					new ObjectInputStream(skt.getInputStream()),
					map, player, sender);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void StopReceiver(){
		receiver.setStop(true);
	}
	
	public RequestSender getRequestSender(){ return sender; }
	
	
	
}
