package ru.lab.app.visual;

import ru.lab.config.Config;

public class Camera {

    private double zoom ;
    private double offsetX;
    private double offsetY;

    private final int screenWidth;
    private final int screenHeight;

    public Camera(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.zoom = Config.MIN_ZOOM;
        centerOnWorld();
    }

    public void centerOnWorld() {
        offsetX = (Config.WORLD_WIDTH - screenWidth / zoom) / 2;
        offsetY = (Config.WORLD_HEIGHT - screenHeight / zoom) / 2;
    }

    public void move(double dx, double dy) {
        offsetX += dx / zoom;
        offsetY += dy / zoom;
        clampToBounds();
    }

    public void zoomIn() {
        if (zoom < Config.MAX_ZOOM) {
            changeZoom(Config.ZOOM_STEP);
        }
    }

    public void zoomOut() {
        if (zoom > Config.MIN_ZOOM) {
            changeZoom(-Config.ZOOM_STEP);
        }
    }

    private void changeZoom(double dZoom) {

        double worldXBefore = screenToWorldX(screenWidth / 2);
        double worldYBefore = screenToWorldY(screenHeight / 2);

        zoom += dZoom;

        offsetX = worldXBefore - ((double) screenWidth / 2) / zoom;
        offsetY = worldYBefore - ((double) screenHeight / 2) / zoom;

        clampToBounds();
    }

    private void clampToBounds() {
        offsetX = Math.max(0, Math.min(offsetX, Config.WORLD_WIDTH - screenWidth / zoom));
        offsetY = Math.max(0, Math.min(offsetY, Config.WORLD_HEIGHT - screenHeight / zoom));
    }

    public int worldToScreenX(double worldX) {
        return (int) ((worldX - offsetX) * zoom);
    }

    public int worldToScreenY(double worldY) {
        return (int) ((worldY - offsetY) * zoom);
    }

    public double screenToWorldX(int screenX) {
        return screenX / zoom + offsetX;
    }

    public double screenToWorldY(int screenY) {
        return screenY / zoom + offsetY;
    }

    public double getZoom() { return zoom; }
    public double getOffsetX() { return offsetX; }
    public double getOffsetY() { return offsetY; }
    public boolean isZoomedIn() { return zoom > Config.MIN_ZOOM; }
}
