package com.kevinvg.umalauncherj.helpertable.elements;

import com.kevinvg.umalauncherj.helpertable.Preset;
import com.kevinvg.umalauncherj.helpertable.domain.HelperTableElement;
import com.kevinvg.umalauncherj.helpertable.domain.TrainingState;
import com.kevinvg.umalauncherj.l18n.Localizer;

public class HelperTable extends HelperTableElement {
    private final StringBuilder sb = new StringBuilder();

    protected HelperTable(Preset preset) {
        super(preset);
    }

    @Override
    public String generate(TrainingState state, Localizer loc) {
        sb.setLength(0);
        sb.append("<table id=\"training-table\">");
        generateHeader(state, loc);
        generateBody(state, loc);
        sb.append("</table>");
        return sb.toString();
    }

    private void generateHeader(TrainingState state, Localizer loc) {
        sb.append("<thead>");
        sb.append("<tr>");
        sb.append("<th style=\"text-overflow: clip;white-space: nowrap;overflow: hidden;\">Facility</th>");
        for (var commandType : state.getRelevantCommands().keySet().stream().sorted().toList()) {
            sb.append("<th style=\"text-overflow: clip;white-space: nowrap;overflow: hidden;\">");
            sb.append(loc.get("COMMAND_TYPE_" + commandType.toString()));
            sb.append("</th>");
        }
        sb.append("</tr>");
        sb.append("</thead>");
    }

    private void generateBody(TrainingState state, Localizer loc) {
        sb.append("<tbody>");
        for (var row : preset.getRows()) {
            sb.append(row.generate(state, loc));
        }
        sb.append("</tbody>");
    }
}
