package com.github.misosoupTgit.noresploadfix.platform;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

/** JNA binding for user32.dll. Windows-only; guarded by OS checks in callers. */
public interface User32Native extends StdCallLibrary {

    User32Native INSTANCE = Native.load("user32", User32Native.class, W32APIOptions.DEFAULT_OPTIONS);

    /** Permanently disables window ghosting for the entire process. */
    void DisableProcessWindowsGhosting();

    /** Non-blocking message post. Safe to call from any thread (Win32 spec). */
    boolean PostMessageW(WinDef.HWND hWnd, int msg, WinDef.WPARAM wParam, WinDef.LPARAM lParam);
}
