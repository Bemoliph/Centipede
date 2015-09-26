package info.miningyour.games.centipede.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import info.miningyour.games.centipede.game.GameObject;
import info.miningyour.games.centipede.utils.AssetLoader;
import info.miningyour.games.centipede.utils.Event;
import info.miningyour.games.centipede.utils.EventListener;
import info.miningyour.games.centipede.utils.EventPump;
import java.util.ArrayList;

public class GameRenderer implements EventListener {

    private OrthographicCamera camera;
    private ShapeRenderer shapes;
    private SpriteBatch batcher;

    private ArrayList<Animated> animatedObjects = new ArrayList<Animated>();

    public GameRenderer(Rectangle bounds) {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, bounds.getWidth(), bounds.getHeight());

        shapes = new ShapeRenderer();
        shapes.setProjectionMatrix(camera.combined);

        batcher = new SpriteBatch();
        batcher.setProjectionMatrix(camera.combined);

        EventPump.subscribe(Event.Spawn, this);
        EventPump.subscribe(Event.Death, this);
    }

    public void render(float runTime) {
        // Fill black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapes.begin(ShapeType.Filled);

        shapes.setColor(Color.BLUE);
        shapes.rect(0, 0, 240, 8);
        shapes.rect(0, 248, 240, 8);

        shapes.setColor(Color.GREEN);
        shapes.rect(0, 8, 240, 48);

        shapes.end();

        batcher.begin();

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

        batcher.end();
    }

    @Override
    public void onEvent(Event event, Object obj) {
        GameObject gameObj = (GameObject) obj;

        switch (event) {
            case Spawn:
                animatedObjects.add(gameObj);
                break;

            case Death:
                animatedObjects.remove(gameObj);
                break;
        }
    }
}
