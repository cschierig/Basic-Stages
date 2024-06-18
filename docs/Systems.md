# Affected Systems

This document describes which minecraft systems can be locked behind stages and how.

The following description contains an example for how the effect of locking systems behind stages should be documented.

## Example System

Short description of the scope of the system

### Desired effect

- List of all things which should be locked.

### Client changes

- Modifications which are done on the client side

### Server changes

- Modifications which are done on the server side

### Handling in Multiplayer

When playing on servers with multiple players, some players might have unlocked more
stages than others. Systems need to account for that. This section is intended to document how a system ensures a
coherent playing experience for all players.

### Limitations

What can't the system lock behind a stage.

## Blocks

If a block is locked behind a stage, it is replaced by a different block.
The replacement is not only a visual, but also changes loot, hardness and required tools for players who haven't
unlocked the block yet.

### Desired effect

Replace the following properties of the locked block:

- model/texture
- loot table
- hardness
- breaking tool

### Client changes

- Swap out the model and the texture of the block.

### Server changes

Change how the block acts if it is broken by a player who doesn't have the stage.

### Handling in Multiplayer

Other players will notice the difference in mining speed.
The loot table change is also noticeable.

### Limitations

These aspects are currently unchanged:

- Hitbox

## Items

If a item is locked behind a stage, it is visually replaced by a different item.
Players who haven't unlocked it, can't pick it up

### Desired effect

Replace the following properties of the locked item:

- model/texture
- Remove recipe from emi/rei

Prevents players from picking it up, logging a message.

### Client changes

- Swap out the model and the texture of the block.
- Remove recipes from emi/rei

### Server changes

- Prevent player from picking up the item
- drop item if it is in inventory

When the item is dropped or a player tries to pick it up, a message is displayed telling them why.
Currently, the wording for this is relatively abstract.

### Handling in Multiplayer

Instead of replacing the item on being picked up, the items simply can't be picked up.
This was done to prevent valuable items being destroyed when one player with a stage drops them to one
without it.

### Limitations

- 
