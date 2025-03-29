package com.kevinvg.umalauncherj.ui.frames;

import com.kevinvg.umalauncherj.l18n.Localizer;
import com.kevinvg.umalauncherj.mdb.MdbService;
import com.kevinvg.umalauncherj.settings.Setting;
import com.kevinvg.umalauncherj.settings.Settings;
import jakarta.enterprise.inject.spi.CDI;

import javax.swing.*;
import java.awt.*;

public class SettingsPanel<T extends Enum<?>> extends JPanel {
    private final Settings<T> settings;
    private static final Localizer loc = CDI.current().select(Localizer.class).get();

    public SettingsPanel(Settings<T> settings) {
        super(new GridLayout(1, 1));

        this.settings = settings;

        setPreferredSize(new Dimension(500, 400));

        var verticalPanel = new JPanel();
        var scrollPane = new JScrollPane(verticalPanel);

        verticalPanel.setLayout(new BoxLayout(verticalPanel, BoxLayout.Y_AXIS));

        makeGroupBoxes(verticalPanel);

        verticalPanel.add(Box.createVerticalGlue());

        add(scrollPane);
    }

    private void makeGroupBoxes(JPanel verticalPanel) {
        for (var entry : settings.getSettings().entrySet()) {
            if (entry.getValue().isHidden()) {
                continue;
            }

            verticalPanel.add(new GroupBox(entry.getKey(), entry.getValue()));
        }
    }

    class GroupBox extends JPanel {
        private T key;
        private Setting<?> setting;
        private JComponent component;

        public GroupBox(T key, Setting<?> setting) {
            this.key = key;
            this.setting = setting;

            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            setBorder(BorderFactory.createTitledBorder(loc.get(settings.getLocName(key))));

            var descLabel = new JLabel("<html>" + loc.get(settings.getLocDesc(key)) + "</html>");
            descLabel.setHorizontalAlignment(SwingConstants.LEFT);
            descLabel.setMaximumSize(new Dimension(200, 999));
            add(descLabel);

            add(Box.createRigidArea(new Dimension(5, 0)));

            this.component = setting.makeUiComponent();
            add(component);
        }

        public void applyValue() {
            setting.applyUiComponent(this.component);
        }
    }
}
