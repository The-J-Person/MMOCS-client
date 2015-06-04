package network;

import java.io.IOException;
import java.io.ObjectOutputStream;

import common.Request;

public class RequestSender{
	private ObjectOutputStream stream;
	private boolean processing;
	private Request lastAckReq;
	
	public RequestSender(ObjectOutputStream stream){
		this.stream = stream;
		processing = false;
	}
	
	public boolean sendRequest(Request req){
		try {
			if(processing == false){
				stream.writeObject(req);
				if(req.getType().requireAck()){
					lastAckReq = req;
					processing = true;
				}
				return true;
			} 
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	public void setProcessing(boolean state){ processing = state; }
	public boolean isProcessing(){ return processing; }
	public Request requestToAck(){ return lastAckReq; }
}
