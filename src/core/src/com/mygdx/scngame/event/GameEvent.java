package com.mygdx.scngame.event;

public class GameEvent {

    /** The source object which initiated the event */
    private final Object source;

    public enum EventType {
        ENTITY_EVENT,
        PLAYER_EVENT,
        DIALOG_EVENT,
        ENV_EVENT,
        DEFAULT
    }

    protected final EventType type = EventType.DEFAULT;

    public GameEvent(Object source) {this.source = source;}

    public Object getSource() {return this.source;}

    public EventType getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return "GameEvent<Source: " + source.toString() + ", Type: " + type + ">";
    }
}
