package ru.lab.game;

import ru.lab.config.Config;
import ru.lab.game.world.WorldMapBuilder;
import ru.lab.game.world.simulation.Simulation;
import ru.lab.game.world.WorldMap;
import ru.lab.app.visual.Camera;

public class GameContext {

    private final WorldMap worldMap;
    private final Simulation simulation;

    public GameContext() {
        this.worldMap = WorldMapBuilder.build();
        this.simulation = new Simulation(worldMap);
    }

    public WorldMap getWorldMap() {
        return worldMap;
    }

    public Simulation getSimulation() {
        return simulation;
    }

}
