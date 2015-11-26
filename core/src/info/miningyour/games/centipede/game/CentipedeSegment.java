package info.miningyour.games.centipede.game;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.LinkedList;
import java.util.Queue;

public class CentipedeSegment extends GameObject {

    protected CentipedeSegment leadingSegment;
    protected CentipedeSegment trailingSegment;

    protected boolean isMovingHorizontally;
    protected boolean isMovingVertically;

    protected Queue<Vector2> oldPositions;
    protected Vector2 oldestPosition;

    public CentipedeSegment(String name, Rectangle boundingBox, int scoreValue, CentipedeSegment leadingSegment, int remainingSegments) {
        super(name, name, boundingBox, 1, scoreValue);

        isMovingHorizontally = false;
        isMovingVertically = false;

        oldPositions = new LinkedList<Vector2>();
        for (int i = 0; i < 4; i++) {
            oldPositions.add(new Vector2(boundingBox.x, boundingBox.y));
        }

        setLeadingSegment(leadingSegment);
        if (0 < remainingSegments) {
            setTrailingSegment(new CentipedeBody(boundingBox.x, boundingBox.y, this, remainingSegments - 1));
        }
    }

    @Override
    public void update(float deltaTime) {
        if (leadingSegment != null) {
            Vector2 recycledPosition = leadingSegment.getOldPosition();
            isMovingHorizontally = (int) getY() == (int) recycledPosition.y;
            isMovingVertically = (int) getX() == (int) recycledPosition.x;

            setX(recycledPosition.x);
            setY(recycledPosition.y);
        }

        oldPositions.add(oldestPosition);
        oldestPosition = null;
    }

    public Vector2 getOldPosition() {
        if (!oldPositions.isEmpty()) {
            return oldPositions.remove();
        }
        else {
            return null;
        }
    }

    public Queue<Vector2> getOldPositions() {
        return oldPositions;
    }

    @Override
    public void setX(float x) {
        if (oldestPosition == null) {
            oldestPosition = new Vector2();
        }

        oldestPosition.x = getX();

        super.setX(x);
    }

    @Override
    public void setY(float y) {
        if (oldestPosition == null) {
            oldestPosition = new Vector2();
        }

        oldestPosition.y = getY();

        super.setY(y);
    }

    public void setLeadingSegment(CentipedeSegment newLead) {
        leadingSegment = newLead;
    }

    public CentipedeSegment getLeadingSegment() {
        return leadingSegment;
    }

    public void setTrailingSegment(CentipedeSegment newTrail) {
        trailingSegment = newTrail;
    }

    public CentipedeSegment getTrailingSegment() {
        return trailingSegment;
    }

    public boolean isMovingHorizontally() {
        return isMovingHorizontally;
    }

    public boolean isMovingVertically() {
        return isMovingVertically;
    }

    public void promoteToHead() {
        if (!(this instanceof CentipedeHead)) {
            new CentipedeHead(this);
            this.despawn();
        }
    }

    public boolean isInChain(CentipedeSegment segment) {
        if (this == segment) {
            return true;
        }
        else if (trailingSegment != null) {
            return trailingSegment.isInChain(segment);
        }
        else {
            return false;
        }
    }

    @Override
    public void die() {
        super.die();

        /*
         * Adjust spawn window so they don't spawn "infinitely" instantly
         */
        CentipedeHead.lastSpawned = TimeUtils.millis();

        if (leadingSegment != null) {
            leadingSegment.setTrailingSegment(null);
            leadingSegment = null;
        }

        if (trailingSegment != null) {
            trailingSegment.promoteToHead();
            trailingSegment = null;
        }

        new Mushroom(GameWorld.normalizeMushroomCoordinate(getX()), GameWorld.normalizeMushroomCoordinate(getY()));
    }
}
