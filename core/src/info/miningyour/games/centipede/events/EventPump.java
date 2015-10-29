package info.miningyour.games.centipede.events;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class EventPump {

    private static HashMap<EventType, Set<EventListener>> subscriptions = new HashMap<EventType, Set<EventListener>>();
    private static Queue<PumpEvent> newChanges = new LinkedList<PumpEvent>();
    private static Queue<PumpEvent> oldChanges = new LinkedList<PumpEvent>();

    public static void pump() {
        Queue<PumpEvent> swapping = oldChanges;
        oldChanges = newChanges;
        newChanges = swapping;

        for (PumpEvent event : oldChanges) {
            switch (event.getPumpEventType()) {
                case Subscribe:
                    doSubscribe(event.getEventType(), (EventListener) event.getObject());
                    break;
                case Unsubscribe:
                    doUnsubscribe(event.getEventType(), (EventListener) event.getObject());
                    break;
                case Publish:
                    doPublish(event.getEventType(), event.getObject());
                    break;
            }
        }

        oldChanges.clear();
    }

    private static void enqueue(PumpEventType pumpType, EventType event, Object obj) {
        newChanges.add(new PumpEvent(pumpType, event, obj));
    }

    public static void subscribe(EventType event, EventListener subscriber) {
        enqueue(PumpEventType.Subscribe, event, subscriber);
    }

    public static void unsubscribe(EventType event, EventListener subscriber) {
        enqueue(PumpEventType.Unsubscribe, event, subscriber);
    }

    public static void publish(EventType event, Object obj) {
        enqueue(PumpEventType.Publish, event, obj);
    }

    public static void publish(EventType event) {
        publish(event, null);
    }

    private static void doSubscribe(EventType event, EventListener subscriber) {
        if (!subscriptions.containsKey(event)) {
            subscriptions.put(event, new HashSet<EventListener>());
        }

        subscriptions.get(event).add(subscriber);
    }

    private static void doUnsubscribe(EventType event, EventListener subscriber) {
        if (subscriptions.containsKey(event)) {
            Set<EventListener> listeners = subscriptions.get(event);
            listeners.remove(subscriber);

            if (listeners.isEmpty()) {
                subscriptions.remove(event);
            }
        }
    }

    private static void doPublish(EventType event, Object obj) {
        if (subscriptions.containsKey(event)) {
            for (EventListener listener : subscriptions.get(event)) {
                listener.onEvent(event, obj);
            }
        }
    }
}
