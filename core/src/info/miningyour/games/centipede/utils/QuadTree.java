package info.miningyour.games.centipede.utils;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import info.miningyour.games.centipede.game.GameObject;
import java.util.ArrayList;
import java.util.List;

public class QuadTree {

    private static int MAX_ITEMS = 4;
    private static int MAX_DEPTH = 3;

    private Rectangle bounds;
    private int depth;
    private List<GameObject> items;
    private QuadTree[] nodes;

    public QuadTree(Rectangle bounds, int depth, QuadTree parent) {
        this.bounds = bounds;
        this.depth = depth;
        this.items = new ArrayList<GameObject>();
        this.nodes = new QuadTree[4];
    }

    public QuadTree(Rectangle bounds) {
        this(bounds, 0, null);
    }

    public void clear() {
        items.clear();

        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] != null) {
                nodes[i].clear();
                nodes[i] = null;
            }
        }
    }

    private void split() {
        int subWidth = (int) (bounds.getWidth() / 2);
        int subHeight = (int) (bounds.getHeight() / 2);
        int x = (int) bounds.getX();
        int y = (int) bounds.getY();

        nodes[0] = new QuadTree(new Rectangle(x + subWidth, y, subWidth, subHeight), depth + 1, this);
        nodes[1] = new QuadTree(new Rectangle(x, y, subWidth, subHeight), depth + 1, this);
        nodes[2] = new QuadTree(new Rectangle(x, y + subHeight, subWidth, subHeight), depth + 1, this);
        nodes[3] = new QuadTree(new Rectangle(x + subWidth, y + subHeight, subWidth, subHeight), depth + 1, this);
    }

    private int getIndexForObject(GameObject obj) {
        return getIndexForObject(obj.getBoundingBox());
    }

    private int getIndexForObject(Rectangle rect) {

        double verticalMidpoint = bounds.getX() + (bounds.getWidth() / 2);
        double horizontalMidpoint = bounds.getY() + (bounds.getHeight() / 2);

        boolean fitsInLeftQuad = rect.getX() < verticalMidpoint
                                 && rect.getX() + rect.getWidth() < verticalMidpoint;
        boolean fitsInRightQuad = rect.getX() > verticalMidpoint;

        boolean fitsInTopQuad = rect.getY() < horizontalMidpoint
                                && rect.getY() + rect.getHeight() < horizontalMidpoint;
        boolean fitsInBottomQuad = rect.getY() > horizontalMidpoint;

        int index = -1; // Won't fit; belongs to parent

        if (fitsInLeftQuad) {
            if (fitsInTopQuad) {
                index = 1; // Fits top left
            }
            else if (fitsInBottomQuad) {
                index = 2; // Fits bottom left
            }
        }
        else if (fitsInRightQuad) {
            if (fitsInTopQuad) {
                index = 0; // Fits top right
            }
            else if (fitsInBottomQuad) {
                index = 3; // Fits bottom right
            }
        }

        return index;
    }

    private boolean hasSubtrees() {
        return nodes[0] != null;
    }

    private boolean isOverfilled() {
        return MAX_ITEMS < items.size();
    }

    private boolean isMaxDepth() {
        return MAX_DEPTH <= depth;
    }

    private boolean fitsInSubtree(int index) {
        return index != -1;
    }

    private void redistributeItems() {
        for (int i = 0; i < items.size();) {
            int index = getIndexForObject(items.get(i));

            if (fitsInSubtree(index)) {
                nodes[index].insert(items.remove(i));

            }
            else {
                i++;
            }
        }
    }

    public void insert(GameObject obj) {
        if (hasSubtrees()) {
            int index = getIndexForObject(obj);

            if (fitsInSubtree(index)) {
                nodes[index].insert(obj);
                return;
            }
        }

        items.add(obj);

        if (isOverfilled() && !isMaxDepth()) {
            if (!hasSubtrees()) {
                split();
            }

            redistributeItems();
        }
    }

    public List<GameObject> retrieve(List<GameObject> selectedItems, GameObject obj) {
        retrieve(selectedItems, obj.getBoundingBox());
        selectedItems.remove(obj); // Don't collide with self

        return selectedItems;
    }

    public List<GameObject> retrieve(List<GameObject> selectedItems, Rectangle rect) {
        int index = getIndexForObject(rect);

        if (hasSubtrees() && fitsInSubtree(index)) {
            nodes[index].retrieve(selectedItems, rect);
        }

        selectedItems.addAll(items);

        return selectedItems;
    }

    public void render(ShapeRenderer shapes) {
        shapes.rect(bounds.x, bounds.y, bounds.width, bounds.height);

        if (hasSubtrees()) {
            for (QuadTree subtree : nodes) {
                subtree.render(shapes);
            }
        }
    }
}
