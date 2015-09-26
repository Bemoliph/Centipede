package info.miningyour.games.centipede.game;

import com.badlogic.gdx.math.Rectangle;
import info.miningyour.games.centipede.utils.Event;
import info.miningyour.games.centipede.utils.EventListener;
import info.miningyour.games.centipede.utils.EventPump;
import info.miningyour.games.centipede.utils.InputState;

public class Player extends GameObject implements EventListener {

    private static final int speed = 128;

    private int minX;
    private int maxX;
    private int minY;
    private int maxY;

    public Player(float x, float y) {
        super("player", "player", new Rectangle(x, y, 7, 8), 1);

        minX = 0;
        maxX = 240 - getWidth();
        minY = 8;
        maxY = minY + 6 * 8 - getHeight();

        EventPump.subscribe(Event.Input, this);
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
