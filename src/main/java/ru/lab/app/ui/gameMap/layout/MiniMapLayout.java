package ru.lab.app.ui.gameMap.layout;

import ru.lab.config.Config;
import ru.lab.app.state.AppState;
import ru.lab.app.visual.Camera;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

// TODO("Перерисовка автоматически, попробовать через обсервер")
public class MiniMapLayout extends JPanel {

    private final AppState appState;

    public MiniMapLayout(AppState appState, double div) {
        this.appState = appState;
        setPreferredSize(
                new Dimension(
                        (int) (Config.WORLD_WIDTH / div),
                        (int) (Config.WORLD_HEIGHT / div)
                )
        );

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        drawMiniMap(g2d);
        drawCameraView(g2d);
    }

    private void drawMiniMap(Graphics2D g2d) {
        g2d.setColor(new Color(55, 211, 38, 180));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setColor(new Color(0, 0, 0));
        g2d.drawRect(0, 0, getWidth()-1, getHeight()-1);
    }

    private void drawCameraView(Graphics2D g2d) {

        Camera camera = appState.getCamera();

        // масштаб миникарты относительно мира
        double scaleX = getWidth() / (double) Config.WORLD_WIDTH;
        double scaleY = getHeight() / (double) Config.WORLD_HEIGHT;

        // позиция камеры в мире
        double offsetX = camera.getOffsetX();
        double offsetY = camera.getOffsetY();

        // размер видимой области в мире
        double visibleWidth = Config.MAP_WIDTH / camera.getZoom();
        double visibleHeight = Config.MAP_HEIGHT / camera.getZoom();

        // перевод в координаты миникарты
        int miniX = (int) (offsetX * scaleX);
        int miniY = (int) (offsetY * scaleY);
        int miniW = (int) (visibleWidth * scaleX);
        int miniH = (int) (visibleHeight * scaleY);

        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRect(miniX, miniY, miniW, miniH);
    }

}
