package ru.lab.app.visual.models;

import ru.lab.app.visual.Camera;
import ru.lab.config.Config;

public class DebugModel {

    private final boolean isZoomedIn;
    private final int cameraCenterX;
    private final int cameraCenterY;
    private final int mouseX;
    private final int mouseY;

    private DebugModel(boolean isZoomedIn,  int cameraCenterX, int cameraCenterY, int mouseX, int mouseY) {
        this.isZoomedIn = isZoomedIn;
        this.cameraCenterX = cameraCenterX;
        this.cameraCenterY = cameraCenterY;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    public static DebugModel build(Camera camera, int mouseX, int mouseY) {
        return new DebugModel(
                camera.isZoomedIn(),
                (int) camera.screenToWorldX(Config.MAP_WIDTH / 2),
                (int) camera.screenToWorldY(Config.MAP_HEIGHT / 2),
                (int) camera.screenToWorldX(mouseX),
                (int) camera.screenToWorldY(mouseY)
        );
    }

    public boolean isZoomedIn() {
        return isZoomedIn;
    }

    public int getMouseY() {
        return mouseY;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getCameraCenterY() {
        return cameraCenterY;
    }

    public int getCameraCenterX() {
        return cameraCenterX;
    }
}
