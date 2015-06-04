package utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class MyDialog extends Dialog {
	
	public MyDialog(String title, String message){
		super(title,  new Skin(Gdx.files.internal("uiskin.json")));
		text(message);
		button("OK");
	}
	
	protected void result(Object obj){
		remove();
	}
}
