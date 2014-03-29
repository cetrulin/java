import java.util.*;


public class spotifyeasy {
	/**
	 * @author Andres Leon Suarez Cetrulo 01-01-2014 Resolucion de puzzle Spotify
	 */
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		String binary=Integer.toBinaryString(in.nextInt());
		binary=trasposeBinary(binary);
		System.out.println(Integer.parseInt(binary, 2));
	}

	public static String trasposeBinary(String binary){
		String aux = String.valueOf(binary);
		String aux2 = "";
		for(int i = 1; i<=aux.length() ;i++)
			aux2+=aux.charAt(aux.length()-i);
		return aux2;
	}
}
