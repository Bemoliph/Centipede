package info.miningyour.games.centipede.game;

import com.badlogic.gdx.math.Rectangle;
import info.miningyour.games.centipede.utils.AssetLoader;

public class Flea extends GameObject {

    private static int minGameLevel = 2;
    private static int minMushrooms = 50;
    private static int maxFleas = 1;
    private static float mushroomSpawnChance = 0.075f;

    private static float minY = -8.0f;

    private GameWorld world;

    public Flea(float x, float y, GameWorld world) {
        super("flea", "flea", new Rectangle(0.0f, 0.0f, 9.0f, 8.0f), 1, 200);
        setCenterX(x);
        setCenterY(y);

        this.world = world;

        explosionSize = ExplosionSize.Large;

        velocity.set(0.0f, -128.0f);
    }

    public static boolean shouldSpawn(GameWorld world) {
        return minGameLevel <= world.getLevel()
               && world.getSpawnCount("mushroom") < minMushrooms
               && world.getSpawnCount("flea") < maxFleas;
    }

    private boolean shouldSpawnMushroom() {
        return minY + 24.0f < getY()
               && AssetLoader.rng.nextFloat() < mushroomSpawnChance
               && !world.isMushroomAt(getCenterX(), getCenterY());
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (shouldSpawnMushroom()) {
            world.spawnMushroom(getCenterX(), getCenterY());
        }
    }
}
