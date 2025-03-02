package com.kevinvg.umalauncherj.settings.types;

import com.kevinvg.umalauncherj.util.Rect;
import com.kevinvg.umalauncherj.settings.Setting;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class RectSetting extends Setting<Rect> {
    public RectSetting(Rect value, String name, String description) {
        super(value, name, description);
    }

    public RectSetting(Rect value, String name, String description, boolean hidden) {
        super(value, name, description, hidden);
    }

    public RectSetting(Rect value, String name, String description, boolean hidden, String tab) {
        super(value, name, description, hidden, tab);
    }
}
