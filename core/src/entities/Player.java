package entities;

import com.badlogic.gdx.physics.box2d.Body;

public class Player extends B2DSprite {
	private int level;
	private int points;
	//....
	
	public Player(Body body){
		super(body);
		setTexture("blue");
		level = 1;
		points = 0;
	}
}
