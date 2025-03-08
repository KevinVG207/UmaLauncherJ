package com.kevinvg.umalauncherj.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.jna.platform.win32.WinDef;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class Rect{
    private static final int JANK_OFFSET = 8;

    private int x;
    private int y;
    private int width;
    private int height;

    public static Rect clone(Rect rect) {
        var out = new Rect();
        out.setX(rect.x);
        out.setY(rect.y);
        out.setWidth(rect.width);
        out.setHeight(rect.height);
        return out;
    }

    public static Rect fromWin32Rect(WinDef.RECT win32Rect) {
        var rect = new Rect();
        rect.setX(win32Rect.left);
        rect.setY(win32Rect.top);
        rect.setWidth(win32Rect.right - win32Rect.left);
        rect.setHeight(win32Rect.bottom - win32Rect.top);
        return rect;
    }

    @JsonIgnore
    public boolean isValid() {
        return width > 0 && height > 0;
    }

    public void applySafezone(Safezone safezone) {
        x += safezone.getLeft();
        y += safezone.getTop();
        width -= safezone.getLeft() + safezone.getRight();
        height -= safezone.getTop() + safezone.getBottom();
        if (width < 0) width = 1;
        if (height < 0) height = 1;
    }

    @JsonIgnore
    public Orientation getOrientation() {
        if (width > height) {
            return Orientation.LANDSCAPE;
        }
        return Orientation.PORTRAIT;
    }

    public void dejankify() {
        x += JANK_OFFSET;
        width -= JANK_OFFSET * 2;
        height -= JANK_OFFSET;
    }

    public void jankify() {
        x -= JANK_OFFSET;
        width += JANK_OFFSET * 2;
        height += JANK_OFFSET;
    }

    public void scaleByHeight(int newHeight) {
        float mult = (float) newHeight / height;
        height = newHeight;
        width = Math.round(width * mult);
    }

    public void scaleByWidth(int newWidth) {
        float mult = (float) newWidth / width;
        width = newWidth;
        height = Math.round(height * mult);
    }
}
