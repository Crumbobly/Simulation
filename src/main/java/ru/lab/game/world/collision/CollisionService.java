package ru.lab.game.world.collision;


import ru.lab.game.entity.Entity;
import ru.lab.game.world.collision.rules.CollisionRule;
import ru.lab.game.world.collision.rules.GrassCreatureRule;
import ru.lab.game.world.collision.rules.RockHerbivoreRule;
import ru.lab.game.world.collision.rules.UnoRule;


import java.util.ArrayList;
import java.util.List;

public class CollisionService {

    private final List<CollisionRule> rules = new ArrayList<>();

    public CollisionService() {
        addRule(new GrassCreatureRule());
        addRule(new RockHerbivoreRule());
        addRule(new UnoRule());
    }

    public void addRule(CollisionRule rule) {
        rules.add(rule);
    }


    public boolean canPlace(Entity newEntity, List<Entity> existing) {
        for (CollisionRule rule : rules) {
            // Если ХОТЯ БЫ ОДНО правило сказало "ДА" — пропускаем
            if (rule.canCoexist(newEntity, existing)) {
                return true;
            }
        }
        // Если НИ ОДНО правило не сказало "ДА" — запрещаем
        return false;
    }

}
