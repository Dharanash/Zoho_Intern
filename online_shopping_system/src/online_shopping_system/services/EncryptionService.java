package online_shopping_system.services;

public class EncryptionService {

	public static String encryptPassword(String password) {
		String hpassword = "";
		for (int i = 0; i < password.length(); i++) {
			char c = password.charAt(i);
			if (c == 'z') {
				c = 'a';
			} else if (c == 'Z') {
				c = 'Z';
			} else if (c == '9') {
				c = '0';
			} else {
				c++;
			}

			hpassword += c;
		}

		return hpassword;
	}

	public static boolean isPasswordMatch(String opassword, String hpassword) {
		String npassword = encryptPassword(opassword);
		if (hpassword.equals(npassword)) {
			return true;
		}

		return false;
	}
}
