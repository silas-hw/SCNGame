> Warning: any value of a map object must be explicitly set when added to a tile map (even to its default value) in order to be included in the exported TMX file. Some values aren't needed for an object and are optional. These nullable values are indicated by a suffix `?`, such as "String?"
## Bitmask

Bitmask represents a binary value used as a mask. In-editor these are set using checkboxes:

![[Pasted image 20240901222125.png]] 
*a bitmask used for a collision layer with bits 0 and 5 set*
## Damage Type (enum)
An enumerated value used for defining the type of damage for damage emitting objects. Values available:
- Default

## Dialog
A dialog value represents the information needed to load a dialog node from a file. See [[Dialog File]] for more information on dialog.

| Value      | Type   | Description                                                                                                                                                                |
| ---------- | ------ | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| DialogFile | String | A path to the [[Dialog File]], relative to the `/dialog/` folder in the assets directory                                                                                   |
| Group      | String | The ID of the group that a dialog node should be selected from. If multiple dialog nodes exist within a group, one is chosen at random every time the group is referenced. |
