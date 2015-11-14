package info.miningyour.games.centipede.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import info.miningyour.games.centipede.events.EventListener;
import info.miningyour.games.centipede.events.EventPump;
import info.miningyour.games.centipede.events.EventType;
import info.miningyour.games.centipede.game.GameObject;
import info.miningyour.games.centipede.game.GameWorld;
import info.miningyour.games.centipede.utils.AssetLoader;
import java.util.ArrayList;

public class GameRenderer implements EventListener {

    private OrthographicCamera camera;
    private ShapeRenderer shapes;
    private SpriteBatch batcher;

    private ShaderProgram shader;
    private int palette;

    private ArrayList<Animated> animatedObjects = new ArrayList<Animated>();

    private GameWorld world;

    public GameRenderer(Rectangle bounds) {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, bounds.getWidth(), bounds.getHeight());
        /*
         * camera.position.set(120.0f, 128.0f, 0.0f); camera.update();
         */

        shapes = new ShapeRenderer();
        shapes.setProjectionMatrix(camera.combined);

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

        world = null;

        EventPump.subscribe(EventType.NewGame, this);
        EventPump.subscribe(EventType.Spawn, this);
        EventPump.subscribe(EventType.Death, this);
        EventPump.subscribe(EventType.NextLevel, this);
        EventPump.subscribe(EventType.GameOver, this);
    }

    private void reset() {
        animatedObjects.clear();
    }

    private String getScore() {
        return world.getScore().toString();
    }

    private String getHighScore() {
        return world.getHighScore().toString();
    }

    public void setWorld(GameWorld world) {
        this.world = world;
    }

    public void render(float runTime) {
        // Fill black
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);

        shapes.begin(ShapeType.Line);

        shapes.end();

        batcher.begin();

        drawGameObjects(runTime);
        drawLives(runTime);
        drawScore();

        batcher.end();
    }

    private void drawGameObjects(float runTime) {
        batcher.enableBlending();

        for (Animated animObj : animatedObjects) {
            Animation anim = AssetLoader.getAnimation(animObj.getAnimationName());
            batcher.draw(anim.getKeyFrame(runTime),
                         animObj.getX(), animObj.getY(),
                         animObj.getWidth() / 2.0f, animObj.getHeight() / 2.0f,
                         animObj.getWidth(), animObj.getHeight(),
                         animObj.getScaleX(), animObj.getScaleY(),
                         animObj.getRotation());
        }
    }

    private void drawLives(float runTime) {
        batcher.enableBlending();

        Animation life = AssetLoader.getAnimation("player");
        for (int i = 0; i < world.getLives(); i++) {
            batcher.draw(life.getKeyFrame(runTime),
                         49.0f + 8.0f * i, 248.0f,
                         3.5f, 4.0f,
                         7.0f, 8.0f,
                         1.0f, 1.0f,
                         0.0f
            );
        }
    }

    private void drawScore() {
        batcher.disableBlending();

        String score = getScore();
        AssetLoader.font.draw(batcher, score, (6 - score.length()) * 8.0f, 256.0f);

        String highScore = getHighScore();
        AssetLoader.font.draw(batcher, highScore, (camera.viewportWidth - highScore.length() * 8.0f) / 2.0f, 256.0f);
    }

    private void setPalette(int index) {
        palette = index;
        batcher.setColor((palette + 0.5f) / 8.0f, 0.0f, 0.0f, 0.0f);
        AssetLoader.font.setColor((palette + 0.5f) / 8.0f, 0.0f, 0.0f, 0.0f);
    }

    @Override
    public void onEvent(EventType event, Object obj) {
        switch (event) {
            case NewGame:
                setPalette(world.getLevel() - 1);
                break;
            case Spawn:
                animatedObjects.add((GameObject) obj);
                break;

            case Death:
                animatedObjects.remove((GameObject) obj);
                break;

            case NextLevel:
                setPalette(world.getLevel() - 1);
                break;

            case GameOver:
                reset();
                break;
        }
    }

    public void dispose() {
        batcher.dispose();
        shader.dispose();
        shapes.dispose();

        EventPump.unsubscribe(EventType.NewGame, this);
        EventPump.unsubscribe(EventType.Spawn, this);
        EventPump.unsubscribe(EventType.Death, this);
        EventPump.unsubscribe(EventType.NextLevel, this);
        EventPump.unsubscribe(EventType.GameOver, this);
    }
}
