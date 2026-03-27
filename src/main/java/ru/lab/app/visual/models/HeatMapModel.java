package ru.lab.app.visual.models;

import ru.lab.app.state.AppState;
import ru.lab.app.state.HeatMapType;
import ru.lab.app.visual.Camera;
import ru.lab.config.Config;
import ru.lab.game.GameContext;
import ru.lab.game.world.CellDensity;
import ru.lab.game.world.WorldMap;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class HeatMapModel {

    // todo pooling + alloc

    public record CellModel(Color color, int x, int y, int width, int height) { }

    private final List<CellModel> cells;

    private HeatMapModel(List<CellModel> cells) {
        this.cells = cells;
    }

    public List<CellModel> getCells() {
        return cells;
    }

    public static HeatMapModel build(AppState appState, GameContext gameContext) {

        final List<CellModel> cells = new ArrayList<>();

        final WorldMap worldMap = gameContext.getWorldMap();
        final HeatMapType heatMapType = appState.getCurrentHeatMapType();
        final Camera camera = appState.getCamera();

        final int viewMinX = (int) camera.screenToWorldX(0);
        final int viewMinY = (int) camera.screenToWorldY(0);
        final int viewMaxX = (int) camera.screenToWorldX(Config.MAP_WIDTH);
        final int viewMaxY = (int) camera.screenToWorldY(Config.MAP_HEIGHT);
        final int totalEntityCount = worldMap.getEntityCount();

        final List<CellDensity> densities = worldMap.getCellDensities(
                viewMinX, viewMinY, viewMaxX, viewMaxY, heatMapType == HeatMapType.HEAT_ALIVE
        );

        int cellOffsetX = (int) camera.getOffsetX() % Config.SPATIAL_CELL_SIZE;
        int cellOffsetY = (int) camera.getOffsetY() % Config.SPATIAL_CELL_SIZE;

        for (CellDensity density : densities) {
            int cellLT = density.worldX() - cellOffsetX;
            int cellRT = density.worldY() - cellOffsetY;

            int screenX1 = camera.worldToScreenX(cellLT) ;
            int screenX2 = camera.worldToScreenX(cellLT + Config.SPATIAL_CELL_SIZE);
            int screenY1 = camera.worldToScreenY(cellRT) ;
            int screenY2 = camera.worldToScreenY(cellRT + Config.SPATIAL_CELL_SIZE);
            int count = density.count();

            cells.add(
                    new CellModel(
                            heatColor(totalEntityCount, count),
                            screenX1,
                            screenY1,
                            screenX2 - screenX1,
                            screenY2 - screenY1
                    )
            );
        }

        return new HeatMapModel(cells);

    }

    private static Color heatColor(int totalEntityCount, int countCell) {
        float ratioNorm = getCellEntityRatioNorm(totalEntityCount, countCell);
        int rgb = Color.HSBtoRGB((1f - ratioNorm) * 2f/3f, 1f, 1f);
        return new Color((rgb & 0xFFFFFF) | 0x99000000, true);
    }

    private static float getCellEntityRatioNorm(int totalEntityCount, int countCell) {

        final float maxRatio = 5.0F;
        final int totalCells = (Config.WORLD_WIDTH / Config.SPATIAL_CELL_SIZE) *
                (Config.WORLD_HEIGHT / Config.SPATIAL_CELL_SIZE);

        final float expected =  (float) totalEntityCount / totalCells;
        // > 1 если в ячейке больше мат. ожидания
        // < 1 если меньше
        float ratio = countCell / expected;

        ratio = Math.min(maxRatio, ratio);
        return ratio / maxRatio;
    }

}
