There are a variety of map object classes available to use when editing a tile map for SCNGame. These classes are taken in by the games code and parsed to be added to the world or scene of a game screen.

Objects can also be added to individual tiles using Tiled's collision editor. This can allow for sprite objects to hold additional collision information, as well as tiles themselves of course.
## Decor
A decor or sprite element (that is, some TextureMapObject you wish to be inserted into the scene as a sprite) can be defined by inserting a TextureMapObject and leaving its class blank. 

If the tile used for the texture map object has other objects attached to them, as done with Tiled's collision editor, those will also be parsed and added.

TextureMapObjects may be used for other types of map object (e.g. when you want some image as a placeholder representation of some object, such as entities), in which case the class won't be left blank.
## Wall
These represent basic walls that the player or other entities can potentially collide with. 
**Object Type:** Rectangle

| ==Property==   | ==Data Type==    | ==Description==                                                                                                                                                                                                                                                                                                                                                         |
| -------------- | ---------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| CollisionLayer | String (bitmask) | A bitmask string (i.e. a string representing binary, such as "10100011") of what layers this wall should be collidable on.<br><br>Layer masks are held as 32bit signed integers, thus 31 layers are at disposal. The least significant (0th) layer is represented by the rightmost bit. This is such that "0001" and "000001" would represent the same collision layer. |

## DamageWall
These represent collidable areas that deal damage to entities.

**Object Type:** Rectangle


| ==Property==   | ==Data Type==    | ==Description==                                                                                                                                                                                                                                                                                                                                                         |
| -------------- | ---------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| CollisionLayer | String (bitmask) | A bitmask string (i.e. a string representing binary, such as "10100011") of what layers this wall should be collidable on.<br><br>Layer masks are held as 32bit signed integers, thus 31 layers are at disposal. The least significant (0th) layer is represented by the rightmost bit. This is such that "0001" and "000001" would represent the same collision layer. |
| DamageType     | enum             | Represents what type of damage the area is to give. Currently the only available damage type is DEFAULT.                                                                                                                                                                                                                                                                |
| damage         | float            | The amount of damage to be dealt. The speed at which the damage to be dealt is implementation specific. Different entities may consume damage at different rates (such as with different effects, etc...).                                                                                                                                                              |
