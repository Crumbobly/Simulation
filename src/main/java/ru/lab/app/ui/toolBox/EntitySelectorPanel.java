package ru.lab.app.ui.toolBox;

import ru.algo.spatial.dto.PositionComponent;
import ru.lab.app.state.AppState;
import ru.lab.game.entity.Entity;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

public class EntitySelectorPanel extends JPanel {

    private final JPanel contentPanel;
    private JButton closeBtn;
    private final AppState appState;
    private Runnable onClose;

    public EntitySelectorPanel(List<Entity> entities, PositionComponent position, AppState appState) {
        this.contentPanel = new JPanel();
        this.appState = appState;
        setup(entities, position);
    }

    public void setOnClose( Runnable onClose){
        closeBtn.addActionListener(e -> onClose.run());
        this.onClose = onClose;
    }


    private void setup(List<Entity> entities, PositionComponent position) {
        setLayout(new BorderLayout());
        setBackground(new Color(45, 45, 45));
        setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 1));
        setPreferredSize(new Dimension(250, 200));

        // Заголовок
        JLabel title = new JLabel("Объекты в клетке: " + position.x() + "," + position.y());
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 13));
        title.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        add(title, BorderLayout.NORTH);

        // Список сущностей
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(45, 45, 45));

        for (Entity entity : entities) {
            contentPanel.add(createEntityRow(entity));
            contentPanel.add(Box.createVerticalStrut(5));
        }

        // Кнопка закрытия
        closeBtn = new JButton("Закрыть");
        closeBtn.setBackground(new Color(70, 70, 70));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setFocusPainted(false);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setBackground(new Color(45, 45, 45));
        footer.add(closeBtn);

        add(new JScrollPane(contentPanel), BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
    }

    private JPanel createEntityRow(Entity entity) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setBackground(new Color(60, 60, 60));
        row.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        row.setMaximumSize(new Dimension(230, 35));

        // Название сущности
        JLabel name = new JLabel("🔹 " + entity.getClass().getSimpleName());
        name.setForeground(Color.WHITE);
        name.setFont(new Font("SansSerif", Font.PLAIN, 12));

        // Кнопка удаления
        JButton deleteBtn = new JButton("🗑");
        deleteBtn.setToolTipText("Удалить эту сущность");
        deleteBtn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        deleteBtn.setBackground(new Color(200, 60, 60));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFocusPainted(false);
        deleteBtn.setPreferredSize(new Dimension(35, 25));
        deleteBtn.setMaximumSize(new Dimension(35, 25));

        // Сохраняем ссылку на сущность через клиентские свойства
        deleteBtn.putClientProperty("entity", entity);
        deleteBtn.addActionListener(e -> {
            Entity toDelete = (Entity) deleteBtn.getClientProperty("entity");
            // 👇 Здесь будет вызов удаления из WorldMap
            appState.getGameContext().getWorldMap().remove(entity);
            System.out.println("Удалить: " + toDelete.getClass().getSimpleName());
            onClose.run();


        });

        row.add(name, BorderLayout.WEST);
        row.add(deleteBtn, BorderLayout.EAST);
        return row;
    }
}
