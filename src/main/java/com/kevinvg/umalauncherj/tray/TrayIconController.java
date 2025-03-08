package com.kevinvg.umalauncherj.tray;

import com.kevinvg.umalauncherj.settings.app.AppSettingsManager;
import com.kevinvg.umalauncherj.util.ResourcesUtil;
import com.kevinvg.umalauncherj.window.GameWindowManager;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.Shutdown;
import jakarta.inject.Inject;
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
    private final PopupMenu popupMenu = new PopupMenu();

    private static final String DEFAULT_TOOLTIP = "Uma Launcher";

    private static final String DEFAULT_ICON = "trayIcons/default.png";
    private static final String CONNECTING_ICON = "trayIcons/connecting.png";
    private static final String CONNECTED_ICON = "trayIcons/connected.png";


    @Inject
    TrayIconController() {
        // Setup
        if (!SystemTray.isSupported()) {
            log.error("SystemTray not supported");
            return;
        }

        systemTray = SystemTray.getSystemTray();

        Image image = ResourcesUtil.loadImageFromResources(DEFAULT_ICON);

        if (image == null) {
            throw new RuntimeException("Unable to load image from " + DEFAULT_ICON);
        }

        MenuItem defaultItem = new MenuItem("Quit");
        ActionListener listener = e -> {
            log.info(e.getActionCommand());
            this.quitAction();
        };
        defaultItem.addActionListener(listener);
        popupMenu.add(defaultItem);

        trayIcon = new TrayIcon(image, DEFAULT_TOOLTIP, popupMenu);

        try {
            systemTray.add(trayIcon);
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertMenuItem(MenuItem menuItem, int index) {
        popupMenu.insert(menuItem, index);
    }

    private void quitAction() {
        Quarkus.asyncExit();
    }

    public void setConnecting() {
        if (trayIcon == null) {
            return;
        }

        trayIcon.setImage(ResourcesUtil.loadImageFromResources(CONNECTING_ICON));
        trayIcon.setToolTip("Connecting to VPN...");
    }

    public void setConnected() {
        if (trayIcon == null) {
            return;
        }

        trayIcon.setImage(ResourcesUtil.loadImageFromResources(CONNECTED_ICON));
        trayIcon.setToolTip("UmaLauncher - VPN Connected");
    }

    public void resetStatus() {
        if (trayIcon == null) {
            return;
        }

        trayIcon.setImage(ResourcesUtil.loadImageFromResources(DEFAULT_ICON));
        trayIcon.setToolTip(DEFAULT_TOOLTIP);
    }

    @Shutdown
    void shutdown() {
        log.info("TrayIconController shutting down");
        if (systemTray != null && trayIcon != null) {
            systemTray.remove(trayIcon);
        }
    }
}
