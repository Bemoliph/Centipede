package info.miningyour.games.centipede.game;

import com.badlogic.gdx.math.Rectangle;
import info.miningyour.games.centipede.utils.AssetLoader;

public class Scorpion extends GameObject {

    private static int minGameLevel = 1;
    private static int maxScorpions = 1;
    private static float spawnChance = 0.0075f;

    private static int minX = -16;
    private static int maxX = 240 + 16;

    private GameWorld world;

    public Scorpion(float x, float y, GameWorld world) {
        super("scorpion", "scorpion", new Rectangle(x, y, 16, 8), 1, 1000);
        setCenterX(x);
        setCenterY(y);

        this.world = world;

        if (x < 0) {
            velocity.set(128, 0);
        }
        else {
            velocity.set(-128, 0);
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
