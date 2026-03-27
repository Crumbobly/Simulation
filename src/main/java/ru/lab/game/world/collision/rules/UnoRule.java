package ru.lab.game.world.collision.rules;

import ru.lab.game.entity.Entity;

import java.util.List;


public class UnoRule implements CollisionRule {

    @Override
    public boolean canCoexist(Entity newEntity, List<Entity> existing) {
        return existing.isEmpty();
    }

}
