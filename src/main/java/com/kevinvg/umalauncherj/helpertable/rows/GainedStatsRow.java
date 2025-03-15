package com.kevinvg.umalauncherj.helpertable.rows;

import com.kevinvg.umalauncherj.helpertable.domain.Cell;
import com.kevinvg.umalauncherj.helpertable.domain.ParamType;
import com.kevinvg.umalauncherj.helpertable.domain.Row;
import com.kevinvg.umalauncherj.helpertable.domain.TrainingState;

import java.util.ArrayList;
import java.util.List;

public class GainedStatsRow extends Row {
    @Override
    public String getLongName() {
        return "Stats gained total";
    }

    @Override
    public String getShortName() {
        return "Stat Gain";
    }

    @Override
    public String getDescription() {
        return "Shows the total stats gained per facility. This includes stats gained outside the facility itself. \nExcludes skill points by default.";
    }

    @Override
    public List<Cell> generateCells(TrainingState state) {
        ArrayList<Cell> cells = new ArrayList<>();
        var commands = state.getRelevantCommands();
        for (var commandState : commands.values()) {

            int gained = 0;
            for (var entry : commandState.getParamsIncDecInfo().entrySet().stream().filter(e -> ParamType.STANDARD_TYPES.contains(e.getKey())).toList()) {
                gained += entry.getValue();
            }

            cells.add(new Cell(String.valueOf(gained)));
        }
        return cells;
    }
}
