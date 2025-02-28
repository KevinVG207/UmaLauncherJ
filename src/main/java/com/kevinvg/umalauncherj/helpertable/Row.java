package com.kevinvg.umalauncherj.helpertable;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.kevinvg.umalauncherj.settings.Settings;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@JsonTypeInfo(
        use = JsonTypeInfo.Id.MINIMAL_CLASS
)
public abstract class Row {
    protected String longName = "";
    protected String shortName = "";
    protected String description = "";
    protected Settings settings;
    protected String style = "";
    protected boolean disabled = false;
//    protected RowType rowType;

    public abstract List<Cell> getCells(Map<String, CommandState> commandStates);
}
