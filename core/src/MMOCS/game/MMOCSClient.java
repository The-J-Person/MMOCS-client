package MMOCS.game;

import handlers.Content;
import handlers.GameStateManager;
import handlers.MyInput;
import handlers.MyInputProcessor;

import java.net.InetSocketAddress;
import java.net.Socket;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MMOCSClient extends ApplicationAdapter {
	public static final String TITLE = "Game";
	public static final int HEIGHT = 320;
	public static final int WIDTH = 440;
	public static final int SCALE = 2;
	public static final float STEP = 1 / 60f;
	public static Content sprites;
	
	private SpriteBatch batch;
	private OrthographicCamera cam;
	private OrthographicCamera hudCam;
	private GameStateManager gsm;
	private float accum;
	
	public SpriteBatch getSpriteBatch(){return batch;}
	public OrthographicCamera getCam(){return cam;}
	public OrthographicCamera getHudCam(){return hudCam;}
	
	
	@Override
	public void create () {
		//to be deleted
		sprites = new Content();
		sprites.loadTexture("blue.jpg", "blue");
		
		//Gdx.input.setInputProcessor(new MyInputProcessor());
		batch = new SpriteBatch();
		cam = new OrthographicCamera();
		cam.setToOrtho(false, WIDTH , HEIGHT);
		hudCam = new OrthographicCamera();
		hudCam.setToOrtho(false, WIDTH , HEIGHT);
		gsm = new GameStateManager(this);
	}

	@Override
	public void render () {
		accum += Gdx.graphics.getDeltaTime();
		
		while(accum >= STEP){
			accum -= STEP;
			gsm.update(STEP);
			gsm.render();
			MyInput.update();
		}
		batch.setProjectionMatrix(cam.combined);
	}
}
