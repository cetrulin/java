package practicas.practica3;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

public class intrucciones {

	//valores=8
	public static int generaNumeroAleatorio (int valores){
		// Generamos el número aleatorio.
		double f = Math.random()*valores+1;
		// Convertimos de double a int, mediante un casting para quedarnos con la parte entera
		int a=(int)f;
		return a;
		
	}
	//longitud=4
	public static int [] generaCombinacion (int longitud, int valores){
		int[] combinacion=new int [valores];
		for(int b=0;b<combinacion.length; b++){
			combinacion [b]=generaNumeroAleatorio(valores);
			for(int c=0; c!=b; c++){
				if (combinacion [b]==combinacion [c]){
					b=0;
					break;
					}
										
			}
			
				
		}
		return combinacion;
	}

	
	
	
	public static void main(String[] args) {

		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		String cadena = null;
		System.out.print("Introduce los numeros y pulsa enter: ");
		try{
			cadena = br.readLine();
	int i = Integer.parseInt(cadena);
	System.out.print("El número introducido es: " + i);
		} catch(Exception e) {
			System.out.print("Excepción al leer número de teclado");
			System.exit(-1);
		}
	}
}


