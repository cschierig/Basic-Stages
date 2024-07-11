# Block Provider

Block providers replace a single block or a block tag with a different block state.

## Format

The following example replaces all logs with a redstone lamp if the player doesn't have the stage `stage_name`.

```json5
{
  // The type, must be "privileged:block"
  "type": "privileged:block",
  // The stage at which the privilege is unlocked.
  "stage": "stage_name",
  // Must be a resource location (e.g. "minecraft:oak_log")
  // or a tag (e.g. "#minecraft:logs")
  "privileges": "#minecraft:logs",
  // What the locked blocks should be replaced with.
  // Must be a block state override or a resource location patch.
  // See the document on replacements for more details.
  // The example uses a block state override
  "replacement": {
    // The resource location of the replacement block state
    "Name": "minecraft:redstone_lamp",
    // sets the block state of the replacement block.
    // Optional. Properties which are not mentioned default to the block state value of the replaced block
    // or the default block state of the replacement.
    "Properties": {
      // All entries must be strings, even booleans and numbers.
      "lit": "true"
    },
  },
  // if true, also replaces the items of the replaced blocks
  // Optional: defaults to true
  "replaceItems": false
}
```

For more information on replacements, check out [this document](./Replacements.md)
