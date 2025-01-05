package utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

	/**
	 * 
	 * @param plainPassword
	 * @return
	 */
	public static String hashPassword(String plainPassword) {
		return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
	}

	/**
	 * 
	 * @param plainPassword
	 * @param hashedPassword
	 * @return
	 */
	public static boolean verifyPassword(String plainPassword, String hashedPassword) {
		return BCrypt.checkpw(plainPassword, hashedPassword);
	}
}
