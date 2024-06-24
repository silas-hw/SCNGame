package com.mygdx.scngame.scene;

import com.badlogic.gdx.utils.Array;
import com.dongbat.jbump.World;
import com.mygdx.scngame.entity.Entity;

import java.util.Comparator;

public class Scene {
    protected Array<Entity> entities;
    protected Comparator<Entity> renderComparator;

    public Scene() {
        entities = new Array<>();
        renderComparator = new YComparator();
    }

    public update(World<?> world, float delta) {
        
    }

    public static class YComparator implements Comparator<Entity> {

        @Override
        public int compare(Entity entity, Entity other) {
            return Float.compare(entity.position.y, other.position.y);
        }

        @Override
        public boolean equals(Object o) {
            return false;
        }
    }

}
