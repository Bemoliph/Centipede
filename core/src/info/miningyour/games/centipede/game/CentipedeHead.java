package info.miningyour.games.centipede.game;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
import info.miningyour.games.centipede.utils.AssetLoader;

public class CentipedeHead extends GameObject {

    private static int minGameLevel = 1;
    private static int maxCentipedes = 12;
    private static float spawnChance = 1;
    private static long spawnInterval = 3000;
    private static long lastSpawned = TimeUtils.millis();

    private static float minX = 0.0f;
    private static float maxX = 240.0f - 8.0f;
    private static float minY = 8.0f;
    private static float maxY = 256.0f - 8.0f - 8.0f;
    private static float cellSize = 8.0f;

    private int verticalDirection;
    private int horizontalDirection;
    private float speed;
    private float nextY;

    public CentipedeHead(float x, float y, int segments) {
        super("centipede_head", "centipede_head", new Rectangle(x, y, 8.0f, 8.0f), 1, 100);

        verticalDirection = -1;
        speed = 96;

        if (x < 120) {
            horizontalDirection = 1;
        }
        else {
            horizontalDirection = -1;
        }

        velocity.set(horizontalDirection * speed, verticalDirection * speed);
    }

    public static boolean shouldSpawn(GameWorld world) {
        int totalSegments = world.getSpawnCount("centipede_head") + world.getSpawnCount("centipede_body");
        long timeSinceLastSpawned = TimeUtils.timeSinceMillis(lastSpawned);

        return minGameLevel <= world.getLevel()
               && totalSegments < maxCentipedes
               && spawnInterval < timeSinceLastSpawned
               && AssetLoader.rng.nextFloat() < spawnChance;
    }

    public static void setLastSpawned(long spawnTime) {
        lastSpawned = spawnTime;
    }

    private boolean isMovingHorizontally() {
        return velocity.x != 0;
    }

    private boolean isTouchingLeftOrRight() {
        return getX() < minX || maxX < getX();
    }

    private void startMovingVertically() {
        setX(Math.round(getX() / cellSize) * cellSize);

        int row = Math.round(getY() / cellSize);
        if (row == 1 || row == 30) {
            verticalDirection *= -1;
        }

        velocity.set(0, verticalDirection * speed);
        nextY = getY() + verticalDirection * cellSize;
    }

    private boolean isMovingVertically() {
        return velocity.y != 0;
    }

    private boolean isTouchingTopOrBottom() {
        return getY() < minY || maxY < getY();
    }

    private boolean isAtNextY() {
        if (verticalDirection == -1) {
            return getY() < nextY;
        }
        else {
            return nextY < getY();
        }
    }

    private void startMovingHorizontally() {
        setY(Math.round(getY() / cellSize) * cellSize);

        horizontalDirection *= -1;
        velocity.set(horizontalDirection * speed, 0);
    }

    @Override
    public void update(float deltaTime) {
        setX(getX() + velocity.x * deltaTime);
        setY(getY() + velocity.y * deltaTime);

        if (isTouchingLeftOrRight()) {
            startMovingVertically();
        }
        else if (isTouchingTopOrBottom() || isAtNextY()) {
            startMovingHorizontally();
        }
    }

    @Override
    public void onCollision(GameObject gameObj) {
        if (gameObj instanceof Mushroom || gameObj instanceof CentipedeHead) {
            if (!isMovingVertically()) {
                startMovingVertically();
            }
        }
    }

    @Override
    public void die() {
        super.die();

        /*
         * Adjust spawn window so they don't spawn "infinitely" instantly
         */
        setLastSpawned(TimeUtils.millis());
        /*
         * TODO: Spawn mushroom here
         */
    }
}
