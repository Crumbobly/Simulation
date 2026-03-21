package ru.lab.app.visual;

import ru.lab.game.entity.Grass;
import ru.lab.game.entity.Herbivore;
import ru.lab.game.entity.Predator;
import ru.lab.game.entity.Rock;
import ru.lab.game.entity.Tree;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EntityVisualRegistry {

    private static final Map<Set<Class<?>>, EntityVisualConfig> registry = new HashMap<>();

    static {
        // default
        register(
                Set.of(),
                new EntityVisualConfig(new Color(0, 0, 0), "icons/null.png")
        );

        // singles
        register(
                Set.of(Predator.class),
                new EntityVisualConfig(new Color(235, 127, 106), "icons/wolf.png")
        );
        register(
                Set.of(Herbivore.class),
                new EntityVisualConfig(new Color(66, 58, 34), "icons/deer.png")
        );
        register(
                Set.of(Grass.class),
                new EntityVisualConfig(new Color(151, 235, 106), "icons/grass.png")
        );
        register(
                Set.of(Rock.class),
                new EntityVisualConfig(new Color(55, 59, 53), "icons/stone.png")
        );
        register(
                Set.of(Tree.class),
                new EntityVisualConfig(new Color(68, 166, 15), "icons/grass.png")
        );

        // doubles
        register(
                Set.of(Rock.class, Herbivore.class),
                new EntityVisualConfig(new Color(66, 58, 34), "icons/deer_stone.png")
        );
        register(
                Set.of(Predator.class, Grass.class), //todo
                new EntityVisualConfig(new Color(235, 127, 106), "icons/grass.png")
        );
        register(
                Set.of(Herbivore.class, Grass.class), //todo
                new EntityVisualConfig(new Color(66, 58, 34), "icons/grass.png")
        );
    }

    private static EntityVisualConfig getDefault() {
        return registry.get(Set.of());
    }

    public static void register(Set<Class<?>> types, EntityVisualConfig config) {
        registry.put(types, config);
    }

    public static EntityVisualConfig get(Set<Class<?>> types) {
        return registry.getOrDefault(types, getDefault());
    }

    public static Map<Set<Class<?>>, EntityVisualConfig> getAll() {
        return registry;
    }

}
