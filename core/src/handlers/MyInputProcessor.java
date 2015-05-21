package handlers;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;

public class MyInputProcessor extends InputAdapter {
	
	public boolean keyDown(int k){
		if(k == Keys.Z){
			MyInput.setKey(MyInput.DOWN_KEY, true);
		}
		if(k == Keys.A){
			MyInput.setKey(MyInput.UP_KEY, true);
		}
		return true;
	}
	public boolean keyUp(int k){
		if(k == Keys.Z){
			MyInput.setKey(MyInput.DOWN_KEY, false);
		}
		if(k == Keys.A){
			MyInput.setKey(MyInput.UP_KEY, false);
		}
		return true;
	}
	

}
