package info.miningyour.games.centipede.game;

import com.badlogic.gdx.math.Rectangle;

public class Mushroom extends GameObject {

    private static final String[] normalAnims = {"mushroom25",
                                                 "mushroom50",
                                                 "mushroom75",
                                                 "mushroom100"};

    private static final String[] poisonedAnims = {"poisoned_mushroom25",
                                                   "poisoned_mushroom50",
                                                   "poisoned_mushroom75",
                                                   "poisoned_mushroom100"};

    private boolean isPoisoned;

    public Mushroom(float x, float y) {
        super("mushroom", "mushroom", new Rectangle(x, y, 8, 8), normalAnims.length, 1);

        isPoisoned = false;
        updateAnimationName();
    }

    private void updateAnimationName() {
        int animIndex = currentHP - 1;

        if (isPoisoned()) {
            animationName = poisonedAnims[animIndex];
        }
        else {
            animationName = normalAnims[animIndex];
        }
    }

    public void setPoisoned(boolean isPoisoned) {
        this.isPoisoned = isPoisoned;
        updateAnimationName();
    }

    public boolean isPoisoned() {
        return isPoisoned;
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
