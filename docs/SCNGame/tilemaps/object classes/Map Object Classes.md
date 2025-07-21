> Warning: any value of a map object must be explicitly set when added to a tile map (even to its default value) in order to be included in the exported TMX file. Some values aren't needed for an object and are optional. These nullable values are indicated by a suffix `?`, such as "String?"
## Wall
A wall represent a standard static collidable box, for which other entities will collide with given their collision mask matches the walls collision layer.

| Property       | Type                    | Description                                                         |
| -------------- | ----------------------- | ------------------------------------------------------------------- |
| CollisionLayer | [[Value Types#Bitmask]] | A bitmask representing the layers that this wall can be collided on |
## DamageWall
An area that emits damage to entities collided with it, given their collision mask matches the areas collision layer. 

| Property       | Type                               | Description                                                                                                         |
| -------------- | ---------------------------------- | ------------------------------------------------------------------------------------------------------------------- |
| CollisionLayer | [[Value Types#Bitmask]]            | A bitmask representing the layer at which this area will be collided with.                                          |
| DamageType     | [[Value Types#Damage Type (enum)]] | The type of damage emitted                                                                                          |
| Damage         | float                              | The amount of damage emitted. It is up to the entity absorbing this damage as to how often this damage is received. |
## Sign
A sign is a simple, static, interactable area that causes a dialog event to occur. 

| Value  | Type                   | Description                                     |
| ------ | ---------------------- | ----------------------------------------------- |
| Dialog | [[Value Types#Dialog]] | The dialog event to be emitted upon interaction |
## Spawn Location
Spawn locations represent some specific point on a map that the player can be spawned at or transported to. 

| Value   | Type   | Description                                                                                                                                                                                                                                                                                                                                                           |
| ------- | ------ | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| SpawnID | String | The ID by which the spawn location is referenced by. This is used as opposed to using the Name of the object such that the Name can be used for visual purposes in the editor (the same name may be used by multiple spawn locations). Furthermore, the numeric ID isn't used as such wouldn't allow for the spawn location to be referenced in a different map file. |
## Terrain Box
These represent some unique ground that player may be walking over, causing them to speed up or slow down. 

| Value             | Type                    | Description                                                                                                                                                                             |
| ----------------- | ----------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Collision Layer   | [[Value Types#Bitmask]] | The layers for which entities residing on will be effected by the terrain                                                                                                               |
| Speed Coefficient | float                   | The coefficient to multiply an entities speed by when they are on the terrain. For example, if some muddy ground needs to half an entities speed, it's speed coefficient would be `0.5` |
## Door
Doors are interactable areas that cause the player to be transported to another map.

| Value   | Type   | Description                                                                                                                                                           |
| ------- | ------ | --------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Map     | String | The path of the tmx map file to change to, relative to `assets/tilemaps/`                                                                                             |
| SpawnID | String | The ID of the [[#Spawn Location]] to spawn at when loaded into the map. The SpawnID must be of a present spawn location, otherwise the player will spawn at `(0, 0)`. |
## Portal
Similar to a [[#Door]], but instead transports the player upon collision instead of interaction.

| Value          | Type                    | Description                                                                                                                                                           |
| -------------- | ----------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Map            | String                  | The path of the tmx map file to change to, relative to `assets/tilemaps/`                                                                                             |
| SpawnID        | String                  | The ID of the [[#Spawn Location]] to spawn at when loaded into the map. The SpawnID must be of a present spawn location, otherwise the player will spawn at `(0, 0)`. |
| Collision Mask | [[Value Types#Bitmask]] | The collision layers to check for collision on. Upon a collision on this layer, the map is changed.                                                                   |
> **WARNING**: portals cause a map change event on ANY collision in the defined collision mask. It has no way of knowing whether such a collision was a player, a wall, or an NPC. Be careful with your collision masks and layers to avoid bugs. 
> 
> For example, if a portal overlaps a wall and the walls collision layer matches the portal collision mask, the map will instantly change upon being loaded.

