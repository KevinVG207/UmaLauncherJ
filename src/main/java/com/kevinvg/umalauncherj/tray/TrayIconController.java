package com.kevinvg.umalauncherj.tray;

import com.kevinvg.umalauncherj.util.ResourcesUtil;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.Shutdown;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;

@Slf4j
@Singleton
public class TrayIconController {
    private TrayIcon trayIcon = null;
    private SystemTray systemTray = null;

    private static final String ICON_LOCATION = "trayIcons/default.png";

    @Startup
    void init() {
        if (!SystemTray.isSupported()) {
            log.error("SystemTray not supported");
            return;
        }

        systemTray = SystemTray.getSystemTray();

        log.info(new File(ICON_LOCATION).getAbsolutePath());

        Image image = ResourcesUtil.loadImageFromResources(ICON_LOCATION);

        if (image == null) {
            throw new RuntimeException("Unable to load image from " + ICON_LOCATION);
        }

        ActionListener listener = e -> {
            log.info(e.getActionCommand());
            this.quitAction();
        };

        PopupMenu popupMenu = new PopupMenu();

        MenuItem defaultItem = new MenuItem("Quit");
        defaultItem.addActionListener(listener);
        popupMenu.add(defaultItem);

        trayIcon = new TrayIcon(image, "Uma Launcher", popupMenu);

        try {
            systemTray.add(trayIcon);
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    private void quitAction() {
        Quarkus.asyncExit();
    }

    @Shutdown
    void shutdown() {
        log.info("TrayIconController shutting down");
        if (systemTray != null && trayIcon != null) {
            systemTray.remove(trayIcon);
        }
    }
}
