import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;


/**
 * Algoritmo genetico que resuelve el problema de la particion
 * P1 PARTE_2 ORIENTADA A LA COMPETICION ALGORITMOS GENETICOS Y EVOLUTIVOS UC3M
 * @author Andres Leon Suarez Cetrulo
 */
public class Competicion10 {

	/**
	 * Definimos variables globales
	 */
	static int TAMANYO_POBLACION;
	static int TAMANYO_INDIVIDUO;
	static int EVALUACION;
	static int GENERACION;
	static int SUMATORIO_INDIV;
	static int NUM_SOLUCIONES;
	static double T_EJECUCION;
	
	static double coef_mutacion;
	static double coef_rotacion;
	
	static int poblacion [][];
	static int fitness [];
	static int informacion_genetica [];
	
	/**
	 * Inicializa la poblacion de forma de aleatoria
	 * */
	static void inicializa_rdm () {
		double coef_random = 0;
		
		for(int k = 0; k < poblacion.length; k++){
			for(int i = 0; i < poblacion[0].length ;i++){
				coef_random = Math.random();			
	
				if(coef_random < 0.5)
					poblacion [k][i] = 0;
				else
					poblacion [k][i] = 1;
			}	
		}	
	}
	
	/**
	 * Genera un individuo aleatorio y usa el concepto de distancia hamming para crear los demas repartidos
	 * a distintas distancias de este
	 * */
	static void inicializa_explorando(){
		double coef_random = 0;
		int [] individuo = new int [TAMANYO_INDIVIDUO];
		
		//Inicializamos el individuo desde el que miraremos la distancia hamming
		for(int i = 0; i < individuo.length ;i++){
			coef_random = Math.random();			

			if(coef_random < 0.5)
				individuo [i] = 0;
			else
				individuo [i] = 1;
		}	
		
		
		//Inicializamos la pre_poblacion cambiando n genes al individuo
			int n = 0;
			for(int k = 0; k < poblacion.length; k++){
				for(int i = 0; i < n ;i++){
					coef_random = Math.random()*(TAMANYO_INDIVIDUO-n);			
					poblacion [k]= individuo.clone();
					
					if(individuo[i]==0)
						poblacion [k][(int)coef_random] = 1;
					else
						poblacion [k][(int)coef_random] = 0;
				
				}n++;
				
				if(n == TAMANYO_INDIVIDUO)
					n = 1;				
			}		
	}
	
	/**
	 * Calcula la funcion fitness de cada individuo como la diferencia de los sumatorios de los grupos
	 * El mejor fitness es el menor numero (0 es el optimo)
	 * */
	static void fFitness (){
		
		EVALUACION++;
		
		//Declaramos los sumatorios de los grupos
		int sumatorioA;
		int sumatorioB;

		
		for(int i = 0 ; i < TAMANYO_POBLACION ; i++){
			
			sumatorioA = 0; //0's en el genotipo
			sumatorioB = 0; //1's en el genotipo

			for (int j = 0 ; j < TAMANYO_INDIVIDUO ; j++){
								
				//Obtenemos el resultado de la suma en los nodos
				if(poblacion[i][j]==0)
					sumatorioA += informacion_genetica [j];
				else
					sumatorioB += informacion_genetica [j];			
					
			}fitness [i] = sumatorioA-sumatorioB;
			if(fitness[i]<0)
				fitness[i]*=-1;
		}
	}
	
	/**
	 * Seleccion por ranking. 
	 * @param numero_reemplazo: numero de individuos que se reproduciran en la poblacion
	 * @return pos_mejores Devuelve las posiciones de los elegidos para la reproduccion
	 * */
	public static int[][] ranking (double presion_selectiva){	
		
		int [][] rank = new int [TAMANYO_POBLACION][TAMANYO_INDIVIDUO];
		//int [] pos_rank = new int [TAMANYO_POBLACION];
		double probabilidad = 0;
		int [][] poblacion_ord;
		//int [] fitness_ord;
		int[] ruleta = new int [TAMANYO_POBLACION];
		
		//Presion selectiva de ranking
		double A = presion_selectiva;
		double B = 0;
		double n = TAMANYO_POBLACION/8;

		//Ordenamos segun fitness
		poblacion_ord = ordenar();

		int z = 0;
		for (int i = 0; z < ruleta.length && n >= 0; i++) {
			double g = ((A*(n--)+B)*100);
			//System.out.println(g);
			if ((g%1.0)>=0.5){
				g=(int)((A*(n+1)+B)*100)+1.0;
			}else if(g%1.0>=0){
				g=(int)((A*((n+1))+B)*100);
			}
						
			for (int j = 0; z < TAMANYO_POBLACION && j < g ; j++, z++) {
				ruleta[z] = i;
			}//System.out.println(g+" "+z+"  "+i);
		}
				
		//seleccionamos proporcionalmente segun el rango
		for(int i = 0; i < rank.length;i++){
			probabilidad = Math.random()*(double)TAMANYO_POBLACION;
			if(probabilidad<0)
				probabilidad*=-1;
			rank [i] = poblacion_ord [ruleta[(int) probabilidad]];
			//pos_rank[i] = ruleta[(int) probabilidad];
			//System.out.println(pos_rank[i]);
			
		}return (rank);	
	}

	/**
	 * Seleccion por ruleta
	 * */
	public static int[][] ruleta(){
		
		//Definimos variables necesarias
		int Si = -1; //Simboliza el individuo ganador i en S(i) para el vector S
		int poblacion_intermedia [][] = new int [TAMANYO_POBLACION][TAMANYO_INDIVIDUO];
		
		double fr [] = new double [TAMANYO_POBLACION]; //(fitness relativo = fitness / fitness total)
		double ft = 0; //Mide en cuanto falla la población (fitness total)
		double S [] = new double [TAMANYO_POBLACION]; //vector S
		double K; //numero aleatorio		
		
		ft = 0;			
		//Hallamos el fitness total de la poblacion
		for(int i = 0; i<TAMANYO_POBLACION;i++)
			ft += fitness[i];
							
		//calculamos el fitness relativo para cada individuo
		for(int i = 0; i < TAMANYO_POBLACION ;i++){										
			if(ft!=0)
				fr[i] = (double)fitness[i]/(double)ft;	
			else 
				fr[i] = 0;	
			
		}	
		
		//generamos el vector de n elementos
		S[0] = fr[0];
		for(int i = 1; i < TAMANYO_POBLACION ;i++)							
			S[i] = fr[i]+S[i-1];
		
		int rellena_poblacion = 0;		
		while (rellena_poblacion < poblacion_intermedia.length) {
			
			//Generamos un numero aleatorio
			K = Math.random();
					
			//Buscamos el menor mayor que K en S(i) comprobando que S(i-1) es menor
			if (K < S[0]){
				Si = 0;	
				
			}else{
				for (int i = 1; i < S.length; i++) {

					if (K <= S[i] && K > S[i-1]){
						Si = i;
						i = S.length;
						
					}else if(i == S.length-1){			
						Si = i;	
					}
				}
			}
			
			//Seleccionamos S(i)
			poblacion_intermedia [rellena_poblacion] = poblacion[Si].clone();
			Si = -1;	
			
			//Pasamos al buscar el siguiente individuo de la poblacion intermedia
			rellena_poblacion++;		
		
		}return (poblacion_intermedia);
	}
	
	public static int[][] ordenar () {
		int aux_i [], aux_j [];
		int aux_k, aux_m;
		int [][] poblacion_ord = poblacion.clone();
		int [] fitness_ord = fitness.clone();
		
		for (int i = 0 ; i<fitness_ord.length ; i++){
			for (int j = i + 1 ; j < fitness_ord.length ; j++){
				if (fitness_ord[i]>fitness[j]){
					aux_i = poblacion_ord[j];
					aux_j = poblacion_ord[i];
					aux_k = fitness_ord[j];
					aux_m = fitness_ord[i];
					poblacion_ord[i] = aux_i;
					poblacion_ord[j] = aux_j;
					fitness_ord[i] = aux_k;
					fitness_ord[j] = aux_m;					
				}
			}
		}return (poblacion_ord);
	}
	
	/**
	 * Desordena la poblacion tras el ranking por medio del algoritmo Fisher–Yates shuffle
	 * */
	public static int [][] desordena_poblacion(int[][] pob){		
		int poblacion_intermedia [][] = poblacion.clone();
		
        for (int i = 0; i < poblacion_intermedia.length; i++) {
            int r = i + (int) (Math.random() * (poblacion_intermedia.length - i));
            int swap []= poblacion_intermedia[r].clone();
            poblacion_intermedia[r] = poblacion_intermedia[i].clone();
            poblacion_intermedia[i] = swap.clone();
            
        }return poblacion_intermedia;
		
	}

	/**
	 * Seleccion por torneo
	 * */
	public static int[][] torneo(double presion_selectiva){
		int mayor = 0;
		int pos_mayor = 0;		
		int poblacion_intermedia [][] = new int [TAMANYO_POBLACION][TAMANYO_INDIVIDUO];
		int torneo [][] = new int [(int)presion_selectiva][TAMANYO_INDIVIDUO];		
		int fitness_torneo [] = new int [(int)presion_selectiva];
		double rdn = 0;

		for (int rellena_pob = 0;rellena_pob<poblacion_intermedia.length;rellena_pob++){
			for(int i = 0; i<torneo.length;i++){
				rdn = Math.random()*TAMANYO_POBLACION;
				torneo [i] = poblacion[(int)rdn].clone();
				fitness_torneo[i]= fitness [(int)rdn];
				
				if(i==0){
					mayor=fitness_torneo[i];
				
				}else{
					if(fitness_torneo[i]>mayor){
						mayor=fitness_torneo[i];
						pos_mayor=i;
					}
				}			
			}poblacion_intermedia [rellena_pob] = torneo[pos_mayor].clone();		
		}return poblacion_intermedia; 	
	}
	
	/**
	 * Cruce
	 * @param porcent: nos dice en que lugar se hace el cruce multipunto.
	 * @return 
	 * */
	public static int[][] cruce (int[][] pob,int porcent){
		int [][] pob_intermedia = new int [pob.length][pob[0].length];

		for (int i = 0, j = pob.length-1; i < pob.length/2; i++, j--) {
			
			int k = 0;
			for(; k<TAMANYO_INDIVIDUO/2 ;k++){
				pob_intermedia[i][k]=pob[i][k];
				pob_intermedia[j][k]=pob[j][k];	
			}

			for(; k<TAMANYO_INDIVIDUO ;k++){
				pob_intermedia[j][k]=pob[i][k];
				pob_intermedia[i][k]=pob[j][k];	
			}
		}		
		return pob_intermedia;	
	}
	
	/**
	 * Cruce multipunto
	 * @param sitios: contiene una array cuyas posiciones son los cortes del cruce
	 * @return 
	 * */
	public static int[][] cruce_multipunto (int[][]pob, int tamanyo_punto){
		int [][] pob_intermedia = new int [pob.length][pob[0].length];
		int cuenta = 0;
		for (int i = 0, j = pob.length-1; i < tamanyo_punto; i++, j--) {
			
			while(cuenta<TAMANYO_INDIVIDUO){
				for(int k = 0; k<tamanyo_punto && cuenta<TAMANYO_INDIVIDUO ;k++,cuenta++){
					pob_intermedia[i][k]=pob[i][k];
					pob_intermedia[j][k]=pob[j][k];	
				
				}for(int k = 0; k<tamanyo_punto && cuenta<TAMANYO_INDIVIDUO;k++,cuenta++){
					pob_intermedia[j][k]=pob[i][k];
					pob_intermedia[i][k]=pob[j][k];	
				}
			}
		}		
		return pob_intermedia;	
	}
	
	/**
	 * Cruce multipunto
	 * @param sitios: contiene una array cuyas posiciones son los cortes del cruce
	 * */
	public static int[][] mutacion (int [][] no_mutada){
		
		double rdn = Math.random();
		
		for(int j = 0; j<no_mutada.length;j++){	
			
			for (int i = 0; i < TAMANYO_INDIVIDUO; i++) {
								
				if(rdn<=coef_mutacion){
					if(no_mutada[j][i]==0)
						no_mutada[j][i]=1;
					else
						no_mutada[j][i]=0;
									
				}rdn = Math.random();
			}

		}if (coef_mutacion > 0.002){	
			coef_mutacion /= (double)1.0001;
		}
		
		return no_mutada;
	}
	
	public static int[][] mut_rotacion (int [][] no_rotada){
		
		double rnd_rota = Math.random();
		
		//Rotacion aleatoria
		//int tamanyo_rotacion = (int)(TAMANYO_INDIVIDUO*coef_mutacion);
		//double empieza_rotacion = Math.random()*(TAMANYO_INDIVIDUO-tamanyo_rotacion);
			
		//Rotacion fija variable
		int tamanyo_rotacion = (int)(TAMANYO_INDIVIDUO*coef_mutacion);
		double empieza_rotacion = (int)(TAMANYO_INDIVIDUO/3);
		
		//Rotacion fija estatica
		//int tamanyo_rotacion = TAMANYO_INDIVIDUO/4;
		//double empieza_rotacion = (int)(TAMANYO_INDIVIDUO/4);
		
		int [][] rota = no_rotada.clone();
		
		for(int j = 0; j<no_rotada.length;j++){				
			for (int i = 0; i < tamanyo_rotacion; i++) {					
				if(rnd_rota<=coef_rotacion){
					rota[j][(int) (empieza_rotacion+tamanyo_rotacion-1-i)] = no_rotada[j][(int)(empieza_rotacion-1)+i];			
				}rnd_rota = Math.random();
			}

		}if (coef_mutacion > 0.002){			
			coef_mutacion /= (double)1.0001;
		}if (coef_rotacion > 0.002){			
			coef_rotacion /= (double)1.0005;
		}
		
		return no_rotada;		
	}	
	
	/**
	 * Ejecucion del algoritmo genetico
	 * */
	public static void main(String[] args) {
		
		
		System.out.println("PRUEBA PARA 10 BITS");
		
		//Definimos las salidas finales del programa
		double GEN_MEDIA = 0;
		int MEJOR = 0;
		double MEJOR_MEDIA = 0;
		double EV_MEDIA = 0;
		double T_MEDIA = 0;
		double SOLUCIONES_MEDIA = 0;
		
		//Definimos auxiliares para las salidas
		int [] MEJORES = new int [10];
		int [] GENERACIONES = new int [10];				
		int [] EVS_MEDIA = new int [10];
		int [] SOLS_ENC =  new int [10];
		double [] TIEMPOS =  new double [10];
		
		for(int ejecucion = 0; ejecucion < 10; ejecucion++){
			
			System.out.println("///////////////////////////////////////////////////////////////////////////////////");
			System.out.println();
			System.out.println();
			System.out.println("EJECUCION NUMERO "+(ejecucion+1));
			System.out.println();
			System.out.println();
			
			//Damos valor a la longitud de cada individuo, a la longitud de la poblacion y al numero de generaciones
			T_EJECUCION = System.currentTimeMillis();
			TAMANYO_POBLACION = 20;
			TAMANYO_INDIVIDUO = 10; //tiene que funcionar min hasta 50 info_dom.length sera como mucho 50
			GENERACION = 0;
			EVALUACION = 0;
			NUM_SOLUCIONES = TAMANYO_POBLACION;
			coef_mutacion = 0.30;
			coef_rotacion = 0.30;
			coef_rotacion = 0.30;
	
			int gen_valida = 0;
			int soluciones_encontradas = 0;
			int soluciones [][]= new int [NUM_SOLUCIONES][TAMANYO_INDIVIDUO+1];
			
			//Inicializamos matrices de poblacion, fitness e informacion genetica
			poblacion = new int [TAMANYO_POBLACION][TAMANYO_INDIVIDUO];
			fitness = new int [TAMANYO_POBLACION];
			informacion_genetica = new int [TAMANYO_INDIVIDUO];
	
			//System.out.println();
			//System.out.print("La informacion de dominio es: ");
			
			//Obtenemos informacion del dominio
			//int info_dom [] = { "copiamos aqui los numeros separados por comas dados en competicion"}; //info_dom para competicion
			int info_dom [] = {771, 121, 281, 854, 885, 734, 486, 1003, 83, 62};
			//int info_dom [] = {1,1,3,4,5,6,7,7,8};
			
			//Pruebas con individuos aleatorios de 50 bits
			/*int info_dom [] = new int [TAMANYO_INDIVIDUO];
			for (int i = 0; i < info_dom.length; i++) {
				info_dom[i]=(int)(Math.random()*15000);
				if(i<info_dom.length-1)
					System.out.print(info_dom[i]+",");
				else
				System.out.print(info_dom[i]);
				
			}System.out.println();*/
			informacion_genetica = info_dom;
			
			//Obtenemos el peor valor posible de un fitness
			for(int i = 0; i < TAMANYO_INDIVIDUO ; i++)
				SUMATORIO_INDIV += informacion_genetica[i];
			//El mejor valor posible es 0
						
			//Inicializamos la poblacion inicial
			//inicializa_rdm();
			inicializa_explorando();
			
			//Hallamos la funcion fitness inicial de cada individuo
			fFitness();
				
			//Definimos una variable para que, si se lleva un numero determinado de generaciones sin encontrar resultado nuevo, se pare
			boolean no_halla_mas = false; 
			
			//Comenzamos con las generaciones hasta un maximo de 200000 si no alcanza oto criterio de parada
			while(soluciones_encontradas == 0 && GENERACION < 200000 && (GENERACION - gen_valida) < 10000 &&! no_halla_mas) {
							
				//Seleccion con ranking
				double top_ranking = (double)1/(double)(TAMANYO_POBLACION*0.8);
				int [][] pob = ranking(top_ranking);
				
				//Seleccion con ruleta
				//int [][] pob = ruleta();
				
				//Seleccion con torneo
				/*int participantes = 4;
				double presion_selectiva = (double)TAMANYO_POBLACION/(double)participantes;
				int [][] pob = torneo(presion_selectiva);*/			
				
				//Desordenamos la poblacion aleatoriamente para que el cruce no sea estatico
				pob = desordena_poblacion(pob); 
	
				//Cruce simple
				poblacion = cruce(pob, TAMANYO_POBLACION).clone();	
				
				//Cruce multipunto
				/*int tamanyo_punto =  TAMANYO_INDIVIDUO/3 ;
				poblacion = cruce_multipunto(pob, tamanyo_punto);*/
				
				//Realizamos la mutacion
				poblacion = mutacion(poblacion.clone());
				
				//Mutacion mediante rotacion
				//poblacion = mut_rotacion(poblacion.clone());
				
				//Hallamos la funcion fitness de cada individuo
				fFitness();
	
				//Almacenamos los resultados obtenidos que no sean iguales en un vector
				for (int j = 0; j < poblacion.length && soluciones_encontradas == 0; j++) {
					
					//Si son resultado, entran
					if(fitness[j]==0 || (SUMATORIO_INDIV%2 == 1 && fitness [j] == 1)) {	
						
						//Miramos que las soluciones no sean repetidas	
						boolean no_cuenta = false;
						for(int k = 0; k < soluciones_encontradas; k++){
							boolean igual = true;
							for(int z = 0; z < TAMANYO_INDIVIDUO; z++){
								if (poblacion[j][z]!=soluciones[k][z]){
									igual = false;
								}
							
							}if (igual){
								no_cuenta = true;
							}			
						}		
						
						//Si la solucion no es repetida, se almacena y se sustituye en la poblacion
						if(!no_cuenta){
							for(int k = 0; k < TAMANYO_INDIVIDUO; k++){
								soluciones [soluciones_encontradas][k] = poblacion[j][k];
							}soluciones [soluciones_encontradas][TAMANYO_INDIVIDUO] = GENERACION;
	
							soluciones_encontradas++;
							gen_valida = GENERACION;
							
							//Cambiamos la solucion correcta por un nuevo individuo para evitar elitismo
							double rdn = Math.random();
							for(int k = 0; k < poblacion[j].length;k++){
								rdn = Math.random();					
								if(rdn < 0.5)
									poblacion [j][k] = 0;
								else
									poblacion [j][k] = 1;							
							}
							
							//hallamos el nuevo fitness de dicho individuo y se cambia en el vector de fitness global
							int sumatorioA = 0; //0's en el genotipo
							int sumatorioB = 0; //1's en el genotipo
	
							for (int k = 0 ; k < TAMANYO_INDIVIDUO ; k++){
												
								//Obtenemos el resultado de la suma en los nodos
								if(poblacion[j][k]==0)
									sumatorioA += informacion_genetica [k];
								else
									sumatorioB += informacion_genetica [k];			
									
							}fitness [j] = sumatorioA-sumatorioB;
							if(fitness[j]<0)
								fitness[j]*=-1;
						}
					}
				}
							
				//Si lleva muchas generaciones sin encontrar soluciones y ya tenemos alguna, paramos
				//Si lleva 20000 generaciones sin encontrar solucion, paramos y devolvemos las mejores
				if((GENERACION - gen_valida == 9999 && soluciones_encontradas > 1) || (GENERACION - gen_valida == 20000)){
					//Si paramos por este criterio, imprimimos el ranking
					no_halla_mas = true;
					int tamanyo_ranking = TAMANYO_POBLACION/4;
					System.out.println(tamanyo_ranking+" soluciones proximas:");
					poblacion = ordenar();
					fFitness();
					for (int z = 0; z < tamanyo_ranking; z++) {
							System.out.print("Nº "+(z+1)+ " Fitness: "+fitness[z]+" individuo: ");				
							for(int j = 0; j < TAMANYO_INDIVIDUO ;j++){
								System.out.print(poblacion[z][j]);
							}System.out.println(" ");			
					}
				}
	
				GENERACION++;
			}
			
			//Imprimimos las soluciones
			System.out.println();
			System.out.println("Numero de evaluaciones hechas: " + EVALUACION);	
			System.out.println("Numero de generaciones hechas: " + GENERACION);
			T_EJECUCION = System.currentTimeMillis()-T_EJECUCION;
			System.out.println("El tiempo de ejecucion ha sido: " + T_EJECUCION + " milisegundos");
			
			if (soluciones_encontradas > 0){
				if(SUMATORIO_INDIV % 2 == 0){
					System.out.println("La mejor solución es 0");
					MEJORES [ejecucion] = 0;
					MEJOR = 0;
				}
				else{
					System.out.println("La mejor solucion es: 1 (el numero es impar)");
					MEJORES [ejecucion] = 1;
					MEJOR = 1;
				}
				
			}else{
				poblacion = ordenar();
				fFitness();
				System.out.println("La mejor solución es "+fitness[0]);
				MEJORES [ejecucion] = fitness[0];
				if (fitness[0]<MEJOR)
					MEJOR = fitness[0];
			}
			
			System.out.println();
			for (int i = 0; i < soluciones_encontradas; i++) {
				System.out.print("La solucion "+i+" es: ");
				for (int j = 0; j < TAMANYO_INDIVIDUO; j++) {
					System.out.print(soluciones[i][j]);
				}System.out.print(" ---> Hallada en la generacion ");
				System.out.println(soluciones[i][TAMANYO_INDIVIDUO]);
			}	
			GENERACIONES [ejecucion] = GENERACION;
			TIEMPOS [ejecucion] = T_EJECUCION;
			EVS_MEDIA [ejecucion] = EVALUACION;
			SOLS_ENC [ejecucion] = soluciones_encontradas;
			
		}
		
		System.out.println();
		
		//IMPRIMIMOS LAS SALIDAS FINALES	
		
		for (int i = 0; i < GENERACIONES.length; i++) {
			GEN_MEDIA += (double)GENERACIONES[i];
			
		}GEN_MEDIA = (double)GEN_MEDIA/(double)GENERACIONES.length;
		
		for (int i = 0; i < MEJORES.length; i++) {
			MEJOR_MEDIA += (double)MEJORES[i];
			
		}MEJOR_MEDIA = (double)MEJOR_MEDIA/(double)MEJORES.length;
		
		for (int i = 0; i < TIEMPOS.length; i++) {
			T_MEDIA += (double)TIEMPOS[i];
			
		}T_MEDIA = (double)T_MEDIA/(double)TIEMPOS.length;

		for (int i = 0; i < EVS_MEDIA.length; i++) {
			EV_MEDIA += (double)EVS_MEDIA[i];
			
		}EV_MEDIA = (double)EV_MEDIA/(double)EVS_MEDIA.length;	

		for (int i = 0; i < SOLS_ENC.length; i++) {
			SOLUCIONES_MEDIA += (double)SOLS_ENC[i];
			
		}SOLUCIONES_MEDIA = (double)SOLUCIONES_MEDIA/(double)SOLS_ENC.length;
		
		System.out.println("///////////////////////////////////////////////////////////////////////////////////");
		System.out.println();
		System.out.println();
		System.out.println("SALIDAS FINALES");
		System.out.println();
		System.out.println();
		
		System.out.println("Num generaciones medio:  "+ GEN_MEDIA);
		System.out.println("Mejor individuo encontrado (en 10 ejecuciones): " + MEJOR);
		System.out.println("Mejor individuo encontrado (de media): " + MEJOR_MEDIA);
		System.out.println("Numero de evaluaciones (de media): " + (EV_MEDIA*TAMANYO_INDIVIDUO));
		System.out.println("Tiempo de ejecución (de media): " + T_MEDIA + " milisegundos");
		System.out.println("Numero medio de soluciones de fitness 0 o 1 (si el sumatorio es impar) halladas para un maximo establecido de 1: "+ SOLUCIONES_MEDIA);
				
	}
}
