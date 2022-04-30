package toolkit;

public class typeCkecker {
	
	public static boolean isInt(String number) {
		
		boolean result = false;
		
		try {
			
			Integer.parseInt(number);
			
			result = true; // reach this -> is int
			
		} catch (Exception e) {
			// Not an int
		}
		
		return result;
		
	}

}
