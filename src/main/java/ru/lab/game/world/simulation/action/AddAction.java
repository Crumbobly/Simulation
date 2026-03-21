package ru.lab.game.world.simulation.action;

import ru.algo.spatial.dto.PositionComponent;
import ru.lab.game.entity.Entity;
import ru.lab.game.world.WorldMap;


public record AddAction(Entity entity, PositionComponent pos) implements WorldAction {
    @Override
    public void apply(WorldMap worldMap) {
        worldMap.add(entity, pos);
    }
}
