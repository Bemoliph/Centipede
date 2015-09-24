package info.miningyour.games.centipede.game;

import com.badlogic.gdx.math.Rectangle;
import info.miningyour.games.centipede.utils.InputState;
import java.util.Observable;
import java.util.Observer;

public class Bullet extends GameObject implements Observer {

    private Player player;
    private boolean shouldFire;
    private boolean isFired;

    private int maxY;

    public Bullet(Player player) {
        super("bullet", new Rectangle(0, 0, 1, 6));

        this.player = player;
        this.shouldFire = false;
        this.isFired = false;

        this.maxY = 248 - getHeight();
        this.velocity.y = 386;

        reset();
    }

    private void alignToPlayer() {
        setX(player.getX() + player.getWidth() / 2.0f - 0.5f);
        setY(player.getY() + player.getHeight() - this.getHeight() / 2.0f);
    }

    public final void reset() {
        if (!shouldFire) {
            isFired = false;
        }

        alignToPlayer();
    }

    @Override
    public void update(float deltaTime) {
        if (shouldFire && !isFired()) {
            isFired = true;
        }

        if (isFired()) {
            setY(getY() + velocity.y * deltaTime);

            if (maxY < getY()) {
                reset();
            }
        }
        else {
            alignToPlayer();
        }
    }

    @Override
    public void onCollision(GameObject obj) {
        if (isFired() && !(obj instanceof Player)) {
            obj.onDamage();
            this.reset();
        }
    }

    public boolean isFired() {
        return isFired;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof InputState) {
            InputState state = (InputState) o;
            shouldFire = state.getShouldFire();
        }
    }
}
