package com.kevinvg.umalauncherj.util;

//import com.kevinvg.umalauncherj.util.win32.User32;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import io.quarkus.logging.Log;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Win32Util {

    private static final String WINDOW_NAME = "umamusume";

    private static final User32 USER_32 = User32.INSTANCE;

    public static WinDef.HWND getGameHandle() {
        WinDef.HWND[] umawindow = new WinDef.HWND[1];

        USER_32.EnumWindows((hWnd, arg) -> {
            char[] windowText = new char[1024];
            USER_32.GetWindowText(hWnd, windowText, windowText.length);
            String wText = Native.toString(windowText);
            if (wText.equals(WINDOW_NAME)) {
                umawindow[0] = hWnd;
            }
            return true;
        }, null);

        return umawindow[0];
    }

    public static WinDef.RECT getWindowRect(WinDef.HWND hWnd) {
        var rect = new WinDef.RECT();
        USER_32.GetWindowRect(hWnd, rect);
        return rect;
    }

    public static void moveWindow(WinDef.HWND hWnd, int left, int top, int width, int height) {
        if (width <= 0 || height <= 0) {
            log.warn("Window width / height is less than 1: {} {}", width, height);
            return;
        }

        log.info("Moving {} with params {} {} {} {}", hWnd, left, top, width, height);
        USER_32.MoveWindow(hWnd, left, top, width, height, true);
    }

    public static boolean isWindow(WinDef.HWND hWnd) {
        return USER_32.IsWindow(hWnd);
    }
}
