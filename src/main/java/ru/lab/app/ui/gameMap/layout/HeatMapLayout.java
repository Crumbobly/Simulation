package ru.lab.app.ui.gameMap.layout;

import ru.lab.app.state.AppState;
import ru.lab.app.state.HeatMapType;
import ru.lab.app.state.StateType;
import ru.lab.app.visual.Camera;
import ru.lab.app.visual.models.HeatMapModel;
import ru.lab.config.Config;
import ru.lab.game.GameContext;
import ru.lab.game.world.CellDensity;
import ru.lab.game.world.WorldMap;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;

public class HeatMapLayout extends MapLayout {

    private final AppState appState;
    private final GameContext gameContext;

    public HeatMapLayout(AppState appState, GameContext gameContext) {
        this.appState = appState;
        this.gameContext = gameContext;

        appState.addChangeListener(change -> {
            if (change == StateType.HEATMAP) {
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2d = (Graphics2D) g;

        if (appState.getCurrentHeatMapType() == HeatMapType.DISABLED) {
            return;
        }

        final HeatMapModel model = HeatMapModel.build(appState, gameContext);
        for (HeatMapModel.CellModel cell: model.getCells()) {
            g2d.setColor(cell.color());
            g2d.fillRect(cell.x(), cell.y(), cell.width(), cell.height());
        }

    }


}
