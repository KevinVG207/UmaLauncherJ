package com.kevinvg.umalauncherj.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class Rect{
    private int x;
    private int y;
    private int width;
    private int height;

    @JsonIgnore
    public boolean isValid() {
        return width > 0 && height > 0;
    }
}
