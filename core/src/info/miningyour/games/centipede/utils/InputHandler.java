package info.miningyour.games.centipede.utils;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import info.miningyour.games.centipede.events.EventPump;
import info.miningyour.games.centipede.events.EventType;

public class InputHandler implements InputProcessor {

    private InputState state;

    public InputHandler() {
        this.state = new InputState();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Keys.W:
                state.setMoveUp(true);
                return true;
            case Keys.S:
                state.setMoveDown(true);
                return true;
            case Keys.A:
                state.setMoveLeft(true);
                return true;
            case Keys.D:
                state.setMoveRight(true);
                return true;
            case Keys.NUMPAD_5:
                state.setShouldFire(true);
                return true;
            case Keys.F1:
                EventPump.publish(EventType.Invulnerability);
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Keys.W:
                state.setMoveUp(false);
                return true;
            case Keys.S:
                state.setMoveDown(false);
                return true;
            case Keys.A:
                state.setMoveLeft(false);
                return true;
            case Keys.D:
                state.setMoveRight(false);
                return true;
            case Keys.NUMPAD_5:
                state.setShouldFire(false);
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
