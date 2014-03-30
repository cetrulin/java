import java.util.LinkedList;


/**
 * Algoritmo que resuelve el problema de la particion mediante fuerza bruta por distribucion aleatoria
 * P1 PARTE_1 ALGORITMOS GENETICOS Y EVOLUTIVOS UC3M
 * @author Andres Leon Suarez Cetrulo
 */
public class A_Estocastico {

	static int poblacion [] = {1,1,3,4,5,6,7,7,8};
	static int sumatorioA = 0, sumatorioB = 0;		
	static LinkedList <LinkedList>arbol = new LinkedList<LinkedList>(); 
	static LinkedList <Integer>nodoA = new LinkedList<Integer>(); 
	static LinkedList <Integer>nodoB = new LinkedList<Integer>(); 

	static void genera_nodos () {
		
		double coef_random = 0;
		arbol.add(nodoA);
		arbol.add(nodoB);
		
		do{
			
			//Reseteamos los nodos
			nodoA.clear();
			nodoB.clear();
			
			//Reseteamos el valor de los sumatorios
			sumatorioA = 0;
			sumatorioB = 0;
			
			//Distribuimos los numeros en los nodos de forma aleatoria
			for(int i = 0; i < poblacion.length ; i++){				
				//Elegimos de forma estocastica
				coef_random = Math.random();			
				if(coef_random < 0.5)
					nodoA.add(poblacion[i]);
				else if (coef_random > 0.5)
					nodoB.add(poblacion[i]);
				else{
						i--;
						continue;
					}		
			}
			
			//Obtenemos el resultado de la suma en los nodos
			for(int i = 0; i < nodoA.size();i++)
				sumatorioA += nodoA.get(i);
			for(int i = 0; i < nodoB.size();i++)
				sumatorioB += nodoB.get(i);
			
		}while(sumatorioA!=sumatorioB);
		
	}
	
	public static void main(String[] args) {

		//Distribuimos los nodos
		genera_nodos();	
		
		//Imprimimos el resultado
		System.out.println("El nodo inicial es: ");
		
		System.out.print("[");
		for(int i = 0; i < poblacion.length ;i++){
			System.out.print(poblacion[i]);
			if(i<poblacion.length-1){
				System.out.print(", ");
			}
		}System.out.println("]");
		
		System.out.println("");
		System.out.println("La primera solucion encontrada es: ");
		System.out.println(arbol);
		System.out.println("");
		System.out.println("La suma de los valores de cada nodo es: ");
		System.out.println("nodoA --> "+ sumatorioA);
		System.out.println("nodoB --> "+ sumatorioB);
	}

}
