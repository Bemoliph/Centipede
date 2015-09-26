package info.miningyour.games.centipede.game;

import com.badlogic.gdx.math.Rectangle;
import info.miningyour.games.centipede.utils.Event;
import info.miningyour.games.centipede.utils.EventListener;
import info.miningyour.games.centipede.utils.EventPump;
import info.miningyour.games.centipede.utils.InputState;

public class Bullet extends GameObject implements EventListener {

    private Player player;
    private boolean shouldFire;
    private boolean isFired;

    private int maxY;

    public Bullet(Player player) {
        super("bullet", "bullet", new Rectangle(0, 0, 1, 6), 1);

        this.player = player;
        this.shouldFire = false;
        this.isFired = false;

        this.maxY = 248 - getHeight();
        this.velocity.y = 386;

        EventPump.subscribe(Event.Input, this);

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

    public boolean isFired() {
        return isFired;
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
    public void onCollision(GameObject gameObj) {
        if (isFired() && !(gameObj instanceof Player)) {
            gameObj.damage();
            this.reset();
        }
    }

    @Override
    public void onEvent(Event event, Object obj) {
        if (event == Event.Input && obj instanceof InputState) {
            InputState state = (InputState) obj;
            shouldFire = state.getShouldFire();
        }
    }
}
