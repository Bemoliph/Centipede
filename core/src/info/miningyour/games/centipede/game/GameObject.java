package info.miningyour.games.centipede.game;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import info.miningyour.games.centipede.rendering.Animated;
import info.miningyour.games.centipede.rendering.GameRenderer;

public abstract class GameObject implements Animated {

    protected String animationName;
    protected boolean isVisible;

    private Rectangle boundingBox;
    protected Vector2 velocity;
    protected float rotation;
    protected float scale;

    public GameObject(String spriteName, Rectangle boundingBox) {
        this.animationName = spriteName;
        this.isVisible = true;

        this.boundingBox = new Rectangle(boundingBox);
        this.velocity = new Vector2();
        this.rotation = 0.0f;
        this.scale = 1.0f;

        GameRenderer.animatedObjects.add(this);
    }

    public void update(float deltaTime) {
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    public boolean collides(GameObject obj) {
        return Intersector.overlaps(obj.getBoundingBox(), boundingBox);
    }

    public void onCollision(GameObject obj) {
    }

    public void onDamage() {
    }

    @Override
    public String getAnimationName() {
        return animationName;
    }

    @Override
    public boolean isVisible() {
        return isVisible;
    }

    @Override
    public float getX() {
        return boundingBox.x;
    }

    public void setX(float x) {
        boundingBox.x = x;
    }

    @Override
    public float getY() {
        return boundingBox.y;
    }

    public void setY(float y) {
        boundingBox.y = y;
    }

    @Override
    public int getWidth() {
        return (int) boundingBox.width;
    }

    @Override
    public int getHeight() {
        return (int) boundingBox.height;
    }

    @Override
    public float getScaleX() {
        return scale;
    }

    @Override
    public float getScaleY() {
        return scale;
    }

    @Override
    public float getRotation() {
        return rotation;
    }
}
