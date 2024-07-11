# Replacements

There are several different replacements available.
Not all providers support all replacements, which replacements are supported by each provider is documented in their
respective documents.

## Item Stack

An item stack.
More details on item components can be found on the [Minecraft Wiki](https://minecraft.wiki/w/Data_component_format).

### Example

```json5
{
  // The resource location of the item
  "item": "minecraft:oak_log",
  // Optional, defaults to 1
  "count": 2,
  // Optional. Data component patches. See the wiki for more details.
  "components": {}
}
```

## Block State Override

Block state overrides contain a block state and information on how to override it.
An override will inherit all shared block state properties from the block state it is replacing,
but override the values which are specified in the `Properties` section.
Due to a Minecraft limitation, the property values __must__ be Strings.

### Example

```json5
{
  // The resource location of the replacement block state
  "Name": "minecraft:redstone_lamp",
  // sets the block state of the replacement block.
  // Optional. Properties which are not mentioned default to the block state value of the replaced block
  // or the default block state of the replacement.
  "Properties": {
    // All entries must be strings, even booleans and numbers.
    "lit": "true"
  }
}
```

## Resource Location Patch

A resource location patch modifies the resource locations of all replaced objects.
It uses a regex as an input to determine what to replace and replaces this with the given string.

### Example

```json5
{
  // Optional.
  "namespace": {
    "matching": "minecraft",
    "with": "rudimentary"
  },
  // Optional.
  "path": {
    "matching": "iron",
    "with": "coal"
  }
}
```
