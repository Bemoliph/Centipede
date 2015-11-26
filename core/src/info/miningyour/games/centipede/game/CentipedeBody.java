package info.miningyour.games.centipede.game;

import com.badlogic.gdx.math.Rectangle;

public class CentipedeBody extends CentipedeSegment {

    public CentipedeBody(float x, float y, CentipedeSegment leadingSegment, int remainingSegments) {
        super("centipede_body", new Rectangle(x, y, 8.0f, 8.0f), 10, leadingSegment, remainingSegments);
    }
}
