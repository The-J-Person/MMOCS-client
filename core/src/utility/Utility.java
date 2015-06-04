package utility;

import handlers.MyDialog;
import network.Connection;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;

public class Utility {
	public static Timer.Task timeOutCounter(final Connection con,final Stage stage){
		return new Timer.Task(){
			
			@Override
			public void run() {
				con.close();
				MyDialog dia = new MyDialog("Timeout", "Connection timeout");
				con.getRequestSender().setProcessing(false);
				dia.show(stage);
			}
		};
	}

}
