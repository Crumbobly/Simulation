package ru.lab.app.ui.gameMap.layout;

import ru.algo.spatial.dto.PositionComponent;
import ru.lab.app.state.AppState;
import ru.lab.app.state.LODType;
import ru.lab.app.visual.Camera;
import ru.lab.app.visual.VisualCache;
import ru.lab.config.Config;
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

public class EntitiesLayout extends JPanel {

    private final AppState appState;

    public EntitiesLayout(AppState appState) {
        this.appState = appState;

        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0));
        setAlignmentX(0f);
        setAlignmentY(0f);
    }

    @Override
    public void paintComponent(Graphics g) {
//        System.out.println("Entities layout paintComponent");
        super.paintComponent(g);
        final Graphics2D g2d = (Graphics2D) g;

        final Camera camera = appState.getCamera();
        final WorldMap worldMap = appState.getGameContext().getSimulation().getWorldMap();
        final LODType lodType = appState.getCurrentLodType();
        //
        final int viewMinX = (int) camera.screenToWorldX(0);
        final int viewMinY = (int) camera.screenToWorldY(0);
        final int viewMaxX = (int) camera.screenToWorldX(Config.MAP_WIDTH);
        final int viewMaxY = (int) camera.screenToWorldY(Config.MAP_HEIGHT);

        final PositionComponent topLeft = new PositionComponent(viewMinX, viewMinY);
        final PositionComponent bottomRight = new PositionComponent(viewMaxX, viewMaxY);
        final List<Entity> visibleEntities = worldMap.getInRect(
                topLeft,
                bottomRight
        );

        final Map<PositionComponent, List<Entity>> entities = new HashMap<>();
        for (Entity entity : visibleEntities) {
            PositionComponent p = worldMap.getPosition(entity);
            entities.computeIfAbsent(p, k -> new ArrayList<>()).add(entity);
        }
        //

        render(g2d, camera, entities, lodType);
    }

    private void render(Graphics2D g2d, Camera camera, Map<PositionComponent, List<Entity>> entities, LODType lodType) {
        double zoom = camera.getZoom();
        int size = (int) Math.max(1, zoom);

        for (var entry : entities.entrySet()) {
            PositionComponent pos = entry.getKey();
            List<Entity> entitiesToRender = entry.getValue();

            int screenX = camera.worldToScreenX(pos.x());
            int screenY = camera.worldToScreenY(pos.y());

            renderEntity(g2d, entitiesToRender, screenX, screenY, size, lodType);
        }
    }

    private void renderEntity(Graphics2D g2d, List<Entity> entity, int x, int y, int size, LODType lodType) {
        Set<Class<?>> types = entity.stream().map(Entity::getClass).collect(Collectors.toSet());
        switch (lodType) {
            case ICONS -> {
                BufferedImage icon = VisualCache.getIcon(types);
                g2d.drawImage(icon, x, y, size, size, null);
            }
            case PIXELS -> {
                g2d.setColor(VisualCache.getColor(types));
                g2d.fillRect(x, y, size, size);
            }
            case DISABLED -> {
                break;
            }
        }
    }
}
