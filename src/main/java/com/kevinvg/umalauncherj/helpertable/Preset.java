package com.kevinvg.umalauncherj.helpertable;

import com.kevinvg.umalauncherj.MapSerializable;
import com.kevinvg.umalauncherj.Util;
import com.kevinvg.umalauncherj.helpertable.rows.CurrentStatsRow;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Preset implements MapSerializable {
    private String name = "";
    private List<Row> rows = new ArrayList<>();
    private PresetSettings settings = new PresetSettings();

    public Preset() {
        rows.add(new CurrentStatsRow());
    }

    public Map<String, ?> toMap() {
        var out = new HashMap<String, Object>();
        out.put("name", name);
        out.put("rows", Util.listToMap(rows));
        out.put("settings", settings.toMap());
        return out;
    }

    @SuppressWarnings("unchecked")
    public void fromMap(Map<String, ?> map) {
        try {
            this.name = (String) map.get("name");
            this.rows = rowsMapListToList((List<Map<String, ?>>) map.get("rows"));
            this.settings = new PresetSettings();
            this.settings.fromMap((Map<String, ?>) map.get("settings"));
        } catch (ClassCastException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Row> rowsMapListToList(List<Map<String,?>> rows) {
        List<Row> out = new ArrayList<>();

        for (Map<String,?> row : rows) {
            RowType rowType = RowType.valueOf((String) row.get("rowType"));
            try {
                var instance = rowType.rowClass.getConstructor().newInstance();
                instance.fromMap(row);
                out.add(instance);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return out;
    }
}
