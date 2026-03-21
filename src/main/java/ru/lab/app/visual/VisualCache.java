package ru.lab.app.visual;


import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class VisualCache {

    // Кэш загруженных иконок
    private static final Map<Set<Class<?>>, BufferedImage> iconCache = new ConcurrentHashMap<>();

    // Статическая инициализация — грузим все иконки при старте
    static {
        preloadIcons();
    }

    private static void preloadIcons() {
        for (Map.Entry<Set<Class<?>>, EntityVisualConfig> entry : EntityVisualRegistry.getAll().entrySet()) {
            Set<Class<?>> types = entry.getKey();
            String iconPath = entry.getValue().iconPath();
            loadImage(types, iconPath);
        }
    }

    private static void loadImage(Set<Class<?>> types, String iconPath) {
        try {
            BufferedImage icon = ImageIO.read(
                    Objects.requireNonNull(VisualCache.class.getClassLoader().getResourceAsStream(iconPath))
            );
            iconCache.put(types, icon);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage getIcon(Set<Class<?>> types) {
        final BufferedImage image = iconCache.get(types);
        if  (image == null) {
            return iconCache.get(Set.of());
        }
        return image;
    }

    public static Color getColor(Set<Class<?>> types) {
        return EntityVisualRegistry.get(types).color();
    }
}
