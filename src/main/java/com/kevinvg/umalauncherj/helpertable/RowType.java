package com.kevinvg.umalauncherj.helpertable;

import com.kevinvg.umalauncherj.helpertable.rows.CurrentStatsRow;
import com.kevinvg.umalauncherj.settings.Settings;

public enum RowType {
    CURRENT_STATS(CurrentStatsRow.class, null);

    public final Class<? extends Row> rowClass;
    public final Class<? extends Settings> settingsClass;

    RowType(final Class<? extends Row> rowClass, final Class<? extends Settings> settingsClass) {
        this.rowClass = rowClass;
        this.settingsClass = settingsClass;
    }
}
