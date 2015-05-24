package states;

import handlers.GameMap;
import handlers.GameStateManager;
import handlers.MyInput;
import MMOCS.game.MMOCSClient;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import common.Coordinate;
import common.FloorType;
import common.MapObjectType;
import common.Tile;

import entities.Player;

public class Play extends GameState {

	private World world;
	private Box2DDebugRenderer b2dr;
	private GameMap map;
	private Player player;
	
	public Play(GameStateManager gsm){
		super(gsm);
		
		map = new GameMap(gsm.getGame().getSpriteBatch(),new Coordinate(0,0), MMOCSClient.WIDTH, MMOCSClient.HEIGHT);
		//testing area
		map.update(new Tile(2,0,FloorType.STONE_BRICK, MapObjectType.MONSTER));
		map.update(new Tile(0,2,FloorType.WATER, null));
		map.update(new Tile(2,2,FloorType.WOOD, MapObjectType.PILE));
		//end of testing area
		world = new World(new Vector2(0,0),true);
		b2dr = new Box2DDebugRenderer();
		
		BodyDef bdef = new BodyDef();
		bdef.position.set(160,120);
		bdef.type = BodyType.StaticBody;
		Body body = world.createBody(bdef);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(20, 20);
		
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		body.createFixture(fdef);
		player = new Player(body);
		
		
	}
	@Override
	public void handleInput() {
		Tile tile;
		if(MyInput.isPressed(MyInput.DOWN_KEY)){
			tile = map.lookInDirection(map.DOWN);
			if(tile == null)
				return;
			if(tile.canMoveOn()){
				if(tile.canPickUp()){
					System.out.println("im picking things up");
					tile.setMapObjectType(null);
				}
				map.moveCenter(GameMap.DOWN);
			}
		}
		else if(MyInput.isPressed(MyInput.UP_KEY)){
			tile = map.lookInDirection(map.UP);
			if(tile == null)
				return;
			if(tile.canMoveOn()){
				if(tile.canPickUp()){
					System.out.println("im picking things up");
					tile.setMapObjectType(null);
				}
				map.moveCenter(GameMap.UP);
			}
		}
		else if(MyInput.isPressed(MyInput.LEFT_KEY)){
			tile = map.lookInDirection(map.LEFT);
			if(tile == null)
				return;
			if(tile.canMoveOn()){
				if(tile.canPickUp()){
					System.out.println("im picking things up");
					tile.setMapObjectType(null);
				}
				map.moveCenter(GameMap.LEFT);
			}
		}
		else if(MyInput.isPressed(MyInput.RIGHT_KEY)){
			tile = map.lookInDirection(map.RIGHT);
			if(tile == null)
				return;
			if(tile.canMoveOn()){
				if(tile.canPickUp()){
					System.out.println("im picking things up");
					tile.setMapObjectType(null);
				}
				map.moveCenter(GameMap.RIGHT);
			}		
		}

	}

	@Override
	public void update(float dt) {
		//map.moveCenter(GameMap.LEFT);
		handleInput();
		
		world.step(dt,1,1);
		player.update(dt);

	}

	@Override
	public void render() {
		Gdx.gl30.glClear(GL30.GL_COLOR_BUFFER_BIT);
		
		player.render(batch);
		
		b2dr.render(world, cam.combined);
		map.drawMap();

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
