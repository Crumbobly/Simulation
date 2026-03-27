package ru.lab.game.world;

import ru.algo.spatial.dto.PositionComponent;
import ru.algo.spatial.type.ConcurrentSpatialGrid;
import ru.algo.spatial.type.ValidatedSpatialGrid;
import ru.lab.config.Config;
import ru.lab.game.entity.Entity;
import ru.lab.game.entity.Grass;
import ru.lab.game.entity.Herbivore;
import ru.lab.game.entity.Predator;
import ru.lab.game.entity.Rock;
import ru.lab.game.entity.Tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorldMapBuilder {

    public static WorldMap build() {

        Random random = new Random();
        WorldMap worldMap = new WorldMap(new ValidatedSpatialGrid<>(
                new ConcurrentSpatialGrid<>(Config.SPATIAL_CELL_SIZE, Config.WORLD_WIDTH, Config.WORLD_HEIGHT)
        ));

        int centerX = Config.WORLD_WIDTH / 2;
        int centerY = Config.WORLD_HEIGHT / 2;

//        worldMap.add( new Rock(), new PositionComponent(centerX, centerY));
//        worldMap.add( new Herbivore(), new PositionComponent(centerX, centerY));
//

        double spreadX = Config.WORLD_WIDTH / 6.0;
        double spreadY = Config.WORLD_HEIGHT / 6.0;

        for (int i = 0; i < 1000; i++) {

            int x = (int) (centerX + random.nextGaussian() * spreadX);
            int y = (int) (centerY + random.nextGaussian() * spreadY);
            int typeIndex = random.nextInt(5);

            List<Class<? extends Entity>> entities = new ArrayList<>();
            entities.add(Grass.class);
            entities.add(Herbivore.class);
            entities.add(Predator.class);
            entities.add(Rock.class);
            entities.add(Tree.class);
            Entity type = createEntity(entities.get(typeIndex));

            x = Math.max(0, Math.min(Config.WORLD_WIDTH - 1, x));
            y = Math.max(0, Math.min(Config.WORLD_HEIGHT - 1, y));

            try {
                worldMap.add(type, new PositionComponent(x, y));
            }
            catch (IllegalArgumentException e) {
                continue;
            }
        }

        return worldMap;
    }

    private static Entity createEntity(Class<? extends Entity> type) {
        try {
            return switch (type.getSimpleName()) {
                case "Grass" -> new Grass();
                case "Herbivore" -> new Herbivore(); // например, энергия
                case "Predator" -> new Predator();
                case "Rock" -> new Rock();
                case "Tree" -> new Tree();
                default -> null;
            };
        } catch (Exception e) {
            System.err.println("Failed to create entity: " + type.getSimpleName());
            return null;
        }
    }

}
