package com.mygdx.scngame.event;

public class StateChangeEvent<T extends Enum<T>> extends GameEvent{
    public T prevState;
    public T newState;


    public StateChangeEvent(Object source) {
        super(source);
    }
}
