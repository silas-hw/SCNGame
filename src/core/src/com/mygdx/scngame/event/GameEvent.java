package com.mygdx.scngame.event;

public class GameEvent {

    /** The source object which initiated the event */
    protected final Object source;

    protected final Object payload;

    public enum Type {
        GAME_OVER,
        SOURCE_KILLED,
        DIALOG,
        CUSTOM
    }

    protected Type type;

    public GameEvent(Object source, Type type, Object payload) {
        this.source = source;
        this.type = type;
        this.payload = payload;
    }

    public GameEvent(Object source, Type type) {
        this(source, type, null);
    }

    public GameEvent(Object source) {
        this(source, null, null);
    }

    public Object getSource() {return this.source;}

    public Object getPayload() {return this.payload;}

    @Override
    public String toString() {
        return "GameEvent<Source: " + source.toString() + ">";
    }
}
