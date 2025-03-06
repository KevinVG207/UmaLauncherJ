package com.kevinvg.umalauncherj.helpertable.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Cell extends HelperTableElement {
    private String value = "";
    private boolean bold = false;
    private String color = "";
    private String background = "";
    private boolean percent = false;
    private String title = "";
    private String style = "text-overflow: clip;white-space: nowrap;overflow: hidden;";

    public Cell(String value) {
        this.value = value;
    }

    public Cell(String value, String title) {
        this.value = value;
        this.title = title;
    }

    public String generate(TrainingState state) {
        var curStyle = this.style;
        if (this.bold) {
            curStyle += "font-weight:bold;";
        }
        if (!this.color.isEmpty()){
            curStyle += "color:" + this.color + ";";
        }
        if (!this.background.isEmpty()){
            curStyle += "background:" + this.background + ";";
        }
        if (!curStyle.isEmpty()){
            curStyle = String.format(" style=\"%s\"", curStyle);
        }

        var title = this.title;
        if (!title.isEmpty()){
            title = title.replace("\n", "");
            title = String.format(" title=\"%s\"", title);
        }

        return String.format("<td%s%s>%s%s</td>", curStyle, title, this.value, this.percent ? "%" : "");
    }
}
