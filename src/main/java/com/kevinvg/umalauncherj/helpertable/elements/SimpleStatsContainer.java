package com.kevinvg.umalauncherj.helpertable.elements;

import com.kevinvg.umalauncherj.helpertable.Preset;
import com.kevinvg.umalauncherj.helpertable.domain.HelperTableElement;
import com.kevinvg.umalauncherj.helpertable.domain.TrainingState;
import com.kevinvg.umalauncherj.l18n.Localizer;

public class SimpleStatsContainer extends HelperTableElement {
    public SimpleStatsContainer(Preset preset) {
        super(preset);
    }

    @Override
    public String generate(TrainingState state, Localizer loc) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div style=\"width: 100%; display: flex; flex-direction: row; gap: 1rem; flex-wrap: wrap; justify-content: center;\">");

        sb.append(new Energy(preset).generate(state, loc));
        sb.append(new SkillPoints(preset).generate(state, loc));
        sb.append(new Fans(preset).generate(state, loc));

        sb.append("</div>");
        return sb.toString();
    }
}
