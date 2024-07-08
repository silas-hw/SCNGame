package com.mygdx.scngame.event;

public class GameEvent {

    /** The source object which initiated the event */
    protected final Object source;

    public enum Type {
        GAME_OVER,
        SOURCE_KILLED,
        CUSTOM
    }

    protected Type type;

    public GameEvent(Object source, Type type) {
        this.source = source;
        this.type = type;
    }

    public Object getSource() {return this.source;}

    @Override
    public String toString() {
        return "GameEvent<Source: " + source.toString() + ">";
    }
}
