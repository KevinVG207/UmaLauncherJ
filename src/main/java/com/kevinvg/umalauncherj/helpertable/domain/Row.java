package com.kevinvg.umalauncherj.helpertable.domain;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.kevinvg.umalauncherj.l18n.Localizer;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonTypeInfo(
        use = JsonTypeInfo.Id.MINIMAL_CLASS
)
public abstract class Row extends HelperTableElement {
    protected String longName = "";
    protected String shortName = "";
    protected String description = "";
    protected String style = "";
    protected boolean disabled = false;

    public abstract List<Cell> generateCells(TrainingState state);

    @Override
    public String generate(TrainingState state, Localizer loc) {
        StringBuilder sb = new StringBuilder();
        sb.append("<tr");
        if (!getStyle().isBlank()) {
            sb.append(" style=\"").append(getStyle()).append("\"");
        }
        sb.append(">");
        sb.append(new Cell(getShortName(), getDescription()).generate(state, loc));
        for (var cell : generateCells(state)) {
            sb.append(cell.generate(state, loc));
        }
        sb.append("</tr>");
        return sb.toString();
    }
}
