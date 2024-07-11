## JSON Resource documentation

This directory contains information on the Privileged json format.

## Location

Privileged resources are stored in the `<namespace>/privilege` directory in data packs.
Files can also be stored in subdirectories of the aforementioned folder.

## Format

Privileged resources are json files.
Contrary to most json files, a privileged resource is a list, i.e. a file with a simple privilege could look like this:

```json
[
  {
    "type": "privileged:block",
    "stage": "stuff",
    "privileges": "#minecraft:logs",
    "replacement": {
      "Name": "minecraft:iron_block"
    }
  }
]
```

Note that the file starts with `[` and ends with `]`.

The list contains any amount of providers, which are json objects.
The following providers are currently available:

1. [Block Provider](./Block Provider.md)
2. [Block State Provider](./Block State Provider.md)
3. [Item Provider](./Item Provider.md)
