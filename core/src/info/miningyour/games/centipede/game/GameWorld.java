package info.miningyour.games.centipede.game;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
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

    private boolean isGameOver;
    private boolean hasCentipedeSpawned;
    private int level;

    private int score;
    private int highScore;

    private static int lifeThreshold = 12000;
    private int extraLives;
    private int lives;

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

    private void spawnPlayer() {
        player = new Player(16.0f, 0.0f);
        bullet = new Bullet(player);
        modifyLives(-1);
    }

    public void newGame() {
        isGameOver = false;
        level = 1;

        score = 0;
        highScore = AssetLoader.prefs.getInteger("high_score", 0);

        lives = 3;
        extraLives = 0;

        spawnCounts.clear();
        gameObjects.clear();
        spawningObjects.clear();
        dyingObjects.clear();

        collider.clear();

        spawnPlayer();
        populateMushrooms(minMushrooms + AssetLoader.rng.nextInt(5));
        EventPump.publish(Event.NewGame);
    }

    private void nextLevel() {
        level += 1;
        EventPump.publish(Event.NextLevel);
    }

    private void updateHighScore() {
        if (highScore < score) {
            highScore = score;
            AssetLoader.prefs.putInteger("high_score", highScore);
            AssetLoader.prefs.flush();
        }
    }

    public int getLives() {
        return lives;
    }

    private void modifyLives(int delta) {
        lives += delta;
    }

    public void gameOver() {
        updateHighScore();
        newGame();
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
        for (GameObject gameObj : gameObjects) {
            if (!(gameObj instanceof Mushroom)) {
                collider.collide(gameObj);
            }
        }

        spawnQualifyingEntities();

        if (isGameOver) {
            gameOver();
        }
    }

    private void onSpawn(GameObject gameObj) {
        spawningObjects.add(gameObj);
    }

    private void respawnPlayer() {
        bullet.die();

        if (0 < lives) {
            spawnPlayer();
        }
        else {
            EventPump.publish(Event.GameOver);
        }
    }

    private void checkNextLevel() {
        int totalSegments = getSpawnCount("centipede_head") + getSpawnCount("centipede_body");

        if (0 < totalSegments) {
            nextLevel();
        }
    }

    private void onDeath(GameObject gameObj) {
        dyingObjects.add(gameObj);

        if (gameObj instanceof Player) {
            respawnPlayer();
        }

        if (gameObj instanceof CentipedeHead) {
            checkNextLevel();
        }
    }

    private void onScore(GameObject gameObj) {
        if (gameObj.wasKilled()) {
            score += gameObj.getScoreValue();

            int newLives = score / lifeThreshold;
            if (extraLives < newLives) {
                extraLives = newLives;
                modifyLives(+1);
            }
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
}
