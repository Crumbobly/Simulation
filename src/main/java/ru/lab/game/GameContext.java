package ru.lab.game;

import ru.lab.config.Config;
import ru.lab.game.world.WorldMapBuilder;
import ru.lab.game.world.simulation.Simulation;
import ru.lab.game.world.WorldMap;
import ru.lab.app.visual.Camera;

public class GameContext {

    private final WorldMap worldMap;
    private final Simulation simulation;
    private boolean running = false;

    public GameContext(WorldMap worldMap, Simulation simulation, Camera camera) {
        this.worldMap = worldMap;
        this.simulation = simulation;
    }

    public static GameContext createDefault() {
        WorldMap worldMap = WorldMapBuilder.build();
        Simulation simulation = new Simulation(worldMap);
        Camera camera = new Camera(Config.MAP_WIDTH, Config.MAP_HEIGHT);
        return new GameContext(worldMap, simulation, camera);
    }

    public void changeRunning(){
        running = !running;
    }

    public WorldMap getWorldMap() {
        return worldMap;
    }

    public Simulation getSimulation() {
        return simulation;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
