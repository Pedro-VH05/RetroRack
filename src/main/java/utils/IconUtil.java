package utils;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.Platform;

import java.util.HashMap;
import java.util.Map;

public class IconUtil {
    // Mapa de plataformas y sus respectivos iconos
    private final static Map<String, Image> platformIcons = new HashMap<>();
    
    static {
        // Cargar los íconos comunes para PlayStation y Xbox
        platformIcons.put("PlayStation", new Image(IconUtil.class.getResource("/iconos/playStation.png").toExternalForm()));
        platformIcons.put("Xbox", new Image(IconUtil.class.getResource("/iconos/xbox.png").toExternalForm()));
        
        // Cargar íconos específicos para otras plataformas (si es necesario)
        platformIcons.put("PC", new Image(IconUtil.class.getResource("/iconos/pc.png").toExternalForm()));
        //platformIcons.put("Nintendo Switch", new Image(IconUtil.class.getResource("/iconos/nintendo.png").toExternalForm()));
    }

    public static ImageView getPlatformIcon(Platform platform) {
        String platformName = platform.getName();
        System.out.println(platformName);

        // Comprobar si platformName es null o vacío
        if (platformName == null || platformName.isEmpty()) {
            platformName = "Unknown"; // O algún valor predeterminado
        }

        Image platformIcon = null;
        
        // Comprobar si el nombre contiene "PlayStation" o "Xbox" para asignar el ícono correspondiente
        if (platformName.contains("PlayStation")) {
            platformIcon = platformIcons.get("PlayStation"); // Asignar el ícono común de PlayStation
        } else if (platformName.contains("Xbox")) {
            platformIcon = platformIcons.get("Xbox"); // Asignar el ícono común de Xbox
        } else {
            platformIcon = platformIcons.get(platformName); // Usar el ícono específico si está en el mapa
        }

        // Si no se encuentra el icono, usar uno por defecto
        if (platformIcon == null) {
            platformIcon = new Image(IconUtil.class.getResource("/iconos/playStation.png").toExternalForm());
        }

        ImageView imageView = new ImageView(platformIcon);
        imageView.setFitWidth(24);
        imageView.setFitHeight(24);
        return imageView;
    }

}
