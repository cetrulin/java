import java.util.LinkedList;


/**
 * Algoritmo por fuerza bruta que resuelve el problema de la particion
 * P1 PARTE_1 ALGORITMOS GENETICOS Y EVOLUTIVOS UC3M
 * @author Andres Leon Suarez Cetrulo
 */
public class A_Bruto {

	static int poblacion [] = {1,1,3,4,5,6,7,7,8};
	static int sumatorioAux1 = 0, sumatorioAux2 = 0;
	static LinkedList <LinkedList>lista_arboles = new LinkedList<LinkedList>(); 
	static LinkedList <LinkedList>arbol = new LinkedList<LinkedList>();  
	static LinkedList <Integer>nodoAux1 = new LinkedList<Integer>(); 
	static LinkedList <Integer>nodoAux2 = new LinkedList<Integer>();

	static void genera_nodos (int num_lista) {	
		
		arbol.clear();
		nodoAux1.clear();
		nodoAux2.clear();
		
		String aux [] = new String [poblacion.length]; //posiciones distribuidas
		for(int i = 0; i<aux.length; i++){
			aux [i] = "";
		}
		
		do{			
			
		/*	//Distribuimos los numeros en los nodos de forma aleatoria
			for(int i = (int)Math.random()*(poblacion.length-1); (nodoAux1.size()+nodoAux2.size())<poblacion.length; i+=(int)(Math.random()*(poblacion.length-1))){
				
				if(!aux[i%num_lista].equals("ya esta")){			
					if(nodoAux1.size() < num_lista)
						nodoAux1.add(poblacion[i%num_lista]);
					else
						nodoAux2.add(poblacion[i%num_lista]);	
				}else{
					i = (int)Math.random()*(poblacion.length-1);
					continue;
				}
				
				aux[i%num_lista] = "ya esta";
			}			
			*/
			arbol.add((LinkedList) nodoAux1.clone());
			arbol.add((LinkedList) nodoAux2.clone());		
			System.out.println(arbol);
			lista_arboles.add(num_lista, (LinkedList) arbol.clone());
			
		}while(sumatorioAux1!=sumatorioAux2);
		
	}
	
	/**
	 * Este metodo devolveria la longitud del resultado en un problema de p partes / p!=2
	 * */
	static int factorial(){
		int fact = 1, cuenta_atras = poblacion.length;
		
		while(cuenta_atras != 0){
			fact += fact * cuenta_atras;
			cuenta_atras--;
		}
		return fact;
	}
	
	static void reordena_poblacion(){		
		int [] aux = new int [poblacion.length];
		int auxiliar = 0;
		int est_1 = (int) Math.random()*(poblacion.length-1);
		int est_2 = (int) Math.random()*(poblacion.length-1);
		
		//rotamos un numero a la derecha en la poblacion
		for(int i = 1; i <= poblacion.length; i++){
			if (i < poblacion.length)
				aux[i-1] = poblacion[i];
			else
				aux[i-1] = poblacion[0];
		}
		
		//hacemos dos permutaciones estocasticas
		auxiliar = aux [est_1];
		aux [est_1] = aux [est_2];
		aux [est_2] = auxiliar;
		
		do{
		est_1 = (int) Math.random()*(poblacion.length-1);
		est_2 = (int) Math.random()*(poblacion.length-1);
		}while(est_1!=est_2);
		
		auxiliar = aux [est_1];
		aux [est_1] = aux [est_2];
		aux [est_2] = auxiliar;
		
		//sustituimos en la poblacion inicial
		poblacion = aux.clone();		
	}
	
	static int encuentra_sol(int num_lista, int num_res){

		//Reseteamos los nodos
		arbol.clear();
		nodoAux1.clear();
		nodoAux2.clear();
		
		sumatorioAux1 = 0;
		sumatorioAux2 = 0;		
		
		//Damos valor a los nodos
		arbol = (LinkedList) lista_arboles.get(num_lista).clone();
		nodoAux1 = (LinkedList) arbol.get(0).clone();
		nodoAux2 = (LinkedList) arbol.get(1).clone();			
				
		//Obtenemos el resultado de la suma en los nodos
		for(int i = 0; i < nodoAux1.size();i++)
			sumatorioAux1 += nodoAux1.get(i);
		for(int i = 0; i < nodoAux2.size();i++)
			sumatorioAux2 += nodoAux2.get(i);
				
		//Vemos si es solucion y en ese caso la sacamos por pantalla
		if(sumatorioAux1==sumatorioAux2){
			num_res++;			
			System.out.println("");
			System.out.println("La solucion numero "+num_lista+" encontrada es: ");
			System.out.println(arbol);
			System.out.println("");
			System.out.println("La suma de los valores de cada nodo es: ");
			System.out.println("nodoA --> "+ sumatorioAux1);
			System.out.println("nodoB --> "+ sumatorioAux2);
		}
		
		return num_res;
		
	}
	
	public static void main(String[] args) {
	
		int grado_bruta = 1;
		
		for(int h = 0; h < grado_bruta; h++){
			//Distribuimos los nodos
			for(int i = 2; i <= poblacion.length ;i++){
				genera_nodos(i);
			}
			//Reordenamos para siguiente distribucion
			//reordena_poblacion();
		}
		
		//Imprimimos
		System.out.println("El nodo inicial es: ");	
		System.out.print("[");
		for(int i = 0; i < poblacion.length ;i++){
			System.out.print(poblacion[i]);
			if(i<poblacion.length-1)
				System.out.print(", ");
		}System.out.println("]");

		//Buscamos resultados
		for(int i = 0, num_res = 0; i < lista_arboles.size() ;i++)		
			num_res = encuentra_sol(i, num_res);

	}
}
