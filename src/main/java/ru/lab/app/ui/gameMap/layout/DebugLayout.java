package ru.lab.app.ui.gameMap.layout;

import ru.lab.app.visual.Camera;
import ru.lab.app.visual.models.DebugModel;
import ru.lab.config.Config;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.function.Supplier;

public class DebugLayout extends MapLayout {

    private final Camera camera;
    private final Supplier<Integer> mouseXProvider;
    private final Supplier<Integer> mouseYProvider;

    public DebugLayout(Camera camera, Supplier<Integer> mouseXProvider, Supplier<Integer> mouseYProvider) {
        this.camera = camera;
        this.mouseXProvider = mouseXProvider;
        this.mouseYProvider = mouseYProvider;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2d = (Graphics2D) g;

        final DebugModel model = DebugModel.build(camera, mouseXProvider.get(), mouseYProvider.get());

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString("Zoom: " + (model.isZoomedIn() ? "IN (приближено)" : "OUT (вся карта)"), 10, 25);
        g2d.drawString("WASD: Движение | ЛКМ: Перетаскивание | SPACE: Zoom", 10, 45);
        g2d.drawString("Центр камеры: (" + model.getCameraCenterX() + ", " + model.getCameraCenterY() + ")", 10, 65);
        g2d.drawString("Мышь: (" + model.getMouseX() + ", " + model.getMouseY() + ")", 10, 85);
        g2d.setColor(Color.RED);
    }

}
