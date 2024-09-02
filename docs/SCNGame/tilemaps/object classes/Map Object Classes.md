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
