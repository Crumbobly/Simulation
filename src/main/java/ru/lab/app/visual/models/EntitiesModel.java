package ru.lab.app.visual.models;

import ru.algo.spatial.dto.PositionComponent;
import ru.lab.app.state.AppState;
import ru.lab.app.state.LODType;
import ru.lab.app.visual.Camera;
import ru.lab.app.visual.VisualCache;
import ru.lab.config.Config;
import ru.lab.game.GameContext;
import ru.lab.game.entity.Entity;
import ru.lab.game.world.WorldMap;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class EntitiesModel {

    public record EntityModel(Color color, BufferedImage icon, int x, int y, int width, int height) { }

    private final List<EntitiesModel.EntityModel> entities;

    private EntitiesModel(List<EntityModel> entities) {
        this.entities = entities;
    }

    public List<EntityModel> getEntities() {
        return entities;
    }

    public static EntitiesModel build(AppState appState, GameContext gameContext){

        List<EntitiesModel.EntityModel> models = new ArrayList<>();

        final Camera camera = appState.getCamera();
        final WorldMap worldMap = gameContext.getWorldMap();

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

            if (p != null) {
                entities.computeIfAbsent(p, k -> new ArrayList<>()).add(entity);
            }
        }

        double zoom = camera.getZoom();
        int size = (int) Math.max(1, zoom);

        for (var entry : entities.entrySet()) {
            PositionComponent pos = entry.getKey(); // null
            List<Entity> entitiesToRender = entry.getValue();

            int x = camera.worldToScreenX(pos.x());
            int y = camera.worldToScreenY(pos.y());

            Set<Class<?>> types = entitiesToRender.stream().map(Entity::getClass).collect(Collectors.toSet());
            models.add(
                    new EntityModel(
                            VisualCache.getColor(types),
                            VisualCache.getIcon(types),
                            x,
                            y,
                            size,
                            size

                    )
            );

        }

        return new EntitiesModel(models);
    }

}
