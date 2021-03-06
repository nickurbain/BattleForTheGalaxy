package battle.galaxy.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import battle.galaxy.BattleForTheGalaxy;

public class DesktopLauncher {
			
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Battle For The Galaxy";
		config.width = 1600;
		config.height = 900;
		config.resizable = true;
		config.fullscreen = false;
		
		new LwjglApplication(new BattleForTheGalaxy(), config);
	}
}
