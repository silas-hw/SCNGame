package com.mygdx.scngame.dialog;

import com.badlogic.gdx.utils.Array;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;

public class DialogFile {
    private final HashMap<String, DialogGroup> dialogGroups = new HashMap<>();

    public void addDialogGroup(DialogGroup group) {
        dialogGroups.put(group.name, group);
    }

    public DialogNode getDialogNode(String id, Set<String> storyFlags) {
        DialogGroup group = dialogGroups.get(id);

        if(group == null) return null;

        return group.getNode(storyFlags);
    }
}
