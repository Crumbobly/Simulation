package ru.lab.game.world.simulation.action;

import ru.lab.game.entity.Entity;
import ru.lab.game.world.WorldMap;

public record RemoveAction(Entity entity) implements WorldAction {
    @Override
    public void apply(WorldMap worldMap) {
        worldMap.remove(entity);
    }
}
