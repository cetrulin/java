package practicas.practica3;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class InterfazUsuario {
	
	public int esperaEnter(){
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		String cadena = null;
		try{
		    cadena = br.readLine();
		
	}
	public int piensaCombinacion(){
		
		System.out.println("Piensa una combinaci—n de X nœmeros elegidos entre 1 y Z y pulsa enter");
	}
}
