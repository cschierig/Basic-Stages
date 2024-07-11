# Block State Provider

Block State providers block states of a block matching a predicate.

## Format

The following example replaces the upper half of an iron door if the player doesn't have the stage `stage_name`.

```json5
{
  // The type, must be "privileged:block_state"
  "type": "privileged:block_state",
  // The stage at which the privilege is unlocked.
  "stage": "stage_name",
  // Must be a block state override (See the document on replacements for more information)
  // This will only replace block states which match the given state
  "privilege": {
    "Name": "minecraft:iron_door",
    "Properties": {
      "half": "upper"
    }
  },
  // What the locked block state should be replaced with.
  // Must be a block state override
  "replacement": {
    // The resource location of the replacement block state
    "Name": "minecraft:oak_door",
    // sets the block state of the replacement block.
    // Optional. Properties which are not mentioned default to the block state value of the replaced block
    // or the default block state of the replacement.
    "Properties": {
      // All entries must be strings, even booleans and numbers.
    },
  },
  // if true, also replaces the item of the replaced block state
  // Optional: defaults to true
  "replaceItem": false
}
```

For more information on replacements, check out [this document](./Replacements.md)
