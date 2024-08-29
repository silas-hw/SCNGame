package com.mygdx.scngame.map;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.World;
import com.mygdx.scngame.entity.context.EntityContext;
import com.mygdx.scngame.entity.npc.NPC;
import com.mygdx.scngame.entity.sprite.AnimatedSpriteEntity;
import com.mygdx.scngame.entity.sprite.SpriteEntity;
import com.mygdx.scngame.entity.trigger.Trigger;
import com.mygdx.scngame.event.DialogEventBus;
import com.mygdx.scngame.event.MapChangeEventBus;
import com.mygdx.scngame.event.SaveEventBus;
import com.mygdx.scngame.path.PathNode;
import com.mygdx.scngame.path.PathNodes;
import com.mygdx.scngame.physics.Box;
import com.mygdx.scngame.physics.DamageBox;
import com.mygdx.scngame.physics.InteractBox;
import com.mygdx.scngame.physics.TerrainBox;

import java.util.*;

/**
 * Parses a tile map and loads in map objects into:
 * <ul>
 *     <li>
 *         {@link com.dongbat.jbump.World} for physics related objects
 *     </li>
 *
 *      <li>
 *          {@link com.mygdx.scngame.entity.context.EntityContext} for entities
 *      </li>
 * </ul>
 */
public class MapObjectLoader {

    private World<Box> world;
    private EntityContext entityContext;
    private Map<String, Vector2> spawnLocations;
    private TextureAtlas animAtlas;
    PathNodes pathNodes;

    private AssetManager assets;

    private DialogEventBus dialogBus;
    private MapChangeEventBus mapBus;

    public PathNodes getPathNodes() {return pathNodes;}
    public Map<String, Vector2> getSpawnLocations() {return spawnLocations;}

    public MapObjectLoader(TiledMap map, World<Box> world, EntityContext entityContext,
                           AssetManager assets, DialogEventBus dialogBus, MapChangeEventBus mapBus,
                           SaveEventBus bus) {
        this.world = world;
        this.entityContext = entityContext;
        this.spawnLocations = new HashMap<>();
        this.pathNodes = new PathNodes();

        this.mapBus = mapBus;

        this.dialogBus = dialogBus;

        this.assets = assets;
        this.animAtlas = assets.get("animations/animation_atlas.atlas");

        for(MapLayer layer : map.getLayers()) {
            if(layer instanceof TiledMapTileLayer) parseTileLayer((TiledMapTileLayer) layer);
            else parseObjectLayer(layer);
        }
    }

    private void parseTileLayer(TiledMapTileLayer layer) {
        int width = layer.getTileWidth();
        int height = layer.getTileHeight();

        for(int x = 0; x<layer.getWidth(); x++) {
            for(int y = 0 ; y<layer.getHeight(); y++) {

                // the x and y of objects within a tile layer or held by a tile are relative to that tile
                // also, if a tile is empty then the cell is null!
                TiledMapTileLayer.Cell cell = layer.getCell(x, y);
                if(cell != null) parseTile(cell.getTile(), x * width, y * height);
            }
        }
    }

    /**
     *
     * @param tile
     * @param x the x position of the tile in pixels
     * @param y the y position of the tile in pixels
     */
    public void parseTile(TiledMapTile tile, float x, float y) {
        for(MapObject obj : tile.getObjects()) {
            // offset the object by the tiles position
            // this is done because the objects x and y position are stored relative to the tile
            // but we want to add it to the world at its absolute position

            parseMapObject(obj, x, y);
        }
    }

    private void parseObjectLayer(MapLayer layer) {
        for(MapObject obj : layer.getObjects()){

            // x and y properties in object layers are absolute
            parseMapObject(obj, 0, 0);
        }
    }

    private void parseMapObject(MapObject obj, float offsetX, float offsetY) {
        MapProperties properties = obj.getProperties();

        float x = properties.get("x", float.class);
        float y = properties.get("y", float.class);
        float width = properties.get("width", float.class);
        float height = properties.get("height", float.class);
        String name = properties.get("Name", String.class);

        String type = obj.getProperties().get("type", String.class);
        if(type == null) type = "";

        switch(type) {
            case "Wall":
                Box newWall = new Box();
                newWall.solid = true;

                MapProperties wallCollisionLayer = obj.getProperties().get("CollisionLayer", MapProperties.class);
                setBoxCollisionLayers(wallCollisionLayer, newWall);

                world.add(new Item<>(newWall), x + offsetX, y + offsetY, width, height);
                break;

            case "DamageWall":
                String dtype = properties.get("DamageType", String.class);
                float damage = properties.get("damage", float.class);
                DamageBox newDamage = new DamageBox(damage, DamageBox.DamageType.valueOf(dtype));

                newDamage.solid = false;

                MapProperties damageCollisionLayer = obj.getProperties().get("CollisionLayer", MapProperties.class);
                setBoxCollisionLayers(damageCollisionLayer, newDamage);

                world.add(new Item<>(newDamage), x + offsetX, y + offsetY, width, height);

                break;

            case "Sign":
                String signDialogID = properties.get("DialogID", String.class);
                String signDialogFile = properties.get("DialogFile", String.class);

                InteractBox signBox = new InteractBox() {
                    @Override
                    public void interact() {
                        dialogBus.startDialog(signDialogFile, signDialogID);
                    }
                };

                world.add(new Item<>(signBox), x + offsetX, y + offsetY, width, height);
                break;

            case "Portal":
                MapProperties collisionMask = obj.getProperties().get("CollisionMask", MapProperties.class);
                int[] maskIndices = getMaskLayers(collisionMask);

                String portalMapPath = properties.get("Map", String.class);
                String portalSpawnID = properties.get("SpawnID", String.class);

                Trigger portal = new Trigger(
                        x, y, width, height, maskIndices,
                        new Runnable() {
                            @Override
                            public void run() {
                                mapBus.changeMap(portalMapPath, portalSpawnID);
                            }
                        }
                );

                entityContext.addEntity(portal);
                break;

            case "PathNode":
                pathNodes.put(obj);
                break;

            case "SpawnLocation":
                String spawnID = properties.get("SpawnID", String.class);
                Vector2 spawnLocation = new Vector2(x, y);

                spawnLocations.put(spawnID, spawnLocation);
                break;

            case "Door":
                String doorMapPath = properties.get("Map", String.class);
                String doorSpawnID = properties.get("SpawnID", String.class);

                InteractBox doorBox = new InteractBox() {

                    @Override
                    public void interact() {
                        mapBus.changeMap(doorMapPath, doorSpawnID);
                    }
                };

                world.add(new Item<>(doorBox), x + offsetX, y + offsetY, width, height);
                break;

            case "NPC":
                String npcDialogID = properties.get("DialogID", String.class);
                String npcDialogFile = properties.get("DialogFile", String.class);
                MapObject pathNode = properties.get("StartingNode", MapObject.class);

                String walkUpAnimPath = properties.get("walkUpAnim", String.class);
                String walkDownAnimPath = properties.get("walkDownAnim", String.class);
                String walkRightAnimPath = properties.get("walkRightAnim", String.class);

                assert walkUpAnimPath != null : "Walk Up animation not set for NPC: " + name;
                assert walkDownAnimPath != null : "Walk down animation not set for NPC: " + name;
                assert walkRightAnimPath != null : "Walk right animation not set for NPC: " + name;

                Float walkingSpeed = properties.get("walkingSpeed", Float.class);

                PathNode startingNode = pathNodes.put(pathNode);
                // will eventually be configured in tile map
                Animation.PlayMode playmode = Animation.PlayMode.LOOP;

                NPC.NPCBreed breed = new NPC.NPCBreed();
                breed.startingPathNode = startingNode;
                breed.dialogID = npcDialogID;
                breed.dialogFile = npcDialogFile;

                breed.walkDownAnim = new Animation<>(0.2f, animAtlas.findRegions(walkDownAnimPath), playmode);
                breed.walkUpAnim = new Animation<>(0.2f, animAtlas.findRegions(walkUpAnimPath), playmode);
                breed.walkRightAnim = new Animation<>(0.2f, animAtlas.findRegions(walkRightAnimPath), playmode);

                if(walkingSpeed != null) breed.walkingSpeed = walkingSpeed;

                NPC npc = new NPC(breed, dialogBus);

                entityContext.addEntity(npc);
                break;

            case "TerrainBox":
                float terrainSpeedCoeffient = properties.get("speedCoefficient", Float.class);
                MapProperties terrainCollisionLayer = properties.get("CollisionLayer", MapProperties.class);

                TerrainBox terrainBox = new TerrainBox(terrainSpeedCoeffient);
                setBoxCollisionLayers(terrainCollisionLayer, terrainBox);

                world.add(new Item<>(terrainBox), x + offsetX, y + offsetY, width, height);
                break;

            default:
                if(!(obj instanceof TiledMapTileMapObject)) return;

                TiledMapTile tile = ((TiledMapTileMapObject) obj).getTile();

                if(tile instanceof AnimatedTiledMapTile) {
                    float interval = (float) ((AnimatedTiledMapTile) tile).getAnimationIntervals()[0]/1000f;
                    entityContext.addEntity(new AnimatedSpriteEntity(((AnimatedTiledMapTile) tile).getFrameTiles(), interval, x, y));

                } else {
                    TextureRegion texture = ((TextureMapObject) obj).getTextureRegion();
                    entityContext.addEntity(new SpriteEntity(texture, x, y));
                }


                // parse the tile held by the tiled map tile object - getting any child objects of that tile
                parseTile(tile, x,  y);
                break;
        }
    }

    /**
     * Sets the collision layer of a box based on a CollisionLayer map property
     */
    private void setBoxCollisionLayers(MapProperties collisionLayer, Box box) {
        for (Iterator<String> it = collisionLayer.getKeys(); it.hasNext(); ) {
            String key = it.next();

            if("type".equals(key)) continue;

            boolean set = collisionLayer.get(key, Boolean.class);
            box.setLayer(Integer.parseInt(key, 10), set);
        }
    }

    private int[] getMaskLayers(MapProperties collisionLayer) {
        ArrayList<Integer> out = new ArrayList<>();
        for (Iterator<String> it = collisionLayer.getKeys(); it.hasNext(); ) {
            String key = it.next();

            if("type".equals(key)) continue;

            boolean set = collisionLayer.get(key, Boolean.class);
            if(set) {
                out.add(Integer.parseInt(key, 10));
            }
        }

        return out.stream().mapToInt(i->i).toArray();
    }
}
