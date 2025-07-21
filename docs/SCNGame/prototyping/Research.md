## What's been Figured Out
- JBump is wonderful for collision
	- Separate 'hitbox' and 'wall collider' where the wall collider is 1 pixel tall - allowing for proper 2.5d wall collision
	- The 'wall collider' is moved first, and then based on any collision it experiences the other hitboxes move as well
	- Need to create a 'Wall' class so 'instanceof' can be used to determine if something is a wall or not
- Use a sprite offset between collision and rendering to allow for sword animations
	- The simulated position should not change
	- It is the render component's responsibility to account for any offset for sprites or animations
	- Every other component (i.e. input and physics) can live never knowing the sprite is offset from the characters actual position
- ~~Use background and foreground layers in Tiled to allow the player and other entities to walk behind and in front of things like buildings or tree
	- For trees (and other 'entity like' objects, such as NPCs or interactables), it is best to use an object layer with tile objects to represent entities to be created programmatically, which can then be rendered in front of or behind the player by sorting the entities by their y value. The concept of foreground and background can still be used for other, simpler things, however. 
	- The best way to adjust the z-index of actors so that 'higher up' actors are drawn earlier (as per how 2.5d should look) is to sort the actor array at every frame using a comparator
	- As a result of messing about with this, I figured that `BaseActor` should override setX, setY, getX, and getY so it only relies on a `Vector2` for an actors position rather than having some weird possible integrity issues with the pre-existing x and y values in `scene2d.Actor` and my own position vector
- A factory can be used to build up different actors.
- Some basics of Scene2D.ui
	- In order to get keyboard interactable menus the best option is to use some extended version of a List. However, to get this working properly you would need some focusing system. 
		- Works well for something with a single list of options to scroll between, e.g. dialogue options
		- Remember to set the keyboard focus of the stage to the list!
		- For other UI elements, such as more complicated menus, I would probably stick to just using buttons. This doesn't allow for keyboard navigation, but that is an alright limitation for this project
	- Idea: as part of the runnable for a list option, you could switch focus to some other list. Would require the runnable being aware of the list that now needs focus
		- **Don't bother doing this yet**. Just getting something working is most important right now, and this likely will require more effort than it is worth

#### Important Things to Remember
- Make sure all walls and collision boxes have a whole integer position and bounding width + height. Fractional position or size can make collision with the player weirdddd
## What still needs to be Figured Out
- Where to hold the 'World' object from JBump
	- in the Game class? in some manager class? in a factory?
	- How should references be passed between actors/entities? Should it be a global static member? or should it be injected?
	- To achieve loose coupling, perhaps we pass a World reference to the physics component of each entity per frame. This allows the 'World' to be quickly swapped out without having to update every entity to match the new World - we simply pass a difference reference. It also means we can effectively have several 'layers', being different worlds, so certain objects can exist in a completely separate collision layer to others (although management of this may be trickier, but this way gives us the freedom to do so)
		- This also encourages the idea that collision is a matter for the physics component, and not the input or render component. The Player itself, as well as the input and render components, do not (and should not) be aware of the physics World.
- Managing state
	- enums and switch cases seem the best way to me (at least for the scope of this project)
- Animation handling