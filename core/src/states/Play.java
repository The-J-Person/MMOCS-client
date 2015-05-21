package states;

import handlers.GameStateManager;
import handlers.MyInput;

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

public class Play extends GameState {

	private World world;
	private Box2DDebugRenderer b2dr;
	
	public Play(GameStateManager gsm){
		super(gsm);
		world = new World(new Vector2(0,-9f),true);
		b2dr = new Box2DDebugRenderer();
		
		BodyDef bdef = new BodyDef();
		bdef.position.set(160,120);
		bdef.type = BodyType.StaticBody;
		Body body = world.createBody(bdef);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(50, 50);
		
		FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		body.createFixture(fdef);
		
		//falling body
		bdef.position.set(160,200);
		bdef.type = BodyType.DynamicBody;
		body = world.createBody(bdef);
		shape.setAsBox(5, 5);
		fdef.shape = shape;
		fdef.filter.maskBits = 0;
		body.createFixture(fdef);
		
	}
	@Override
	public void handleInput() {


	}

	@Override
	public void update(float dt) {
		
		handleInput();
		
		world.step(dt,1,1);

	}

	@Override
	public void render() {
		Gdx.gl30.glClear(GL30.GL_COLOR_BUFFER_BIT);
		
		b2dr.render(world, cam.combined);

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
