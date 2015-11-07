package info.miningyour.games.centipede.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import info.miningyour.games.centipede.events.EventListener;
import info.miningyour.games.centipede.events.EventPump;
import info.miningyour.games.centipede.events.EventType;
import info.miningyour.games.centipede.utils.AssetLoader;
import info.miningyour.games.centipede.utils.InputState;

public class ScoreScreen implements Screen, EventListener {

    private SpriteBatch batcher;
    private OrthographicCamera camera;
    private ShaderProgram shader;
    private int palette;

    private boolean startingNewGame;

    private static final String leaderboard_highScores = "HIGH SCORES";
    private static final String leaderboard_freePlay = "FREE PLAY";
    private static final String leaderboard_bonus = "BONUS EVERY 12,000";
    private static final String leaderboard_copyright = "©1980 ATARI";

    public ScoreScreen() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 240.0f, 256.0f);

        ShaderProgram.pedantic = false;
        shader = new ShaderProgram(Gdx.files.internal("palette.vert"), Gdx.files.internal("palette.frag"));

        batcher = new SpriteBatch(1000, shader);
        batcher.setProjectionMatrix(camera.combined);
        batcher.setShader(shader);

        setPalette(0);
        AssetLoader.getColorPalette().bind(1);
        shader.begin();
        shader.setUniformi("colorTable", 1);
        shader.end();

        startingNewGame = false;

        EventPump.subscribe(EventType.Input, this);
    }

    @Override
    public void onEvent(EventType event, Object obj) {
        if (event == EventType.Input && obj instanceof InputState) {
            InputState state = (InputState) obj;
            boolean pressedFire = state.getShouldFire();

            if (pressedFire) {
                startingNewGame = true;
            }
        }
    }

    private void setPalette(int index) {
        palette = index;
        batcher.setColor((palette + 0.5f) / 8.0f, 0.0f, 0.0f, 0.0f);
        AssetLoader.font.setColor((palette + 0.5f) / 8.0f, 0.0f, 0.0f, 0.0f);
    }

    private void drawLeaderboard() {
        batcher.disableBlending();

        AssetLoader.font.draw(batcher, leaderboard_highScores, (camera.viewportWidth - leaderboard_highScores.length() * 8.0f) / 2.0f, 240.0f);

        for (int i = 0; i < AssetLoader.leaderboard.size(); i++) {
            String score = AssetLoader.leaderboard.get(i).toString();
            AssetLoader.font.draw(batcher, score, (camera.viewportWidth - score.length() * 8.0f) / 2.0f, 232.0f - 8.0f * i);
        }

        AssetLoader.font.draw(batcher, leaderboard_freePlay, (camera.viewportWidth - leaderboard_freePlay.length() * 8.0f) / 2.0f, 160.0f);
        AssetLoader.font.draw(batcher, leaderboard_bonus, (camera.viewportWidth - leaderboard_bonus.length() * 8.0f) / 2.0f, 144.0f);
        AssetLoader.font.draw(batcher, leaderboard_copyright, (camera.viewportWidth - leaderboard_copyright.length() * 8.0f) / 2.0f, 8.0f);
    }

    @Override
    public void render(float delta) {
        if (startingNewGame) {
            EventPump.publish(EventType.GameScreen);
        }
        else {
            Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);

            batcher.begin();

            drawLeaderboard();

            batcher.end();

        }

        EventPump.pump();
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
        batcher.dispose();

        EventPump.unsubscribe(EventType.Input, this);
    }
}
