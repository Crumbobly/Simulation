package ru.lab.app.ui.toolBox;

import ru.lab.app.state.AppState;
import ru.lab.app.state.HeatMapType;
import ru.lab.app.state.LODType;
import ru.lab.app.state.InteractionMode;
import ru.lab.app.state.SpawnType;
import ru.lab.app.state.StateType;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;

import static ru.lab.config.Config.TOOLBOX_WIDTH;
import static ru.lab.config.Config.MAP_HEIGHT;

public class ToolBox extends JPanel {

    private static final Color PRIMARY = new Color(72, 179, 52);
    private static final Color BG = new Color(45, 45, 45);
    private static final Color BG_LIGHT = new Color(70, 70, 70);
    private static final Color TEXT = Color.WHITE;

    private final AppState appState;

    private ToolBoxButton panBtn, selectBtn;

    public ToolBox(AppState appState) {
        this.appState = appState;

        setPreferredSize(new Dimension(TOOLBOX_WIDTH, MAP_HEIGHT));
        setBackground(BG);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        setup();
        bind();
    }

    private void setup() {

        final JLabel title = new JLabel("Инструменты");
        title.setForeground(TEXT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createVerticalStrut(10));
        add(title);
        add(Box.createVerticalStrut(15));

        add(new ToolBoxLabel("Режим:"));
        final JPanel modes = new JPanel();
        modes.setLayout(new BoxLayout(modes, BoxLayout.X_AXIS));
        modes.setBackground(BG);
        panBtn = new ToolBoxButton("Перемещение", PRIMARY);
        selectBtn = new ToolBoxButton("Выделение", BG_LIGHT);
        modes.add(panBtn);
        modes.add(selectBtn);
        add(modes);
        add(Box.createVerticalStrut(15));

        add(new ToolBoxLabel("Heatmap:"));
        add(new ToolBoxCombo<HeatMapType>(HeatMapType.values(), appState::getCurrentHeatMapType, appState::setCurrentHeatMapType));
        add(Box.createVerticalStrut(8));

        add(new ToolBoxLabel("LOD:"));
        add(new ToolBoxCombo<LODType>(LODType.values(), appState::getCurrentLodType, appState::setCurrentLodType));
        add(Box.createVerticalStrut(20));

        add(new ToolBoxLabel("Добавить:"));
        add(new ToolBoxCombo<SpawnType>(SpawnType.values(), appState::getSpawnType, appState::setSpawnType));
        add(Box.createVerticalStrut(20));

        final ToolBoxButton startBtn = new ToolBoxButton("Запуск", PRIMARY);
        startBtn.addActionListener(e -> appState.setSimActive(!appState.isSimActive()));
        add(startBtn);
        add(Box.createVerticalStrut(10));

        updateModeButtons();
    }

    private void bind() {
        panBtn.addActionListener(e -> {
            appState.setCurrentInteractionMode(InteractionMode.MOVE);
        });
        selectBtn.addActionListener(e -> {
            appState.setCurrentInteractionMode(InteractionMode.SELECT);
        });

        appState.addChangeListener(change -> {
            if (change == StateType.INTERACTION) {
                updateModeButtons();
            }
        });
    }

    private void updateModeButtons() {
        boolean isPan = appState.getCurrentInteractionMode() == InteractionMode.MOVE;
        panBtn.setSelected(isPan);
        selectBtn.setSelected(!isPan);
        panBtn.setBackground(isPan ? PRIMARY : BG_LIGHT);
        selectBtn.setBackground(!isPan ? PRIMARY : BG_LIGHT);
    }
}
