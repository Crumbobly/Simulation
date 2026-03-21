package ru.lab.app.state;

import ru.lab.game.entity.Entity;
import ru.lab.game.entity.Grass;
import ru.lab.game.entity.Herbivore;
import ru.lab.game.entity.Predator;
import ru.lab.game.entity.Rock;
import ru.lab.game.entity.Tree;

import java.util.function.Supplier;

public enum SpawnType {
    PREDATOR("Хищник", Predator::new),
    HERBIVORE("Травоядное", Herbivore::new),
    GRASS("Трава", Grass::new),
    ROCK("Камень", Rock::new),
    TREE("Дерево", Tree::new);

    private final String displayName;
    private final Supplier<? extends Entity> factory;

    SpawnType(String displayName, Supplier<? extends Entity> factory) {
        this.displayName = displayName;
        this.factory = factory;
    }

    public String getDisplayName() { return displayName; }

    public Entity create() { return factory.get(); }

    @Override
    public String toString() { return displayName; }
}
