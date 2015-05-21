package states;

import handlers.GameStateManager;
import MMOCS.game.MMOCSClient;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class GameState {
	protected GameStateManager gsm;
	protected MMOCSClient game;
	protected SpriteBatch batch;
	protected OrthographicCamera cam;
	protected OrthographicCamera hudCam;
	
	protected GameState(GameStateManager gsm){
		this.gsm = gsm;
		game = gsm.getGame();
		batch = game.getSpriteBatch();
		cam = game.getCam();
		hudCam = game.getHudCam();
	}
	
	public abstract void handleInput();
	public abstract void update(float dt);
	public abstract void render();
	public abstract void dispose();
	
}
