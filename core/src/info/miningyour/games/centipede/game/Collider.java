package info.miningyour.games.centipede.game;

import com.badlogic.gdx.math.Rectangle;
import info.miningyour.games.centipede.utils.QuadTree;
import java.util.LinkedList;
import java.util.List;

public class Collider {

    private List<GameObject> gameObjects;
    private QuadTree collisionTree;
    private List<GameObject> candidates;

    public Collider(Rectangle bounds) {
        gameObjects = new LinkedList<GameObject>();
        collisionTree = new QuadTree(bounds);
        candidates = new LinkedList<GameObject>();
    }

    public void add(GameObject gameObj) {
        gameObjects.add(gameObj);
        collisionTree.insert(gameObj);
    }

    public void remove(GameObject gameObj) {
        gameObjects.remove(gameObj);
        update();
    }

    public void clear() {
        gameObjects.clear();
        collisionTree.clear();
        candidates.clear();
    }

    public void update() {
        /*
         * TODO: Allow for updating individual objects without full clears
         */
        collisionTree.clear();
        for (GameObject gameObj : gameObjects) {
            collisionTree.insert(gameObj);
        }
    }

    public void collide(GameObject gameObj) {
        candidates.clear();
        collisionTree.retrieve(candidates, gameObj);

        for (GameObject candidate : candidates) {
            if (gameObj.collidesWith(candidate)) {
                gameObj.onCollision(candidate);
            }
        }
    }
}
