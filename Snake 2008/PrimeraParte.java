package practicas.practica3;

//Llamamos a codigos ya creados en java.
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

public class PrimeraParte {
/* Creamos una variable de tipo boolean global para que en caso de que hayamos acertado, se pare y no siga ejecutando el metodo
    y para que en caso contrario, siga ejecutando el metodo.*/
	static boolean solNoAceptada;
	
	//Creamos un metodo para generar un numero aleatorio de n valores.
	public static int generaNumeroAleatorio (int valores){
		// Generamos el numero aleatorio.
		double f = Math.random()*valores+1;
		// Convertimos de double a int, mediante un casting para quedarnos con la parte entera.
		int a=(int)f;
		return a;
		
	}

	//Creamos otro metodo para generar la combinacion de n longitud y n valores.
	public static int [] generaCombinacion (int longitud, int valores){
		//Creamos un array llamdo combinacion con la longitud introducida por: int longitud.
		int[] combinacion=new int [longitud];
		//Recorremos el array de combinacion. Para comprobar que no se repite ningun número de la combinación aleatoria.
		for(int b=0;b<combinacion.length; b++){
			combinacion [b]=generaNumeroAleatorio(valores);
			for(int c=0; c!=b; c++){
				if (combinacion [b]==combinacion [c]){
					b=0;
					break;
					}						
			}	
		}
		//Se devuelve la combinacion creada.
		return combinacion;
	}
	
	public static void imprimeCombinacion (){
		//Leemos el teclado.
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		String cadena = null;
		//Leemos la cadena, y en caso de que no se introduzca nada imprimira: Excepcion al leer numero de teclado.
		try{
			cadena = br.readLine();
			int i = Integer.parseInt(cadena);
			System.out.print("El numero introducido es: " + i);
		} 
		catch(Exception e) {
			System.out.print("Excepcion al leer numero de teclado");
		}
	}
	
	//Creamos un método que compruebe que la combinación que introduce el usuario es correcta.
	public static int [] recibeCombinacion(int longitud, int valores){
		//Leemos el teclado.
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		String cadena = null;
		try{
		    cadena = br.readLine();
		   //Si recibe por teclado la letra 's' se saldra del juego. 
		    if(cadena.length()==1){
		    	if(cadena.equals("s")){
		    		System.out.println("Se ha salido del juego");
		    		System.exit(-1);
		    		
		    	}
		    }
		    //Si la cadena es demasiado larga, imprimira: Numero demasiado largo, introduzca solo (longitud) cifras. Longitud es el numero que nosotros le introducimos.
		    if(cadena.length()>longitud){
				System.out.println("Numero demasiado largo, introduzca solo "+longitud+" cifras");
				//Al ser true el metodo no se para y sigue ejecutandose.
				solNoAceptada = true;
		    }
		}
		//En caso de que no se introduzca nada imprimira: Excepcion al leer numero de teclado.
			catch(Exception e) {
			System.out.println("Excepcion al leer numero de teclado");
			}				
	    int []a=new int [longitud];
		try{
			int valor1 = 0;
			int valor2 = 0;
			
			for(int b=0;b<longitud;b++){
				if(b>0)
					valor2 = valor1;

		        a[b]=Integer.parseInt(String.valueOf(cadena.charAt(b)));
		        //Si introducimos valores que no esten comprendidos entre el 1 y el 8, imprimira: Solo se aceptan digitos del 1 al 8.
			   if(a[b]>valores){
					System.out.println("Solo se aceptan digitos del 1 al 8");
					solNoAceptada = true;
			   }
			   //Si al introducir valores repetimos los números, imprimira: No se admiten numeros repetidos.
			    valor1 = a[b];
			    if(valor1 == valor2 && !solNoAceptada){
					System.out.println("No se admiten numeros repetidos");
					solNoAceptada = true;
			    }
		    }
		}
		//En caso de que no se introduzca nada imprimira: Excepcion al leer numero de teclado.
		catch(Exception e) { 
				System.out.println("Excepcion al leer numero de teclado");
			}
		return a;
	}

	//Creamos un metodo que compruebe la combinación propuesta.
	public static boolean compruebaCombinacion (int[] a,int[] b){
		boolean [] d=new boolean [4];
		//Número de valores bien colocados.
		int e=0;
		//Número de valores mal colocados.
		int i=0;
		//Creamos un bucle que recorra el array y lo comparamos con la combinación correcta, para saber cuantos valores están bien colocados.
		for (int c=0;c<d.length;c++)
			if(a[c]==b[c]){
				e++;
				d[c]=true;
			}
		//Creamos un bucle que recorra el array y lo comparamos con la combinación correcta, para saber cuantos valores están mal colocados.
		for (int f=0;f<a.length;f++){
			for (int g=0;g<b.length;g++){
				//Si la el numero creado por la maquina es igual al numero del jugador, siempre que no esten en la misma posicion.
				if(a[f]==b[g] && f!=g){
					i++;}
				}
			}
	//Imprimimos el númer de valores que están bien colocados y los que están mal colocados.
		System.out.println("Tienes "+e+" bien colocados y "+i+" mal colocados.");
		//Si los 4 valores están bien colocados, el usuario ha ganado, y se imprimira:¡Enhorabuena! Ha ganado la partida..
		if (e==4){
			System.out.println("¡Enhorabuena! Ha ganado la partida.");
			//Para saber la solucion hace falta una variable String ya que sino nos imprimiria la posicion del array.
			String solucion= " ";
			for(int f = 0; f< b.length; f++){
				solucion += b[f];
			}
			//Cuando el usuario ha ganado, se imprime por pantalla la combinación correcta.
			System.out.println ("La combinacion correcta era: "+solucion);
			return true;
		}
		//Si los 4 valores no están bien colocados, se continua jugando.
		return false;
	}

	public static void jugar (int longitud,int max,int intentos){
		//Creamos un array llamado combinacionSecreta, y lo igualamos a la combinación generada por el ordenador.
		int[] combinacionSecreta=generaCombinacion(longitud,max);
		//Creamos una variable, que representara el número de intentos.
		int k=0;
		while(k<intentos){
			//Imprimimos el número de intentos, y a continuación dejamos que el usuario introduzca el número.
			System.out.println("-Intento "+(k+1) +":  Introduce los numeros  (o pulsa 's' para salir del juego):");
			int[] combinacionUsuario=recibeCombinacion (longitud,max);
			//Comprobara si el usuario ha ganado y si lo ha hecho, dejara de ejecutar el codigo.
			boolean win = false;
			if(!solNoAceptada){
				win = compruebaCombinacion(combinacionSecreta,combinacionUsuario);
			}
			solNoAceptada = false;
			if(win){
				break;
			}
			//Si el usuario no ha ganado el numero de intentos se incrementa.
			k++;
		}
		//Si se supera el número máximo de intentos, se imprimira:Se ha superado el numero maximo de intentos. Y se terminara el juego.
		int b=intentos;
		if (k==b){
			System.out.println("Se ha superado el numero maximo de intentos");
			String solucion= " ";
			//Imprimimos la combinación correcta generada por el ordenador.
			for(int i = 0; i < combinacionSecreta.length; i++){
				solucion += combinacionSecreta[i];
			}
			System.out.println("La combinacion correcta era: "+solucion);
		}
	}

	public static void main(String[] args) {
		//Introducimos por parametros.
		int longitud=Integer.parseInt(args[0]);
		int maximo=Integer.parseInt(args[1]);
		int intentos=Integer.parseInt(args[2]);
		jugar(longitud,maximo,intentos);
		
		

		}
		

	}


