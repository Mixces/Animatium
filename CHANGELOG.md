# Release 4.2.1

## Changes

- Remove "Smooth Particles" option temporarily as it is broken in 1.21.11 and requires a lot of work to fix.
- Small change to info payload
    - Version is now a packed integer containing the (major, minor, and patch) values as byte representations
    - MAJOR is 8
    - MINOR is 16
    - Pack via: ```(major << MAJOR) | (minor << MINOR) | patch```