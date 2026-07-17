package com.github.misosoupTgit.noresploadfix.config;

import net.minecraftforge.common.ForgeConfigSpec;

public final class NoRespLoadFixConfig {

    public static final ForgeConfigSpec CLIENT_SPEC;

    public static final ForgeConfigSpec.BooleanValue ENABLED;
    public static final ForgeConfigSpec.BooleanValue ENABLE_GHOSTING_DISABLE;
    public static final ForgeConfigSpec.BooleanValue ENABLE_HEARTBEAT_WATCHDOG;
    public static final ForgeConfigSpec.IntValue HEARTBEAT_INTERVAL_MS;
    public static final ForgeConfigSpec.BooleanValue DEBUG_LOGGING;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment("NoRespLoadFix Settings").push("general");

        ENABLED = builder
                .comment("Set to false to disable the mod entirely.")
                .define("enabled", true);

        ENABLE_GHOSTING_DISABLE = builder
                .comment("Layer 1: Call DisableProcessWindowsGhosting() to permanently suppress OS ghosting.",
                         "Recommended to keep enabled. One-time call with no ongoing cost.")
                .define("enableGhostingDisable", true);

        ENABLE_HEARTBEAT_WATCHDOG = builder
                .comment("Layer 2: Start a background thread that periodically sends PostMessage(WM_NULL).",
                         "Acts as a fallback in case Layer 1 is unavailable.")
                .define("enableHeartbeatWatchdog", true);

        HEARTBEAT_INTERVAL_MS = builder
                .comment("Heartbeat interval in milliseconds. Minimum: 500.")
                .defineInRange("heartbeatIntervalMs", 1000, 500, 10000);

        DEBUG_LOGGING = builder
                .comment("Enable verbose debug logging.")
                .define("debugLogging", false);

        builder.pop();
        CLIENT_SPEC = builder.build();
    }

    private NoRespLoadFixConfig() {}
}
