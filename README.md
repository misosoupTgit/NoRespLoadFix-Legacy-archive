# <span style="color:#f00">This project has already been archived!</span>

This is due to special circumstances related to project management, so we are simply migrating the page.</span>

Since the modpack has already been added, we are treating it as an archived project that has not been listed. We recommend migrating it as soon as possible.</span>

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

## ⚠️ Important Considerations

### Masking Real Issues

This mod works by **deceiving the Windows OS** about the process responsiveness using heartbeat signals (`PostMessage(WM_NULL)`). While this prevents false "Not Responding" dialogs during legitimate high-load operations, it has a critical drawback:

**If a genuine crash, hang, or serious problem occurs, you may not notice it immediately.** The mod will continue to report the process as "alive" to Windows, potentially delaying your awareness of actual issues.

### Recommendation

- **Stable Modpacks**: Safe to use. If your modpack is already well-optimized and stable, this mod prevents frustrating false-positive notifications.
- **Unstable/Experimental Configurations**: Use with caution. You risk missing early warning signs of real problems. Consider monitoring logs actively and watching for actual freezes or performance degradation rather than relying on the OS dialog to alert you.
- **Development/Testing**: Not recommended. You want immediate feedback when something breaks.

### Best Practice

Use this mod only in **well-tested, stable configurations** where you're confident the underlying modpack is functioning correctly. Do not rely on it as a substitute for proper troubleshooting in problematic setups.
