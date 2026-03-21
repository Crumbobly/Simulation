package ru.lab.game.world.simulation.action;

import ru.lab.game.world.WorldMap;


public interface WorldAction {
    void apply(WorldMap worldMap);
}
