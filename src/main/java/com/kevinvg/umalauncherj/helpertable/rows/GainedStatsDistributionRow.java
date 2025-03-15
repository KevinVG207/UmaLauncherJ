package com.kevinvg.umalauncherj.helpertable.rows;

import com.cronutils.utils.StringUtils;
import com.kevinvg.umalauncherj.helpertable.domain.Cell;
import com.kevinvg.umalauncherj.helpertable.domain.ParamType;
import com.kevinvg.umalauncherj.helpertable.domain.Row;
import com.kevinvg.umalauncherj.helpertable.domain.TrainingState;

import java.util.ArrayList;
import java.util.List;

public class GainedStatsDistributionRow extends Row {
    @Override
    public String getLongName() {
        return "Stats gained distribution";
    }

    @Override
    public String getShortName() {
        return "Stat Gain <br>Distribution";
    }

    @Override
    public String getDescription() {
        return "Shows the stats gained per facility per type. This includes stats gained outside the facility itself.";
    }

    @Override
    public List<Cell> generateCells(TrainingState state) {
        ArrayList<Cell> cells = new ArrayList<>();
        var commands = state.getRelevantCommands();
        for (var commandState : commands.values()) {
            List<String> lines = new ArrayList<>();
            for (var entry : commandState.getParamsIncDecInfo().entrySet().stream().filter(e -> ParamType.STANDARD_TYPES.contains(e.getKey())).toList()) {
                lines.add(entry.getKey().displayName + ": " + entry.getValue());
            }

            cells.add(new Cell(StringUtils.join(lines.toArray(), "<br>")));
        }
        return cells;
    }
}
