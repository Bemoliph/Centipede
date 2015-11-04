package info.miningyour.games.centipede.game;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import info.miningyour.games.centipede.events.EventListener;
import info.miningyour.games.centipede.events.EventPump;
import info.miningyour.games.centipede.events.EventType;
import info.miningyour.games.centipede.utils.InputState;

public class Bullet extends GameObject implements EventListener {

    private Player player;

    private boolean shouldFire;
    private boolean isFired;
    private Vector2 firePosition;

    private float maxY;

    public Bullet(Player player) {
        super("bullet", "bullet", new Rectangle(0.0f, 0.0f, 1.0f, 6.0f), 1, 0);

        this.player = player;

        this.shouldFire = false;
        this.isFired = false;
        this.firePosition = new Vector2();

        this.maxY = 248.0f - getHeight();
        this.velocity.y = 386.0f;

        EventPump.subscribe(EventType.Input, this);

        reset();
    }

    @Override
    public void die() {
        super.die();

        EventPump.unsubscribe(EventType.Input, this);
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

    private void fire() {
        isFired = true;

        firePosition.x = getX();
        firePosition.y = getY();
    }

    @Override
    public void update(float deltaTime) {
        if (shouldFire && !isFired()) {
            fire();
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

    private boolean canDamage(GameObject gameObj) {
        return !(gameObj instanceof Player)
               && !(gameObj instanceof Explosion)
               && !(gameObj instanceof ScorePopup);
    }

    @Override
    public void onCollision(GameObject gameObj) {
        if (isFired() && canDamage(gameObj)) {
            if (gameObj instanceof Spider) {
                Spider spider = (Spider) gameObj;

                float distance = (float) Math.sqrt(Math.pow(firePosition.x - spider.getX(), 2.0f) + Math.pow(firePosition.y - spider.getY(), 2.0f));
                spider.setShotDistance(distance);

                spider.damage();
            }
            else {
                gameObj.damage();
            }

            this.reset();
        }
    }

    @Override
    public void onEvent(EventType event, Object obj) {
        if (event == EventType.Input && obj instanceof InputState) {
            InputState state = (InputState) obj;
            shouldFire = state.getShouldFire();
        }
    }
}
