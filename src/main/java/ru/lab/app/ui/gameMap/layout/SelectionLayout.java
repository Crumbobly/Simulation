package ru.lab.app.ui.gameMap.layout;

import ru.lab.app.state.AppState;
import ru.lab.app.state.InteractionMode;
import ru.lab.app.visual.Camera;
import ru.lab.config.Config;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.function.Supplier;

public class SelectionLayout extends JPanel{


    private final AppState appState;
    private final Supplier<Integer> mouseXProvider;
    private final Supplier<Integer> mouseYProvider;

    public SelectionLayout(AppState appState,
                           Supplier<Integer> mouseXProvider,
                           Supplier<Integer> mouseYProvider) {
        this.appState = appState;
        this.mouseXProvider = mouseXProvider;
        this.mouseYProvider = mouseYProvider;

        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0));
        setAlignmentX(0f);
        setAlignmentY(0f);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2d = (Graphics2D) g;
        render(g2d, appState.getCamera());
    }

    public void render(Graphics2D g2d, Camera camera) {
//        System.out.println("selected render");

        drawSelected(g2d, camera, mouseXProvider.get(), mouseYProvider.get(), appState.getCurrentInteractionMode());
        drawSight(g2d, camera);
    }

    private void drawSight(Graphics2D g2d, Camera camera) {
        final int midX = Config.MAP_WIDTH / 2;
        final int midY = Config.MAP_HEIGHT / 2;
        g2d.setColor(Color.RED);
        g2d.drawOval(midX, midY, 1, 1);
    }

    private void drawSelected(Graphics2D g2d, Camera camera, int mouseX, int mouseY, InteractionMode interactionMode) {

        if (interactionMode == InteractionMode.MOVE) {
            return;
        }

        final int worldX = (int) camera.screenToWorldX(mouseX);
        final int worldY = (int) camera.screenToWorldY(mouseY);

        final int screenX = camera.worldToScreenX(worldX);
        final int screenY = camera.worldToScreenY(worldY);
        final int screenX2 = camera.worldToScreenX(worldX + 1);
        final int screenY2 = camera.worldToScreenY(worldY + 1);

        g2d.setColor(Color.RED);
        g2d.drawRect(screenX, screenY, screenX2 - screenX, screenY2 - screenY);

    }

}
