package com.github.misosoupTgit.noresploadfix.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFWNativeWin32;

/** Retrieves the Win32 HWND from a GLFW window handle. Must be called on the main thread. */
public final class WindowHandleProvider {

    private static final Logger LOGGER = LogManager.getLogger("NoRespLoadFix/WindowHandleProvider");

    private WindowHandleProvider() {}

    public static long getHwnd(long glfwWindowHandle) {
        if (glfwWindowHandle == 0L) {
            LOGGER.warn("[NoRespLoadFix] GLFW window handle is 0. Cannot retrieve HWND.");
            return 0L;
        }
        try {
            long hwnd = GLFWNativeWin32.glfwGetWin32Window(glfwWindowHandle);
            if (hwnd == 0L) {
                LOGGER.warn("[NoRespLoadFix] glfwGetWin32Window() returned 0.");
            }
            return hwnd;
        } catch (Throwable t) {
            LOGGER.error("[NoRespLoadFix] Failed to get HWND.", t);
            return 0L;
        }
    }
}
