package com.github.misosoupTgit.noresploadfix.early;

import cpw.mods.modlauncher.api.IEnvironment;
import cpw.mods.modlauncher.api.ITransformationService;
import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.IncompatibleEnvironmentException;

import java.util.List;
import java.util.Set;

/**
 * Runs before any Minecraft/Forge code executes, including the early loading window.
 * Calls DisableProcessWindowsGhosting() as early as possible so that the OS
 * ghosting mechanism is suppressed for the entire process lifetime from the start.
 */
public final class EarlyGhostingService implements ITransformationService {

    @Override
    public String name() {
        return "noresploadfix_early";
    }

    @Override
    public void initialize(IEnvironment environment) {
        if (!System.getProperty("os.name", "").toLowerCase().contains("win")) return;
        try {
            // Use JNA (available in the game bootstrap classpath) to call the Win32 API.
            // Avoid referencing our own User32Native class here to prevent classloader issues.
            com.sun.jna.NativeLibrary user32 = com.sun.jna.NativeLibrary.getInstance("user32");
            com.sun.jna.Function fn = user32.getFunction("DisableProcessWindowsGhosting");
            fn.invoke(Void.class, new Object[]{});
            System.out.println("[NoRespLoadFix/Early] DisableProcessWindowsGhosting() applied via ITransformationService.");
        } catch (Throwable t) {
            // Silently ignore; GhostingGuard in the @Mod constructor will retry.
            System.out.println("[NoRespLoadFix/Early] Early ghosting disable failed (will retry in @Mod): " + t);
        }
    }

    @Override
    public void onLoad(IEnvironment env, Set<String> otherServices) {}

    @Override
    @SuppressWarnings("rawtypes")
    public List<ITransformer> transformers() {
        return List.of();
    }
}
