package info.miningyour.games.centipede.utils;

import info.miningyour.games.centipede.game.Bullet;
import info.miningyour.games.centipede.game.Player;
import java.util.Observable;

public class InputState extends Observable {

    private boolean moveUp;
    private boolean moveDown;
    private boolean moveLeft;
    private boolean moveRight;

    private boolean shouldFire;

    public InputState(Player player, Bullet bullet) {
        addObserver(player);
        addObserver(bullet);

        moveUp = false;
        moveDown = false;
        moveLeft = false;
        moveRight = false;

        shouldFire = false;
    }

    public void setMoveUp(boolean state) {
        moveUp = state;
        setChanged();
        notifyObservers();
    }

    public boolean getMoveUp() {
        return moveUp;
    }

    public void setMoveDown(boolean state) {
        moveDown = state;
        setChanged();
        notifyObservers();
    }

    public boolean getMoveDown() {
        return moveDown;
    }

    public void setMoveLeft(boolean state) {
        moveLeft = state;
        setChanged();
        notifyObservers();
    }

    public boolean getMoveLeft() {
        return moveLeft;
    }

    public void setMoveRight(boolean state) {
        moveRight = state;
        setChanged();
        notifyObservers();
    }

    public boolean getMoveRight() {
        return moveRight;
    }

    public void setShouldFire(boolean state) {
        shouldFire = state;
        setChanged();
        notifyObservers();
    }

    public boolean getShouldFire() {
        return shouldFire;
    }
}
