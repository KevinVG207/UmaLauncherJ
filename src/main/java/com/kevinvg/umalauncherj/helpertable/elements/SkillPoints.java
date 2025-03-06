package com.kevinvg.umalauncherj.helpertable.elements;

import com.kevinvg.umalauncherj.helpertable.Preset;
import com.kevinvg.umalauncherj.helpertable.domain.HelperTableElement;
import com.kevinvg.umalauncherj.helpertable.domain.TrainingState;

public class SkillPoints extends HelperTableElement {
    protected SkillPoints(Preset preset) {
        super(preset);
    }

    @Override
    public String generate(TrainingState state) {
        return "<div id=\"skill-pt\"><b>Skill Points:</b> %,d</div>".formatted(state.getSkillPt());
    }
}
