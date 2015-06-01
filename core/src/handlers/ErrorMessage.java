package handlers;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;

public class ErrorMessage extends Dialog {
	
	public ErrorMessage(String text, WindowStyle style){
		super(text, style);
	}
	
	protected void result(Object object){
		System.out.println("first stop this madness");
		this.remove();
	}

}
