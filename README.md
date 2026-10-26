# Happy Ghast Control

A small Fabric mod for Minecraft Java Edition 26.3. It adds a dedicated key for
descending while steering a Happy Ghast and a per-Ghast parking switch. A
parked Ghast stays where it is after the last passenger dismounts. Mounting it
again temporarily restores movement for riding, while its parking preference
remains saved for the next dismount.

## Controls

- **G:** descend while steering a Happy Ghast. Change this under
  **Options → Controls → Key Binds → Happy Ghast Control**.
- **H:** open the controls for the Ghast you are steering. A small in-game hint
  shows the currently bound key. In the menu, switch **Park after dismount** on
  or off for that Ghast. This setting is saved with the Ghast, independently of
  other Ghasts.
- **Space:** ascend, as in vanilla Minecraft.
- Holding both keys cancels their vertical input. Forward flight still follows
  the rider's view direction.

The controls only act for the first passenger, who steers the Ghast. With
parking off, the Ghast resumes normal AI after dismounting. With parking on,
its movement AI remains off while nobody is riding it.

## Installation

Install Fabric Loader, Fabric API, and this mod for Minecraft Java 26.3. Java 25
is required. Put Fabric API and the mod JAR in the `mods` folder. Install the mod
and Fabric API on **both the client and server** for multiplayer. A client-only
installation cannot send descent input to the server.

## Build

Use Java 25 and run:

```sh
bash ./gradlew build
```

On Windows, use `gradlew.bat build`. The distributable JAR is
`build/libs/happy-ghast-control-fabric-26.3-1.1.0.jar`. The `-sources.jar` is
source code for development, not the mod to install.

## Publishing

Upload the distributable JAR to Modrinth with game version **26.3**, loader
**Fabric**, side **client and server**, and dependency **Fabric API**. Use
`dist/happy-ghast-control-icon.png` as the project icon. Ready-to-use [project
description](modrinth/description.md), [short summary](modrinth/summary.txt),
and the 1.1.0 changelog are in [`modrinth/`](modrinth/).

The public source repository is
[MaxWehrle/happy-ghast-control-fabric](https://github.com/MaxWehrle/happy-ghast-control-fabric).

## License

MIT © 2026 Max Wehrle. See [LICENSE](LICENSE).
