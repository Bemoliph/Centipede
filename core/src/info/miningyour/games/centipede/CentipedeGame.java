package info.miningyour.games.centipede;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import info.miningyour.games.centipede.events.EventListener;
import info.miningyour.games.centipede.events.EventPump;
import info.miningyour.games.centipede.events.EventType;
import info.miningyour.games.centipede.screens.GameScreen;
import info.miningyour.games.centipede.screens.ScoreScreen;
import info.miningyour.games.centipede.utils.AssetLoader;
import info.miningyour.games.centipede.utils.InputHandler;

public class CentipedeGame extends Game implements EventListener {

    @Override
    public void create() {
        Gdx.app.log("CentipedeGame", "created");
        AssetLoader.load();

        Gdx.input.setInputProcessor(new InputHandler());

        EventPump.subscribe(EventType.GameScreen, this);
        EventPump.subscribe(EventType.ScoreScreen, this);

        setScreen(new ScoreScreen());
    }

    @Override
    public void dispose() {
        AssetLoader.dispose();
        screen.dispose();

        EventPump.unsubscribe(EventType.GameScreen, this);
        EventPump.unsubscribe(EventType.ScoreScreen, this);
    }

    @Override
    public void onEvent(EventType event, Object obj) {
        Screen oldScreen = null;

        switch (event) {
            case GameScreen:
                oldScreen = this.screen;
                setScreen(new GameScreen());
                break;
            case ScoreScreen:
                oldScreen = this.screen;
                setScreen(new ScoreScreen());
                break;
        }

        if (oldScreen != null) {
            oldScreen.dispose();
        }
    }
}
