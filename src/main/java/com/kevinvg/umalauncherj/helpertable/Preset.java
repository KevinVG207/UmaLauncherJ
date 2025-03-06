package com.kevinvg.umalauncherj.helpertable;

import com.kevinvg.umalauncherj.helpertable.domain.Row;
import com.kevinvg.umalauncherj.helpertable.rows.CurrentStatsRow;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Preset {
    private String name = "";
    private List<Row> rows = new ArrayList<>();
    private PresetSettings settings = new PresetSettings();

    public Preset() {
        rows.add(new CurrentStatsRow());
    }
}
