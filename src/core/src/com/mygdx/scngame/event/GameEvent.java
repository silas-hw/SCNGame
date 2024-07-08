package com.mygdx.scngame.event;

public class GameEvent {

    /** The source object which initiated the event */
    protected final Object source;

    /**
     * Used by CUSTOM events. This allows you to describe some data to be sent by your custom event type.
     * Checking the specific type of event can then be done by an <code>instanceof</code> check on the payload.
     */
    protected final Object payload;

    /**
     * Some events are simple enough to just be described by an id. For example, a DIALOG_START
     * event just involves dispatching the dialog id to be loaded into the dialog system. Having just
     * a plain old int stored here prevents an unneeded instanceof check on the payload.
     * <p>
     * All non 'CUSTOM' events make use of a simple integer ID as opposed to an Object payload.
     */
    public int id = 0;

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
