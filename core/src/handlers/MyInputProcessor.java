package handlers;

import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class MyInputProcessor extends Stage {
	
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
		return super.keyDown(k);
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
		return super.keyUp(k);
	}
	
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Buttons.LEFT) {
        	MyInput.setMouseXY(MyInput.LEFT_MOUSE,screenX, screenY);
        }
        if (button == Buttons.RIGHT){
        	MyInput.setMouseXY(MyInput.RIGHT_MOUSE,screenX, screenY);
        }
        return super.touchDown(screenX, screenY, pointer, button);
    }
	

}
