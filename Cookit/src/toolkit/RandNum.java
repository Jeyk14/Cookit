package toolkit;

public class RandNum {
	
	public static String getCode(int length) {
		String code = "";
		String template = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

		for (int i = 0; i < length; i++) {
			code += template.charAt((int) Math.floor(Math.random() * template.length()));
		}

		return code;
	}

}