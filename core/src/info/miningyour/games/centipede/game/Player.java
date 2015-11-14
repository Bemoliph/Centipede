package info.miningyour.games.centipede.game;

import com.badlogic.gdx.math.Rectangle;
import info.miningyour.games.centipede.events.EventListener;
import info.miningyour.games.centipede.events.EventPump;
import info.miningyour.games.centipede.events.EventType;
import info.miningyour.games.centipede.utils.InputState;

public class Player extends GameObject implements EventListener {

    private static boolean isInvincible = false;

    private static final float speed = 128;

    private float minX;
    private float maxX;
    private float minY;
    private float maxY;

    public Player(float x, float y) {
        super("player", "player", new Rectangle(x, y, 7.0f, 8.0f), 1, 0);

        minX = 0.0f;
        maxX = 240.0f - getWidth();
        minY = 8.0f;
        maxY = minY + 6.0f * 8.0f - getHeight();

        explosionSize = ExplosionSize.Large;

        EventPump.subscribe(EventType.Input, this);
        EventPump.subscribe(EventType.Invulnerability, this);
    }

    private boolean canDamagePlayer(GameObject gameObj) {
        return !(gameObj instanceof Bullet)
               && !(gameObj instanceof Mushroom)
               && !(gameObj instanceof Explosion)
               && !(gameObj instanceof ScorePopup);
    }

    @Override
    public void onCollision(GameObject gameObj) {
        if (!isInvincible && canDamagePlayer(gameObj)) {
            this.damage();
        }
    }

    @Override
    public void update(float deltaTime) {
        setX(Math.max(minX, Math.min(maxX, getX())));
        setY(Math.max(minY, Math.min(maxY, getY())));
    }

    @Override
    public void die() {
        super.die();
        EventPump.unsubscribe(EventType.Input, this);
        EventPump.unsubscribe(EventType.Invulnerability, this);
    }

    @Override
    public void onEvent(EventType event, Object obj) {
        if (event == EventType.Input && obj instanceof InputState) {
            InputState state = (InputState) obj;

            velocity.x = state.getMoveRight() ? 1 : 0;
            velocity.x -= state.getMoveLeft() ? 1 : 0;
            velocity.y = state.getMoveUp() ? 1 : 0;
            velocity.y -= state.getMoveDown() ? 1 : 0;

            velocity.setLength(speed);
        }
        else if (event == EventType.Invulnerability) {
            isInvincible = !isInvincible;
        }
    }
}
