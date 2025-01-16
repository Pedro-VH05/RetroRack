package utils;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.Platform;

import java.util.HashMap;
import java.util.Map;

public class IconUtil {

	private static final Map<String, Image> platformIcons = new HashMap<>(); 
	private static final double LARGE = 36;

	static {
		// PlayStation
		platformIcons.put("PlayStation 5",
				new Image(IconUtil.class.getResource("/iconos/playstation/playstation.png").toExternalForm()));
		platformIcons.put("PlayStation 4",
				new Image(IconUtil.class.getResource("/iconos/playstation/playstation.png").toExternalForm()));
		platformIcons.put("PlayStation 3",
				new Image(IconUtil.class.getResource("/iconos/playstation/playstation.png").toExternalForm()));
		platformIcons.put("PlayStation 2",
				new Image(IconUtil.class.getResource("/iconos/playstation/playstation.png").toExternalForm()));
		platformIcons.put("PlayStation",
				new Image(IconUtil.class.getResource("/iconos/playstation/playstation.png").toExternalForm()));

		// Xbox
		platformIcons.put("Xbox Series S/X",
				new Image(IconUtil.class.getResource("/iconos/xbox/xbox.png").toExternalForm()));
		platformIcons.put("Xbox One",
				new Image(IconUtil.class.getResource("/iconos/xbox/xbox.png").toExternalForm()));
		platformIcons.put("Xbox 360",
				new Image(IconUtil.class.getResource("/iconos/xbox/xbox.png").toExternalForm()));
		platformIcons.put("Xbox", new Image(IconUtil.class.getResource("/iconos/xbox/xbox.png").toExternalForm()));

		// Nintendo
		platformIcons.put("Nintendo Switch",
				new Image(IconUtil.class.getResource("/iconos/nintendo/switch.png").toExternalForm()));
		platformIcons.put("Wii U", new Image(IconUtil.class.getResource("/iconos/nintendo/wiiU.png").toExternalForm()));
		platformIcons.put("Wii", new Image(IconUtil.class.getResource("/iconos/nintendo/wii.png").toExternalForm()));
		platformIcons.put("GameCube",
				new Image(IconUtil.class.getResource("/iconos/nintendo/gameCube.png").toExternalForm()));
		platformIcons.put("Nintendo 64",
				new Image(IconUtil.class.getResource("/iconos/nintendo/n64.png").toExternalForm()));
		platformIcons.put("SNES", new Image(IconUtil.class.getResource("/iconos/nintendo/snes.png").toExternalForm()));
		platformIcons.put("NES", new Image(IconUtil.class.getResource("/iconos/nintendo/nes.png").toExternalForm()));
		platformIcons.put("Game Boy",
				new Image(IconUtil.class.getResource("/iconos/nintendo/gameboy.png").toExternalForm()));
		platformIcons.put("Game Boy Advance",
				new Image(IconUtil.class.getResource("/iconos/nintendo/gameboy.png").toExternalForm()));
		platformIcons.put("Game Boy Advance",
				new Image(IconUtil.class.getResource("/iconos/nintendo/gameboy.png").toExternalForm()));
		platformIcons.put("Game Boy Advance",
				new Image(IconUtil.class.getResource("/iconos/nintendo/gameboy.png").toExternalForm()));

		// OLD
		platformIcons.put("Dreamcast",
				new Image(IconUtil.class.getResource("/iconos/old/dreamcast.png").toExternalForm()));

		// PC
		platformIcons.put("PC", new Image(IconUtil.class.getResource("/iconos/pc.png").toExternalForm()));

		// Default icon
		platformIcons.put("default",
				new Image(IconUtil.class.getResource("/iconos/default-icon.png").toExternalForm()));
	}

	/**
	 * Obtiene el icono de una plataforma específica.
	 *
	 * @param platform Objeto Platform con el nombre de la plataforma.
	 * @return ImageView con el icono correspondiente.
	 */
	public static ImageView getPlatformIcon(Platform platform) {
		Image platformIcon = null;
		try {
			if (platform == null || platform.getName() == null || platform.getName().isEmpty()) {
				return createDefaultIcon();
			}

			String platformName = platform.getName();

			// Si es Linux o macOS, no mostrar ningún icono
			if (platformName.equals("Linux") || platformName.equals("macOS")) {
				return null; // Retorna un ImageView vacío
			}

			// Obtener el icono desde el mapa o usar el predeterminado
			platformIcon = getMatchingIcon(platformName);

		} catch (NullPointerException e) {
			System.err.println("Nodo nulo detectado, continuando ejecución.");
		}
		return createImageView(platformIcon);
	}
	
	/**
	 * Determina el grupo principal al que pertenece una plataforma.
	 *
	 * @param platformName Nombre de la plataforma.
	 * @return Nombre del grupo principal de la plataforma.
	 */
	public static String getPlatformGroup(String platformName) {
	    if (platformName == null || platformName.isEmpty()) {
	        return "unknown";
	    }

	    // Agrupaciones personalizadas
	    if (platformName.contains("PlayStation")) {
	        return "PlayStation";
	    } else if (platformName.contains("Xbox")) {
	        return "Xbox";
	    } else if (platformName.contains("Nintendo")) {
	        return "Nintendo";
	    } else if (platformName.equals("PC")) {
	        return "PC";
	    } else {
	        return "Other";
	    }
	}


	/**
	 * Encuentra el icono que coincide con el nombre de la plataforma.
	 *
	 * @param platformName Nombre de la plataforma.
	 * @return Imagen correspondiente o la imagen por defecto.
	 */
	private static Image getMatchingIcon(String platformName) {
		for (Map.Entry<String, Image> entry : platformIcons.entrySet()) {
			if (platformName.equals(entry.getKey())) {
				return entry.getValue();
			}
		}
		return platformIcons.get("default");
	}

	/**
	 * Crea un ImageView estándar a partir de una imagen.
	 *
	 * @param image La imagen a utilizar.
	 * @return ImageView con dimensiones estándar.
	 */
	private static ImageView createImageView(Image image) {
		ImageView imageView = new ImageView(image);
		imageView.setFitWidth(LARGE);
		imageView.setFitHeight(LARGE);
		imageView.setSmooth(true);
		return imageView;
	}

	/**
	 * Crea un icono predeterminado.
	 *
	 * @return ImageView con el icono por defecto.
	 */
	private static ImageView createDefaultIcon() {
		Image defaultIcon = platformIcons.get("default");
		return createImageView(defaultIcon);
	}
}
