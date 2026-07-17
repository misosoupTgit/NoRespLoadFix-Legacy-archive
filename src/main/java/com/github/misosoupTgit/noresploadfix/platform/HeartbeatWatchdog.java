package com.github.misosoupTgit.noresploadfix.platform;

import com.github.misosoupTgit.noresploadfix.config.NoRespLoadFixConfig;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Layer 2: Fallback heartbeat thread. Periodically sends PostMessage(WM_NULL)
 * to keep the message queue active. Does NOT call GetMessage/PeekMessage or
 * any GLFW function, so it is safe to call from a background thread.
 */
public final class HeartbeatWatchdog {

    private static final Logger LOGGER = LogManager.getLogger("NoRespLoadFix/HeartbeatWatchdog");
    private static final int WM_NULL = 0x0000;

    private volatile boolean running = false;
    private Thread thread;

    public void start(long hwnd, boolean isWindows) {
        if (!isWindows) return;
        if (!NoRespLoadFixConfig.ENABLE_HEARTBEAT_WATCHDOG.get()) {
            LOGGER.debug("[NoRespLoadFix] Layer 2 disabled by config.");
            return;
        }
        if (hwnd == 0L) {
            LOGGER.warn("[NoRespLoadFix] HWND is 0. Layer 2 will not start.");
            return;
        }

        running = true;
        thread = new Thread(() -> loop(hwnd), "NoRespLoadFix-Heartbeat");
        thread.setDaemon(true);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();

        LOGGER.info("[NoRespLoadFix] HeartbeatWatchdog started. interval={}ms, hwnd=0x{}",
                NoRespLoadFixConfig.HEARTBEAT_INTERVAL_MS.get(), Long.toHexString(hwnd));
    }

    private void loop(long hwnd) {
        WinDef.HWND nativeHwnd = new WinDef.HWND(new Pointer(hwnd));
        WinDef.WPARAM wParam = new WinDef.WPARAM(0);
        WinDef.LPARAM lParam = new WinDef.LPARAM(0);

        while (running) {
            try {
                User32Native.INSTANCE.PostMessageW(nativeHwnd, WM_NULL, wParam, lParam);
                Thread.sleep(NoRespLoadFixConfig.HEARTBEAT_INTERVAL_MS.get());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Throwable t) {
                LOGGER.error("[NoRespLoadFix] HeartbeatWatchdog error. Stopping.", t);
                break;
            }
        }
    }

    public void stop() {
        running = false;
        if (thread != null) thread.interrupt();
    }
}
