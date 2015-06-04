package network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.LinkedList;

import common.Request;
import common.Update;

public class Connection {

	private Socket skt;
	private RequestSender sender;
	private UpdateReceiver receiver;
	private LinkedList<Update> updates;
	private String address;
	private int port;
	public static final int TIMEOUT = 10;
	
	public Connection(){
		skt = new Socket();
		sender = null;
		receiver = null;
		address = null;
		port = 0;
		updates = new LinkedList<Update>();
	}
	
	public void setAddress(String add, int port){
		this.address = add;
		this.port = port;
	}
	
	public boolean connect(){
		if (address.equals("")){
			address = "unknown";
		}
		skt = new Socket();
		try{
			skt.connect(new InetSocketAddress(address, port), 1000);
			sender = new RequestSender(new ObjectOutputStream(skt.getOutputStream()));
			skt.getOutputStream().flush();
			receiver = new UpdateReceiver( new ObjectInputStream(skt.getInputStream()), updates);
			UpdateReceiver.setStop(false);
			receiver.start();
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
		UpdateReceiver.setStop(true);
		if(isConnected())
			try {
				receiver = null;
				sender = null;
				synchronized(updates){
					updates.clear();
				}
				skt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
//	private void startReceiver(){
//		if(receiver != null){
//			receiver.start();
//		}
//	}
	
//	public void stopReceiver(){
//		UpdateReceiver.setStop(true);
//	}
	
	public RequestSender getRequestSender(){ return sender; }
	public boolean sendRequest(Request req){
		if(sender != null)
			return sender.sendRequest(req);
		return false;
	}
	public boolean isProcessing(){
		if(sender != null)
			return sender.isProcessing();
		return false;
	}
	public boolean setProcessing(boolean state){
		if(sender != null){
			sender.setProcessing(state);
		}
		return false;
	}
	
	public LinkedList<Update> getUpdates() { return updates; }
	
	public boolean isEmpty(){
		synchronized(updates){
			return updates.isEmpty();
		}
	}
	
	
	
}
