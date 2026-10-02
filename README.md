# Custom Shield Scale — Fabric 26.3

An independent Fabric 26.3 port of the Custom Shield Scale behavior.

## Target

- Minecraft 26.3
- Fabric Loader 0.19.5
- Java 25
- Fabric API 0.161.0+26.3
- YetAnotherConfigLib 3.9.7+26.3-fabric
- Mod Menu 21.0.0 (optional)

## Features

- Enable/disable shield transforms.
- Separate scale for your own shield and shields held by other players.
- Separate X/Y offsets for your own shield and other players' shields.
- Works with Minecraft 26.3's render-state pipeline:
  - first person: `FirstPersonHandsAndItemsRenderer`
  - player body / third person: `ItemInHandLayer`

Scale range: 0.10–3.00. Offset range: -1.00–1.00.

## Build

Install Java 25 and Gradle 9.7.1, then run:

```bash
gradle build
```

The remapped mod jar is written to `build/libs/`.

## Install

Put the built jar in your Fabric 26.3 `mods` folder together with Fabric API and YACL. Mod Menu is optional but provides the config button.

## Notes

This repository does not include the uploaded 1.21.11 jar's artwork or other packaged assets. The 26.3 implementation is source-level code written for the current renderer APIs.
