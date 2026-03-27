package ru.lab.app.state;

import ru.lab.app.visual.Camera;
import ru.lab.config.Config;
import ru.lab.game.GameContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AppState {

    private final Camera camera = new Camera(Config.MAP_WIDTH, Config.MAP_HEIGHT);
    private InteractionMode currentInteractionMode = InteractionMode.MOVE;
    private LODType currentLodType = LODType.PIXELS;
    private HeatMapType currentHeatMapType = HeatMapType.DISABLED;
    private SpawnType spawnType;
    private boolean simActive = false;

    private final List<Consumer<StateType>> listeners = new ArrayList<>();

    public void addChangeListener(Consumer<StateType> listener) {
        listeners.add(listener);
    }

    private void notifyChanged(StateType change) {
        for (var l : listeners) {
            l.accept(change);
        }
    }

    public InteractionMode getCurrentInteractionMode() {
        return currentInteractionMode;
    }

    public void setCurrentInteractionMode(InteractionMode currentInteractionMode) {
        if (this.currentInteractionMode != currentInteractionMode) {
            this.currentInteractionMode = currentInteractionMode;
            notifyChanged(StateType.INTERACTION);
        }
    }

    public LODType getCurrentLodType() {
        return currentLodType;
    }

    public void setCurrentLodType(LODType currentLodType) {
        if (this.currentLodType != currentLodType) {
            this.currentLodType = currentLodType;
            notifyChanged(StateType.LOD);
        }
    }

    public HeatMapType getCurrentHeatMapType() {
        return currentHeatMapType;
    }

    public void setCurrentHeatMapType(HeatMapType currentHeatMapType) {
        if (this.currentHeatMapType != currentHeatMapType) {
            this.currentHeatMapType = currentHeatMapType;
            notifyChanged(StateType.HEATMAP);
        }
    }

    public SpawnType getSpawnType() {
        return spawnType;
    }

    public void setSpawnType(SpawnType spawnType) {
        if (this.spawnType != spawnType) {
            this.spawnType = spawnType;
            notifyChanged(StateType.SPAWN);
        }
    }

    public boolean isSimActive() {
        return simActive;
    }

    public void setSimActive(boolean simActive) {
        if (this.simActive != simActive) {
            this.simActive = simActive;
            notifyChanged(StateType.SIM);
        }
    }

    public Camera getCamera() {
        return camera;
    }
}
