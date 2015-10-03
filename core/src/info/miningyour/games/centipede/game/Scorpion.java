package info.miningyour.games.centipede.game;

import com.badlogic.gdx.math.Rectangle;
import info.miningyour.games.centipede.utils.AssetLoader;

public class Scorpion extends GameObject {

    private static int minGameLevel = 2;
    private static int maxScorpions = 1;
    private static float spawnChance = 0.0075f;

    private static float minX = -16.0f;
    private static float maxX = 240.0f + 16.0f;

    public Scorpion(float x, float y) {
        super("scorpion", "scorpion", new Rectangle(x, y, 16.0f, 8.0f), 1, 1000);
        setCenterX(x);
        setCenterY(y);

        if (x < 0.0f) {
            velocity.set(128.0f, 0.0f);
        }
        else {
            velocity.set(-128.0f, 0.0f);
        }
    }

    public static boolean shouldSpawn(GameWorld world) {
        return minGameLevel <= world.getLevel()
               && world.getSpawnCount("scorpion") < maxScorpions
               && AssetLoader.rng.nextFloat() < spawnChance;
    }

    @Override
    public void update(float deltaTime) {
        setX(getX() + velocity.x * deltaTime);

        if (getX() < minX || maxX < getX()) {
            die();
        }
    }

    @Override
    public void onCollision(GameObject gameObj) {
        if (gameObj instanceof Mushroom) {
            Mushroom mushroom = (Mushroom) gameObj;
            mushroom.setPoisoned(true);
        }
    }
}
