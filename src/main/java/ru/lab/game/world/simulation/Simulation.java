package ru.lab.game.world.simulation;

import ru.algo.spatial.dto.PositionComponent;
import ru.lab.config.Config;
import ru.lab.game.entity.Entity;
import ru.lab.game.entity.Grass;
import ru.lab.game.entity.Herbivore;
import ru.lab.game.entity.Predator;
import ru.lab.game.entity.Rock;
import ru.lab.game.entity.Tree;
import ru.lab.game.world.WorldMap;
import ru.lab.game.world.simulation.action.AddAction;
import ru.lab.game.world.simulation.action.CompositeAction;
import ru.lab.game.world.simulation.action.MoveAction;
import ru.lab.game.world.simulation.action.RemoveAction;
import ru.lab.game.world.simulation.action.WorldAction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static ru.algo.spatial.dto.PositionComponent.distance;


public class Simulation {

    private final WorldMap worldMap;
    private final Random random = new Random();
    private final Set<Entity> removed = new HashSet<>();

    public Simulation(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    public void nextTurn() {

        try {
            List<Entity> entities = new ArrayList<>(worldMap.getEntities().keySet());
            List<WorldAction> actions = new ArrayList<>(entities.size());

            for (Entity entity : entities) {
                if (removed.contains(entity)) {
                    continue;
                }

                PositionComponent pos = worldMap.getPosition(entity);
                if (pos == null) continue;

                WorldAction action = processEntity(entity, pos);
                if (action != null) {
                    actions.add(action);
                }
            }

            worldMap.applyActions(actions);
            removed.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private WorldAction processEntity(Entity entity, PositionComponent pos) {
        return switch (entity) {
            case Predator p -> processPredator(p, pos);
            case Herbivore h-> processHerbivore(h, pos);
            case Grass g -> processGrass(g, pos);
            default -> null;
        };
    }

    private WorldAction processPredator(Entity entity, PositionComponent pos) {

        Entity prey = worldMap.findNearest(entity, e -> e.getClass() == Herbivore.class);
        if (prey != null && !removed.contains(prey)) {
            PositionComponent preyPos = worldMap.getPosition(prey);

            if (preyPos != null) {
                double dist = PositionComponent.distance(pos, preyPos);
                if (dist <= 2) {
                    removed.add(prey);
                    return new CompositeAction(
                            new RemoveAction(prey),
                            new MoveAction(entity, preyPos)
                    );
                }

                final PositionComponent target = moveTowards(pos, preyPos);
                return new MoveAction(entity, target);

            }
        }

        final PositionComponent randomPose = randomMove(pos, 1);
        return new MoveAction(entity, randomPose);

    }


    private WorldAction processHerbivore(Entity entity, PositionComponent pos) {

        if (removed.contains(entity)) {
            return null;
        }

        Entity predator = worldMap.findNearest(entity, e -> e.getClass() == Predator.class);
        if (predator != null) {
            PositionComponent predPos = worldMap.getPosition(predator);
            if (predPos != null) {
                double dist = PositionComponent.distance(pos, predPos);
                if (dist <= Config.SPATIAL_CELL_SIZE * 5) {
                    return new MoveAction(entity, moveAway(pos, predPos));
                }
            }
        }

        Entity grass = worldMap.findNearest(entity, e -> e.getClass() == Grass.class);
        if (grass != null && !removed.contains(grass)) {
            PositionComponent grassPos = worldMap.getPosition(grass);
            if (grassPos != null) {
                double dist = PositionComponent.distance(pos, grassPos);
                if (dist <= 1) {
                    removed.add(grass);
                    return new CompositeAction(
                            new RemoveAction(grass),
                            new MoveAction(entity, grassPos)
                    );
                }
                return new MoveAction(entity, moveTowards(pos, grassPos));
            }
        }

        return new MoveAction(entity, randomMove(pos, 1));
    }


    private WorldAction processGrass(Entity entity, PositionComponent pos) {

        if (random.nextDouble() < 0.01) {
            PositionComponent newPos = randomMove(pos, 10);

            if (worldMap.canPlace(entity, newPos)) {
                return new AddAction(new Grass(), newPos);
            }
        }
        return null;
    }

    private PositionComponent randomMove(PositionComponent pos, int dist) {
        int dx = random.nextInt(dist + 1) * random.nextInt(-1, 2);
        int dy = random.nextInt(dist + 1) * random.nextInt(-1, 2);
        int newX = Math.max(0, Math.min(Config.WORLD_WIDTH - 1, pos.x() + dx));
        int newY = Math.max(0, Math.min(Config.WORLD_HEIGHT - 1, pos.y() + dy));
        return new PositionComponent(newX, newY);
    }

    private PositionComponent moveTowards(PositionComponent from, PositionComponent to) {
        int dx = Integer.compare(to.x(), from.x());
        int dy = Integer.compare(to.y(), from.y());
        return new PositionComponent(
                Math.max(0, Math.min(Config.WORLD_WIDTH - 1, from.x() + dx)),
                Math.max(0, Math.min(Config.WORLD_HEIGHT - 1, from.y() + dy))
        );
    }

    private PositionComponent moveAway(PositionComponent from, PositionComponent to) {
        int dx = -Integer.compare(to.x(), from.x());
        int dy = -Integer.compare(to.y(), from.y());
        return new PositionComponent(
                Math.max(0, Math.min(Config.WORLD_WIDTH - 1, from.x() + dx)),
                Math.max(0, Math.min(Config.WORLD_HEIGHT - 1, from.y() + dy))
        );
    }

}
