package info.miningyour.games.centipede.game;

import com.badlogic.gdx.math.Rectangle;

public class Mushroom extends GameObject {

    private static final String[] animationNames = {"mushroom100",
                                                    "mushroom75",
                                                    "mushroom50",
                                                    "mushroom25"};

    private int currentHP;
    private static final int maxHP = animationNames.length;

    public Mushroom(float x, float y) {
        super("mushroom", new Rectangle(x, y, 8, 8));

        currentHP = 0;

        updateAnimationName();
    }

    private void updateAnimationName() {
        animationName = animationNames[currentHP];
    }

    public boolean isAlive() {
        return currentHP < maxHP;
    }

    @Override
    public void onDamage() {
        currentHP++;

        if (isAlive()) {
            updateAnimationName();
        }
    }
}
