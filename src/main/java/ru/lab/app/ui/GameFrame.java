package ru.lab.app.ui;

import ru.lab.app.state.AppState;
import ru.lab.app.ui.gameMap.GameMap;
import ru.lab.app.ui.toolBox.ToolBox;
import ru.lab.app.visual.Camera;
import ru.lab.config.Config;
import ru.lab.game.GameContext;
import ru.lab.game.GameController;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import java.awt.BorderLayout;
import java.awt.Dimension;

import static javax.swing.JLayeredPane.DEFAULT_LAYER;

public class GameFrame extends JFrame {

    private final AppState appState;
    private final GameContext gameContext;
    private final GameController gameController;

    public GameFrame() {
        super("Simulation");
        this.appState = new AppState();
        this.gameContext = new GameContext();
        this.gameController = new GameController(gameContext, appState);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        GameMap gameMap = new GameMap(appState, gameContext, gameController);
        gameMap.setBounds(0, 0, Config.MAP_WIDTH, Config.MAP_HEIGHT);
        ToolBox toolbox = new ToolBox(appState);

        add(gameMap, BorderLayout.CENTER);
        add(toolbox, BorderLayout.EAST);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        gameMap.requestFocusInWindow();
    }

}
