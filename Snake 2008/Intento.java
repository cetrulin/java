package practicas.practica3;

public class Intento {

	int [] combinacion;
	
	public Combinacion valores=new Combinacion (combinacion);
	
	public int bienColocados; //metodo 
	
	public int malColocados;  //metodo
	
	public  String esGanador(){
		String a;
		if (bienColocados==4){
			a="Combinacion ganadora";
		}
		else a="Combinacion incorrecta";
		return a;
	}
	
	public Intento (int [] k){
		combinacion = k;
	}
	

}
