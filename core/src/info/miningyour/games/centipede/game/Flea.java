package info.miningyour.games.centipede.game;

import com.badlogic.gdx.math.Rectangle;
import info.miningyour.games.centipede.utils.AssetLoader;

public class Flea extends GameObject {

    private static int minGameLevel = 1;
    private static int minMushrooms = 50;
    private static int maxFleas = 1;
    private static float mushroomSpawnChance = 0.075f;

    private static int minY = -8;

    private GameWorld world;

    public Flea(float x, float y, GameWorld world) {
        super("flea", "flea", new Rectangle(0, 0, 9, 8), 1, 200);
        setCenterX(x);
        setCenterY(y);

        this.world = world;

        velocity.set(0, -128);
    }

    public static boolean shouldSpawn(GameWorld world) {
        return minGameLevel <= world.getLevel()
               && world.getSpawnCount("mushroom") < minMushrooms
               && world.getSpawnCount("flea") < maxFleas;
    }

    private boolean shouldSpawnMushroom() {
        return minY + 24 < getY()
               && AssetLoader.rng.nextFloat() < mushroomSpawnChance
               && !world.isMushroomAt(getX(), getY());
    }

    @Override
    public void update(float deltaTime) {
        setY(getY() + velocity.y * deltaTime);

        if (getY() < minY) {
            die();
        }
        else if (shouldSpawnMushroom()) {
            world.spawnMushroom(getX(), getY());
        }
    }
}
