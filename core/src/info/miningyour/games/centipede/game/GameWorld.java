package info.miningyour.games.centipede.game;

import com.badlogic.gdx.math.Rectangle;
import info.miningyour.games.centipede.utils.AssetLoader;
import info.miningyour.games.centipede.utils.Event;
import info.miningyour.games.centipede.utils.EventListener;
import info.miningyour.games.centipede.utils.EventPump;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class GameWorld implements EventListener {

    private static final int mushroomSize = 8;
    private static final int minMushrooms = 50;

    private int level;
    private int score;

    private Rectangle bounds;

    private HashMap<String, Integer> spawnCounts;
    private List<GameObject> gameObjects;
    private Queue<GameObject> spawningObjects;
    private Queue<GameObject> dyingObjects;

    private Collider collider;

    private Player player;
    private Bullet bullet;

    public GameWorld(Rectangle bounds) {
        this.bounds = bounds;

        spawnCounts = new HashMap<String, Integer>();
        gameObjects = new LinkedList<GameObject>();
        spawningObjects = new LinkedList<GameObject>();
        dyingObjects = new LinkedList<GameObject>();

        collider = new Collider(bounds);

        EventPump.subscribe(Event.Spawn, this);
        EventPump.subscribe(Event.Death, this);
        EventPump.subscribe(Event.Score, this);
        EventPump.subscribe(Event.GameOver, this);
    }

    public void newGame() {
        level = 1;
        score = 0;

        spawnCounts.clear();
        gameObjects.clear();
        spawningObjects.clear();
        dyingObjects.clear();

        collider.clear();

        player = new Player(16, 16);
        bullet = new Bullet(player);
        populateMushrooms(minMushrooms + AssetLoader.rng.nextInt(5));
    }

    public void gameOver() {
        newGame();
    }

    public Integer getScore() {
        return score;
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
            int x = mushroomSize * AssetLoader.rng.nextInt((int) bounds.width / mushroomSize);
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

    private void spawnSpider(float x, float y) {

    }

    private void spawnCentipede(float x, float y, int segments) {

    }

    private void spawnScorpion() {
        int col = AssetLoader.rng.nextInt(2) == 0 ? -1 : 31;
        int row = AssetLoader.rng.nextInt((int) (bounds.height / mushroomSize) - 1) + 2;

        float x = mushroomSize * (col + 0.5f);
        float y = mushroomSize * (row + 0.5f);

        Scorpion scorpion = new Scorpion(x, y, this);
    }

    private void spawnQualifyingEntities() {
        if (Flea.shouldSpawn(this)) {
            spawnFlea();
        }

        if (Scorpion.shouldSpawn(this)) {
            spawnScorpion();
        }
    }

    private void churnGameObjects() {
        for (GameObject dyingObj : dyingObjects) {
            gameObjects.remove(dyingObj);
            collider.remove(dyingObj);
            incSpawnCount(dyingObj.getName(), -1);
        }

        dyingObjects.clear();

        for (GameObject spawningObj : spawningObjects) {
            gameObjects.add(spawningObj);
            collider.add(spawningObj);
            incSpawnCount(spawningObj.getName(), +1);
        }

        spawningObjects.clear();
    }

    public void update(float deltaTime) {
        churnGameObjects();

        for (GameObject gameObj : gameObjects) {
            gameObj.update(deltaTime);
        }

        collider.update();
        collider.collide(player);
        collider.collide(bullet);

        spawnQualifyingEntities();
    }

    private void onSpawn(GameObject gameObj) {
        spawningObjects.add(gameObj);
    }

    private void onDeath(GameObject gameObj) {
        dyingObjects.add(gameObj);
    }

    private void onScore(GameObject gameObj) {
        if (gameObj.wasKilled()) {
            score += gameObj.getScoreValue();
        }
    }

    @Override
    public void onEvent(Event event, Object obj) {
        switch (event) {
            case Spawn:
                onSpawn((GameObject) obj);
                break;

            case Death:
                onDeath((GameObject) obj);
                break;

            case Score:
                onScore((GameObject) obj);
                break;

            case GameOver:
                gameOver();
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
}
