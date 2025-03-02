package com.kevinvg.umalauncherj.util;

import lombok.Data;

@Data
public class Rect{
    private int x;
    private int y;
    private int width;
    private int height;

    public boolean isValid() {
        return width > 0 && height > 0;
    }
}
