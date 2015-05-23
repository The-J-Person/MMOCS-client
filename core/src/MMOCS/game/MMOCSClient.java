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
	Texture img;
	Socket skt;
	
	public SpriteBatch getSpriteBatch(){return batch;}
	public OrthographicCamera getCam(){return cam;}
	public OrthographicCamera getHudCam(){return hudCam;}
	
	
	@Override
	public void create () {
		Gdx.input.setInputProcessor(new MyInputProcessor());
		batch = new SpriteBatch();
		cam = new OrthographicCamera();
		cam.setToOrtho(false, WIDTH , HEIGHT);
		hudCam = new OrthographicCamera();
		hudCam.setToOrtho(false, WIDTH , HEIGHT);
		sprites = new Content();
		sprites.loadTexture("blue.jpg", "blue");
		sprites.loadTexture("red.jpg", "red");
		sprites.loadTexture("example.jpg", "example");
		gsm = new GameStateManager(this);
		
		
		skt = new Socket();
		try{
			skt.connect(new InetSocketAddress("77.125.250.34", 9098), 1000);
			img = new Texture("badlogic.jpg");
			skt.close();
		}
		catch(Exception e){
			img = new Texture("sadface.jpg");
		}
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
		batch.begin();
		batch.draw(sprites.getTexture("blue"), 0,0);
		batch.end();
		batch.setProjectionMatrix(cam.combined);
	}
}
