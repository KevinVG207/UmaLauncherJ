package com.kevinvg.umalauncherj.util;

//import com.kevinvg.umalauncherj.util.win32.User32;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.*;
import com.sun.jna.ptr.IntByReference;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public static Rect getWindowRect(WinDef.HWND hWnd) {
        var rect = new WinDef.RECT();
        USER_32.GetWindowRect(hWnd, rect);
        return Rect.fromWin32Rect(rect);
    }

    public static void moveWindow(WinDef.HWND hWnd, Rect rect) {
        moveWindow(hWnd, rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }

    public static void moveWindow(WinDef.HWND hWnd, int left, int top, int width, int height) {
        if (width <= 0 || height <= 0) {
            log.warn("Window width / height is less than 1: {} {}", width, height);
            return;
        }

//        log.info("Moving {} with params {} {} {} {}", hWnd, left, top, width, height);
        USER_32.MoveWindow(hWnd, left, top, width, height, true);
    }

    public static boolean isWindow(WinDef.HWND hWnd) {
        return USER_32.IsWindow(hWnd);
    }

    public static Rect getMonitorRect(WinDef.HWND handle) {
        var monitor = USER_32.MonitorFromWindow(handle, WinUser.MONITOR_DEFAULTTONEAREST);
        WinUser.MONITORINFOEX monitorInfo = new WinUser.MONITORINFOEX();
        USER_32.GetMonitorInfo(monitor, monitorInfo);
        return Rect.fromWin32Rect(monitorInfo.rcWork);
    }

    public static int getCurrentProcessId() {
        return KERNEL_32.GetCurrentProcessId();
    }

    public static List<Integer> getProcessIdsOfExecutable() {
        var processes = new ArrayList<ProcessInfo>();
        var snapshot = KERNEL_32.CreateToolhelp32Snapshot(Tlhelp32.TH32CS_SNAPPROCESS, new WinDef.DWORD(0));
        Tlhelp32.PROCESSENTRY32 pe = new Tlhelp32.PROCESSENTRY32();
        KERNEL_32.Process32First(snapshot, pe);
        ProcessInfo parent = null;
        do {
            var processInfo = new ProcessInfo(pe.th32ProcessID.intValue(), pe.th32ParentProcessID.intValue(), Native.toString(pe.szExeFile));
            processes.add(processInfo);
            if (processInfo.exeFile.equalsIgnoreCase(FileUtil.EXE_NAME)) {
                parent = processInfo;
            }
        } while(KERNEL_32.Process32Next(snapshot, pe));

        if (parent == null) {
            return Collections.emptyList();
        }

        var out = new ArrayList<Integer>();

        out.add(parent.processId);

        for (var processInfo : processes) {
            if (processInfo.parentId == parent.processId) {
                out.add(processInfo.processId);
            }
        }

        return out;
    }

    record ProcessInfo(int processId, int parentId, String exeFile){}

    //    public static void sendCloseSignal(WinDef.HWND handle) {
//        log.info("Send close signal to {}", handle);
//        USER_32.PostMessage(handle, WinUser.WM_CLOSE, new WinDef.WPARAM(0), new WinDef.LPARAM(0));
//    }
}
