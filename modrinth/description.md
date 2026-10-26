# Happy Ghast Control

Take your Happy Ghast exactly where you need it, then park it while you build. Happy Ghast Control adds a dedicated descend key and a parking switch for each individual Happy Ghast.

## Features

- **Fly down on command.** Hold the descend key while steering a Happy Ghast, just as you hold Space to fly up. The default descend key is **G**.
- **Park one Ghast at a time.** Press the menu key while riding the Ghast you want to configure, then turn **Park after dismount** on or off. The default menu key is **H**.
- **Keep your build area clear.** A Ghast with parking enabled stops moving after its last passenger dismounts. It can be ridden again normally, and it will park again after the next dismount.
- **Save each Ghast's choice.** Parking is stored with that Ghast, so changing one does not affect any other Ghast. Ghasts with parking disabled resume their normal movement after dismounting.
- **Find the menu key in game.** A small hint appears while you steer a Happy Ghast and displays your current menu binding.
- **Change both keys.** Rebind descend and menu under **Options → Controls → Key Binds → Happy Ghast Control**.
- **Use English or German.** The controls, hint, and menu have English and German translations and follow your Minecraft language setting.

## How to park your Ghast

1. Mount the Happy Ghast you want to park and take the steering position.
2. Press **H** (or your assigned menu key) to open **Happy Ghast Controls**.
3. Set **Park after dismount** to **ON**, then close the menu.
4. Dismount. The Ghast stays in place until someone rides it again. Its parking preference remains saved for future dismounts.

Open the same menu and set the switch to **OFF** when you want that Ghast to move normally after dismounting.

## Installation

This mod requires **Minecraft Java Edition 26.3**, **Fabric Loader**, **Fabric API**, and **Java 25**. Install Fabric API and Happy Ghast Control in your `mods` folder. For multiplayer, install the mod and Fabric API on both the client and server so the controls and parking state can communicate correctly.

## Notes

Only the passenger steering the Ghast can use the flight and menu controls. The descend key applies while riding a Happy Ghast; it does not change normal player flight. Holding descend and Space together cancels vertical input. Parking takes effect when the last passenger dismounts.

Happy Ghast Control is open source under the **MIT License**. Source code, issue reports, and release information are available on [GitHub](https://github.com/MaxWehrle/happy-ghast-control-fabric).
