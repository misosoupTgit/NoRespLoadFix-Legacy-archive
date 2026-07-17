# NoRespLoadFix

A Minecraft Forge mod that prevents Windows from showing the "Not Responding" dialog during high-load operations such as modpack startup, texture reloading, world loading, and language loading.

## How It Works

Uses `DisableProcessWindowsGhosting()` — a Win32 API provided by Microsoft for exactly this use case — to permanently disable OS-level window ghosting for the process. This is applied via a Forge `ITransformationService`, which runs before any Minecraft code (including the early loading window), ensuring coverage from the very first moment.

A secondary heartbeat thread (`PostMessage(WM_NULL)`) runs as a lightweight fallback throughout the game session.

## Layers of Protection

| Layer | Mechanism | When Applied |
|---|---|---|
| 1 (Primary) | `DisableProcessWindowsGhosting()` | Before the loading window appears |
| 2 (Fallback) | `PostMessage(WM_NULL)` thread | From client setup until shutdown |

## Compatibility

- Does not call any GLFW functions from background threads
- Does not hook `WindowProc` or intercept messages
- Compatible with Embeddium(Rubidium), ModernFix
...etc optimization mods

## Requirements

- Minecraft 1.20.1
- Forge 47.x
- Windows (no-op on Linux/macOS)
- Client-side only