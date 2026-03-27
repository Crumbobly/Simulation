package ru.lab.app.ui.gameMap.layout;

import ru.algo.spatial.dto.PositionComponent;
import ru.lab.app.state.AppState;
import ru.lab.app.state.LODType;
import ru.lab.app.state.StateType;
import ru.lab.app.visual.Camera;
import ru.lab.app.visual.VisualCache;
import ru.lab.app.visual.models.EntitiesModel;
import ru.lab.app.visual.models.HeatMapModel;
import ru.lab.config.Config;
import ru.lab.game.GameContext;
import ru.lab.game.entity.Entity;
import ru.lab.game.world.WorldMap;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class EntitiesLayout extends MapLayout {

    private final AppState appState;
    private final GameContext gameContext;

    public EntitiesLayout(AppState appState, GameContext gameContext) {
        this.appState = appState;
        this.gameContext = gameContext;

        appState.addChangeListener(change -> {
            if (change == StateType.LOD) {
                repaint();
            }
        });
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2d = (Graphics2D) g;

        final EntitiesModel model = EntitiesModel.build(appState, gameContext);
        for (EntitiesModel.EntityModel entityModel: model.getEntities()) {
            renderEntity(g2d, entityModel);
        }

    }

    private void renderEntity(Graphics2D g2d, EntitiesModel.EntityModel model) {

        final LODType lodType = appState.getCurrentLodType();

        switch (lodType) {
            case ICONS -> {
                g2d.drawImage(model.icon(), model.x(), model.y(), model.width(), model.height(), null);
            }
            case PIXELS -> {
                g2d.setColor(model.color());
                g2d.fillRect(model.x(), model.y(), model.width(), model.height());
            }
            case DISABLED -> {
                break;
            }
        }
    }
}
