# Show Nametag (Minecraft 1.21.x)

A tiny client-side mod that shows **your own nametag above your head in third-person view** (F5), just like other players see it.

- **Minecraft:** 1.21, 1.21.1, 1.21.2, 1.21.3, 1.21.4, 1.21.5, 1.21.6, 1.21.7, 1.21.8, 1.21.9, 1.21.10, 1.21.11
- **Loaders:** Fabric, Forge, NeoForge (one jar works on all three)
- **Java:** 21
- **Side:** Client only. You don't need it on the server.

> Looking for Minecraft 26.x? See the `26.x` version of this mod.

## Features

- Shows your nametag in both third-person views (back and front).
- Hidden in first-person view, so it never blocks your screen.
- Hidden when you are invisible.
- Hidden when the HUD is hidden (F1), so screenshots stay clean.
- No config and no dependencies. It is a single ~8 KB jar.

## Installation

1. Install Fabric Loader (0.15.11+), Forge or NeoForge for your Minecraft version.
2. Put `Show_Nametag-Fabric+Forge+NeoForge-1.0.0-1.21-1.21.11.jar` into your `mods` folder.
3. Start the game and press **F5**.

Fabric API is **not** required.

## How it works

The mod contains a single Mixin on `LivingEntityRenderer.shouldShowName`. When the entity being rendered is the local player, the camera is not in first person, the player is not invisible, and the HUD is visible, the method returns `true`. Vanilla then renders the nametag as usual.

Two details make one jar work across all of 1.21.x:

- **Two method signatures.** In 1.21 and 1.21.1 the method is `shouldShowName(LivingEntity)`. From 1.21.2 onward it is `shouldShowName(LivingEntity, double)`. The Mixin hooks both, and each hook is optional, so only the one that exists is applied.
- **Two name sets.** Forge and NeoForge run 1.21.x with Mojang's official names, so they share the code in `common/`. Fabric runs 1.21.x with intermediary names, so it has its own copy in `com.shownametag.fabric`, remapped at build time. The intermediary names this mod uses are the same in every version from 1.21 to 1.21.11. Each loader only reads its own Mixin config:
  - Fabric reads `shownametag.fabric.mixins.json` (listed in `fabric.mod.json`).
  - Forge and NeoForge read `shownametag.mixins.json` (listed in the jar manifest and `neoforge.mods.toml`).

## Building from source

Requirements: **JDK 21 or newer** and Windows PowerShell.

```powershell
powershell -ExecutionPolicy Bypass -File build.ps1
```

The script builds each loader project against Minecraft 1.21.1 and merges them into one jar, which it writes to this folder.

### Project layout

| Path | Contents |
| --- | --- |
| `common/` | Shared Forge/NeoForge code: the Mixin, the nametag logic, `shownametag.mixins.json` and `pack.mcmeta` |
| `loaders/fabric` | Fabric Loom build (remapped to intermediary), its own Mixin and `fabric.mod.json` |
| `loaders/forge` | ForgeGradle build, `mods.toml` and the `@Mod` entry class |
| `loaders/neoforge` | ModDevGradle build, `neoforge.mods.toml` and the `@Mod` entry class |
| `build.ps1` | Builds all loaders and merges them into a single jar |

## License

MIT, by Somersault41. See [LICENSE](LICENSE).
