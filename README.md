# Rejuvenation Tools

---

## Overview

The Official PROJECT: Rejuvenation Database tool. This plugin serves as a data
generator tool and web-server for the WebUI interface for the database.

---


## Commands

###  /generate

#### Permission
`rejuvenationtools.generate`

#### Usage

`/generate <data_id>`

- data_id: The unique data ID defined in config.yml to determine which plugins 
and folders to read.

The outputs of this command are written to the `/output` directory in the plugin's
config folder. The command only captures YAML entries that have a `Docs: true` property.

#### Example

/generate mobs

---

## Configuration

### WebUI Port \[web-server-port]

The port on which the WebSocket should listen and send HTTP requests from. Connect
to the WebUI by opening the `http://<server-ip>:<web-server-port>` URL in your browser.

### Server Patch \[server-patch]

The current patch version of the server. Follows the major.patch versioning standard. (e.g. 3.8; or Era 3
patch 8 in Rejuvenation standards).

This setting will filter out YAML entries with a `Patch:` property that is equal or above
this value, upon data generation.

### Generate \[generate]
A collection of unique data IDs to define what plugins and folders to read from. New folders
can be added by designating a new data ID that does not share a name with another, and adding
the `plugin:` and `folder:` properties to it.

---

## Notes
Ensure that all folder paths (e.g., /item, /Mobs, /modifiers) correspond to the correct directory names used in your files.
After making changes to the config.yml file, ensure to reload or restart your server for the changes to take effect.