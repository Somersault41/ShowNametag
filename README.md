# Show Nametag (Minecraft 26.x)

A tiny client-side mod that shows **your own nametag above your head in third-person view** (F5), just like other players see it.

- **Minecraft:** 26.1, 26.1.1, 26.1.2, 26.2, 26.3
- **Loaders:** Fabric, Forge, NeoForge (one jar works on all three)
- **Java:** 25
- **Side:** Client only. You don't need it on the server.

> Looking for Minecraft 1.21.x? See the `1.21.x` version of this mod.

## Features

- Shows your nametag in both third-person views (back and front).
- Hidden in first-person view, so it never blocks your screen.
- Hidden when you are invisible.
- Hidden when the HUD is hidden (F1), so screenshots stay clean.
- No config and no dependencies. It is a single ~6 KB jar.

## Installation

1. Install Fabric Loader, Forge or NeoForge for your Minecraft version.
2. Put `Show_Nametag-Fabric+Forge+NeoForge-1.0.0-26.3.jar` into your `mods` folder.
3. Start the game and press **F5**.

Fabric API is **not** required.

## How it works

The mod contains a single Mixin on `LivingEntityRenderer.shouldShowName`. When the entity being rendered is the local player, the camera is not in first person, the player is not invisible, and the HUD is visible, the method returns `true`. Vanilla then renders the nametag as usual.

Minecraft 26.x is no longer obfuscated, so the same Mixin works on all three loaders without remapping. The F1 (hide HUD) check is resolved at runtime because its location changed between 26.1.x and 26.2+.

## Building from source

Requirements: **JDK 25** and Windows PowerShell.

```powershell
powershell -ExecutionPolicy Bypass -File build.ps1
```

The script builds each loader project and merges them into one jar, which it writes to this folder.

### Project layout

| Path | Contents |
| --- | --- |
| `common/` | Shared code: the Mixin, the nametag logic, `shownametag.mixins.json` and `pack.mcmeta` |
| `loaders/fabric` | Fabric Loom build and `fabric.mod.json` |
| `loaders/forge` | ForgeGradle build, `mods.toml` and the `@Mod` entry class |
| `loaders/neoforge` | ModDevGradle build, `neoforge.mods.toml` and the `@Mod` entry class |
| `build.ps1` | Builds all loaders and merges them into a single jar |

## License

MIT, by Somersault41
