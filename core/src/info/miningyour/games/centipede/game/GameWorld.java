package info.miningyour.games.centipede.game;

import com.badlogic.gdx.math.Rectangle;
import info.miningyour.games.centipede.utils.Event;
import info.miningyour.games.centipede.utils.EventListener;
import info.miningyour.games.centipede.utils.EventPump;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class GameWorld implements EventListener {

    private Rectangle bounds;

    private Random rng;
    private List<GameObject> gameObjects;
    private Collider collider;

    private HashMap<String, Integer> spawnCounts;
    private Player player;
    private Bullet bullet;

    private static final int mushroomSize = 8;

    public GameWorld(Rectangle bounds) {
        this.bounds = bounds;

        rng = new Random();
        gameObjects = new ArrayList<GameObject>();
        collider = new Collider(bounds);

        EventPump.subscribe(Event.Spawn, this);
        EventPump.subscribe(Event.Death, this);

        spawnCounts = new HashMap<String, Integer>();

        player = new Player(16, 16);
        bullet = new Bullet(player);

        populateMushrooms(50 - rng.nextInt(5));
    }

    private boolean isMushroomAt(float x, float y) {
        for (GameObject gameObj : gameObjects) {
            if (gameObj instanceof Mushroom) {
                Mushroom mushroom = (Mushroom) gameObj;

                if (mushroom.getX() == x && mushroom.getY() == y) {
                    return true;
                }
            }
        }

        return false;
    }

    private void populateMushrooms(int populationCount) {
        while (spawnCounts.getOrDefault("mushroom", 0) < populationCount) {
            int x = mushroomSize * rng.nextInt((int) bounds.width / mushroomSize);
            int y = mushroomSize * (rng.nextInt((int) bounds.height / mushroomSize - 4) + 4);

            if (!isMushroomAt(x, y)) {
                new Mushroom(x, y);
            }
        }
    }

    private boolean shouldSpawnFlea() {
        return spawnCounts.getOrDefault("mushroom", 0) < 50
               && spawnCounts.getOrDefault("flea", 0) == 0;
        //&& 1 < level;
    }

    public void spawnFlea() {
        int x = mushroomSize * rng.nextInt((int) bounds.width / mushroomSize);
        int y = mushroomSize * 31;

        int mushroomCount = 50 - spawnCounts.getOrDefault("mushroom", mushroomSize) + rng.nextInt(3);

        Flea flea = new Flea(x, y, mushroomCount);
    }

    public void spawnSpider(float x, float y) {

    }

    public void spawnCentipede(float x, float y, int segments) {

    }

    public void spawnScorpion(float x, float y) {

    }

    public void update(float deltaTime) {
        for (GameObject gameObj : gameObjects) {
            gameObj.update(deltaTime);
        }

        collider.update();
        collider.collide(player);
        collider.collide(bullet);
    }

    private void onSpawn(GameObject gameObj) {
        gameObjects.add(gameObj);
        collider.add(gameObj);
        spawnCounts.put(gameObj.getName(), spawnCounts.getOrDefault(gameObj.getName(), 0) + 1);
    }

    private void onDeath(GameObject gameObj) {
        gameObjects.remove(gameObj);
        collider.remove(gameObj);
        spawnCounts.put(gameObj.getName(), spawnCounts.get(gameObj.getName()) - 1);

        if (shouldSpawnFlea()) {
            spawnFlea();
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
        }
    }
}
