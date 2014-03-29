import java.math.BigInteger;
import java.io.*;

public class TimeMillis {

	public static void main(String[] args) throws IOException {

		BigInteger integer;
		long tIni = 0, tFinal = 0;
		
		byte[] cadena = new byte[50];
						
		for (int i = 0; i < cadena.length; i++) 
			cadena[i] = 1;	
		
		System.out.println("pruebas para longitud de cadena: "+cadena.length);
		tIni = System.currentTimeMillis();
		integer = new BigInteger(cadena);
		tFinal = System.currentTimeMillis();
		System.out.println("Tiempo de construccion: "+(tFinal-tIni));		
		
		for (int probabilidad = 1; probabilidad <= 100; probabilidad+=5) {		
			tIni = System.currentTimeMillis();
			integer.isProbablePrime(probabilidad);
			tFinal = System.currentTimeMillis();
			System.out.println(probabilidad+" "+(tFinal-tIni));
		}
	}
}

