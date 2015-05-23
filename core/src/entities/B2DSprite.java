package entities;

import MMOCS.game.MMOCSClient;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

//can include animations here
public class B2DSprite {
	protected Body body;
	protected Texture tex;
	protected float width;
	protected float height;
	
	public B2DSprite(Body body){
		this.body = body;
	}
	
	public void setTexture(String tex){
		this.tex = MMOCSClient.sprites.getTexture(tex);
		width = this.tex.getWidth();
		height = this.tex.getHeight();
	}
	
	public void update(float dt){
		
	}
	
	public void render(SpriteBatch sb){
		sb.begin();
		sb.draw(tex, body.getPosition().x - width/2 , body.getPosition().y - height/2 );
		sb.end();
	}
	
	public Body getBody(){ return body; }
	public Vector2 getPosition() {return body.getPosition(); }
	

}
