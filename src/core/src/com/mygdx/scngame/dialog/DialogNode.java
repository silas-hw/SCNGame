package com.mygdx.scngame.dialog;

import com.badlogic.gdx.utils.Array;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class DialogNode implements Iterable<DialogMessage>  {
    public String id;
    public final Array<DialogMessage> messages = new Array<>();

    @NotNull
    @Override
    public Iterator<DialogMessage> iterator() {
        return messages.iterator();
    }
}
