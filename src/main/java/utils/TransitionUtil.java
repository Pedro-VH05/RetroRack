package utils;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class TransitionUtil {

	/**
	 * Transición de desvanecimiento entre dos nodos.
	 *
	 * @param activePane El nodo actualmente visible.
	 * @param hidePane   El nodo que será visible después de la transición.
	 * @param duration   La duración de la transición en milisegundos.
	 */
	public void fadeSwitch(Node activePane, Node hidePane, double duration) {
	    if (activePane == null) {
	        throw new IllegalArgumentException("El nodo que se va a desaparecer no puede ser nulo.");
	    }

	    FadeTransition fadeOut = new FadeTransition(Duration.millis(duration), activePane);
	    fadeOut.setFromValue(1.0);
	    fadeOut.setToValue(0.0);

	    fadeOut.setOnFinished(e -> {
	        activePane.setVisible(false);
	        
	        if (hidePane != null) {
	        	
	            hidePane.setVisible(true);
	            hidePane.setOpacity(0.0);

	            FadeTransition fadeIn = new FadeTransition(Duration.millis(duration), hidePane);
	            fadeIn.setFromValue(0.0);
	            fadeIn.setToValue(1.0);
	            fadeIn.play();
	        }
	    });

	    fadeOut.play();
	}


	/**
	 * Transición de deslizamiento.
	 *
	 * @param activePane El nodo actualmente visible, puede ser null si no hay uno
	 *                   para esconder.
	 * @param hidePane   El nodo que será visible después de la transición.
	 * @param offsetX    La distancia en X para el desplazamiento.
	 * @param offsetY    La distancia en Y para el desplazamiento.
	 * @param duration   La duración de la transición en milisegundos.
	 * @param isEntering Si esta entrando o saliendo
	 */
	public void slideSwitch(Node activePane, Node hidePane, double offsetX, double offsetY, double duration,
			boolean isEntering, double verticalOffset) {
		if (hidePane == null) {
			throw new IllegalArgumentException("El nodo que se va a mostrar no puede ser nulo.");
		}

		if (activePane != null) {
			TranslateTransition slideOut = new TranslateTransition(Duration.millis(duration), activePane);
			slideOut.setByX(offsetX);
			slideOut.setByY(offsetY);
			slideOut.setOnFinished(e -> activePane.setVisible(false));
			slideOut.play();
		}

		TranslateTransition slideIn = new TranslateTransition(Duration.millis(duration), hidePane);
		if (isEntering) {
			slideIn.setFromX(offsetX);
			slideIn.setFromY(verticalOffset);
			slideIn.setToX(0);
			slideIn.setToY(0);
		} else {
			slideIn.setFromX(hidePane.getTranslateX());
			slideIn.setFromY(0);
			slideIn.setToX(offsetX);
			slideIn.setToY(verticalOffset);
			slideIn.setOnFinished(e -> hidePane.setVisible(false));
		}

		hidePane.setVisible(true);
		slideIn.play();
	}
}
