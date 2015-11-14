package info.miningyour.games.centipede.game;

import com.badlogic.gdx.math.Rectangle;
import info.miningyour.games.centipede.events.EventPump;
import info.miningyour.games.centipede.events.EventType;
import info.miningyour.games.centipede.utils.AssetLoader;

public class Explosion extends GameObject {

    private float duration;
    private float elapsed;

    public Explosion(String name, Rectangle boundingBox) {
        super(name, name, boundingBox, 1, 0);

        duration = AssetLoader.getAnimation(name).getAnimationDuration();
        elapsed = 0.0f;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        elapsed += deltaTime * 2.0f;

        if (duration < elapsed) {
            despawn();
        }
    }

    @Override
    public void despawn() {
        super.despawn();
        EventPump.publish(EventType.ExplosionEnd);
    }
}
