package ru.lab.app.ui.toolBox;

import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Component;

public class ToolBoxLabel extends JLabel {
    public ToolBoxLabel(String text) {
        super();
        setForeground(new Color(200,200,200));
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setText(text);
    }
}
