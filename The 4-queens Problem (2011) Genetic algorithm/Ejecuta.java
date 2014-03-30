
public class Ejecuta {
	
	static int contadorSol;
	static int NUM_REPETICIONES = 10;
	static String [][] soluciones = new String [NUM_REPETICIONES][4];
	//(while que anyade reinas) y (crea un tablero)
	
	public static void main(String[] args) {
		int max = 4;
		int dim = 4;
		
		while (contadorSol<NUM_REPETICIONES){
			//definimos objeto juego
			Juega juego = new Juega (max,dim);
			
			int iteracion = 0;
			while(juego.reinasEnTablero < max){
				iteracion++;
				//test System.out.println("·intento "+(iteracion));
				juego.anyadeReina();
			}
			
			System.out.println("solucion:");
			for(int i = 0; i < max ; i++){
				System.out.print(juego.posDeReina[i]+" ");
			}
			
			System.out.println();
			contadorSol++;
		}
		//System.out.println("Hay " + contadorSol + " soluciones");
 
	}
	
}
