package com.kevinvg.umalauncherj.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Safezone {
    private int left;
    private int top;
    private int right;
    private int bottom;
}
