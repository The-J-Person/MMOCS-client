package handlers;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;

public class MyInputProcessor extends InputAdapter {
	
	public boolean keyDown(int k){
		if(k == Keys.DOWN){
			MyInput.setKey(MyInput.DOWN_KEY, true);
		}
		if(k == Keys.UP){
			MyInput.setKey(MyInput.UP_KEY, true);
		}
		if(k == Keys.LEFT){
			MyInput.setKey(MyInput.LEFT_KEY, true);
		}
		if(k == Keys.RIGHT){
			MyInput.setKey(MyInput.RIGHT_KEY, true);
		}
		return true;
	}
	public boolean keyUp(int k){
		if(k == Keys.DOWN){
			MyInput.setKey(MyInput.DOWN_KEY, false);
		}
		if(k == Keys.UP){
			MyInput.setKey(MyInput.UP_KEY, false);
		}
		if(k == Keys.LEFT){
			MyInput.setKey(MyInput.LEFT_KEY, false);
		}
		if(k == Keys.RIGHT){
			MyInput.setKey(MyInput.RIGHT_KEY, false);
		}
		return true;
	}
	

}