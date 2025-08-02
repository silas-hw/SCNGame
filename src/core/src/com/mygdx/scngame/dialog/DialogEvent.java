package com.mygdx.scngame.dialog;

public class DialogEvent {

    public enum EventType {
        DIALOG_START,
        DIALOG_END
    }

    public final DialogNode dialog;
    public final EventType eventType;

    public DialogEvent(DialogNode dialog, EventType type) {
        this.dialog = dialog;
        this.eventType = type;
    }
}
