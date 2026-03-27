package ru.lab.app.ui.gameMap.layout;

import ru.lab.app.state.AppState;
import ru.lab.app.state.StateType;
import ru.lab.game.GameContext;

import javax.swing.JPanel;
import java.awt.Color;

public abstract class MapLayout extends JPanel {

    public MapLayout() {

        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0));
        setAlignmentX(0f);
        setAlignmentY(0f);

    }

}
