package MMOCS.game;

import handlers.Content;
import handlers.GameMap;
import handlers.GameStateManager;
import handlers.MyInput;
import network.Connection;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import entities.Player;

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
	private Player player;
	private Connection con;
	private GameMap map;
	
	public SpriteBatch getSpriteBatch(){return batch;}
	public OrthographicCamera getCam(){return cam;}
	public OrthographicCamera getHudCam(){return hudCam;}
	public Player getPlayer(){ return player; }
	public Connection getCon() { return con; }
	public GameMap getMap() { return map; }
	public GameStateManager getGSM(){ return gsm; }
	
	
	@Override
	public void create () {
		//to be deleted
		sprites = new Content();
		sprites.loadTexture("blue.jpg", "blue");
		
		con = new Connection();
		map = new GameMap(batch,null, WIDTH, HEIGHT );
		player = new Player(map);
		
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
