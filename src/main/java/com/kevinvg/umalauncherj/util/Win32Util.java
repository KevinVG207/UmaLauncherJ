package com.kevinvg.umalauncherj.util;

//import com.kevinvg.umalauncherj.util.win32.User32;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.*;
import com.sun.jna.ptr.IntByReference;
import io.quarkus.logging.Log;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class Win32Util {

    public static final String WINDOW_NAME = "umamusume";
    public static final String DMM_EXECUTABLE = "dmmgameplayer.exe";

    private static final User32 USER_32 = User32.INSTANCE;
    private static final Kernel32 KERNEL_32 = Kernel32.INSTANCE;
    private static final Psapi PSAPI = Psapi.INSTANCE;

    private Win32Util() {}

    public static WinDef.HWND getGameHandle() {
        return getWindowHandleNameEquals(WINDOW_NAME);
    }

    public static WinDef.HWND getWindowHandleNameEquals(String name) {
        WinDef.HWND[] result = new WinDef.HWND[1];

        USER_32.EnumWindows((hWnd, arg) -> {
            char[] windowText = new char[1024];
            USER_32.GetWindowText(hWnd, windowText, windowText.length);
            String wText = Native.toString(windowText);
            if (wText.equals(name)) {
                result[0] = hWnd;
            }
            return true;
        }, null);

        return result[0];
    }

    public static WinDef.HWND getDmmHandle() {
        return getWindowHandleExecutableEquals(DMM_EXECUTABLE);
    }

    public static WinDef.HWND getWindowHandleExecutableEquals(String executableName) {
        WinDef.HWND[] result = new WinDef.HWND[1];

        USER_32.EnumWindows((hWnd, arg) -> {
            IntByReference pid = new IntByReference();
            USER_32.GetWindowThreadProcessId(hWnd, pid);
            var processHandle = KERNEL_32.OpenProcess(WinNT.PROCESS_QUERY_LIMITED_INFORMATION, false, pid.getValue());

            byte[] pathBuffer = new byte[1024];
            PSAPI.GetModuleFileNameExA(processHandle, null, pathBuffer, 1024);
            String pathString = Native.toString(pathBuffer);
            Path path = Paths.get(pathString);
            String fileName = path.getFileName().toString();

            if (fileName.equalsIgnoreCase(executableName)) {
                result[0] = hWnd;
            }
            return true;
        }, null);

        return result[0];
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

//    public static void sendCloseSignal(WinDef.HWND handle) {
//        log.info("Send close signal to {}", handle);
//        USER_32.PostMessage(handle, WinUser.WM_CLOSE, new WinDef.WPARAM(0), new WinDef.LPARAM(0));
//    }
}
