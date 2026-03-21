package ru.lab.app.state;

import ru.lab.app.visual.Camera;
import ru.lab.config.Config;
import ru.lab.game.GameContext;

import java.util.ArrayList;
import java.util.List;

public class AppState {

    private InteractionMode currentInteractionMode = InteractionMode.MOVE;
    private LODType currentLodType = LODType.PIXELS;
    private HeatMapType currentHeatMapType = HeatMapType.DISABLED;
    private SpawnType spawnType;

    private final List<Runnable> changeListeners = new ArrayList<>();
    // todo убрать камеру?
    private final Camera camera;
    private final GameContext gameContext;

    public AppState() {
        this.gameContext = GameContext.createDefault();
        this.camera = new Camera(Config.MAP_WIDTH, Config.MAP_HEIGHT);
    }

    public void addChangeListener(Runnable listener) {
        changeListeners.add(listener);
    }

    private void notifyChanged() {
        for (Runnable listener : changeListeners) {
            listener.run();
        }
    }

    public Camera getCamera() {
        return camera;
    }

    public GameContext getGameContext() {
        return gameContext;
    }

    public InteractionMode getCurrentInteractionMode() {
        return currentInteractionMode;
    }

    public void setCurrentInteractionMode(InteractionMode currentInteractionMode) {
        if (this.currentInteractionMode != currentInteractionMode) {
            this.currentInteractionMode = currentInteractionMode;
            notifyChanged();
        }
    }

    public LODType getCurrentLodType() {
        return currentLodType;
    }

    public void setCurrentLodType(LODType currentLodType) {
        if (this.currentLodType != currentLodType) {
            this.currentLodType = currentLodType;
            notifyChanged();
        }
    }

    public HeatMapType getCurrentHeatMapType() {
        return currentHeatMapType;
    }

    public void setCurrentHeatMapType(HeatMapType currentHeatMapType) {
        if (this.currentHeatMapType != currentHeatMapType) {
            this.currentHeatMapType = currentHeatMapType;
            notifyChanged();
        }
    }

    public SpawnType getSpawnType() {
        return spawnType;
    }

    public void setSpawnType(SpawnType spawnType) {
        this.spawnType = spawnType;
    }
}
