package utils;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.util.Properties;

/**
 * Clase para manejar los emails de bienvenida y de recuperación de contraseña
 */
public class EmailUtil {

	/**
	 * Manda un correo al usuario de confirmacion de Registro
	 * @param destinatario Usuario al que le debe llegar el correo
	 * @param nombreUsuario Nombre del usuario nuevo registrado
	 */
	public static void enviarCorreoBienvenida(String destinatario, String nombreUsuario) {
		String email = "soporteretrorack@gmail.com";
		// Contraseña de aplicación
		String appPass = "atef uwmq phdl zehu";

		// Configuración de propiedades de conexión al servidor SMTP de Gmail
		Properties propiedades = new Properties();
		propiedades.put("mail.smtp.auth", "true");
		propiedades.put("mail.smtp.starttls.enable", "true");
		propiedades.put("mail.smtp.host", "smtp.gmail.com");
		propiedades.put("mail.smtp.port", "587");

		// Creamos la sesión de correo
		Session session = Session.getInstance(propiedades, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(email, appPass);
			}
		});

		try {
			// Crear el mensaje de correo
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(email));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
			message.setSubject("Bienvenido a Retrorack");

			// Crear el contenido HTML directamente
			String contenido = "<!DOCTYPE html>" + "<html lang=\"es\">" + "<head>" + "<meta charset=\"UTF-8\">"
					+ "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
					+ "<title>Bienvenido</title>" + "<style>" + ":root {" + "  font-family: System-ui;" + "}"
					+ ".container {" + "  text-align: center;" + "}" + ".container img {" + "  width: auto;"
					+ "  height: auto;" + "  margin-top: 20px;" + "  margin-bottom: -10px;" + "}" + ".container h1 {"
					+ "  color: #FF983D;" + "  font-size: 56px;" + "}" + ".container p {" + "  font-weight: bold;"
					+ "  font-size: 20px;" + "  color: #121212;" + "}" + "p {" + "  line-height: 20px;" + "}"
					+ "</style>" + "</head>" + "<body>" + "<div class=\"container\">"
					+ "<img src=\"https://raw.githubusercontent.com/Pedro-VH05/RetroRack/main/src/main/resources/images/retroRack_logo.png\" alt=\"RetroRack Logo\">"
					+ "<h1>Bienvenido " + nombreUsuario + "</h1>"
					+ "<p>Estamos muy contentos de tenerte en nuestra comunidad. ¡Empecemos esta aventura juntos!</p>"
					+ "<p>Si tienes alguna pregunta o necesitas ayuda, no dudes en ponerte en contacto con nosotros.</p>"
					+ "<p>¡Gracias por ser parte de RetroRack!</p>" + "</div>" + "</body>" + "</html>";

			// Establecer el contenido del mensaje como HTML
			message.setContent(contenido, "text/html; charset=utf-8");

			// Enviar el correo
			Transport.send(message);
			System.out.println("Correo enviado exitosamente");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Manda un correo al usuario con un código de recuperación
	 * 
	 * @param destinatario Usuario al que le debe llegar el correo
	 * @param nombreUsuario Nombre del usuario nuevo registrado
	 * @param codigo Codigo generado aleatoriamente
	 */
	public static void correoRecuperacionContrasenya(String destinatario, String codigo) {
        String email = "soporteretrorack@gmail.com";
        // Contraseña de aplicación
        String appPass = "atef uwmq phdl zehu";

        // Configuración de propiedades de conexión al servidor SMTP de Gmail
        Properties propiedades = new Properties();
        propiedades.put("mail.smtp.auth", "true");
        propiedades.put("mail.smtp.starttls.enable", "true");
        propiedades.put("mail.smtp.host", "smtp.gmail.com");
        propiedades.put("mail.smtp.port", "587");

        // Creamos la sesión de correo
        Session session = Session.getInstance(propiedades, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, appPass);
            }
        });

        try {
            // Crear el mensaje de correo
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject("Recuperación de Contraseña");

            // Crear el contenido HTML directamente
            String contenido = "<!DOCTYPE html>" + 
                    "<html lang=\"es\">" + 
                    "<head>" + 
                    "<meta charset=\"UTF-8\">" + 
                    "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" + 
                    "<title>Recuperación de Contraseña</title>" + 
                    "<style>" + 
                    ":root { font-family: System-ui; }" + 
                    ".container { text-align: center; }" + 
                    ".container img { width: auto; height: auto; margin-top: 20px; margin-bottom: -10px; }" + 
                    ".container h2 { color: #FF983D; font-size: 36px; }" + 
                    ".container p { font-weight: bold; font-size: 20px; color: #121212; }" + 
                    "p { line-height: 20px; }" + 
                    "#containerCodigo { background-color: #303030; display: inline-block; padding-left: 20px; padding-right: 20px; border-radius: 10px; }" + 
                    "</style>" + 
                    "</head>" + 
                    "<body>" + 
                    "<div class=\"container\">" + 
                    "<img src=\"https://raw.githubusercontent.com/Pedro-VH05/RetroRack/main/src/main/resources/images/retroRack_logo.png\" alt=\"RetroRack Logo\">" + 
                    "<h2>Recuperación de Contraseña</h2>" + 
                    "<p>Hemos recibido una solicitud para recuperar tu contraseña.</p>" + 
                    "<p>Para completar el proceso, por favor ingresa el siguiente código en tu aplicación:</p>" + 
                    "<div id=\"containerCodigo\"><h2>" + codigo + "</h2></div>" + 
                    "<p>¡Gracias por ser parte de RetroRack!</p>" + 
                    "</div>" + 
                    "</body>" + 
                    "</html>";

            // Establecer el contenido del mensaje como HTML
            message.setContent(contenido, "text/html; charset=utf-8");

            // Enviar el correo
            Transport.send(message);
            System.out.println("Correo enviado exitosamente");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
