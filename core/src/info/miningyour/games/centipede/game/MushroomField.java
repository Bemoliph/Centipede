package info.miningyour.games.centipede.game;

import info.miningyour.games.centipede.rendering.GameRenderer;
import info.miningyour.games.centipede.utils.AssetLoader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

public class MushroomField {

    private final Random rng;

    private final int width;
    private final int height;
    private final int verticalPadding;
    private final int verticalOffset;
    private static final int mushroomSize = 8;

    private int currentMushrooms;
    private Mushroom[] mushrooms;

    public MushroomField(int width, int height) {
        rng = AssetLoader.rng;

        this.width = width;
        this.height = height;

        verticalPadding = 2;
        verticalOffset = 4;

        reset();
    }

    public final void reset() {
        mushrooms = new Mushroom[width * height];
        this.currentMushrooms = 0;
        populateMushrooms(45 - rng.nextInt(5));
    }

    private void populateMushrooms(int mushroomCount) {
        while (currentMushrooms < mushroomCount) {
            int x = mushroomSize * (rng.nextInt(width));
            int y = mushroomSize * (rng.nextInt(height - verticalPadding * 2) + verticalOffset);

            if (!isMushroomAt(x, y)) {
                addMushroom(x, y);
                currentMushrooms++;
            }
        }
    }

    public Mushroom getMushroom(float x, float y) {
        return mushrooms[getMushroomIndex(x, y)];
    }

    public Iterator<Mushroom> getMushrooms() {
        return Arrays.asList(mushrooms).iterator();
    }

    public void addMushroom(float x, float y) {
        mushrooms[getMushroomIndex(x, y)] = new Mushroom(x, y);
    }

    public void removeMushroom(float x, float y) {
        mushrooms[getMushroomIndex(x, y)] = null;
    }

    public boolean isMushroomAt(float x, float y) {
        return getMushroom(x, y) != null;
    }

    private int getMushroomIndex(float x, float y) {
        return (int) (y / mushroomSize - 1) * width + (int) (x / mushroomSize);
    }

    public boolean collides(GameObject obj) {
        return null != getMushroom(obj.getX(), obj.getY());
    }

    public void damageMushroom(float x, float y) {
        Mushroom mushroom = getMushroom(x, y);
        mushroom.onDamage();

        if (!mushroom.isAlive()) {
            removeMushroom(x, y);
            GameRenderer.animatedObjects.remove(mushroom);
        }
    }
}
