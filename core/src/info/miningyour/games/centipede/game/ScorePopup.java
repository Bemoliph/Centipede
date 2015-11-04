package info.miningyour.games.centipede.game;

import com.badlogic.gdx.math.Rectangle;

public class ScorePopup extends GameObject {

    private static final float duration = 3.0f;
    private float elapsed;

    public ScorePopup(String name, float x, float y) {
        super(name, name, new Rectangle(x, y, 11, 5), 1, 0);

        elapsed = 0.0f;
    }

    @Override
    public void update(float deltaTime) {
        elapsed += deltaTime;

        if (duration < elapsed) {
            despawn();
        }
    }
}
