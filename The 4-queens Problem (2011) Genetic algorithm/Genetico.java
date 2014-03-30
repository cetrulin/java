import java.util.Random;


public class Genetico {

	static int POBLACION;
	static int n;	
	static int CICLO;
	static boolean sol = false;
	static int posicionSol = 0;
	static int indexReina[][];
	static int indexFitness [];
	static int numeroReproducciones; //numero de veces que se realizara el cruce en cada generacion
	
	public static boolean primeraGeneracion(){	
		//Iniciamos con una primera generacion aleatoria de poblacion = 150
		for ( int j = 0 ; j < indexReina.length ; j++ ){
			for( int i = 0 ; i < n ; i++ ){
				indexReina[j][i] = (int)(Math.random()*(n));// fila i / columna random
			}
			indexFitness [j] = calculaColisiones (indexReina[j]);
			if(indexFitness [j] == 0){
				posicionSol = j;
				CICLO++;
				return true;
			}
		}
		return false;
	}
	
	public static void ordenar () {
		int aux_i [], aux_j [];
		int aux_k, aux_m;
		for (int i = 0 ; i<indexFitness.length ; i++){
			for (int j = i + 1 ; j < indexFitness.length ; j++){
				if (indexFitness[i]>indexFitness[j]){
					aux_i = indexReina[j];
					aux_j = indexReina[i];
					aux_k = indexFitness[j];
					aux_m = indexFitness[i];
					indexReina[i] = aux_i;
					indexReina[j] = aux_j;
					indexFitness[i] = aux_k;
					indexFitness[j] = aux_m;					
				}
			}
		}
	}
	
	public static void cruzar () {
		int aux_1[] = new int [(indexReina[0].length)], aux_2[] = new int [(indexReina[0].length)];
		//Cruza por único punto al 50%
		for(int i = indexReina.length-1,cuentaRep=0;cuentaRep<numeroReproducciones&&i>0;i-=2,cuentaRep++){
			
			//int padre = ruleta (indexFitness, i);
			//int madre = ruleta (indexFitness, i-1);
			
			int padre = torneo ();
			int madre = torneo ();
			int a=0,b=0;
			a=((indexReina.length*((int) Math.random()*(n-1)+1)));
			b=((indexReina.length*((int) Math.random()*(n-1)+1)));
			
			while(a>b){
				b=((indexReina.length*((int) Math.random()*(n-1)+1)));
			}
			
			if(indexFitness[i]!=0&&indexFitness[i-1]!=0){							
				for(int k = 0 ; k<indexReina[i].length ; k++){
					
					if (k<a/b){
						aux_1 [k] = indexReina[padre][k];
						aux_2 [k] = indexReina[madre][k];
					}
					else{
						aux_1 [k] = indexReina[madre][k];					
						aux_2 [k] = indexReina[padre][k];
					}
				}
				
				//Fase de transposicion
				/*int fila1 = (int)Math.random()*n;
				int fila2 = (int)Math.random()*n;		
				int aux = aux_1[fila1];
				aux_1[fila1] = aux_1[fila2];
				aux_1[fila2] = aux;
							
				fila1 = (int)Math.random()*n;
				fila2 = (int)Math.random()*n;			    
				aux = aux_2[fila1];
				aux_2[fila1] = aux_2[fila2];
				aux_2[fila2] = aux;*/
				
				indexReina[i] = aux_1;
				indexReina[i-1] = aux_2;
				
				//mutar 2% (cambio aleatorio de 1 reina el 2% de los 'nacimientos'). 
				for(int j=0;j<n;j++){
					if(Math.random()<0.2){
						indexReina[i][j] = (int)(Math.random()*(n));//mutar(indexReina[i]);
					}
					if(Math.random()<0.2){
						indexReina[i-1][j] = (int)(Math.random()*(n));//mutar(indexReina[i-1]);
					}
				}
			}
		}
	}
	
	public static int torneo(){
		int t = (int) (0.15*POBLACION);
		int grupo_index [] = new int [t];
		int grupo_posiciones [] = new int[t];
		int mayor = 0;
		int pos_mayor = 0, pos_ganador = 0;
		for(int i = 0; i<t;i++){
			int rand = (int)(Math.random()*(n));
			grupo_index[i]= indexFitness [rand];
			grupo_posiciones [i] = rand;			
			if(i==0){
				mayor=grupo_index[i];
			}
			else{
				if(grupo_index[i]>mayor){
					mayor=grupo_index[i];
					pos_mayor=i;
				}
			}
		}
		//System.out.println("pos_mayor: "+pos_mayor+" long array: "+grupo_posiciones.length+" t "+t);
		pos_ganador = grupo_posiciones[pos_mayor];
		return pos_ganador;
		
	}
	
	public static int ruleta(int arr_fit [], int rango){
		int fit_total = 0, id_menor = 0, identificadores [] = new int [rango];
		double fitness_rel [] = new double [rango], rand = (Math.random()), 
		fit_acumulado []=new double [rango], almacen [] = new double [rango], menor = 0.0;
		
		for(int i = 0; i<rango;i++){
			fit_total += arr_fit[i];	
		}

		for(int j = 0; j<rango;j++){
			if(fit_total!=0){
				fitness_rel[j] = (double)arr_fit[j]/(double)fit_total;
			}
			
			if(j!=0)
				fit_acumulado[j]=fitness_rel[j]+fit_acumulado[j-1];
			else
				fit_acumulado[0]=fitness_rel[0];

		}
		for(int k = 0, cont = 0; k<rango;k++){
			if(fit_acumulado[k]>rand){
				almacen[cont] = fit_acumulado[k];
				identificadores[cont] = k;
				cont++;
			}
		}
		for(int k = 0; k<rango;k++){
			if (k==0){
				menor = almacen[k];
				id_menor = 0;                
			}
			else{
				if (almacen[k]<menor){
					menor = almacen[k];
					id_menor = k; 
				}
			}
		}

		return identificadores[id_menor];//envia el numero ganador de la matriz de la poblacion
		
	}

	
	public static int calculaColisiones (int [] matriz){
		int colision = 0;
		//int [] auxCol = new int [matriz.length];

		//colisiones columnas
		/*for(int i = 0 ; i < matriz.length ; i++){
			auxCol[matriz[i]]++;
			if(auxCol[matriz[i]]>1)
				colision++;
		}
		
		//colisiones diagonales
		for(int i = 0 ; i < matriz.length ; i++){
			boolean diagonalIzq = false, diagonalDer = false;
			
			if(i!=matriz.length-1){
				for(int j = i+1, k = 1; j<n;j++, k++){
					if(j==0){
						if(!diagonalDer&&matriz[i]+k==(matriz[j])){	
							diagonalDer = true;
							colision++;
						}
					}
					else if(j==(n-1)){
						if(!diagonalIzq&&matriz[i]-k==(matriz[j])){
							diagonalIzq = true;
							colision++;
						}
					}
					else{
						if((!diagonalDer&&matriz[i]+k==matriz[j])){
							diagonalDer = true;
							colision++;
						}
						if((!diagonalIzq&&matriz[i]-k==matriz[j])){
							diagonalIzq = true;
							colision++;
						}
					}
					if(diagonalDer&&diagonalIzq)
						j=n;
				}
			}
		}*/
		 for (int i=0; i<n-1; i++){  
			 
			 for (int j=i+1; j<n; j++){  
		    
				 if ((matriz[i]==matriz[j]))
						colision++;
	             if(i-j==(matriz[i]-matriz[j]))
	            	 	colision++;
		         if(i-j== -(matriz[i]-matriz[j]))
					 	colision++;
			 }
		 	/*if(i==matriz.length-1){
				 for (int j=0; j<matriz.length; j++){  
					    
					 if (matriz[i]==matriz[j])
						 colision++;
				 }
			}*/
		 }
		return colision;
	}
	
	public static void imprimir_Solucion(){
		int num_soluciones = 0;
		for(int i = 0;i<indexFitness.length;i++){
			System.out.println(indexFitness[i]);
			if(indexFitness[i]==0){
				num_soluciones++;
			}
		}
		System.out.println("El programa ha encontrado "+num_soluciones+" soluciones");
	}
	
	public static void main(String[] args){		
		//Definimos parametros iniciales
		POBLACION = 30;
		n = 8;
		indexReina= new int [POBLACION][n];
		indexFitness = new int [indexReina.length];
		numeroReproducciones = 10; //ej:25 reproducciones haran que se reemplacen 50 individuos en cada generacion
		
		//Inicializamos la población y calculamos su fitness
		sol = primeraGeneracion();

		//Se repite el algoritmo para el resto de generaciones
		for(;!sol ; CICLO++){
			ordenar();
			cruzar();
			for ( int j = 0 ; j < indexFitness.length ; j++ ){
				indexFitness [j] = calculaColisiones (indexReina[j]);
				if(indexFitness [j] == 0){
					posicionSol = j;
					sol = true;
					j = indexReina.length;
				}			
			}
		}
		
		System.out.println("La solucion se obtiene en el ciclo "+CICLO);
		System.out.println("Y es: ");
		
		for(int i = 0 ; i<n ; i++){
			int j = 0;
			while(j < indexReina[posicionSol][i]){
				System.out.print("X");
				j++;
			}
			if(j==indexReina[posicionSol][i]){
				System.out.print("R");
				if(j==(n-1)){
					System.out.println("");
				}
				j++;
			}
			while(j>indexReina[posicionSol][i]&&j<(n-1)){
				System.out.print("X");
				j++;
			}
			if(j>indexReina[posicionSol][i]&&j<(n))
				System.out.println("X");
			
		}
		//imprimir_Solucion();
	}	
}
