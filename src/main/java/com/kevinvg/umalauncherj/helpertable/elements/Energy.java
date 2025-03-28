package com.kevinvg.umalauncherj.helpertable.elements;

import com.kevinvg.umalauncherj.helpertable.Preset;
import com.kevinvg.umalauncherj.helpertable.domain.HelperTableElement;
import com.kevinvg.umalauncherj.helpertable.domain.TrainingState;
import com.kevinvg.umalauncherj.l18n.Localizer;

public class Energy extends HelperTableElement {
    protected Energy(Preset preset) {
        super(preset);
    }

    @Override
    public String generate(TrainingState state, Localizer loc) {
        return "<div id=\"energy\"><b>Energy:</b> %d/%d</div>".formatted(state.getEnergy(), state.getMaxEnergy());
    }
}
