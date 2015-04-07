package MMOCS.game;

import java.net.InetSocketAddress;
import java.net.Socket;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MMOCSClient extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Socket skt;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		skt = new Socket();
		try{
			skt.connect(new InetSocketAddress("77.125.250.34", 9098), 5000);
			img = new Texture("badlogic.jpg");
			skt.close();
		}
		catch(Exception e){
			img = new Texture("sadface.jpg");
		}
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
	}
}
