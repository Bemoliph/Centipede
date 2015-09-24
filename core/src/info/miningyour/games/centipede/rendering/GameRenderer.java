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
import info.miningyour.games.centipede.game.Bullet;
import info.miningyour.games.centipede.game.GameObject;
import info.miningyour.games.centipede.game.GameWorld;
import info.miningyour.games.centipede.utils.AssetLoader;
import java.util.ArrayList;
import java.util.List;

public class GameRenderer {

    private GameWorld world;

    private OrthographicCamera camera;
    private ShapeRenderer shapes;
    private SpriteBatch batcher;

    public static final ArrayList<Animated> animatedObjects = new ArrayList<Animated>();

    public GameRenderer(GameWorld world, int gameWidth, int gameHeight) {
        this.world = world;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, gameWidth, gameHeight);

        shapes = new ShapeRenderer();
        shapes.setProjectionMatrix(camera.combined);

        batcher = new SpriteBatch();
        batcher.setProjectionMatrix(camera.combined);
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

        shapes.setColor(Color.MAGENTA);
        List<GameObject> selectedObjects = new ArrayList<GameObject>();
        world.getCollisionTree().retrieve(selectedObjects, world.getPlayer());
        for (GameObject obj : selectedObjects) {
            Rectangle rect = obj.getBoundingBox();
            shapes.rect(rect.x, rect.y, rect.width, rect.height);
        }

        Bullet bullet = world.getBullet();
        if (bullet.isFired()) {
            shapes.setColor(Color.YELLOW);
            selectedObjects.clear();
            world.getCollisionTree().retrieve(selectedObjects, bullet);
            for (GameObject obj : selectedObjects) {
                Rectangle rect = obj.getBoundingBox();
                shapes.rect(rect.x, rect.y, rect.width, rect.height);
            }
        }

        shapes.end();

        batcher.begin();

        batcher.enableBlending();
        for (Animated obj : animatedObjects) {
            Animation anim = AssetLoader.getAnimation(obj.getAnimationName());
            batcher.draw(anim.getKeyFrame(runTime),
                         obj.getX(), obj.getY(),
                         obj.getWidth() / 2.0f, obj.getHeight() / 2.0f,
                         obj.getWidth(), obj.getHeight(),
                         obj.getScaleX(), obj.getScaleY(),
                         obj.getRotation());
        }

        batcher.end();

        shapes.begin(ShapeType.Line);
        shapes.setColor(Color.RED);
        world.getCollisionTree().render(shapes);
        shapes.end();
    }

}
