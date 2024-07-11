# Item Provider

Block providers replace a single block or a block tag with a different block state.

## Format

```json5
{
  // The type, must be "privileged:item"
  "type": "privileged:item",
  // The stage at which the privilege is unlocked.
  "stage": "stage_name",
  // Must be a resource location (e.g. "minecraft:oak_log")
  // or a tag (e.g. "#minecraft:logs")
  "privileges": "#minecraft:logs",
  // What the locked items should be replaced with.
  // Must be an item stack or a resource location patch.
  // See the document on replacements for more details.
  // The example uses an item stack 
  "replacement": {
    "item": "minecraft:grass_block"
  },
  // if true, also replaces the items of the replaced blocks
  // Optional: defaults to true
  "replaceItems": false
}
```

For more information on replacements, check out [this document](./Replacements.md)
