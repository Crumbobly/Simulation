package ru.lab.app.ui.gameMap;


import ru.algo.spatial.dto.PositionComponent;
import ru.lab.app.state.InteractionMode;
import ru.lab.app.state.SpawnType;
import ru.lab.app.ui.gameMap.layout.DebugLayout;
import ru.lab.app.ui.gameMap.layout.EntitiesLayout;
import ru.lab.app.ui.gameMap.layout.HeatMapLayout;
import ru.lab.app.ui.gameMap.layout.MiniMapLayout;
import ru.lab.app.ui.gameMap.layout.SelectionLayout;
import ru.lab.app.ui.gameMap.layout.WorldLayout;
import ru.lab.app.ui.toolBox.EntitySelectorPanel;
import ru.lab.config.Config;
import ru.lab.app.state.AppState;
import ru.lab.game.entity.Entity;
import ru.lab.game.world.WorldMap;

import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.List;

public class GameMap extends JLayeredPane {

    private final AppState appState;

    private final WorldLayout worldLayout;
    private final EntitiesLayout entitiesLayout;
    private final MiniMapLayout miniMapLayout;
    private final HeatMapLayout heatmapLayout;
    private final DebugLayout debugLayout;
    private final SelectionLayout selectionLayout;
    private EntitySelectorPanel activeSelectorPanel;

    int currentX = 0;
    int currentY = 0;
    private boolean dragging = false;
    private int dragStartMouseX, dragStartMouseY;

    public GameMap(AppState appState) {
        this.appState = appState;
        appState.addChangeListener(this::repaint);

        setLayout(null);
        setPreferredSize(new Dimension(Config.MAP_WIDTH, Config.MAP_HEIGHT));
        setFocusable(true);

        worldLayout = new WorldLayout(appState.getCamera());
        worldLayout.setBounds(0, 0, Config.MAP_WIDTH, Config.MAP_HEIGHT);

        entitiesLayout = new EntitiesLayout(appState);
        entitiesLayout.setBounds(0, 0, Config.MAP_WIDTH, Config.MAP_HEIGHT);

        heatmapLayout = new HeatMapLayout(appState);
        heatmapLayout.setBounds(0, 0, Config.MAP_WIDTH, Config.MAP_HEIGHT);

        selectionLayout = new SelectionLayout(
                appState,
                () -> currentX,
                () -> currentY
        );
        selectionLayout.setBounds(0, 0, Config.MAP_WIDTH, Config.MAP_HEIGHT);

        miniMapLayout = new MiniMapLayout(appState, 10);
        miniMapLayout.setBounds(
                Config.MAP_WIDTH - miniMapLayout.getPreferredSize().width - 10,
                10,
                miniMapLayout.getPreferredSize().width,
                miniMapLayout.getPreferredSize().height
        );

        debugLayout = new DebugLayout(appState.getCamera());
        debugLayout.setBounds(0, 0, Config.MAP_WIDTH, Config.MAP_HEIGHT);

        add(selectionLayout, DEFAULT_LAYER);
        add(entitiesLayout, DEFAULT_LAYER);
        add(heatmapLayout, DEFAULT_LAYER);
        add(worldLayout, DEFAULT_LAYER);
        add(miniMapLayout, PALETTE_LAYER);
        add(debugLayout, POPUP_LAYER);

        setupInputHandlers();
    }
    private void setupInputHandlers() {

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e) &&
                        appState.getCurrentInteractionMode() == InteractionMode.MOVE) {
                    dragging = true;
                    dragStartMouseX = e.getX();
                    dragStartMouseY = e.getY();
                }
                if (appState.getCurrentInteractionMode() == InteractionMode.SELECT) {

                    int worldX = (int) appState.getCamera().screenToWorldX(currentX);
                    int worldY = (int) appState.getCamera().screenToWorldY(currentY);
                    PositionComponent position = new PositionComponent(worldX, worldY);

                    WorldMap world = appState.getGameContext().getSimulation().getWorldMap();
                    SpawnType selectedSpawn = appState.getSpawnType();

                    if (SwingUtilities.isLeftMouseButton(e)){
                        if (selectedSpawn == null) {
                            System.out.println("❌ Сущность для добавления не выбрана");
                            return;
                        }
                        Entity newEntity = selectedSpawn.create();
                        if (world.tryAdd(newEntity, position)) {
                            System.out.println("✅ Добавлено: " + newEntity.getClass().getSimpleName());
                        } else {
                            System.out.println("❌ Нельзя разместить здесь (коллизия)");
                        }
                    }
                    else if(SwingUtilities.isRightMouseButton(e)){
                        showEntitySelector(worldX, worldY);
                    }
                }

                entitiesLayout.repaint();

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                dragging = false;
            }

        });

        addMouseWheelListener(e -> {
            if (e.getWheelRotation() < 0) {
                appState.getCamera().zoomIn();
            } else if (e.getWheelRotation() > 0) {
                appState.getCamera().zoomOut();
            }
            repaint();
        });

        addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {

                if (dragging && appState.getCurrentInteractionMode() == InteractionMode.MOVE) {
                    int dx = e.getX() - dragStartMouseX;
                    int dy = e.getY() - dragStartMouseY;
                    appState.getCamera().move(-dx, -dy);
                    dragStartMouseX = e.getX();
                    dragStartMouseY = e.getY();
                    repaint();
                }

                currentX = e.getX();
                currentY = e.getY();

                debugLayout.setMousePosition(currentX, currentY);
                selectionLayout.repaint();
            }


            @Override
            public void mouseMoved(MouseEvent e) {
                currentX = e.getX();
                currentY = e.getY();
                debugLayout.setMousePosition(currentX, currentY);
                selectionLayout.repaint();
            }
        });

    }


    private void showEntitySelector(int worldX, int worldY) {
        if (activeSelectorPanel != null) {
            remove(activeSelectorPanel);
            activeSelectorPanel = null;
        }

        PositionComponent position = new PositionComponent(worldX, worldY);
        WorldMap world = appState.getGameContext().getSimulation().getWorldMap();
        List<Entity> entities = world.getInPosition(position);

        if (entities.isEmpty()) {
            System.out.println("Клетка пуста");
            return;
        }

        // Создаём панель выбора
        EntitySelectorPanel selector = new EntitySelectorPanel(entities, position, appState);
        selector.setOnClose(
                () -> {
                    remove(selector);
                    activeSelectorPanel = null;
                    revalidate();
                    repaint();
                }
        );

        // Позиционируем панель рядом с кликом
        int screenX = (int) appState.getCamera().worldToScreenX(worldX);
        int screenY = (int) appState.getCamera().worldToScreenY(worldY);
        selector.setBounds(
                Math.min(screenX + 20, Config.MAP_WIDTH - 260),
                Math.min(screenY + 20, Config.MAP_HEIGHT - 210),
                250,
                200
        );

        // Добавляем поверх всех слоёв
        add(selector, JLayeredPane.POPUP_LAYER);
        activeSelectorPanel = selector;
        selector.setVisible(true);
        revalidate();
        repaint();
    }

}
