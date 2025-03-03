package com.kevinvg.umalauncherj.tray;

import com.kevinvg.umalauncherj.util.ResourcesUtil;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;

@Singleton
public class TrayIconController {
    private TrayIcon trayIcon = null;
    private SystemTray systemTray = null;

    private final String iconLocation = "trayIcons/default.png";

    @Inject
    public TrayIconController() {
        if (!SystemTray.isSupported()) {
            System.err.println("SystemTray not supported");
            return;
        }

        systemTray = SystemTray.getSystemTray();

        System.out.println(new File(iconLocation).getAbsolutePath());

        // Image image = Toolkit.getDefaultToolkit().getImage(iconLocation);
        Image image = ResourcesUtil.loadImageFromResources(iconLocation);

        if (image == null) {
            throw new RuntimeException("Unable to load image from " + iconLocation);
        }

        ActionListener listener = e -> {
            System.out.println(e.getActionCommand());
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
        systemTray.remove(trayIcon);
    }
}
