package info.miningyour.games.centipede.game;

import com.badlogic.gdx.math.Rectangle;

public class Mushroom extends GameObject {

    private static final String[] animationNames = {"mushroom25",
                                                    "mushroom50",
                                                    "mushroom75",
                                                    "mushroom100"};

    public Mushroom(float x, float y) {
        super("mushroom", new Rectangle(x, y, 8, 8), animationNames.length);

        updateAnimationName();
    }

    private void updateAnimationName() {
        animationName = animationNames[currentHP - 1];
    }

    @Override
    public void damage() {
        currentHP--;

        if (isAlive()) {
            updateAnimationName();
        }
        else {
            die();
        }
    }
}
