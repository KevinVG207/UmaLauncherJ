package com.kevinvg.umalauncherj.helpertable.rows;

import com.kevinvg.umalauncherj.helpertable.domain.Cell;
import com.kevinvg.umalauncherj.helpertable.domain.Row;
import com.kevinvg.umalauncherj.helpertable.domain.TrainingState;

import java.util.ArrayList;
import java.util.List;

public class CurrentStatsRow extends Row {
    @Override
    public String getLongName() {
        return "Current stats";
    }

    @Override
    public String getShortName() {
        return "Current Stats";
    }

    @Override
    public String getDescription() {
        return "Shows the current stats of each facility.";
    }

    @Override
    public List<Cell> generateCells(TrainingState state) {
        ArrayList<Cell> cells = new ArrayList<>();
        for (var commandState : state.getRelevantCommands()) {
            cells.add(new Cell(Integer.toString(commandState.getCurrentStats())));
        }
        return cells;
    }
}
