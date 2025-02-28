package com.kevinvg.umalauncherj.helpertable.rows;

import com.kevinvg.umalauncherj.helpertable.Cell;
import com.kevinvg.umalauncherj.helpertable.CommandState;
import com.kevinvg.umalauncherj.helpertable.Row;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CurrentStatsRow extends Row {
    private final String longName = "Current stats";
    protected String shortName = "Current Stats";
    protected String description = "Shows the current stats of each facility.";

    @Override
    public List<Cell> getCells(Map<String, CommandState> commandStates) {
        ArrayList<Cell> cells = new ArrayList<>();
        cells.add(new Cell(this.shortName, this.description));

        for (Map.Entry<String, CommandState> entry : commandStates.entrySet()) {
            cells.add(new Cell(Integer.toString(entry.getValue().getCurrentStats())));
        }

        return cells;
    }
}
