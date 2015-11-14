package info.miningyour.games.centipede.game;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
import info.miningyour.games.centipede.events.EventListener;
import info.miningyour.games.centipede.events.EventPump;
import info.miningyour.games.centipede.events.EventType;
import info.miningyour.games.centipede.utils.AssetLoader;
import info.miningyour.games.centipede.utils.QuadTree;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class GameWorld implements EventListener {

    private static final int mushroomSize = 8;
    private static final int minMushrooms = 50;

    private boolean isWorldFrozen;
    private boolean isGameOver;
    private boolean hasCentipedeSpawned;
    private int level;

    private int score;
    private int highScore;

    private static final int lifeThreshold = 12000;
    private int extraLives;
    private int lives;

    private Rectangle bounds;

    private HashMap<String, Integer> spawnCounts;
    private List<GameObject> gameObjects;
    private Queue<GameObject> spawningObjects;
    private Queue<GameObject> dyingObjects;
    private Iterator<Mushroom> damagedMushrooms;

    private QuadTree collisionTree;
    private List<GameObject> collisionCandidates;
    private MinimumTranslationVector mtv;

    private Player player;
    private Bullet bullet;

    public GameWorld(Rectangle bounds) {
        this.bounds = bounds;

        spawnCounts = new HashMap<String, Integer>();
        gameObjects = new LinkedList<GameObject>();
        spawningObjects = new LinkedList<GameObject>();
        dyingObjects = new LinkedList<GameObject>();

        collisionTree = new QuadTree(bounds);
        collisionCandidates = new LinkedList<GameObject>();
        mtv = new MinimumTranslationVector();

        EventPump.subscribe(EventType.Spawn, this);
        EventPump.subscribe(EventType.Death, this);
        EventPump.subscribe(EventType.Score, this);
        EventPump.subscribe(EventType.Freeze, this);
        EventPump.subscribe(EventType.ExplosionEnd, this);
        EventPump.subscribe(EventType.GameOver, this);
    }

    private void spawnPlayer() {
        player = new Player(16.0f, 16.0f);
        bullet = new Bullet(player);
        modifyLives(-1);
    }

    public void newGame() {
        isWorldFrozen = false;
        isGameOver = false;
        level = 1;

        score = 0;
        highScore = AssetLoader.leaderboard.get(0);

        lives = 3;
        extraLives = 0;

        spawnCounts.clear();
        gameObjects.clear();
        spawningObjects.clear();
        dyingObjects.clear();

        collisionTree.clear();
        collisionCandidates.clear();

        spawnPlayer();
        populateMushrooms(minMushrooms + AssetLoader.rng.nextInt(5));
        EventPump.publish(EventType.NewGame);
    }

    private void nextLevel() {
        level += 1;
        hasCentipedeSpawned = false;
        EventPump.publish(EventType.NextLevel);
    }

    private void tryNextLevel() {
        int totalSegments = getSpawnCount("centipede_head") + getSpawnCount("centipede_body");
        /*
         * HACK: Subtracting 1 segment since this gets called as the segment
         * dies but before its GameObject gets removed from the list, which
         * otherwise prevents hitting 0 and consequently level changing.
         */
        totalSegments -= 1;

        if (hasCentipedeSpawned && totalSegments == 0) {
            nextLevel();
        }
    }

    public int getLives() {
        return lives;
    }

    private void modifyLives(int delta) {
        lives += delta;
    }

    public Integer getScore() {
        return score;
    }

    public Integer getHighScore() {
        return highScore;
    }

    private int normalizeMushroomCoordinate(float a) {
        return (int) (a - a % mushroomSize);
    }

    public boolean isMushroomAt(float x, float y) {
        for (GameObject gameObj : gameObjects) {
            if (gameObj instanceof Mushroom) {
                Mushroom mushroom = (Mushroom) gameObj;

                if ((int) mushroom.getX() == normalizeMushroomCoordinate(x)
                    && (int) mushroom.getY() == normalizeMushroomCoordinate(y)) {
                    return true;
                }
            }
        }

        return false;
    }

    public void spawnMushroom(float x, float y) {
        x = normalizeMushroomCoordinate(x);
        y = normalizeMushroomCoordinate(y);

        Mushroom mushroom = new Mushroom(x, y);
    }

    private void populateMushrooms(int populationCount) {
        for (int i = getSpawnCount("mushroom"); i < populationCount;) {
            int x = mushroomSize * (AssetLoader.rng.nextInt((int) bounds.width / mushroomSize - 2) + 1);
            int y = mushroomSize * (AssetLoader.rng.nextInt((int) bounds.height / mushroomSize - 4) + 4);

            if (!isMushroomAt(x, y)) {
                spawnMushroom(x, y);
                churnGameObjects();
                i++;
            }
        }
    }

    private void spawnFlea() {
        int col = AssetLoader.rng.nextInt((int) (bounds.width / mushroomSize));
        int row = 31;

        float x = mushroomSize * (col + 0.5f);
        float y = mushroomSize * (row + 0.5f);

        Flea flea = new Flea(x, y, this);
    }

    private void spawnSpider() {
        int col = AssetLoader.rng.nextInt(2) == 0 ? -1 : 31;
        int row = AssetLoader.rng.nextInt((int) (bounds.height / mushroomSize) - 15) + 5;

        float x = mushroomSize * (col + 0.5f);
        float y = mushroomSize * (row + 0.5f);

        Spider spider = new Spider(x, y);
    }

    private void spawnCentipede() {
        int col = AssetLoader.rng.nextInt(2) == 0 ? -1 : 29;
        int row = AssetLoader.rng.nextInt((int) (bounds.height / mushroomSize) - 2) + 2;

        float x = mushroomSize * (col + 0.5f);
        float y = mushroomSize * (row + 0.5f);

        int segments = 12 - (int) (getLevel() / 2);
        CentipedeHead centipede = new CentipedeHead(x, y, segments);
        CentipedeHead.setLastSpawned(TimeUtils.millis());
        hasCentipedeSpawned = true;
    }

    private void spawnScorpion() {
        int col = AssetLoader.rng.nextInt(2) == 0 ? -1 : 31;
        int row = AssetLoader.rng.nextInt((int) (bounds.height / mushroomSize) - 1) + 2;

        float x = mushroomSize * (col + 0.5f);
        float y = mushroomSize * (row + 0.5f);

        Scorpion scorpion = new Scorpion(x, y);
    }

    private void spawnQualifyingEntities() {
        if (Flea.shouldSpawn(this)) {
            spawnFlea();
        }

        if (Scorpion.shouldSpawn(this)) {
            spawnScorpion();
        }

        if (Spider.shouldSpawn(this)) {
            spawnSpider();
        }

        if (CentipedeHead.shouldSpawn(this)) {
            spawnCentipede();
        }
    }

    private void churnGameObjects() {
        for (GameObject dyingObj : dyingObjects) {
            gameObjects.remove(dyingObj);
            incSpawnCount(dyingObj.getName(), -1);
        }

        dyingObjects.clear();

        for (GameObject spawningObj : spawningObjects) {
            gameObjects.add(spawningObj);
            incSpawnCount(spawningObj.getName(), +1);
        }

        spawningObjects.clear();
    }

    private void moveColliderOutOfCollidee(GameObject collider, GameObject collidee) {
        Polygon gameygon = collider.getPolygon();
        Polygon mushygon = collidee.getPolygon();

        Intersector.overlapConvexPolygons(gameygon, mushygon, mtv);

        collider.setX(collider.getX() + mtv.normal.x * mtv.depth);
        collider.setY(collider.getY() + mtv.normal.y * mtv.depth);
    }

    public void update(float deltaTime) {
        churnGameObjects();

        if (isWorldFrozen) {
            for (GameObject gameObj : gameObjects) {
                if (gameObj instanceof Explosion) {
                    gameObj.update(deltaTime);
                }
            }
        }
        else {
            for (GameObject gameObj : gameObjects) {
                // TODO: Come back after QuadTree.remove()
                collisionTree.clear();
                for (GameObject collisionObj : gameObjects) {
                    collisionTree.insert(collisionObj);
                }

                gameObj.setX(gameObj.getX() + gameObj.velocity.x * deltaTime);
                gameObj.setY(gameObj.getY() + gameObj.velocity.y * deltaTime);

                collisionCandidates.clear();
                collisionTree.retrieve(collisionCandidates, gameObj);

                for (GameObject candidate : collisionCandidates) {
                    if (gameObj.overlaps(candidate)) {
                        gameObj.onCollision(candidate);

                        if (gameObj instanceof Player && candidate instanceof Mushroom) {
                            moveColliderOutOfCollidee(gameObj, candidate);
                        }
                    }
                }

                gameObj.update(deltaTime);
            }

            spawnQualifyingEntities();
        }

        if (isGameOver) {
            onGameOver();
        }
    }

    private void onSpawn(GameObject gameObj) {
        spawningObjects.add(gameObj);
    }

    private void onDeath(GameObject gameObj) {
        dyingObjects.add(gameObj);

        if (gameObj instanceof Player) {
            EventPump.publish(EventType.Freeze);
        }

        if (gameObj instanceof CentipedeHead && gameObj.wasKilled()) {
            tryNextLevel();
        }
    }

    private void onScore(Integer delta) {
        this.score += delta;

        int newLives = score / lifeThreshold;
        if (extraLives < newLives) {
            extraLives = newLives;
            modifyLives(+1);
        }
    }

    private void onFreeze() {
        isWorldFrozen = true;

        bullet.despawn();

        List<Mushroom> mushrooms = new LinkedList<Mushroom>();
        for (GameObject gameObj : gameObjects) {
            if (gameObj instanceof Mushroom && gameObj.isDamaged() && gameObj.isAlive()) {
                mushrooms.add((Mushroom) gameObj);
            }
        }
        damagedMushrooms = mushrooms.iterator();
    }

    private void despawnEnemies() {
        for (GameObject gameObj : gameObjects) {
            if (gameObj.isAlive() && !(gameObj instanceof Mushroom)) {
                gameObj.despawn();
            }
        }
    }

    private void respawnPlayer() {
        bullet.despawn();

        if (0 < lives) {
            spawnPlayer();
        }
        else {
            EventPump.publish(EventType.GameOver);
        }
    }

    private void onExplosionEnd() {
        if (isWorldFrozen) {
            if (damagedMushrooms.hasNext()) {
                Mushroom mushroom = damagedMushrooms.next();
                mushroom.repair();
            }
            else {
                isWorldFrozen = false;
                despawnEnemies();
                respawnPlayer();
            }
        }

    }

    private void onGameOver() {
        AssetLoader.leaderboard.add(score);
        EventPump.publish(EventType.ScoreScreen);
    }

    @Override
    public void onEvent(EventType event, Object obj) {
        switch (event) {
            case Spawn:
                onSpawn((GameObject) obj);
                break;

            case Death:
                onDeath((GameObject) obj);
                break;

            case Score:
                onScore((Integer) obj);
                break;

            case Freeze:
                onFreeze();
                break;

            case ExplosionEnd:
                onExplosionEnd();
                break;

            case GameOver:
                /*
                 * We can't directly call onGameOver() without causing a
                 * ConcurrentModificationException due to the player dying.
                 * Instead, we'll do it at the end of the tick after everything
                 * has been processed safely.
                 */
                isGameOver = true;
                break;
        }
    }

    public int getLevel() {
        return level;
    }

    public int getSpawnCount(String gameObjectName) {
        return spawnCounts.getOrDefault(gameObjectName, 0);
    }

    private void incSpawnCount(String gameObjectName, int inc) {
        spawnCounts.put(gameObjectName, getSpawnCount(gameObjectName) + inc);
    }

    public void dispose() {
        EventPump.unsubscribe(EventType.Spawn, this);
        EventPump.unsubscribe(EventType.Death, this);
        EventPump.unsubscribe(EventType.Score, this);
        EventPump.unsubscribe(EventType.Freeze, this);
        EventPump.unsubscribe(EventType.ExplosionEnd, this);
        EventPump.unsubscribe(EventType.GameOver, this);
    }
}
