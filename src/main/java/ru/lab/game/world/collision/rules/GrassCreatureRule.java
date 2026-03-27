package ru.lab.game.world.collision.rules;

import ru.lab.game.entity.Creature;
import ru.lab.game.entity.Entity;
import ru.lab.game.entity.Grass;

import java.util.List;

public class GrassCreatureRule implements CollisionRule {

    @Override
    public boolean canCoexist(Entity newEntity, List<Entity> existing) {

        if (newEntity instanceof Grass){
            for (Entity e : existing) {
                if (!(e instanceof Creature)) {
                    return false;
                }
            }
            return true;
        }

        else if (newEntity instanceof Creature) {
            for (Entity e : existing) {
                if (!(e instanceof Grass)) {
                    return false;
                }
            }
            return true;
        }


        // правило не применимо
        return false;
    }

}
