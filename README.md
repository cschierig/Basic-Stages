# Privileged

Provides stages which lock certain aspects of the game, such as blocks or items, behind stages.
Pack makers can use json files to specify when an object should become accessible and how it should be hidden.

## Documentation

Replacement data is placed into the `data/${namespace}/privilege` folder and has the following structure:

Currently, the following types are available:
| Identifier | System |
|----|----|
| `privileged:block` | Blocks |
| `privileged:item` | Items |
| `privileged:block_state` | Block States |

More in-depth documentation can be found in [the docs folder on GitHub](https://github.com/cschierig/Privileged).
There you can find information on what the individual systems achieve and what their limitations are.

To gain a stage you can use the `/stage` command. The syntax is very similar to that of the `/advancement` command.

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
