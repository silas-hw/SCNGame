Dialog is written in a small domain-specific mark-up language with XML. You can have a number of dialog groups, each containing a sequence of text messages. When defining what dialog to use in the map editor, you define a dialog group to choose from. At runtime, a random dialog node from the group is chosen.

For example, say you have an NPC named bob, and you want a choice of 10 dialogs to be chosen when the player interacts with them. You would define a group with id "bob" and add 10 dialog nodes into this group. Each dialog node would then contain a sequence of messages making up the actual dialog.

Messages are made up by their text body, the actual text shown to the player, as well as a `speaker` (the name of the person speaking, shown above the text) and an `icon` (a path to a texture or sprite to be displayed beside the dialog text, relative to the root assets directory)

```xml
<root>
	<group id:String> [1..*]
		<dialog> [1..*]
			<message speaker:String icon:String> [1..*]
				# Text
			</message>
		</dialog>
	</group>
</root>
```

> Above, `[x..y]` represents that there must exist between x and y of an element, with `*` representing unbounded/infinity. Within an element, the name of its attributes are given alongside their type. A question mark following a type means that attribute is optional.
