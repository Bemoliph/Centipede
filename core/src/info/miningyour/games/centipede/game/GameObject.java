package info.miningyour.games.centipede.game;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import info.miningyour.games.centipede.rendering.Animated;
import info.miningyour.games.centipede.utils.Event;
import info.miningyour.games.centipede.utils.EventPump;

public abstract class GameObject implements Animated {

    protected String name;

    protected String animationName;
    protected boolean isVisible;

    protected Rectangle boundingBox;
    protected Vector2 velocity;
    protected float rotation;
    protected float scale;

    protected int currentHP;
    protected int maxHP;

    public GameObject(String name, String spriteName, Rectangle boundingBox, int hp) {
        this.name = name;

        this.animationName = spriteName;
        this.isVisible = true;

        this.boundingBox = new Rectangle(boundingBox);
        this.velocity = new Vector2();
        this.rotation = 0.0f;
        this.scale = 1.0f;

        this.currentHP = hp;
        this.maxHP = hp;

        EventPump.publish(Event.Spawn, this);
    }

    public String getName() {
        return name;
    }

    public void update(float deltaTime) {
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    public boolean collidesWith(GameObject gameObj) {
        return Intersector.overlaps(gameObj.getBoundingBox(), boundingBox);
    }

    public void onCollision(GameObject gameObj) {
    }

    public boolean isAlive() {
        return 0 < currentHP;
    }

    public void damage() {
        currentHP--;

        if (!isAlive()) {
            die();
        }
    }

    public void die() {
        EventPump.publish(Event.Death, this);
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
