package com.kevinvg.umalauncherj.helpertable.rows;

import com.kevinvg.umalauncherj.helpertable.domain.Cell;
import com.kevinvg.umalauncherj.helpertable.domain.Row;
import com.kevinvg.umalauncherj.helpertable.domain.TrainingState;

import java.util.ArrayList;
import java.util.List;

public class CurrentStatsRow extends Row {
    private final String longName = "Current stats";
    protected String shortName = "Current Stats";
    protected String description = "Shows the current stats of each facility.";

    @Override
    public List<Cell> generateCells(TrainingState state) {
        ArrayList<Cell> cells = new ArrayList<>();
        for (var commandState : state.getCommands()) {
            cells.add(new Cell(Integer.toString(commandState.getCurrentStats())));
        }
        return cells;
    }
}
