package ru.lab.app.ui.toolBox;

import javax.swing.JButton;
import javax.swing.JToggleButton;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ToolBoxButton extends JButton {

    private static final Color BG_LIGHT = new Color(70, 70, 70);

    public ToolBoxButton(String text, Color activeColor) {
        setText(text);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        setForeground(Color.WHITE);
        setBackground(BG_LIGHT);
        setFocusPainted(false);
        setBorderPainted(false);
        setAlignmentX(Component.CENTER_ALIGNMENT);

        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (!isSelected()) setBackground(BG_LIGHT.brighter());
            }
            public void mouseExited(MouseEvent e) {
                if (!isSelected()) setBackground(BG_LIGHT);
            }
        });

        addItemListener(e ->
                setBackground(isSelected() ? activeColor : BG_LIGHT)
        );

    }

}
