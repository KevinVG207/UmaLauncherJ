package com.kevinvg.umalauncherj.helpertable.domain;

import com.kevinvg.umalauncherj.helpertable.Preset;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class HelperTableElement {
    protected Preset preset = null;

    protected HelperTableElement(Preset preset) {
        this.preset = preset;
    }

    public abstract String generate(TrainingState state);
}
