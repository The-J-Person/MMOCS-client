package network;

import java.io.IOException;
import java.io.ObjectOutputStream;

import common.Request;

public class RequestSender{
	ObjectOutputStream stream;
	
	public RequestSender(ObjectOutputStream stream){
		this.stream = stream;
	}
	
	public boolean sendRequest(Request req){
		try {
			stream.writeObject(req);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
