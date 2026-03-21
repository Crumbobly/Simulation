package ru.lab.app.ui.gameMap.layout;

import ru.lab.app.visual.Camera;
import ru.lab.config.Config;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class DebugLayout extends JPanel {

    private int mouseX, mouseY;
    private final Camera camera;

    public DebugLayout(Camera camera) {
        this.camera = camera;

        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0));
        setAlignmentX(0f);
        setAlignmentY(0f);
    }

    public void setMousePosition(int x, int y) {
        this.mouseX = x;
        this.mouseY = y;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2d = (Graphics2D) g;

        render(
                g2d,
                camera.isZoomedIn(),
                (int) camera.screenToWorldX(Config.MAP_WIDTH / 2),
                (int) camera.screenToWorldY(Config.MAP_HEIGHT / 2),
                (int) camera.screenToWorldX(mouseX),
                (int) camera.screenToWorldY(mouseY)
        );

    }

    public void render(Graphics2D g2d,
                       boolean isZoomedIn,
                       int cameraCenterX,
                       int cameraCenterY,
                       int mouseX,
                       int mouseY
    ) {
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString("Zoom: " + (isZoomedIn ? "IN (приближено)" : "OUT (вся карта)"), 10, 25);
        g2d.drawString("WASD: Движение | ЛКМ: Перетаскивание | SPACE: Zoom", 10, 45);
        g2d.drawString("Центр камеры: (" + cameraCenterX + ", " + cameraCenterY + ")", 10, 65);
        g2d.drawString("Мышь: (" + mouseX + ", " + mouseY + ")", 10, 85);
        g2d.setColor(Color.RED);
    }

}
