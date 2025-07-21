package com.mygdx.scngame.event;

import java.util.LinkedList;
import java.util.Queue;

/*
An asyncrhonous event bus that publishes events to listeners on an update tick.
When this tick is received is up to the user, so events may be published in the middle
of the game loop.

This is ideal for events you want to rate limit.
 */
public class AsyncUpdateEventBus<E> extends BaseEventBus<E> {
    private final Queue<E> queue = new LinkedList<>();
    private final int frequency;

    public AsyncUpdateEventBus(int frequency) {
        assert frequency > 0 : "'frequency' must be greater than 0";
        this.frequency = frequency;
    }
    @Override
    public void publish(E event) {
        queue.add(event);
    }

    public void update(float delta) {
        for(int i = 0; i < frequency && !queue.isEmpty(); i++) {
            E event = queue.poll();

            for(EventListener<E> listener : listeners) {
                listener.onEvent(event);
            }
        }
    }
}
