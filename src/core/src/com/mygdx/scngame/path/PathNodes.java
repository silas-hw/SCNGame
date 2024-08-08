package com.mygdx.scngame.path;

import com.badlogic.gdx.maps.MapObject;

import java.util.HashMap;

public final class PathNodes {
    private HashMap<String, PathNode> cache = new HashMap<>();

    public PathNode put(MapObject mapObject) {
        assert  mapObject.getProperties().get("type", String.class).equals("PathNode") :
                "MapObject for creating a PathNode must be of type PathNode";


        String id = mapObject.getProperties().get("id", String.class);

        if(cache.containsKey(id)) {
            return cache.get(id);
        }

        float x = mapObject.getProperties().get("x", Float.class);
        float y = mapObject.getProperties().get("y", Float.class);

        // put the node straight into the cache even if we don't know its neighbours yet
        // this is to allow for circular paths - such that a neighbour may reference this node itself
        PathNode node = new PathNode(id, null, x, y);
        cache.put(id, node);

        PathNode[] neighbours = new PathNode[4];
        neighbours[0] = this.put(mapObject.getProperties().get("NextNode1", MapObject.class));

        node.setNeighbours(neighbours);

        return node;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("PathNodes (" + cache.size() + ") :  \n");
        for(PathNode node : cache.values()) {
            sb.append(node.toString());
            sb.append("\n");
        }

        return sb.toString();
    }
}
