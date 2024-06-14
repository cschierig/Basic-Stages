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

## Desired effect

Replace the following properties of the locked block:

- model/texture
- loot table
- hardness
- breaking tool

### Client changes

- Swap out the model and the texture of the block.

### Server changes

Change how the block acts if it is broken by a player who doesn't have the stage.
