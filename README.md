# Show Nametag (Minecraft 1.20.x)

A tiny client-side mod that shows **your own nametag above your head in third-person view** (F5), just like other players see it.

- **Minecraft:** 1.20, 1.20.1, 1.20.2, 1.20.3, 1.20.4, 1.20.5, 1.20.6
- **Loaders:** Fabric, Forge, NeoForge (one jar works on all three)
- **Java:** 17 (1.20 – 1.20.4) or 21 (1.20.5 – 1.20.6)
- **Side:** Client only. You don't need it on the server.

> Looking for Minecraft 1.21.x or 26.x? See the `1.21.x` and `26.x` versions of this mod.

## Features

- Shows your nametag in both third-person views (back and front).
- Hidden in first-person view, so it never blocks your screen.
- Hidden when you are invisible.
- Hidden when the HUD is hidden (F1), so screenshots stay clean.
- No config and no dependencies. It is a single small jar.

## Installation

1. Install Fabric Loader (0.14.21+), Forge or NeoForge for your Minecraft version.
2. Put `Show_Nametag-Fabric+Forge+NeoForge-1.0.0-1.20-1.20.6.jar` into your `mods` folder.
3. Start the game and press **F5**.

Fabric API is **not** required. Forge has no build for 1.20.5 (Forge went straight to 1.20.6), and NeoForge has no build for 1.20 (its first version is 1.20.1).

## How it works

The mod contains a single Mixin on `LivingEntityRenderer.shouldShowName(LivingEntity)`. When the entity being rendered is the local player, the camera is not in first person, the player is not invisible, and the HUD is visible, the method returns `true`. Vanilla then renders the nametag as usual.

Minecraft 1.20.x runs with three different name sets depending on the loader, so the jar carries the hook three times. Each loader only reads its own Mixin config, and inside a config only the hook whose name exists in the running game is applied:

| Loader and version | Names at runtime | Hook |
| --- | --- | --- |
| Fabric 1.20 – 1.20.6 | intermediary | `method_4055` in `com.shownametag.fabric` (`shownametag.fabric.mixins.json`) |
| Forge 1.20 – 1.20.4, NeoForge 1.20.1 | SRG | `m_6512_`, calls `OwnNametagSrg` (`shownametag.mixins.json`) |
| Forge 1.20.6, NeoForge 1.20.2 – 1.20.6 | official (Mojang) | `shouldShowName`, calls `OwnNametag` (`shownametag.mixins.json`) |

`OwnNametag` and `OwnNametagSrg` have the same source code. The jar takes `OwnNametagSrg` from the Forge build after `reobfJar`, so its Minecraft calls use SRG names. All names used by the mod are the same in every 1.20.x version.

Mod metadata: Forge and NeoForge 1.20.1 – 1.20.4 read `META-INF/mods.toml` and the `MixinConfigs` manifest entry; NeoForge 1.20.5 – 1.20.6 read `META-INF/neoforge.mods.toml`; Fabric reads `fabric.mod.json`.

## Building from source

Requirements: **JDK 21 and JDK 25** in `C:\Program Files\Java`, and Windows PowerShell. (The mod is compiled for Java 17; Gradle downloads JDK 17 if it is missing.)

```powershell
powershell -ExecutionPolicy Bypass -File build.ps1
```

The script builds each loader project and merges them into one jar, which it writes to this folder.

### Project layout

| Path | Contents |
| --- | --- |
| `common/` | Shared Forge/NeoForge code: the Mixin, `OwnNametag`, `OwnNametagSrg`, `shownametag.mixins.json` and `pack.mcmeta` |
| `loaders/fabric` | Fabric Loom build against 1.20.1 (remapped to intermediary), its own Mixin and `fabric.mod.json` |
| `loaders/forge` | ForgeGradle 6 build against Forge 1.20.1 (reobfuscated to SRG), `mods.toml` and the `@Mod` entry class |
| `loaders/neoforge` | ModDevGradle build against NeoForge 1.20.4, `neoforge.mods.toml` and the `@Mod` entry class |
| `build.ps1` | Builds all loaders and merges them into a single jar |

## License

MIT, by Somersault41. See [LICENSE](LICENSE).
