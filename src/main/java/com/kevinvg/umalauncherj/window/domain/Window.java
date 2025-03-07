package com.kevinvg.umalauncherj.window.domain;

import com.kevinvg.umalauncherj.settings.app.AppSettingsManager;
import com.kevinvg.umalauncherj.util.Orientation;
import com.kevinvg.umalauncherj.util.Rect;
import com.kevinvg.umalauncherj.util.Win32Util;
import com.sun.jna.platform.win32.WinDef;
import lombok.Getter;

import static com.kevinvg.umalauncherj.settings.app.AppSettings.SettingKey.MAXIMIZE_SAFEZONE;

public class Window {
    private final AppSettingsManager settings;

    private final WinDef.HWND handle;
    @Getter
    private final Rect rect;

    public Window(WinDef.HWND handle, AppSettingsManager settings) {
        this.settings = settings;
        this.handle = handle;
        this.rect = Win32Util.getWindowRect(handle);
    }

    public Orientation getOrientation() {
        if (rect.getWidth() > rect.getHeight()) {
            return Orientation.LANDSCAPE;
        }
        return Orientation.PORTRAIT;
    }

    public void maximizeAndCenter() {
        Rect workspaceRect = this.getWorkspaceRect();

        var newRect = Rect.clone(rect);
        newRect.dejankify();
        newRect.scaleByHeight(workspaceRect.getHeight());

        if (newRect.getWidth() > workspaceRect.getWidth()) {
            newRect = Rect.clone(rect);
            newRect.dejankify();
            newRect.scaleByWidth(workspaceRect.getWidth());
        }

        int newY = workspaceRect.getY() + (int) Math.round((workspaceRect.getHeight() * 0.5) - (newRect.getHeight() * 0.5));

        newRect.jankify();
        setWidthFromHeight(newRect);

        int newX = workspaceRect.getX() + (int) Math.round((workspaceRect.getWidth() * 0.5) - (newRect.getWidth() * 0.5));

        newRect.setX(newX);
        newRect.setY(newY);
        // FIXME: Something goes wrong on my vertical monitor with landscape mode?

        move(newRect);
    }

    private Rect getWorkspaceRect() {
        var workspaceRect = Win32Util.getMonitorRect(this.handle);
        workspaceRect.applySafezone(settings.get(MAXIMIZE_SAFEZONE));
        return workspaceRect;
    }

    public void move(Rect newRect) {
        // Do not actually set the rect variable since we won't know if the resizing succeeded.
        Win32Util.moveWindow(handle, newRect);
    }

    private static void setWidthFromHeight(Rect rect) {
        if (rect.getOrientation() == Orientation.PORTRAIT) {
           rect.setWidth((int)Math.ceil((rect.getHeight() * 0.5626065430) - 6.2123937177));
        } else {
            rect.setWidth((int)Math.ceil((rect.getHeight() * 1.7770777107) - 52.7501897551));
        }
    }
}
