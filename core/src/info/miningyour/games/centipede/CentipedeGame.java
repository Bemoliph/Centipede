package info.miningyour.games.centipede;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import info.miningyour.games.centipede.screens.GameScreen;
import info.miningyour.games.centipede.utils.AssetLoader;

public class CentipedeGame extends Game {

    @Override
    public void create() {
        Gdx.app.log("CentipedeGame", "created");
        AssetLoader.load();
        this.setScreen(new GameScreen());
    }

    @Override
    public void dispose() {
        AssetLoader.dispose();
    }
}
