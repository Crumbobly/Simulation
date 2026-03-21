package ru.lab.game.controller;

import ru.lab.app.state.AppState;
import ru.lab.game.GameContext;
import ru.lab.game.entity.Entity;
import ru.lab.game.world.WorldMap;
import ru.lab.game.world.simulation.Simulation;

public class GameController {

//    private final GameContext context;
    private final WorldMap worldMap;
    private final Simulation simulation;
    private boolean simulationIsRunning = false;

    public GameController(WorldMap worldMap, Simulation simulation) {
        this.worldMap = worldMap;
        this.simulation = simulation;
    }

//    public void toggleSimulation() {
//        context.changeRunning();
//    }
//
//    public void nextTick() {
//        context.getSimulation().nextTurn();
//    }

    public void removeEntity(Entity entity) {
        worldMap.remove(entity);
    }

//    public void add(Entity entity) {
//        context.getSimulation().spawn(type);
//    }

//    public RenderData getRenderData() {
//        return context.getSimulation().buildRenderData();
//    }

    public boolean isRunning() {
        return simulationIsRunning;
    }
}
