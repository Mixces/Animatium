# Release 4.2.1

## Changes

- Small change to info payload
    - Version is now a packed integer containing the (major, minor, and patch) values as byte representations
    - MAJOR is 8
    - MINOR is 16
    - Pack via: ```(major << MAJOR) | (minor << MINOR) | patch```