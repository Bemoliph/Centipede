package info.miningyour.games.centipede.events;

public class PumpEvent {

    private PumpEventType pumpType;
    private EventType event;
    private Object obj;

    public PumpEvent(PumpEventType pumpType, EventType event, Object obj) {
        this.pumpType = pumpType;
        this.event = event;
        this.obj = obj;
    }

    public PumpEventType getPumpEventType() {
        return pumpType;
    }

    public EventType getEventType() {
        return event;
    }

    public Object getObject() {
        return obj;
    }
}
