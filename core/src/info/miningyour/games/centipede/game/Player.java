package info.miningyour.games.centipede.game;

import com.badlogic.gdx.math.Rectangle;
import info.miningyour.games.centipede.utils.Event;
import info.miningyour.games.centipede.utils.EventListener;
import info.miningyour.games.centipede.utils.EventPump;
import info.miningyour.games.centipede.utils.InputState;

public class Player extends GameObject implements EventListener {

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

        EventPump.subscribe(Event.Input, this);
    }

    @Override
    public void onCollision(GameObject gameObj) {
        if (!(gameObj instanceof Bullet) && !(gameObj instanceof Mushroom)) {
            this.damage();
        }
    }

    @Override
    public void die() {
        EventPump.publish(Event.Death, this);
        EventPump.publish(Event.GameOver);
    }

    @Override
    public void update(float deltaTime) {
        setX(Math.max(minX, Math.min(maxX, getX() + velocity.x * deltaTime)));
        setY(Math.max(minY, Math.min(maxY, getY() + velocity.y * deltaTime)));
    }

    @Override
    public void onEvent(Event event, Object obj) {
        if (event == Event.Input && obj instanceof InputState) {
            InputState state = (InputState) obj;

            velocity.x = state.getMoveRight() ? 1 : 0;
            velocity.x -= state.getMoveLeft() ? 1 : 0;
            velocity.y = state.getMoveUp() ? 1 : 0;
            velocity.y -= state.getMoveDown() ? 1 : 0;

            velocity.setLength(speed);
        }
    }
}
