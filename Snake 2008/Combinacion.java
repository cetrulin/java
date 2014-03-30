package practicas.practica3;

public class Combinacion {
		
  int []valores;
  
  public Combinacion(int [] combinacion){
	  valores = combinacion;
  }
  
  //Generamos el numero aleatorio.
//	double f = Math.random()*8+1;
//	// Convertimos de double a int, mediante un casting para quedarnos con la parte entera
//	int a=(int)f;
//	return a;
//    
//   /*
    public int longitud (){
    	return valores.length;  	
    }
    
    public void imprimir (){
    	//Creamos un for que recorra la longitud del array, para imprimir el array por pantalla.
    	for (int f=0;f<valores.length;f++){
    		System.out.println(valores[f]);
    	}    
    }
	
}
