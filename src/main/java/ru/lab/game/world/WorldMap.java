package ru.lab.game.world;

import ru.algo.spatial.type.ConcurrentSpatialGrid;
import ru.algo.spatial.type.ValidatedSpatialGrid;
import ru.lab.config.Config;
import ru.lab.game.entity.Creature;
import ru.lab.game.entity.Entity;
import ru.lab.game.entity.Grass;
import ru.lab.game.entity.Herbivore;
import ru.lab.game.entity.Predator;
import ru.lab.game.entity.Rock;
import ru.lab.game.entity.StaticEntity;
import ru.lab.game.entity.Tree;
import ru.lab.game.world.collision.CollisionService;
import ru.lab.game.world.simulation.action.WorldAction;
import ru.algo.spatial.dto.PositionComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Predicate;

public class WorldMap {

    private final ValidatedSpatialGrid<Entity> grid;
    private final CollisionService collisionService;

    public WorldMap(ValidatedSpatialGrid<Entity> grid) {
        this.grid = grid;
        this.collisionService = new CollisionService();
    }

    public boolean canPlace(Entity entity, PositionComponent position) {
        List<Entity> existing = grid.getInPosition(position);
        return collisionService.canPlace(entity, existing);
    }

    public void applyActions(List<WorldAction> actions) {
        if (actions == null || actions.isEmpty()) return;

        List<WorldAction> validActions = new ArrayList<>(actions);

        for (WorldAction action : validActions) {
            action.apply(this);
        }

    }

    public List<Entity> getInPosition(PositionComponent position) {
        return grid.getInPosition(position);
    }

    public int getEntityCount() {
        return grid.getEntityCount();
    }

    public Map<Entity, PositionComponent> getEntities() {
        return grid.getEntities();
    }

    public void add(Entity entity, PositionComponent position) {
        if (!canPlace(entity, position)) {
            throw new IllegalArgumentException(
                    "Cannot place %s at %s".formatted(entity.getClass().getSimpleName(), position)
            );
        }
        grid.add(entity, position);
    }

    public void updatePosition(Entity entity, PositionComponent newPosition) {
        if (!canPlace(entity, newPosition)) {
            throw new IllegalArgumentException(
                    "Cannot place %s at %s".formatted(entity.getClass().getSimpleName(), newPosition)
            );
        }
        grid.updatePosition(entity, newPosition);
    }

    public void remove(Entity entity) {
        grid.remove(entity);
    }

    public PositionComponent getPosition(Entity entity) {
        return grid.getPosition(entity);
    }

    public List<Entity> getInRect(PositionComponent topLeft, PositionComponent bottomRight) {
        return grid.getInRect(topLeft, bottomRight);
    }


    public Entity findNearest(Entity entity, Predicate<Entity> predicate) {
        return grid.findNearest(entity, predicate);
    }

    public boolean tryAdd(Entity entity, PositionComponent position) {
        if (canPlace(entity, position)) {
            add(entity, position);
            return true;
        }
        return false;
    }

    public boolean tryRemove(Entity entity) {
        if (grid.contains(entity)) {
            remove(entity);
            return true;
        }
        return false;
    }

    // Количество сущностей в ячейках в определенном "прямоугольнике"
    public List<CellDensity> getCellDensities(
            int minX, int minY, int maxX, int maxY,
            boolean onlyAlive
    ) {
        final int cellSize = grid.getCellSize();
        final List<CellDensity> result = new ArrayList<>();
        final Predicate<Entity> filterPredicate = (entity) -> {
            if (!onlyAlive){
                return true;
            }
            return entity instanceof Creature;
        };

        for (int x = minX; x <= maxX + cellSize; x += cellSize) {
            for (int y = minY; y <= maxY + cellSize; y += cellSize) {

                final PositionComponent cellPos = new PositionComponent(
                        Math.min(x, maxX),
                        Math.min(y, maxY)
                );
                final List<Entity> cellEntities = grid.getInCell(cellPos);
                final int count = (int) cellEntities.stream().filter(filterPredicate).count();

                result.add(new CellDensity(x, y, count));
            }
        }
        return result;
    }

}
