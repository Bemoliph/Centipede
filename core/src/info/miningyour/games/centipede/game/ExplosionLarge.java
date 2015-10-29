package info.miningyour.games.centipede.game;

import com.badlogic.gdx.math.Rectangle;

public class ExplosionLarge extends Explosion {

    public ExplosionLarge(float x, float y) {
        super("explosion_wide", new Rectangle(x, y, 16.0f, 8.0f));
    }

}
