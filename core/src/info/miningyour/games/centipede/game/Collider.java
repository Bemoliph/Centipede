package info.miningyour.games.centipede.game;

import com.badlogic.gdx.math.Rectangle;
import info.miningyour.games.centipede.utils.QuadTree;
import java.util.ArrayList;
import java.util.List;

public class Collider {

    private List<GameObject> objects;
    private QuadTree collisionTree;
    private List<GameObject> candidates;

    public Collider(Rectangle bounds) {
        objects = new ArrayList<GameObject>();
        collisionTree = new QuadTree(bounds);
        candidates = new ArrayList<GameObject>();
    }

    public void add(GameObject obj) {
        objects.add(obj);
        collisionTree.insert(obj);
    }

    public void remove(GameObject obj) {
        objects.remove(obj);
        update();
    }

    public void update() {
        /*
         * TODO: Allow for updating individual objects without full clears
         */
        collisionTree.clear();
        for (GameObject obj : objects) {
            collisionTree.insert(obj);
        }
    }

    public void collide(GameObject obj) {
        candidates.clear();
        collisionTree.retrieve(candidates, obj);

        for (GameObject candidate : candidates) {
            if (obj.collidesWith(candidate)) {
                obj.onCollision(candidate);
            }
        }
    }
}
