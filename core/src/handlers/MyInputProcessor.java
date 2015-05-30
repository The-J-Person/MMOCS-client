package handlers;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;

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
	
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Buttons.LEFT) {
        	MyInput.setMouseXY(MyInput.LEFT_MOUSE,screenX, screenY);
            return true;
        }
        if (button == Buttons.RIGHT){
        	MyInput.setMouseXY(MyInput.RIGHT_MOUSE,screenX, screenY);
            return true;
        }
        return false;
    }
	

}
