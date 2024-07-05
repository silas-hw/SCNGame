package com.mygdx.scngame.event;

public class GameEvent {

    /** The source object which initiated the event */
    private final Object source;

    public GameEvent(Object source) {this.source = source;}

    public Object getSource() {return this.source;}

    @Override
    public String toString() {
        return "GameEvent<Source: " + source.toString() + ">";
    }
}
