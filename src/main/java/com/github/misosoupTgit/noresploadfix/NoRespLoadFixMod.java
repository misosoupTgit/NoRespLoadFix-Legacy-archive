package com.github.misosoupTgit.noresploadfix;

import com.github.misosoupTgit.noresploadfix.config.NoRespLoadFixConfig;
import com.github.misosoupTgit.noresploadfix.platform.GhostingGuard;
import com.github.misosoupTgit.noresploadfix.platform.HeartbeatWatchdog;
import com.github.misosoupTgit.noresploadfix.util.WindowHandleProvider;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.event.GameShuttingDownEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(NoRespLoadFixMod.MOD_ID)
public final class NoRespLoadFixMod {

    public static final String MOD_ID = "noresploadfix";

    private static final Logger LOGGER = LogManager.getLogger("NoRespLoadFix");

    private static final boolean IS_WINDOWS =
            System.getProperty("os.name", "").toLowerCase().contains("win");

    private final HeartbeatWatchdog heartbeatWatchdog = new HeartbeatWatchdog();

    public NoRespLoadFixMod() {
        if (FMLEnvironment.dist != Dist.CLIENT) {
            return;
        }

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, NoRespLoadFixConfig.CLIENT_SPEC,
                MOD_ID + "-client.toml");

        ModLoadingContext.get().registerExtensionPoint(
                IExtensionPoint.DisplayTest.class,
                () -> new IExtensionPoint.DisplayTest(
                        () -> net.minecraftforge.network.NetworkConstants.IGNORESERVERONLY,
                        (a, b) -> true));

        // Layer 1: Disable ghosting as early as possible (covers all blocking phases)
        GhostingGuard.apply(IS_WINDOWS);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
        MinecraftForge.EVENT_BUS.addListener(this::onGameShuttingDown);
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        if (!NoRespLoadFixConfig.ENABLED.get()) {
            LOGGER.info("[NoRespLoadFix] Disabled by config.");
            return;
        }
        if (!IS_WINDOWS) {
            return;
        }

        long glfwWindowHandle = Minecraft.getInstance().getWindow().getWindow();
        long hwnd = WindowHandleProvider.getHwnd(glfwWindowHandle);
        heartbeatWatchdog.start(hwnd, IS_WINDOWS);
    }

    private void onGameShuttingDown(GameShuttingDownEvent event) {
        heartbeatWatchdog.stop();
    }
}
