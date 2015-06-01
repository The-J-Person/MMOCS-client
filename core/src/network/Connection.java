package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import MMOCS.game.MMOCSClient;

public class Connection {

	private Socket skt;
	private RequestSender sender;
	private UpdateReceiver receiver;
	private String address;
	private int port;
	
	public Connection(){
		skt = new Socket();
		sender = null;
		receiver = null;
		address = null;
		port = 0;
	}
	
	public void setAddress(String add, int port){
		this.address = add;
		this.port = port;
	}
	
	public boolean connect(){
		if (address.equals("")){
			address = "unknown";
		}
		try{
			skt.connect(new InetSocketAddress(address, port), 1000);
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
	
	public void close(){
		if(isConnected())
			try {
				skt.close();
				stopReceiver();
				receiver = null;
				sender = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	public void startReceiver(){
		if(receiver != null){
			receiver.start();
		}
	}
	
	public boolean setReceiver(MMOCSClient game){
		try {
			receiver = new UpdateReceiver(
					new ObjectInputStream(skt.getInputStream()),
					game, this);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public void stopReceiver(){
		UpdateReceiver.setStop(true);
	}
	
	public RequestSender getRequestSender(){ return sender; }
	
	
	
}
