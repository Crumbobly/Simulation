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
import ru.lab.app.ui.popup.EntitySelectorPanel;
import ru.lab.config.Config;
import ru.lab.app.state.AppState;
import ru.lab.game.GameContext;
import ru.lab.game.GameController;
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
    private final GameContext gameContext;
    private final GameController gameController;

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

    public GameMap(AppState appState, GameContext gameContext, GameController gameController) {
        this.appState = appState;
        this.gameContext = gameContext;
        this.gameController = gameController;

        gameController.addTickListener(this::repaint);

        setLayout(null);
        setPreferredSize(new Dimension(Config.MAP_WIDTH, Config.MAP_HEIGHT));
        setFocusable(true);

        worldLayout = new WorldLayout(appState.getCamera());
        worldLayout.setBounds(0, 0, Config.MAP_WIDTH, Config.MAP_HEIGHT);

        entitiesLayout = new EntitiesLayout(appState, gameContext);
        entitiesLayout.setBounds(0, 0, Config.MAP_WIDTH, Config.MAP_HEIGHT);

        heatmapLayout = new HeatMapLayout(appState, gameContext);
        heatmapLayout.setBounds(0, 0, Config.MAP_WIDTH, Config.MAP_HEIGHT);

        selectionLayout = new SelectionLayout(
                appState,
                () -> currentX,
                () -> currentY
        );
        selectionLayout.setBounds(0, 0, Config.MAP_WIDTH, Config.MAP_HEIGHT);

        miniMapLayout = new MiniMapLayout(appState.getCamera(), 10);
        miniMapLayout.setBounds(
                Config.MAP_WIDTH - miniMapLayout.getPreferredSize().width - 10,
                10,
                miniMapLayout.getPreferredSize().width,
                miniMapLayout.getPreferredSize().height
        );

        debugLayout = new DebugLayout(
               appState.getCamera(),
                () -> currentX,
                () -> currentY
        );
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
        setupMouseClickHandler();
        setupMouseWheelHandler();
        setupMouseDragHandler();
    }


    private void setupMouseClickHandler() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (appState.getCurrentInteractionMode() == InteractionMode.MOVE) {
                    handleMoveModePress(e);
                } else if (appState.getCurrentInteractionMode() == InteractionMode.SELECT) {
                    handleSelectModePress(e);
                }
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                dragging = false;
            }
        });
    }

    private void handleMoveModePress(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            dragging = true;
            dragStartMouseX = e.getX();
            dragStartMouseY = e.getY();
        }
    }


    private void handleSelectModePress(MouseEvent e) {
        int worldX = (int) appState.getCamera().screenToWorldX(e.getX());
        int worldY = (int) appState.getCamera().screenToWorldY(e.getY());

        if (SwingUtilities.isLeftMouseButton(e)) {
            handleLeftClickSpawn(worldX, worldY);
        } else if (SwingUtilities.isRightMouseButton(e)) {
            handleRightClickAction(worldX, worldY);
        }
    }

    private void handleLeftClickSpawn(int worldX, int worldY) {
        SpawnType selectedSpawn = appState.getSpawnType();
        if (selectedSpawn == null) {
            System.out.println("Сущность для добавления не выбрана");
            return;
        }

        Entity newEntity = selectedSpawn.create();
        PositionComponent position = new PositionComponent(worldX, worldY);
        WorldMap world = gameContext.getWorldMap();

        if (world.canPlace(newEntity, position)) {
            world.add(newEntity, position);
            System.out.println("Добавлено: " + newEntity.getClass().getSimpleName());
        } else {
            System.out.println("Нельзя разместить здесь (коллизия)");
        }
    }

    private void handleRightClickAction(int worldX, int worldY) {
        showEntitySelector(worldX, worldY);
    }

    private void setupMouseWheelHandler() {
        addMouseWheelListener(e -> {
            if (e.getWheelRotation() < 0) {
                appState.getCamera().zoomIn();
            } else if (e.getWheelRotation() > 0) {
                appState.getCamera().zoomOut();
            }
            repaint();
        });
    }

    private void setupMouseDragHandler() {
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragging && appState.getCurrentInteractionMode() == InteractionMode.MOVE) {
                    int dx = e.getX() - dragStartMouseX;
                    int dy = e.getY() - dragStartMouseY;
                    appState.getCamera().move(-dx, -dy);
                    dragStartMouseX = e.getX();
                    dragStartMouseY = e.getY();
                }
                updateMousePosition(e.getX(), e.getY());
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                updateMousePosition(e.getX(), e.getY());
            }
        });
    }

    private void updateMousePosition(int x, int y) {
        currentX = x;
        currentY = y;
        repaint();
    }


    private void showEntitySelector(int worldX, int worldY) {
        if (activeSelectorPanel != null) {
            remove(activeSelectorPanel);
            activeSelectorPanel = null;
        }

        PositionComponent position = new PositionComponent(worldX, worldY);
        WorldMap world = gameContext.getWorldMap();
        List<Entity> entities = world.getInPosition(position);

        if (entities.isEmpty()) {
            return;
        }

        EntitySelectorPanel selector = new EntitySelectorPanel(entities, position, gameContext.getWorldMap()::remove);
        selector.setOnClose(
                () -> {
                    remove(selector);
                    activeSelectorPanel = null;
                    revalidate();
                    repaint();
                }
        );

        int screenX = appState.getCamera().worldToScreenX(worldX);
        int screenY = appState.getCamera().worldToScreenY(worldY);
        selector.setBounds(
                Math.min(screenX + 20, Config.MAP_WIDTH - 260),
                Math.min(screenY + 20, Config.MAP_HEIGHT - 210),
                250,
                200
        );

        add(selector, JLayeredPane.POPUP_LAYER);
        activeSelectorPanel = selector;
        selector.setVisible(true);
        revalidate();
        repaint();
    }

}
