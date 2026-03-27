package ru.lab.game.world.collision.rules;

import ru.lab.game.entity.Entity;
import ru.lab.game.entity.Herbivore;
import ru.lab.game.entity.Rock;

import java.util.List;

public class RockHerbivoreRule implements CollisionRule {

    @Override
    public boolean canCoexist(Entity newEntity, List<Entity> existing) {

        if (newEntity instanceof Herbivore){
            for (Entity e : existing) {
                if (!(e instanceof Rock)) {
                    return false;
                }
            }
            return true;
        }

        else if (newEntity instanceof Rock) {
            for (Entity e : existing) {
                if (!(e instanceof Herbivore)) {
                    return false;
                }
            }
            return true;
        }


        // правило не применимо
        return false;
    }
}
