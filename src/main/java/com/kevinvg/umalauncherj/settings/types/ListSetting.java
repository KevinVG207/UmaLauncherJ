package com.kevinvg.umalauncherj.settings.types;

import com.fasterxml.jackson.core.type.TypeReference;
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
    public ListSetting(List<?> value, TypeReference<List> typeReference, String name, String description) {
        super(value, typeReference, name, description);
    }

    public ListSetting(List<?> value, TypeReference<List> typeReference, String name, String description, boolean hidden) {
        super(value, typeReference, name, description, hidden);
    }

    public ListSetting(List<?> value, TypeReference<List> typeReference, String name, String description, boolean hidden, String tab) {
        super(value, typeReference, name, description, hidden, tab);
    }
}
