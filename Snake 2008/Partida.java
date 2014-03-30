package practicas.practica3;

public class Partida {

	public int valoresPosibles=8;
	
	public int longitud=4;
	
	public int maximosIntentos=10;
	
	public Intento []intentos = new Intento [maximosIntentos];
	
	public int contador;
	
	public int getNumerosIntentos(){
		//Indica el número de intento en el que nos encontramos.
		return contador;
	}
	
	public void añadirIntento (Intento k) {
		intentos[contador] = k;
		contador++;
	}
	
	public String ganada (){
		String b = null;
		if (contador < maximosIntentos && intentos[contador].bienColocados==4){
			b="Combinacion ganadora";
		}
		return b;
	}
	
	public String perdida (int maximosIntentos){
		String b = null;
		if (contador >= maximosIntentos || (contador == maximosIntentos && !(intentos[contador].bienColocados==4))){
			b="Se ha superado el numero maximo de intentos";
		}
		return b;
	}
	
	public int[] generaCombinacion (int longitud){
		int[] combinacion=new int [longitud];

		for(int i = 0; i < combinacion.length; i++){
			combinacion [i]=(int) (Math.random()*8+1);
		}
			
		Intento h = new Intento(combinacion);
		añadirIntento (h);
		
		if(contador == maximosIntentos){
			if(perdida (maximosIntentos).equals("Se ha superado el numero maximo de intentos")){
				System.out.println(perdida (maximosIntentos));
			}
		}
		else{
			if(ganada ().equals("Combinacion ganadora"))
				System.out.println(ganada());
		}
		
		
	
		return combinacion;
	
		
	}
	
}

