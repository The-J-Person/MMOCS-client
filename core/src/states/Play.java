package states;

import handlers.GameMap;
import handlers.GameStateManager;
import handlers.MyInput;
import network.Connection;
import MMOCS.game.MMOCSClient;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import common.Coordinate;
import common.FloorType;
import common.MapObjectType;
import common.Resource;
import common.Tile;

import entities.Player;

public class Play extends GameState {

	private GameMap map;
	private Player player;
	private Connection con;
	private Resource selectedRes; 
	
	public Play(GameStateManager gsm){
		super(gsm);
		selectedRes = null;
		map = new GameMap(gsm.getGame().getSpriteBatch(),new Coordinate(0,0), MMOCSClient.WIDTH, MMOCSClient.HEIGHT);
		con = new Connection();
		if(!con.Connect()){
			//display pop up window for the error
		}
		player = new Player(map,con.getRequestSender());
		//con.StartReceiver(map, player);
		
		//testing area
		map.update(new Tile(2,0,FloorType.STONE_BRICK, MapObjectType.MONSTER));
		map.update(new Tile(-3,-3,FloorType.STONE_BRICK, MapObjectType.MONSTER));
		map.update(new Tile(1,0,FloorType.STONE_BRICK, null));
		map.update(new Tile(1,1,FloorType.STONE_BRICK, null));
		map.update(new Tile(1,2,FloorType.STONE_BRICK, null));
		map.update(new Tile(0,2,FloorType.WATER, null));
		map.update(new Tile(2,2,FloorType.WOOD, MapObjectType.PILE));
		//end of testing area
		
		
	}
	@Override
	public void handleInput() {
		if(MyInput.isMouseClicked(MyInput.LEFT_MOUSE)){
			//if mouse is on map, act in the tile pressed
			if(map.mouseOnMap(MyInput.getMouseX(MyInput.LEFT_MOUSE), MyInput.getMouseY(MyInput.LEFT_MOUSE))){
				player.moveOrAct(map.getTile(
						map.parsePixelsX(MyInput.getMouseX(MyInput.LEFT_MOUSE)), 
						map.parsePixelsY(MyInput.getMouseY(MyInput.LEFT_MOUSE))));
			}		
		}
		else if(MyInput.isMouseClicked(MyInput.RIGHT_MOUSE)){
			//if mouse is on map, harvest/attack in the tile pressed
			if(map.mouseOnMap(MyInput.getMouseX(MyInput.RIGHT_MOUSE), MyInput.getMouseY(MyInput.RIGHT_MOUSE))){
				player.act(map.getTile(
						map.parsePixelsX(MyInput.getMouseX(MyInput.RIGHT_MOUSE)), 
						map.parsePixelsY(MyInput.getMouseY(MyInput.RIGHT_MOUSE))));
			}
			
		}
		if(MyInput.isPressed(MyInput.DOWN_KEY)){
			player.act(map.getTile(
					map.parsePixelsX(MyInput.getMouseX(MyInput.RIGHT_MOUSE)), 
					map.parsePixelsY(MyInput.getMouseY(MyInput.RIGHT_MOUSE))));
		}
		else if(MyInput.isPressed(MyInput.UP_KEY)){
			//act to the north
		}
		else if(MyInput.isPressed(MyInput.LEFT_KEY)){
			//act to the west
		}
		else if(MyInput.isPressed(MyInput.RIGHT_KEY)){
			//act to the east
		}
		
		//must reset the mouse every time its handled
		MyInput.resetMouseXY();
		
		

	}

	@Override
	public void update(float dt) {
		
		handleInput();
		

	}

	@Override
	public void render() {
		Gdx.gl30.glClear(GL30.GL_COLOR_BUFFER_BIT);
		
		
		map.drawMap();
		//System.out.println(Gdx.input.getX() + "," + Gdx.input.getY());

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}
	
	private boolean isSelected(){
		return selectedRes != null;
	}
}
