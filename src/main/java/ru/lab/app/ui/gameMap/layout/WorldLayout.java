package ru.lab.app.ui.gameMap.layout;

import ru.lab.app.visual.Camera;
import ru.lab.config.Config;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class WorldLayout extends JPanel {

    private static final Color BACKGROUND_COLOR = new Color(60, 200, 60);
    private static final Color GRID_COLOR = new Color(0, 0, 0, 100);
    private final Camera camera;

    public WorldLayout(Camera camera) {
        this.camera = camera;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2d = (Graphics2D) g;

        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0));
        setAlignmentX(0f);
        setAlignmentY(0f);

        fillColor(g2d);
        drawGrid(g2d, camera);
    }

    private void fillColor(Graphics2D g2d) {
        g2d.setColor(BACKGROUND_COLOR);
        g2d.fillRect(0, 0, Config.MAP_WIDTH, Config.MAP_HEIGHT);
    }

    private void drawGrid(Graphics2D g2d, Camera camera) {

        g2d.setColor(GRID_COLOR);

        for (int x = 0; x <= Config.MAP_WIDTH; x += Config.SPATIAL_CELL_SIZE) {
            int screenX = camera.worldToScreenX(x);
            g2d.drawLine(screenX, 0, screenX, Config.MAP_HEIGHT);
        }

        for (int y = 0; y < Config.MAP_HEIGHT; y += Config.SPATIAL_CELL_SIZE) {
            int screenY = camera.worldToScreenY(y);
            g2d.drawLine(0, screenY, Config.MAP_WIDTH, screenY);
        }
    }
}
