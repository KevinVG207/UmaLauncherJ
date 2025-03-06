package com.kevinvg.umalauncherj.helpertable.elements;

import com.kevinvg.umalauncherj.helpertable.Preset;
import com.kevinvg.umalauncherj.helpertable.domain.HelperTableElement;
import com.kevinvg.umalauncherj.helpertable.domain.TrainingState;

public class SimpleStatsContainer extends HelperTableElement {
    public SimpleStatsContainer(Preset preset) {
        super(preset);
    }

    @Override
    public String generate(TrainingState state) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div style=\"width: 100%; display: flex; flex-direction: row; gap: 1rem; flex-wrap: wrap; justify-content: center;\">");

        sb.append(new Energy(preset).generate(state));
        sb.append(new SkillPoints(preset).generate(state));
        sb.append(new Fans(preset).generate(state));

        sb.append("</div>");
        return sb.toString();
    }
}
