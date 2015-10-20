package info.miningyour.games.centipede.game;

import com.badlogic.gdx.math.Rectangle;
import info.miningyour.games.centipede.utils.AssetLoader;

public class Spider extends GameObject {

    private static int minGameLevel = 1;
    private static int maxSpiders = 1;
    private static float spawnChance = 0.00075f;

    private static float minX = -18.0f;
    private static float maxX = 240.0f + 18.0f;

    public float minY;
    public float maxY;
    public float floor;
    public float ceil;

    private static float speed = 96.0f;
    private boolean isMovingLeft;

    public Spider(float x, float y) {
        super("spider", "spider", new Rectangle(x, y, 16.0f, 8.0f), 1, 300);
        setCenterX(x);
        setCenterY(y);

        minY = floor = 8.0f;
        maxY = ceil = y;

        isMovingLeft = 0.0f < x;
        setDirection();
    }

    public static boolean shouldSpawn(GameWorld world) {
        return minGameLevel <= world.getLevel()
               && world.getSpawnCount("spider") < maxSpiders
               && AssetLoader.rng.nextFloat() < spawnChance;
    }

    private boolean isOffScreenHorizontally() {
        return getX() < minX || maxX < getX();
    }

    private boolean isOutOfRangeVertically() {
        return getY() < floor || ceil < getY();
    }

    private boolean shouldMoveDiagonally() {
        return AssetLoader.rng.nextInt(2) == 0;
    }

    private void setDirection() {
        floor = getY() - (getY() - minY) * AssetLoader.rng.nextFloat();
        ceil = getY() + (maxY - getY()) * AssetLoader.rng.nextFloat();

        float x = shouldMoveDiagonally() ? 1 : 0;
        x = isMovingLeft ? x * -1 : x;

        float y = velocity.y < 0 ? 1 : -1;

        velocity.set(x, y).setLength(speed);
    }

    @Override
    public void update(float deltaTime) {
        setX(getX() + velocity.x * deltaTime);
        setY(getY() + velocity.y * deltaTime);

        if (isOffScreenHorizontally()) {
            die();
        }
        else if (isOutOfRangeVertically()) {
            setDirection();
        }
    }

    @Override
    public void onCollision(GameObject gameObj) {
        if (gameObj instanceof Mushroom) {
            gameObj.die(); // eat the mushroom!
        }
    }
}
