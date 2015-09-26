package info.miningyour.games.centipede.game;

import com.badlogic.gdx.math.Rectangle;

public class Flea extends GameObject {

    private int mushroomCount;
    private int minY;

    public Flea(float x, float y, int mushroomCount) {
        super("flea", "flea", new Rectangle(x, y, 9, 8), 1);

        this.mushroomCount = mushroomCount;

        velocity.set(0, -128);
        minY = 0;
    }

    @Override
    public void update(float deltaTime) {
        setY(getY() + velocity.y * deltaTime);

        if (getY() < minY) {
            die();
        }
    }
}
