package ru.lab.app.ui;

import ru.lab.app.state.AppState;
import ru.lab.app.ui.gameMap.GameMap;
import ru.lab.app.ui.toolBox.ToolBox;
import ru.lab.config.Config;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import java.awt.BorderLayout;
import java.awt.Dimension;

import static javax.swing.JLayeredPane.DEFAULT_LAYER;

public class GameFrame extends JFrame {

    private final AppState appState;

    public GameFrame() {
        super("Simulation");
        this.appState = new AppState();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        GameMap gameMap = new GameMap(appState);
        gameMap.setBounds(0, 0, Config.MAP_WIDTH, Config.MAP_HEIGHT);
        ToolBox toolbox = new ToolBox(appState, gameMap);

        add(gameMap, BorderLayout.CENTER);
        add(toolbox, BorderLayout.EAST);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        gameMap.requestFocusInWindow();
    }

}
