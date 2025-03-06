package com.kevinvg.umalauncherj.helpertable.elements;

import com.kevinvg.umalauncherj.helpertable.Preset;
import com.kevinvg.umalauncherj.helpertable.domain.HelperTableElement;
import com.kevinvg.umalauncherj.helpertable.domain.TrainingState;

import static com.kevinvg.umalauncherj.helpertable.domain.Constants.TABLE_HEADERS;

public class HelperTable extends HelperTableElement {
    private final StringBuilder str = new StringBuilder();

    protected HelperTable(Preset preset) {
        super(preset);
    }

    @Override
    public String generate(TrainingState state) {
        str.setLength(0);
        str.append("<table id=\"training-table\">");
        generateHeader(state);
        generateBody(state);
        str.append("</table>");
        return str.toString();
    }

    private void generateHeader(TrainingState state) {
        str.append("<tr>");
        for (var commandState : state.getCommands()) {
            str.append("<th style=\"text-overflow: clip;white-space: nowrap;overflow: hidden;\">");
            str.append(TABLE_HEADERS.getOrDefault(commandState.getCommandType(), "Facility"));
            str.append("</th>");
        }
        str.append("</tr>");
    }

    private void generateBody(TrainingState state) {
        for (var row : preset.getRows()) {
            row.generate(state);
        }
    }
}
