package info.miningyour.games.centipede.game;

import com.badlogic.gdx.math.Rectangle;
import info.miningyour.games.centipede.utils.InputState;
import java.util.Observable;
import java.util.Observer;

public class Player extends GameObject implements Observer {

    private static final int speed = 128;

    private int minX;
    private int maxX;
    private int minY;
    private int maxY;

    public Player(float x, float y) {
        super("player", new Rectangle(x, y, 7, 8));

        minX = 0;
        maxX = 240 - getWidth();
        minY = 8;
        maxY = minY + 6 * 8 - getHeight();
    }

    @Override
    public void update(float deltaTime) {
        setX(Math.max(minX, Math.min(maxX, getX() + velocity.x * deltaTime)));
        setY(Math.max(minY, Math.min(maxY, getY() + velocity.y * deltaTime)));
    }

    @Override
    public void onCollision(GameObject obj) {

    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof InputState) {
            InputState state = (InputState) o;

            velocity.x = state.getMoveRight() ? 1 : 0;
            velocity.x -= state.getMoveLeft() ? 1 : 0;
            velocity.y = state.getMoveUp() ? 1 : 0;
            velocity.y -= state.getMoveDown() ? 1 : 0;

            velocity.setLength(speed);
        }
    }
}
