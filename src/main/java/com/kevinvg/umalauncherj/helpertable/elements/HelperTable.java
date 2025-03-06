package com.kevinvg.umalauncherj.helpertable.elements;

import com.kevinvg.umalauncherj.helpertable.Preset;
import com.kevinvg.umalauncherj.helpertable.domain.HelperTableElement;
import com.kevinvg.umalauncherj.helpertable.domain.TrainingState;

import static com.kevinvg.umalauncherj.helpertable.domain.Constants.TABLE_HEADERS;

public class HelperTable extends HelperTableElement {
    private final StringBuilder sb = new StringBuilder();

    protected HelperTable(Preset preset) {
        super(preset);
    }

    @Override
    public String generate(TrainingState state) {
        sb.setLength(0);
        sb.append("<table id=\"training-table\">");
        generateHeader(state);
        generateBody(state);
        sb.append("</table>");
        return sb.toString();
    }

    private void generateHeader(TrainingState state) {
        sb.append("<thead>");
        sb.append("<tr>");
        sb.append("<th style=\"text-overflow: clip;white-space: nowrap;overflow: hidden;\">Facility</th>");
        for (var commandState : state.getCommands()) {
            sb.append("<th style=\"text-overflow: clip;white-space: nowrap;overflow: hidden;\">");
            sb.append(TABLE_HEADERS.getOrDefault(commandState.getCommandType(), "Facility"));
            sb.append("</th>");
        }
        sb.append("</tr>");
        sb.append("</thead>");
    }

    private void generateBody(TrainingState state) {
        sb.append("<tbody>");
        for (var row : preset.getRows()) {
            sb.append(row.generate(state));
        }
        sb.append("</tbody>");
    }
}
