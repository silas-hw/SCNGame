package com.mygdx.scngame.event;

public class EventFactory {
    private EventFactory() {}

    public static GameEvent createDialogEvent(Object source, int dialogID) {
        GameEvent event = new GameEvent(source, GameEvent.Type.DIALOG);
        event.id = dialogID;
        return event;
    }
}
