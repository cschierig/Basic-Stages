# Basic Stages

Provides stages which lock certain aspects of the game, such as blocks or items, behind stages.
Pack makers can use json files to specify when an object should become accessible and how it should be hidden.

## Documentation

Replacement data is placed into the `data/${namespace}/stages` folder and has the following structure:

```json5
// Each file is a json array
[
  // you can put as many replacements as you want into a single file, or split them up into subdirectories
  {
    // The type determines what you are replacing. Here, we are telling the mod to treat the values
    // in "privilege" and "replacement" as blocks.
    "type": "basic_stages:block",
    // The stage is an arbitrary string 
    "stage": "iron",
    // The privilege is the object which is being locked away.
    "privilege": "minecraft:grass_block",
    // The replacement is the object which is used instead of the privilege.
    "replacement": "minecraft:iron_block"
  }
]
```

Currently, the following types are available:
| Identifier | System |
|----|----|
| `basic_stages:block` | Blocks |
| `basic_stages:item` | Blocks |

More in-depth documentation can be found in [the docs folder](./docs).
There you can find information on what the individual systems achieve and what their limitations are.

## Usage & Dependencies

This mod requires [Fabric API](https://modrinth.com/mod/fabric-api) on Fabric.

You can optionally use [Jade](https://modrinth.com/mod/jade). Basic Stages will correctly
hide blocks and items when looked at with Jade.

<!-- modrinth_exclude.start -->

## Contributing

If you have encountered a problem or are in need of a feature, feel free to open an issue.
I'm also accepting pull requests, but you should consider contacting me before submitting
bigger changes to the project.

<!-- modrinth_exclude.end -->
