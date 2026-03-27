package ru.lab.game.world.collision.rules;


import ru.lab.game.entity.Entity;

import java.util.List;

/**
 * Правило проверки совместимости сущностей в одной клетке.
 */
@FunctionalInterface
public interface CollisionRule {

    /**
     * @param newEntity сущность, которую хотим разместить
     * @param existing сущности, уже находящиеся в клетке
     * @return true если размещение допустимо
     */
     boolean canCoexist(Entity newEntity, List<Entity> existing);

}
