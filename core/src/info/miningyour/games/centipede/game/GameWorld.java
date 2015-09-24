package info.miningyour.games.centipede.game;

import com.badlogic.gdx.math.Rectangle;
import info.miningyour.games.centipede.utils.QuadTree;
import java.util.Iterator;

public class GameWorld {

    private Rectangle bounds;

    private Collider collider;
    private MushroomField mushField;
    private Player player;
    private Bullet bullet;

    public GameWorld(Rectangle bounds) {
        this.bounds = bounds;

        collider = new Collider(bounds);
        mushField = new MushroomField(30, 30);
        player = new Player(16, 16);
        bullet = new Bullet(player);

        collider.add(player);
        collider.add(bullet);
        Iterator<Mushroom> mushIter = mushField.getMushrooms();
        while (mushIter.hasNext()) {
            Mushroom mushroom = mushIter.next();
            if (mushroom != null) {
                collider.add(mushroom);
            }
        }
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
