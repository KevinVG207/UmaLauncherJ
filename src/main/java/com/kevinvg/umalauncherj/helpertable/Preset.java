package com.kevinvg.umalauncherj.helpertable;

import com.kevinvg.umalauncherj.MapSerializable;
import com.kevinvg.umalauncherj.Util;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Preset extends MapSerializable {
    private String name = "";
    private List<Row> rows = new ArrayList<>();
    private PresetSettings settings = new PresetSettings();

    public Map<String, ?> toMap() {
        var out = new HashMap<String, Object>();
        out.put("name", name);
        out.put("rows", Util.listToMap(rows));
        out.put("settings", settings.toMap());
        return out;
    }

    public void fromMap(Map<String, ?> map) {

    }
}
