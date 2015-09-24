package info.miningyour.games.centipede.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import info.miningyour.games.centipede.CentipedeGame;

public class DesktopLauncher {

    public static void main(String[] arg) {
        Settings settings = new Settings();
        settings.maxWidth = 512;
        settings.maxHeight = 512;
        TexturePacker.process(settings, ".", ".", "sprite_sheet");

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Centipede";

        config.width = 240 * 2;
        config.height = 256 * 2;
        config.resizable = false;

        config.foregroundFPS = 60;
        config.vSyncEnabled = true;

        new LwjglApplication(new CentipedeGame(), config);
    }
}
