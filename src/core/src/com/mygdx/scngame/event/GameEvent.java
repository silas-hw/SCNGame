package com.mygdx.scngame.event;

public class GameEvent {

    /** The source object which initiated the event */
    protected final Object source;

    /** the message/event itself */
    protected final Object payload;

    public GameEvent(Object source, Object payload) {
        this.source = source;
        this.payload = payload;
    }

    public GameEvent(Object source) {
        this(source, null);
    }

    public Object getSource() {return this.source;}

    public Object getPayload() {return this.payload;}

    @Override
    public String toString() {
        return "GameEvent<Source: " + source.toString() + ">";
    }
}
