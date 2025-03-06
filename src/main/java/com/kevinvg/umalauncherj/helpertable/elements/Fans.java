package com.kevinvg.umalauncherj.helpertable.elements;

import com.kevinvg.umalauncherj.helpertable.Preset;
import com.kevinvg.umalauncherj.helpertable.domain.HelperTableElement;
import com.kevinvg.umalauncherj.helpertable.domain.TrainingState;

public class Fans extends HelperTableElement {
    protected Fans(Preset preset) {
        super(preset);
    }

    @Override
    public String generate(TrainingState state) {
        return "<div id=\"fans\"><b>Fans:</b> %,d</div>".formatted(state.getFans());
    }
}
