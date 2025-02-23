package com.kevinvg.umalauncherj.helpertable;

import com.kevinvg.umalauncherj.MapSerializable;
import com.kevinvg.umalauncherj.Util;
import com.kevinvg.umalauncherj.settings.Settings;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public abstract class Row extends MapSerializable {
    private String longName = "";
    private String shortName = "";
    private String description = "";
    private Settings settings;
    private String style = "";
    private boolean disabled = false;
    private RowType rowType;

    public abstract List<Cell> getCells();

    @Override
    public Map<String, ?> toMap() {
        Map<String, Object> out = new HashMap<>();
        out.put("type", rowType.name());
        out.put("settings", settings.toMap());
        return out;
    }

    @Override
    public void fromMap(Map<String, ?> map) {

    }
}
