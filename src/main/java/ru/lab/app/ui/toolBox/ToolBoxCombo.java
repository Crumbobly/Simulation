package ru.lab.app.ui.toolBox;

import javax.swing.JComboBox;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static ru.lab.config.Config.TOOLBOX_WIDTH;

public class ToolBoxCombo <T> extends JComboBox<T> {

    private static final Color BG_LIGHT = new Color(70, 70, 70);

    @SuppressWarnings("unchecked")
    public ToolBoxCombo(T[] values, Supplier<T> get, Consumer<T> set) {
        super(values);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        setSelectedItem(get.get());
        setBackground(BG_LIGHT);
        setForeground(Color.WHITE);
        setAlignmentX(Component.CENTER_ALIGNMENT);
        addActionListener(e -> set.accept((T) getSelectedItem()));
    }


}
