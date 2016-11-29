import java.util.*;

public class GeneratePin{
	public static int getRandomDigit() {
	    return (int)(10.0 * Math.random());
	}
	public static char getRandomChar() {
	    return (char)('A' + (26.0 * Math.random()) );
	}
	public static String getRandomTriple() {
     	return "" + getRandomDigit() + getRandomDigit() + getRandomChar();
    }
    public  String getPin(){
		String pin = getRandomTriple() + "-" + getRandomTriple() + "-" + getRandomTriple() + "-" + getRandomTriple();
		return pin;
	}
}