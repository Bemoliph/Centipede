package info.miningyour.games.centipede.game;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
import info.miningyour.games.centipede.utils.AssetLoader;

public class CentipedeHead extends CentipedeSegment {

    private static int minGameLevel = 1;
    private static int maxCentipedes = 12;
    private static float spawnChance = 1;
    private static long spawnInterval = 3000;
    public static long lastSpawned = TimeUtils.millis();

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
        super("centipede_head", new Rectangle(x, y, 8.0f, 8.0f), 100, null, segments - 1);

        verticalDirection = -1;
        speed = 96;

        if (x < 120) {
            horizontalDirection = 1;
        }
        else {
            horizontalDirection = -1;
        }

        startMovingVertically();

        lastSpawned = TimeUtils.millis();
    }

    public CentipedeHead(CentipedeSegment segment) {
        this(segment.getX(), segment.getY(), 0);

        trailingSegment = segment.getTrailingSegment();
        if (trailingSegment != null) {
            trailingSegment.leadingSegment = this;
        }
        oldPositions = segment.getOldPositions();

        isMovingHorizontally = segment.isMovingHorizontally();
        isMovingVertically = segment.isMovingVertically();
    }

    public static boolean shouldSpawn(GameWorld world) {
        int totalSegments = world.getSpawnCount("centipede_head") + world.getSpawnCount("centipede_body");
        long timeSinceLastSpawned = TimeUtils.timeSinceMillis(lastSpawned);

        return minGameLevel <= world.getLevel()
               && totalSegments < maxCentipedes
               && spawnInterval < timeSinceLastSpawned
               && AssetLoader.rng.nextFloat() < spawnChance;
    }

    private boolean isTouchingLeftOrRight() {
        return getX() < minX || maxX < getX();
    }

    private void startMovingVertically() {
        int row = Math.round(getY() / cellSize);
        if (row == 1 || row == 30) {
            verticalDirection *= -1;
        }

        velocity.set(0, verticalDirection * speed);
        nextY = getY() + verticalDirection * cellSize;
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

    private void snapToNearestCell() {
        setX(Math.round(getX() / cellSize) * cellSize);
    }

    @Override
    public boolean isMovingHorizontally() {
        return velocity.x != 0;
    }

    @Override
    public boolean isMovingVertically() {
        return velocity.y != 0;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (isTouchingLeftOrRight()) {
            snapToNearestCell();
            startMovingVertically();
        }
        else if (isTouchingTopOrBottom() || isAtNextY()) {
            startMovingHorizontally();
        }
    }

    @Override
    public void onCollision(GameObject gameObj) {
        if (!isMovingVertically()) {
            if (gameObj instanceof Mushroom) {
                //snapToNearestCell();
                startMovingVertically();
            }
            else if (gameObj instanceof CentipedeSegment) {
                CentipedeSegment segment = (CentipedeSegment) gameObj;

                if (!isInChain(segment) && !segment.isMovingVertically()) {
                    startMovingVertically();
                }
            }
        }
    }
}
