package info.miningyour.games.centipede.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Rectangle;
import info.miningyour.games.centipede.game.GameWorld;
import info.miningyour.games.centipede.rendering.GameRenderer;
import info.miningyour.games.centipede.utils.InputHandler;

public class GameScreen implements Screen {

    private GameWorld world;
    private GameRenderer renderer;

    private float runTime;

    public GameScreen() {
        renderer = new GameRenderer(new Rectangle(0.0f, 0.0f, 240.0f, 256.0f));
        world = new GameWorld(new Rectangle(0.0f, 8.0f, 240.0f, 240.0f));

        renderer.setWorld(world);
        world.newGame();

        Gdx.input.setInputProcessor(new InputHandler());
    }

    @Override
    public void render(float delta) {
        world.update(delta);

        runTime += delta;
        renderer.render(runTime);
    }

    @Override
    public void resize(int width, int height) {
        Gdx.app.log("GameScreen", "resize(" + width + ", " + height + ") called");
    }

    @Override
    public void pause() {
        Gdx.app.log("GameScreen", "pause() called");
    }

    @Override
    public void resume() {
        Gdx.app.log("GameScreen", "resume() called");
    }

    @Override
    public void show() {
        Gdx.app.log("GameScreen", "show() called");
    }

    @Override
    public void hide() {
        Gdx.app.log("GameScreen", "hide() called");
    }

    @Override
    public void dispose() {
        Gdx.app.log("GameScreen", "dispose() called");
    }

}
