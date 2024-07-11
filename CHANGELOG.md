- better emi recipe blocking, now blocks recipes which contain a locked ingredient
- complete privilege loading redesign:
    - Instead of loading privileges directly, Privileged now loads Providers, which can generate multiple individual
      privileges
    - Added privilege providers:
        - block provider (`privileged:block`):
            - Replaces single blocks or entire block tags.
            - also replaces associated block items, configurable
        - block state provider (`privileged:block_state`):
            - Replaces block states of a block which match certain conditions.
            - also replaces associated block item, configurable
        - item provider (`privileged:item`):
            - Replaces single items or entire item tags.
    - Block privileges now merge the block state of the block they are replacing with the replacement block. This
      enables believable replacement of doors, stairs, lamps, etc.
    - allow resource locations to be patched to find a matching replacement for each value in a tag.
- change resource directory from `stages` to `privilege`
