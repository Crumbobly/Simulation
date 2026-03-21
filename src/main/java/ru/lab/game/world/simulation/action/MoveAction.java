package ru.lab.game.world.simulation.action;


import ru.algo.spatial.dto.PositionComponent;
import ru.lab.game.entity.Entity;
import ru.lab.game.world.WorldMap;

public record MoveAction(Entity entity, PositionComponent newPos) implements WorldAction {
    @Override
    public void apply(WorldMap worldMap) {
        worldMap.updatePosition(entity, newPos);
    }
}
