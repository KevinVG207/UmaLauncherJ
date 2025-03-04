package com.kevinvg.umalauncherj.settings.types;

import com.kevinvg.umalauncherj.settings.Setting;
import lombok.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@Data
public class ListSetting extends Setting<List> {
    public ListSetting(List<?> value, String name, String description) {
        super(value, List.class, name, description);
    }

    public ListSetting(List<?> value, String name, String description, boolean hidden) {
        super(value, List.class, name, description, hidden);
    }

    public ListSetting(List<?> value, String name, String description, boolean hidden, String tab) {
        super(value, List.class, name, description, hidden, tab);
    }
}
