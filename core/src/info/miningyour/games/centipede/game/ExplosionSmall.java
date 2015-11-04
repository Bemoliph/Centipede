package info.miningyour.games.centipede.game;

import com.badlogic.gdx.math.Rectangle;

public class ExplosionSmall extends Explosion {

    public ExplosionSmall(float x, float y) {
        super("explosion_small", new Rectangle(x, y, 8.0f, 8.0f));
    }
}
