import java.nio.charset.Charset;

/**Clase que anyade reinas*/
public class Juega {
	
	int reinasEnTablero;
	static int MAXPERMITIDO, DIMENSIONESTABLERO;
	boolean come;
	
	//En posDeReina se refleja la posición de cada reina de la forma x,y siendo x e y las coordenadas en la matriz tablero
	String posDeReina[] = new String [MAXPERMITIDO];
	
	Juega (int max, int dim){
		MAXPERMITIDO = max;
		DIMENSIONESTABLERO = dim;
		posDeReina = new String [max];
		come = false;
		reinasEnTablero = 0;
	}
		
	void anyadeReina(){
		//La primera reina se anyade aleatoriamente
		if(reinasEnTablero != 0){
			if(reinasEnTablero == MAXPERMITIDO){
				System.out.println("Ya esta en tablero el numero maximo de reinas deseado");
				System.exit(-1);
			}
			else{
				colocaReina(0);//si no come --> colocar reina
			}
		}
		else{		
			int posX=(int)(Math.random()*(DIMENSIONESTABLERO));
			int posY=(int)(Math.random()*(DIMENSIONESTABLERO));
			posDeReina[0] = posX + "," + posY;
			//test System.out.print("REINA COLOCADA EN LA POS: "+posDeReina[reinasEnTablero]+" AHORA HAY ");
			++reinasEnTablero;
			//test System.out.println(reinasEnTablero+" REINA EN EL TABLERO");
		}
	}
	
	/**
	 * Coloca la nueva reina en el tablero
	 * @var posX, posY (posiciones x e y aleatoria de la reina nueva)
	 * @var auxX, auxY (auxiliares que almacenan las posiciones x e y de las reinas ya colocadas con fin de que no se coman)
	 * */
	void colocaReina(int numIntentos){	
		//Definimos variables posicionales
		int posX=(int)(Math.random()*(DIMENSIONESTABLERO)), posY=(int)(Math.random()*(DIMENSIONESTABLERO)), auxX=0, auxY=0;
			
		//Bucle que recorre las posiciones que contienen reinas para que la reina colocada no las coma
		for(int i = 0 ; i < reinasEnTablero && come == false; i++){
			String aux1 = posDeReina[i].charAt(0)+"";
			auxX = Integer.parseInt(aux1);
			String aux2 = posDeReina[i].charAt(2)+"";
			auxY = Integer.parseInt(aux2);
			
			if((auxX==posX)||(auxY==posY))
				come = true;
			
			//diagonales	
			int n1 = 0;
			int n2 = 0;
			
			if(posX < auxX){
				if(posY < auxY){
					n1 = auxX - posX;
					n2 = auxY - posY;
				}
				else{
					n1 = auxX - posX;
					n2 = posY - auxY;	
				}
			}

			else{
				if(posY < auxY){
					n1 = posX - auxX;
					n2 = auxY - posY;
				}
				else{
					n1 = posX - auxX;
					n2 = posY - auxY;
				}
			}
			
			if(n1 == n2){
				come = true;
			}
			
		}			
		numIntentos++;
		
		if(!come){
			posDeReina[reinasEnTablero] = posX + "," + posY;
			//test System.out.print("REINA COLOCADA EN LA POS: "+posDeReina[reinasEnTablero]+" AHORA HAY ");
			++reinasEnTablero;
			//test System.out.println(reinasEnTablero+" REINAS EN EL TABLERO");
		}
		else{
			come = false;
			if(numIntentos==(MAXPERMITIDO*MAXPERMITIDO)){
				//Backtracking 1
				eliminaReina(posX,posY);
				colocaReina(0);					
			}			
			else
				colocaReina(numIntentos);
		}
	}
	
	/**Elimina la ultima reina introducida*/
	void eliminaReina(int posX, int posY){
		//Retrodecemos a la posicion anterior;
		--reinasEnTablero;
		
		//test System.out.print("eliminando..         POS: "+posDeReina[reinasEnTablero]+" AHORA HAY ");

		//Podemos una entrada nula en la posicion
		posDeReina[reinasEnTablero] = null;
		
		//test System.out.println(reinasEnTablero+" REINAS EN EL TABLERO");
	}
	
}
