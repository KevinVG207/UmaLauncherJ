package com.kevinvg.umalauncherj.helpertable;

import com.kevinvg.umalauncherj.settings.Settings;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public abstract class Row {
    private String longName = "";
    private String shortName = "";
    private String description = "";
    private Settings settings;
    private String style = "";
    private boolean disabled = false;

    public abstract List<Cell> getCells();
}
