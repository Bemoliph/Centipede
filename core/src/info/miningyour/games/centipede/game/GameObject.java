package info.miningyour.games.centipede.game;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import info.miningyour.games.centipede.events.EventPump;
import info.miningyour.games.centipede.events.EventType;
import info.miningyour.games.centipede.rendering.Animated;

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
    protected int scoreValue;
    protected ExplosionSize explosionSize;

    public GameObject(String name, String spriteName, Rectangle boundingBox, int hp, int scoreValue) {
        this.name = name;

        this.animationName = spriteName;
        this.isVisible = true;

        this.boundingBox = new Rectangle(boundingBox);
        this.velocity = new Vector2();
        this.rotation = 0.0f;
        this.scale = 1.0f;

        this.currentHP = hp;
        this.maxHP = hp;
        this.scoreValue = scoreValue;
        this.explosionSize = ExplosionSize.Small;

        EventPump.publish(EventType.Spawn, this);
    }

    public String getName() {
        return name;
    }

    public int getScoreValue() {
        return scoreValue;
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

    public boolean isDamaged() {
        return currentHP < maxHP;
    }

    public void damage() {
        currentHP--;

        if (!isAlive()) {
            die();
        }
    }

    public void die() {
        explode();
        EventPump.publish(EventType.Death, this);
        EventPump.publish(EventType.Score, getScoreValue());
    }

    public void despawn() {
        EventPump.publish(EventType.Death, this);
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
    public float getCenterX() {
        return getX() + getWidth() / 2.0f;
    }

    public void setCenterX(float x) {
        setX(x - getWidth() / 2.0f);
    }

    @Override
    public float getCenterY() {
        return getY() + getHeight() / 2.0f;
    }

    public void setCenterY(float y) {
        setY(y - getHeight() / 2.0f);
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

    public Vector2 getVelocity() {
        return velocity;
    }

    public void explode() {
        Explosion explosion;

        switch (explosionSize) {
            case Small:
                explosion = new ExplosionSmall(0, 0);
                break;
            case Large:
                explosion = new ExplosionLarge(0, 0);
                break;
            default:
                explosion = new ExplosionSmall(0, 0);
                break;
        }

        explosion.setCenterX(getCenterX());
        explosion.setCenterY(getCenterY());
    }
}
