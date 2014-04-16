/***************************************************************
 * Programa creado por:
 *  @author Vegas Garcia, Alejandro NIA: 100079147
 *  @author Suarez Cetrulo , Andres Leon NIA: 100080038
 *  
 *  @see Redes de Neuronas Artificiales
 *  @see Practica 1 - Problema de Clasificacion
 *  @version 1.0
 ***************************************************************/


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;


public class perceptron_simple {

	/**@param variables estaticas globales*/
	int NUM_COLUMNAS = 61;
	int NUM_CICLOS = 1000;

	public static double random(){
		double aleatorio = (double)Math.random();
		//Aleatorio para el signo
		double aleatorioNegativo = (double)Math.random();
		if (aleatorioNegativo < 0.5)
			aleatorio*=-1;
		return aleatorio;
	}
	
	/**Recorremos el fichero para saber el numero de filas 
	 * a leer y poder definir mas precisamente la matriz*/
	public static int filaFinder (String fichero) throws IOException{
		
		//Inicialmente 0 filas leidas
		int filas = 0;	
		
		//Leemos el fichero
		FileReader fr=new FileReader(fichero);
		BufferedReader bf=new BufferedReader(fr);
		
		while(bf.readLine() != null){
			//Incrementamos numero de filas por linea leida en el fichero
			filas++;
		}
		
		return filas;
	}
	
	public double [][] lecturaFichero(int n_filas , String fichero) throws IOException{
		
		double [][] matrix = new double [n_filas][NUM_COLUMNAS];
		 
		int fila = 0;

	    File archivo = null;
	    FileReader fr = null;
	    BufferedReader br = null;
		
        // Apertura del fichero y creacion de BufferedReader para poder
        // hacer una lectura comoda (disponer del metodo readLine()).
        archivo = new File ("C:\\Test3\\entrenamiento1.txt");
        fr = new FileReader (archivo);
        br = new BufferedReader(fr);

        // Lectura del fichero
        String linea; 
        		
		while((linea=br.readLine())!=null  && fila < n_filas){
			StringTokenizer datos = new StringTokenizer(linea);
			
			for (int i = 0; i < (byte)NUM_COLUMNAS; i++)
				matrix[fila][i]=Double.valueOf(datos.nextToken());	
						
			fila++;
		}
		return matrix;
	}
		
	public double[] entrenamiento(double matrizValores[][], double umbral, int n_filas, double [] matrizPesos){
		
		double suma_total [] = new double [n_filas];
		
		//Bucle que recorre las filas del fichero leidas
		for (int i = 0; i < n_filas; i++) {
			//Bucle que recorre las columnas del fichero leidas
			for (int j = 0; j < NUM_COLUMNAS; j++) {
				if (j < NUM_COLUMNAS - 1)
					suma_total[i] += (matrizValores[i][j] * matrizPesos[j]);

				else {
					suma_total[i] = suma_total[i] + umbral;
					
					if(suma_total[i] > 0)
						suma_total[i] = 1;					
					else
						suma_total[i] = -1;
					
					//Si la suma total no coincide con la columna final, se cambia el umbral y los valores
					if(matrizValores[i][NUM_COLUMNAS-1] != suma_total[i]){
						double aux_pesos[] = matrizPesos;					
						//Ley de Aprendizaje: si x pertenece a C^1, d(X) = 1, entonces...
						
						// ... Wi(t+1) = Wi(t) + d(x) * Xi --> Aprendizaje pesos
						for(int k = 0 ; k < NUM_COLUMNAS-1 ; k++)
							aux_pesos[k] += matrizValores[i][k] * matrizValores[i][NUM_COLUMNAS-1];
						
						matrizPesos = aux_pesos;
						
						// ... u(t+1) = u(t) + d(x) --> Aprendizaje umbral
						umbral += matrizValores[i][NUM_COLUMNAS-1];
					}				
				}
			}
		}
		return suma_total;
	}

    public String leeTeclado()
    {
    	BufferedReader elijo= new BufferedReader(new
    	InputStreamReader(System.in));
    	String e = null;
    	
		try {
			e = elijo.readLine();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

    	return e;
    	
    }
	
    /**Constructor-ejecutor del perceptron*/
    perceptron_simple (){
    	
		  FileWriter fichero = null;
	      PrintWriter pw = null;
        
		  System.out.println("Paso 1: Por favor, seleccione la ruta del fichero de entrenamiento. ");
		  String analizado = leeTeclado();
		  
		  System.out.println("");
		  System.out.println("Paso 2: Introduzca la ruta del fichero de test:");
			
		  String test = leeTeclado();
		  
		  System.out.println("-----------------------------------------------------------------------------");
		  System.out.println("Paso 3: Ingrese el número de columnas totales");
		  System.out.println("Ayuda: Para la entrada correspondiente a la practica del sonar, " +
		  		"el numero de columnas corresponde a 61.");
		  System.out.println("Ayuda: Si no introduce nada, o el formato es incorrecto se tomará 61 como numero por defecto.");
		  
		  
		  
			try {
				NUM_COLUMNAS = Integer.parseInt(leeTeclado());
				while (NUM_COLUMNAS < 10) {
						System.out.println("Por favor, introduzca un numero de columnas mayor.");
						NUM_COLUMNAS = Integer.parseInt(leeTeclado());
				}
				
			} catch (NumberFormatException e) {
				NUM_COLUMNAS = 61;
			}

		  System.out.println("-----------------------------------------------------------------------------");
		  System.out.println("Paso 4: Ingrese el número de ciclos a realizar");
		  System.out.println("Ayuda: Si no introduce nada, o el formato es incorrecto se tomará 1000 como numero por defecto.");
		  
			try {
				NUM_CICLOS = Integer.parseInt(leeTeclado());
				while (NUM_CICLOS < 2) {
						System.out.println("Por favor, introduzca un numero de columnas mayor.");
						NUM_CICLOS = Integer.parseInt(leeTeclado());
				}
				
			} catch (NumberFormatException e) {
				NUM_CICLOS = 1000;
			}
			
	      double [] matrizPesos = new double [60], suma_total, matrizValores [];
		  double p_acierto, umbral;
      	  double m_pAcierto = 0, m_pesos[] = new double[matrizPesos.length], m_umbral = 0;
		  int m_ciclo = 0;
      
		 //Inicializamos los pesos;
		 for( int i = 0 ; i < 60 ; i++ )
	     	 matrizPesos[i]=random();
		 
		 //Inicializamos umbral
		 umbral=random();
		 
		try {
		//Hallamos el numero de filas del fichero
			System.out.println("Paso 5: Elige ruta y nombre para el fichero de salida");
            fichero = new FileWriter(leeTeclado());
            pw = new PrintWriter(fichero);
            
			pw.println("Programa creado por:");
			pw.println(" Vegas Garcia, Alejandro NIA: 100079147");
			pw.println(" Suarez Cetrulo , Andres Leon NIA: 100080038");
			pw.println("");
			pw.println("Redes de Neuronas Artificiales");
			pw.println("Practica 1 - Problema de Clasificacion");			
			pw.println("");
			pw.println("/////////////////////////////////////////////////////////////////////////////");
			pw.println("");

            pw.println("El fichero analizado tiene la ruta: "+analizado);
            pw.println("El numero de columnas escogido es de : "+NUM_COLUMNAS+" columnas");
            
			pw.println("");

			
			int n_filas = filaFinder(analizado);
			matrizValores = lecturaFichero(n_filas, analizado);

			for(int i = 0 ; i < NUM_CICLOS ; i++){
				suma_total = entrenamiento (matrizValores, umbral, n_filas, matrizPesos);
				
				//Hallamos el porcentaje de acierto
				p_acierto = 0;				
				for (int j = 0; j < n_filas; j++)	
					if(matrizValores [j][NUM_COLUMNAS - 1] == suma_total [j])
						p_acierto++;		
				p_acierto /= n_filas;
				
				System.out.println("El porcentaje de acierto del ciclo " + (i+1) + " es de: " + (p_acierto * 100) + "%");				
	            pw.println("El porcentaje de acierto del ciclo " + (i+1) + " es de: "+(p_acierto * 100) + "%");

				//Si el porcentaje de acierto es el mayor, guardamos pesos, umbral y porcentajes.
				if(m_pAcierto < p_acierto){
					m_pAcierto = p_acierto;
					m_umbral = umbral;
					m_ciclo = i;
					
					for (int j = 0; j < m_pesos.length ; j++)
						m_pesos[j] = matrizPesos[j];					
				}
			}
			
			pw.println("");
			pw.println("//////////////////////////////////////////////////////");
			pw.println("");
			pw.println("El numero de filas es: " + n_filas);
			pw.println("El mejor porcentaje es: " + (m_pAcierto * 100) + "%" + " en el ciclo: " + (m_ciclo + 1));
			pw.println("");
			pw.println("Los pesos correspondientes a la mejor solución son: ");
			pw.print(m_pesos[0]);
			
			for(int i = 1; i < m_pesos.length ; i++){
				pw.print(", ");
				pw.print(m_pesos[i]);
			}
			pw.println("");
			pw.println("El umbral correspondiente a la mejor solución es: " + m_umbral);

			System.out.println("//////////////////////////////////////////////////////");
			System.out.println("");
			System.out.println("El numero de filas es: " + n_filas);
			System.out.println("El mejor porcentaje es: " + (m_pAcierto * 100) + "%" + " en el ciclo: " + (m_ciclo + 1));
			System.out.println("");
			System.out.println("Los pesos correspondientes a la mejor solución son: ");
			System.out.print(m_pesos[0]);
			
			for(int i = 1; i < m_pesos.length ; i++){
				System.out.print(", ");
				System.out.print(m_pesos[i]);
			}
			
			System.out.println("");
			System.out.println("El umbral correspondiente a la mejor solución es: " + m_umbral);

			//COMENZAMOS CON EL TEST//
			int n_filas_test = filaFinder(test);
			double valoresTest [][] = new double [n_filas_test][NUM_COLUMNAS];
			
			valoresTest = lecturaFichero(n_filas_test, test);
			
			double[] suma_test = entrenamiento (valoresTest, m_umbral, n_filas_test, m_pesos);
			
			//Hallamos el porcentaje de acierto del test
			double pa_test = 0;				
			for (int j = 0; j < n_filas_test; j++)	{
				if((int)valoresTest [j][NUM_COLUMNAS - 1] == (int)suma_test [j]){
					pa_test++;		
					}
				}
			pa_test /= n_filas_test;

			System.out.println("");
			System.out.println("El porcentaje de acierto del test con ruta " + test + " es de: " + (pa_test * 100) + "%");
			pw.println("");
            pw.println("El porcentaje de acierto del test con ruta " + test + " es de: " + (pa_test * 100) + "%");
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally { //cierra el fichero

	           if (null != fichero)
				try {
					fichero.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
	        }

    }
    
	public static void main(String[] args){
		
		perceptron_simple p = new perceptron_simple();
		
	}

}
