package com.kevinvg.umalauncherj.helpertable;

import com.kevinvg.umalauncherj.MapSerializable;
import com.kevinvg.umalauncherj.settings.Settings;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class Row extends MapSerializable {
    protected String longName = "";
    protected String shortName = "";
    protected String description = "";
    protected Settings settings;
    protected String style = "";
    protected boolean disabled = false;
    protected RowType rowType;

    public abstract List<Cell> getCells(Map<String, CommandState> commandStates);

    @Override
    public Map<String, ?> toMap() {
        Map<String, Object> out = new HashMap<>();
        out.put("rowType", rowType.name());
        out.put("settings", settings == null ? null : settings.toMap());
        return out;
    }

    @Override
    public void fromMap(Map<String, ?> map) {
        try {
            this.rowType = RowType.valueOf((String) map.get("rowType"));

            var settingsClass = this.rowType.settingsClass;
            if (settingsClass != null) {
                this.settings = settingsClass.getConstructor().newInstance();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
