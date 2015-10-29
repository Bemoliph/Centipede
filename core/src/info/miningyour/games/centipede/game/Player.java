package info.miningyour.games.centipede.game;

import com.badlogic.gdx.math.Rectangle;
import info.miningyour.games.centipede.events.EventListener;
import info.miningyour.games.centipede.events.EventPump;
import info.miningyour.games.centipede.events.EventType;
import info.miningyour.games.centipede.utils.InputState;

public class Player extends GameObject implements EventListener {

    private static final float speed = 128;

    private float minX;
    private float maxX;
    private float minY;
    private float maxY;

    private Rectangle oldBoundingBox;

    public Player(float x, float y) {
        super("player", "player", new Rectangle(x, y, 7.0f, 8.0f), 1, 0);

        minX = 0.0f;
        maxX = 240.0f - getWidth();
        minY = 8.0f;
        maxY = minY + 6.0f * 8.0f - getHeight();

        oldBoundingBox = new Rectangle(boundingBox);

        EventPump.subscribe(EventType.Input, this);
    }

    @Override
    public void onCollision(GameObject gameObj) {
        if (!(gameObj instanceof Bullet) && !(gameObj instanceof Mushroom)) {
            this.damage();
        }
        else if (gameObj instanceof Mushroom) {
            if (velocity.x != 0.0f) {
                float oldX = oldBoundingBox.getX();
                float objX = gameObj.getX();
                float newX = oldX <= objX ? objX - getWidth() : objX + gameObj.getWidth();

                setX(newX);
            }

            if (velocity.y != 0.0f) {
                float oldY = oldBoundingBox.getY();
                float objY = gameObj.getY();
                float newY = oldY <= objY ? objY - getHeight() : objY + gameObj.getHeight();

                setY(newY);
            }
        }
    }

    @Override
    public void die() {
        EventPump.publish(EventType.Death, this);

        EventPump.unsubscribe(EventType.Input, this);
    }

    @Override
    public void update(float deltaTime) {
        oldBoundingBox.setX(getX());
        oldBoundingBox.setY(getY());

        setX(Math.max(minX, Math.min(maxX, getX() + velocity.x * deltaTime)));
        setY(Math.max(minY, Math.min(maxY, getY() + velocity.y * deltaTime)));
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
    }
}
