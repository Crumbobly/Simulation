package ru.lab.config;

public class Config {

    public static final int SPATIAL_CELL_SIZE = 25;

    public static final int WORLD_WIDTH = 800;
    public static final int WORLD_HEIGHT = 600;

    public static final int MAP_WIDTH = 1200;
    public static final int MAP_HEIGHT = 900;

    public static final int TOOLBOX_WIDTH = 500;

    public static final double MIN_ZOOM = Math.max(
            (double) MAP_HEIGHT/WORLD_HEIGHT,
            (double) MAP_WIDTH/WORLD_WIDTH
    );

    public static final double MAX_ZOOM = 50.0;
    public static final double ZOOM_STEP = 0.5;

    public static final int SIMULATION_DELAY_MS = 100;

}
