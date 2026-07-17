package com.github.misosoupTgit.noresploadfix.platform;

import com.github.misosoupTgit.noresploadfix.config.NoRespLoadFixConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Layer 1: Permanently disables Windows ghosting for this process via
 * DisableProcessWindowsGhosting(). Covers all blocking phases (startup,
 * texture/language/world loading, etc.) with a single one-time call.
 */
public final class GhostingGuard {

    private static final Logger LOGGER = LogManager.getLogger("NoRespLoadFix/GhostingGuard");

    private GhostingGuard() {}

    public static void apply(boolean isWindows) {
        if (!isWindows) return;
        if (!NoRespLoadFixConfig.ENABLE_GHOSTING_DISABLE.get()) {
            LOGGER.debug("[NoRespLoadFix] Layer 1 disabled by config.");
            return;
        }
        try {
            User32Native.INSTANCE.DisableProcessWindowsGhosting();
            LOGGER.info("[NoRespLoadFix] DisableProcessWindowsGhosting() applied.");
        } catch (UnsatisfiedLinkError e) {
            LOGGER.error("[NoRespLoadFix] Failed to load user32.dll. Layer 1 disabled.", e);
        } catch (Throwable t) {
            LOGGER.error("[NoRespLoadFix] Unexpected error in Layer 1.", t);
        }
    }
}
