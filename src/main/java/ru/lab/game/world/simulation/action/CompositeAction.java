package ru.lab.game.world.simulation.action;

import ru.lab.game.world.WorldMap;
import java.util.List;

public record CompositeAction(List<WorldAction> actions) implements WorldAction {

    public CompositeAction(WorldAction... actions) {
        this(List.of(actions));
    }

    @Override
    public void apply(WorldMap worldMap) {
        for (WorldAction action : actions) {
            action.apply(worldMap);
        }
    }
}
