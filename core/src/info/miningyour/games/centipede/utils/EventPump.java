package info.miningyour.games.centipede.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EventPump {

    private static HashMap<Event, List<EventListener>> subscriptions = new HashMap<Event, List<EventListener>>();

    public static void subscribe(Event event, EventListener subscriber) {
        if (!subscriptions.containsKey(event)) {
            subscriptions.put(event, new ArrayList<EventListener>());
        }

        subscriptions.get(event).add(subscriber);
    }

    public static void unsubscribe(Event event, EventListener subscriber) {
        if (subscriptions.containsKey(event)) {
            List<EventListener> listeners = subscriptions.get(event);
            listeners.remove(subscriber);

            if (listeners.isEmpty()) {
                subscriptions.remove(event);
            }
        }
    }

    public static void publish(Event event, Object obj) {
        if (subscriptions.containsKey(event)) {
            for (EventListener listener : subscriptions.get(event)) {
                listener.onEvent(event, obj);
            }
        }
    }

    public static void publish(Event event) {
        publish(event, null);
    }
}
