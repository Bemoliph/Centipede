package info.miningyour.games.centipede.game;

import com.badlogic.gdx.math.Rectangle;
import info.miningyour.games.centipede.utils.QuadTree;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameWorld {

    private Rectangle bounds;

    private Random rng;
    private List<GameObject> gameObjects;
    private Collider collider;

    private Player player;
    private Bullet bullet;

    private int mushroomCount;
    private static final int mushroomSize = 8;

    public GameWorld(Rectangle bounds) {
        this.bounds = bounds;

        rng = new Random();
        gameObjects = new ArrayList<GameObject>();
        collider = new Collider(bounds);

        spawnPlayer(16, 16);

        mushroomCount = 0;
        populateMushrooms(45 - rng.nextInt(5));
    }

    private void spawnPlayer(float x, float y) {
        player = new Player(16, 16);

        gameObjects.add(player);
        collider.add(player);

        spawnBullet(player);
    }

    private void spawnBullet(Player player) {
        bullet = new Bullet(player);

        gameObjects.add(bullet);
        collider.add(bullet);
    }

    private boolean isMushroomAt(float x, float y) {
        for (GameObject obj : gameObjects) {
            if (obj instanceof Mushroom) {
                Mushroom mushroom = (Mushroom) obj;

                if (mushroom.getX() == x && mushroom.getY() == y) {
                    return true;
                }
            }
        }

        return false;
    }

    public void spawnMushroom(float x, float y) {
        Mushroom mushroom = new Mushroom(x, y);

        gameObjects.add(mushroom);
        collider.add(mushroom);
        mushroomCount++;
    }

    private void populateMushrooms(int populationCount) {
        while (mushroomCount < populationCount) {
            int x = mushroomSize * rng.nextInt((int) bounds.width / mushroomSize);
            int y = mushroomSize * (rng.nextInt((int) bounds.height / mushroomSize - 4) + 4);

            if (!isMushroomAt(x, y)) {
                spawnMushroom(x, y);
            }
        }
    }

    public void spawnFlea(float x, float y) {

    }

    public void spawnSpider(float x, float y) {

    }

    public void spawnCentipede(float x, float y, int segments) {

    }

    public void spawnScorpion(float x, float y) {

    }

    public Player getPlayer() {
        return player;
    }

    public Bullet getBullet() {
        return bullet;
    }

    public void update(float deltaTime) {
        player.update(deltaTime);
        bullet.update(deltaTime);

        //collider.update(player);
        collider.update();
        collider.collide(player);
        collider.collide(bullet);
    }

    public QuadTree getCollisionTree() {
        return collider.getCollisionTree();
    }
}
