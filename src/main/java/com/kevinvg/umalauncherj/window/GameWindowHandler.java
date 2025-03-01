package com.kevinvg.umalauncherj.window;

import com.kevinvg.umalauncherj.util.Win32Util;
import com.sun.jna.platform.win32.WinDef;
import io.quarkus.scheduler.Scheduled;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class GameWindowHandler {
    private static WinDef.HWND handle;
    private static boolean hasExisted = false;

    private GameWindowHandler() {
    }

    @Scheduled(every = "0.5s")
    void checkForGameWindow() {
        // Trying to find the game for the first time
        if (!gameWindowExists() && !hasExisted) {
            findGameWindow();
            return;
        }

        // Game existed but has closed since
        if (!gameWindowExists()) {
            gameIsClosed();
            return;
        }

        if (!Win32Util.isWindow(handle)) {
            gameJustClosed();
            return;
        }

        if (!hasExisted) {
            gameJustDetected();
            return;
        }

        gameExists();
    }

    private void findGameWindow() {
        log.info("Checking for game window.");
        handle = Win32Util.getGameHandle();
    }

    private void gameJustClosed() {
        log.info("Game window stopped existing.");
        handle = null;
    }

    private void gameIsClosed() {
        // TODO: Start shutdown procedure?
        log.info("Game window has closed.");
    }

    private void gameJustDetected() {
        log.info("Game window detected!");
        hasExisted = true;
    }

    private void gameExists() {
        log.info("Game window exists.");
    }

    public static boolean gameWindowExists() {
        return handle != null;
    }
}
