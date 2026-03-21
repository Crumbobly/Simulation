package ru.lab.app.ui.toolBox;

import ru.lab.app.state.AppState;
import ru.lab.app.state.HeatMapType;
import ru.lab.app.state.LODType;
import ru.lab.app.state.InteractionMode;
import ru.lab.app.state.SpawnType;
import ru.lab.app.ui.gameMap.GameMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static ru.lab.config.Config.TOOLBOX_WIDTH;
import static ru.lab.config.Config.MAP_HEIGHT;

public class ToolBox extends JPanel {

    private static final Color PRIMARY = new Color(72, 179, 52);
    private static final Color BG = new Color(45, 45, 45);
    private static final Color BG_LIGHT = new Color(70, 70, 70);
    private static final Color TEXT = Color.WHITE;

    private final AppState appState;
    private final GameMap gameMap;
    private final ExecutorService worker = Executors.newSingleThreadExecutor();

    private JToggleButton panBtn, selectBtn, startBtn;

    public ToolBox(AppState appState, GameMap gameMap) {
        this.appState = appState;
        this.gameMap = gameMap;
        setup();
        bind();
    }

    private void setup() {

        setPreferredSize(new Dimension(TOOLBOX_WIDTH, MAP_HEIGHT));
        setBackground(BG);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, new Color(100,100,100)));

        JLabel title = new JLabel("Инструменты");
        title.setForeground(TEXT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createVerticalStrut(10));
        add(title);
        add(Box.createVerticalStrut(15));

        add(label("Режим:"));
        JPanel modes = new JPanel(new GridLayout(1, 2, 5, 0));
        modes.setBackground(BG);
        modes.setMaximumSize(new Dimension(TOOLBOX_WIDTH - 20, 35));
        panBtn = btn("Перемещение", PRIMARY);
        selectBtn = btn("Выделение", BG_LIGHT);

        modes.add(panBtn);
        modes.add(selectBtn);
        add(modes);
        add(Box.createVerticalStrut(15));

        add(label("Heatmap:"));
        add(combo(HeatMapType.values(), appState::getCurrentHeatMapType, appState::setCurrentHeatMapType));
        add(Box.createVerticalStrut(8));

        add(label("LOD:"));
        add(combo(LODType.values(), appState::getCurrentLodType, appState::setCurrentLodType));
        add(Box.createVerticalStrut(20));

        add(label("Добавить:"));
        JComboBox<SpawnType> spawnCombo = new JComboBox<>(SpawnType.values());
        spawnCombo.setSelectedItem(appState.getSpawnType());
        spawnCombo.setBackground(BG_LIGHT);
        spawnCombo.setForeground(TEXT);
        spawnCombo.setMaximumSize(new Dimension(TOOLBOX_WIDTH - 20, 30));
        spawnCombo.setAlignmentX(Component.CENTER_ALIGNMENT);
        spawnCombo.addActionListener(e ->
                appState.setSpawnType((SpawnType) spawnCombo.getSelectedItem())
        );
        add(spawnCombo);
        add(Box.createVerticalStrut(15));

        startBtn = btn("Запуск", PRIMARY);
        startBtn.setMaximumSize(new Dimension(TOOLBOX_WIDTH - 20, 40));
        startBtn.addActionListener(e -> toggleSim());
        add(startBtn);
        add(Box.createVerticalStrut(10));
    }

    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(new Color(200,200,200));
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        return l;
    }

    private JToggleButton btn(String text, Color activeColor) {
        JToggleButton b = new JToggleButton(text);
        b.setForeground(TEXT);
        b.setBackground(BG_LIGHT);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setMaximumSize(new Dimension((TOOLBOX_WIDTH - 30) / 2, 35));

        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (!b.isSelected()) b.setBackground(BG_LIGHT.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (!b.isSelected()) b.setBackground(BG_LIGHT);
            }
        });

        b.addItemListener(e ->
                b.setBackground(b.isSelected() ? activeColor : BG_LIGHT)
        );
        return b;
    }

    private <T> JComboBox<T> combo(T[] values, java.util.function.Supplier<T> get, java.util.function.Consumer<T> set) {
        JComboBox<T> c = new JComboBox<>(values);
        c.setSelectedItem(get.get());
        c.setBackground(BG_LIGHT);
        c.setForeground(TEXT);
        c.setMaximumSize(new Dimension(TOOLBOX_WIDTH - 20, 30));
        c.setAlignmentX(Component.CENTER_ALIGNMENT);
        c.addActionListener(e -> set.accept((T) c.getSelectedItem()));
        return c;
    }

    private void toggleSim() {
        appState.getGameContext().changeRunning();
        startBtn.setText(appState.getGameContext().isRunning() ? "Стоп" : "Запуск");

        if (appState.getGameContext().isRunning()) {
            worker.submit(() -> {
                while (appState.getGameContext().isRunning()) {
                    appState.getGameContext().getSimulation().nextTurn();
                    SwingUtilities.invokeLater(gameMap::repaint);
                    try { Thread.sleep(1000); } catch (InterruptedException ex) { break; }
                }
            });
        }
    }

    private void bind() {
        panBtn.addActionListener(e -> {
            appState.setCurrentInteractionMode(InteractionMode.MOVE);
            updateModeButtons();
        });
        selectBtn.addActionListener(e -> {
            appState.setCurrentInteractionMode(InteractionMode.SELECT);
            updateModeButtons();
        });

        appState.addChangeListener(this::updateModeButtons);
        updateModeButtons();
    }

    private void updateModeButtons() {
        boolean isPan = appState.getCurrentInteractionMode() == InteractionMode.MOVE;
        panBtn.setSelected(isPan);
        selectBtn.setSelected(!isPan);
        panBtn.setBackground(isPan ? PRIMARY : BG_LIGHT);
        selectBtn.setBackground(!isPan ? PRIMARY : BG_LIGHT);
    }
}
