package com.mygdx.scngame.dialog;

import com.badlogic.gdx.utils.Array;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;

public class DialogFile {
    private final HashMap<String, Array<DialogNode>> dialogNodes = new HashMap<>();

    public void addDialogNode(String groupName, DialogNode dialogNode) {
        if(!dialogNodes.containsKey(groupName)) {
            dialogNodes.put(groupName, new Array<>());
        }

        dialogNodes.get(groupName).add(dialogNode);
    }

    public DialogNode getDialogNode(String id) {
        Array<DialogNode> nodes = dialogNodes.get(id);

        if(nodes == null) return null;

        return nodes.random();
    }
}
