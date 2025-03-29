package com.kevinvg.umalauncherj.helpertable.elements;

import com.kevinvg.umalauncherj.helpertable.Preset;
import com.kevinvg.umalauncherj.helpertable.domain.HelperTableElement;
import com.kevinvg.umalauncherj.helpertable.domain.TrainingState;
import com.kevinvg.umalauncherj.l18n.Localizer;

public class Overlay extends HelperTableElement {
    public Overlay(Preset preset) {
        super(preset);
    }

    @Override
    public String generate(TrainingState state, Localizer loc) {
        StringBuilder sb = new StringBuilder();

        sb.append(new SimpleStatsContainer(preset).generate(state, loc));
        sb.append(new HelperTable(preset).generate(state, loc));

        return sb.toString();
    }
}
