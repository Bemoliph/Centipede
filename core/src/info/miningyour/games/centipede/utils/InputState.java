package info.miningyour.games.centipede.utils;

public class InputState {

    private boolean moveUp;
    private boolean moveDown;
    private boolean moveLeft;
    private boolean moveRight;

    private boolean shouldFire;

    public InputState() {

        moveUp = false;
        moveDown = false;
        moveLeft = false;
        moveRight = false;

        shouldFire = false;
    }

    public void setMoveUp(boolean state) {
        moveUp = state;
        EventPump.publish(Event.Input, this);
    }

    public boolean getMoveUp() {
        return moveUp;
    }

    public void setMoveDown(boolean state) {
        moveDown = state;
        EventPump.publish(Event.Input, this);
    }

    public boolean getMoveDown() {
        return moveDown;
    }

    public void setMoveLeft(boolean state) {
        moveLeft = state;
        EventPump.publish(Event.Input, this);
    }

    public boolean getMoveLeft() {
        return moveLeft;
    }

    public void setMoveRight(boolean state) {
        moveRight = state;
        EventPump.publish(Event.Input, this);
    }

    public boolean getMoveRight() {
        return moveRight;
    }

    public void setShouldFire(boolean state) {
        shouldFire = state;
        EventPump.publish(Event.Input, this);
    }

    public boolean getShouldFire() {
        return shouldFire;
    }
}
