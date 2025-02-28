package com.kevinvg.umalauncherj.settings.types;

import com.kevinvg.umalauncherj.settings.Setting;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@Data
public class ListSetting<T extends Collection<?>> extends Setting<T> {

    public ListSetting(T value, String name, String description) {
        super(value, name, description);
    }

    public ListSetting(T value, String name, String description, boolean hidden) {
        super(value, name, description, hidden);
    }

    public ListSetting(T value, String name, String description, boolean hidden, String tab) {
        super(value, name, description, hidden, tab);
    }
}
