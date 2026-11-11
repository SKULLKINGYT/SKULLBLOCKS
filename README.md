# SKULLBLOCKS

**Version:** 1.0.0  
**Minecraft:** Java Edition 1.21.11  
**Loader:** Fabric  
**Java:** 21  
**Author:** SKULLKING

Independent client-side hold-to-place convenience and optional Creative-mode fast breaking.

## Build

Use a 64-bit JDK 21 and Gradle. Run `gradle build` from this folder. The JAR is produced in `build/libs/`.

## Install

Install Fabric Loader and Fabric API for Minecraft 1.21.11, then copy the built JAR into `.minecraft/mods`. Press **P** to open the menu.

## Features

- Hold RMB and move the crosshair to a new block face to attempt another vanilla placement.
- Configurable placement cooldown (0–10 ticks).
- Optional Creative-only fast breaking, disabled by default.
- Menu sections: HOME, BLOCK PLACEMENT, FAST BREAKING, KEYBINDS, CONFIGURATION and ABOUT.
- Persistent settings in `.minecraft/config/skullblocks.json`.

## Important limitations

Vanilla reach, collision, item state and server validation remain authoritative. This does not bypass server protections or anti-cheat. The implementation triggers on a changed crosshair block/face; it does not repeatedly spam an unchanged rejected target.

## Build status

Source uploaded. Compilation and in-game runtime tests have **not** yet been executed. A successful GitHub Actions run is required before treating the JAR as built and tested.
