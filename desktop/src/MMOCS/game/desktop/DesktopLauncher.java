package MMOCS.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import MMOCS.game.MMOCSClient;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = MMOCSClient.TITLE;
		config.width = MMOCSClient.WIDTH * MMOCSClient.SCALE;
		config.height = MMOCSClient.HEIGHT * MMOCSClient.SCALE;
		new LwjglApplication(new MMOCSClient(), config);
	}
}
