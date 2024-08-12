package com.mygdx.scngame.map;

import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.XmlReader;

public class MyTmxMapLoader extends TmxMapLoader {

    public MyTmxMapLoader(InternalFileHandleResolver internalFileHandleResolver) {
        super(internalFileHandleResolver);
    }

    @Override
    protected void loadProperties (final MapProperties properties, XmlReader.Element element) {
        if (element == null) return;
        if (element.getName().equals("properties")) {
            for (XmlReader.Element property : element.getChildrenByName("property")) {
                final String name = property.getAttribute("name", null);
                String value = property.getAttribute("value", null);
                String type = property.getAttribute("type", null);
                if (value == null) {
                    value = property.getText();
                }
                if (type != null && type.equals("object")) {
                    // Wait until the end of [loadTiledMap] to fetch the object
                    try {
                        // Value should be the id of the object being pointed to
                        final int id = Integer.parseInt(value);
                        // Create [Runnable] to fetch object and add it to props
                        Runnable fetch = new Runnable() {
                            @Override
                            public void run () {
                                MapObject object = idToObject.get(id);
                                properties.put(name, object);
                            }
                        };
                        // [Runnable] should not run until the end of [loadTiledMap]
                        runOnEndOfLoadTiled.add(fetch);
                    } catch (Exception exception) {
                        throw new GdxRuntimeException(
                                "Error parsing property [\" + name + \"] of type \"object\" with value: [" + value + "]", exception);
                    }
                } else if (type != null && type.equals("class")) {
                    // A 'class' property is a property which is itself a set of properties
                    MapProperties classProperty = new MapProperties();
                    String className = property.getAttribute("propertytype");

                    classProperty.put("type", className);

                    // the actual properties of a 'class' property are stored as a new properties tag
                    loadProperties(classProperty, property.getChildByName("properties"));
                    properties.put(name, classProperty);

                } else {
                    Object castValue = castProperty(name, value, type);
                    properties.put(name, castValue);
                }
            }
        }
    }
}
