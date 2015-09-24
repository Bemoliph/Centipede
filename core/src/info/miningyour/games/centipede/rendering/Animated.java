package info.miningyour.games.centipede.rendering;

public interface Animated {

    public String getAnimationName();

    public boolean isVisible();

    public float getX();

    public float getY();

    public int getWidth();

    public int getHeight();

    public float getScaleX();

    public float getScaleY();

    public float getRotation();
}
