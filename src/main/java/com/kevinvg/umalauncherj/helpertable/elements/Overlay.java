package com.kevinvg.umalauncherj.helpertable.elements;

import com.kevinvg.umalauncherj.helpertable.Preset;
import com.kevinvg.umalauncherj.helpertable.domain.HelperTableElement;
import com.kevinvg.umalauncherj.helpertable.domain.TrainingState;

public class Overlay extends HelperTableElement {
    public Overlay(Preset preset) {
        super(preset);
    }

    @Override
    public String generate(TrainingState state) {
        StringBuilder sb = new StringBuilder();

        sb.append(new Energy(preset).generate(state));
        sb.append(new SkillPoints(preset).generate(state));
        sb.append(new Fans(preset).generate(state));
        sb.append(new HelperTable(preset).generate(state));

        return sb.toString();
    }
}
