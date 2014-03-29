
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.LinkedList;


public class TamanyoRelativo {
	
	/**Por Alex Vegas García & Andrés León Suárez Cetrulo 
	 * Programa que calcula el tamño relativo de los 
	 * conteos según LOC version 1.0*/
	
	static LinkedList locMetodo=new LinkedList(); //Creamos Linked List
	
	public static void leyendo(String a) throws IOException
	{	
	      File archivo = null;
	      FileReader fr = null;
	      BufferedReader br = null;
	      String loc="";
	      String m="";
	      double locPorMetodo;
	      
	      try {
	    	  
	         archivo = new File (a);
	         fr = new FileReader (archivo);
	         br = new BufferedReader(fr);

	         // Lectura del fichero
	         String lineaLeida;
	         while((lineaLeida=br.readLine())!=null)
	         { 
	        	 loc=lineaLeida.substring(lineaLeida.indexOf(",")+1,lineaLeida.lastIndexOf(","));
	        	 m=lineaLeida.substring(lineaLeida.lastIndexOf(",")+1);
	        	 locPorMetodo=Double.parseDouble(loc)/Double.parseDouble(m);
	        	 locMetodo.add(locPorMetodo);	
	        	 

	         }
	         
	      }
	      catch(Exception e){
	         e.printStackTrace();

	      }
	}
	
	public static void escribiendo(double MP,double P,double M, double G, double MG, String r) throws IOException
	{    
		
		FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
        	//Impresión del fichero
            fichero = new FileWriter(r);
            pw = new PrintWriter(fichero);
            
            pw.println("MP: "+MP);
            pw.println("P: "+P);
            pw.println("M: "+M);
            pw.println("G: "+G);
            pw.println("MG: "+MG);

        } catch (Exception e) {
            e.printStackTrace();
        } finally { //cierra el fichero

           if (null != fichero)
              fichero.close();
           try {
           } catch (Exception e2) {
              e2.printStackTrace();
           }
        }
        
	}
	
    public static String devolverLocPorMetodo(int i){ //Devuelve un elemento según su posición en la lista
        return String.valueOf(locMetodo.get(i));
    }
    
    public static String elegirRuta()throws IOException
    {
    	BufferedReader elijo= new BufferedReader(new
    			InputStreamReader(System.in));
    	String e=elijo.readLine();

    	return e;
    	
    }
	
    public static void calcular() throws IOException{
    	
    	
    	//operandos
    	int contador=0;
    	double avg=0.0;   	
    	double logar=0.0;
    	double sumatorio=0.0;
    	double varianza=0.0;
    	double desviacionEstandar=0.0;
    	
    	//rangos logaritmicos
    	
    	double MP=0.0;
    	double P=0.0;
    	double M=0.0;
    	double G=0.0;
    	double MG=0.0;
    	
    	while(contador<locMetodo.size()){
    		logar+=Math.log(Double.parseDouble(devolverLocPorMetodo(contador)));
    		contador++;
    		
    	}
    	
    	contador=0;
    	avg=logar/(locMetodo.size());

    	while(contador<locMetodo.size()){
    		sumatorio+=(Math.log(Double.parseDouble(devolverLocPorMetodo(contador)))-avg)
    					*(Math.log(Double.parseDouble(devolverLocPorMetodo(contador)))-avg);
    		
    		contador++;
    	}
    	
    	varianza=sumatorio/locMetodo.size();
    	desviacionEstandar=Math.sqrt(varianza);
    	
    	//definimos rangos
    	
    	MP=avg-2*desviacionEstandar;
    	P=avg-desviacionEstandar;
    	M=avg;
    	G=avg+desviacionEstandar;
    	MG=avg+2*desviacionEstandar;
    	
    	MP=Math.exp(MP);
    	P=Math.exp(P);
    	M=Math.exp(M);
    	G=Math.exp(G);
    	MG=Math.exp(MG);
    	
    	System.out.println("Insertar la ruta de escritura de la lista" +
  		" con formato 'unidadDeDisco:\\nombre.txt'");
    	String A=elegirRuta();
    	escribiendo(MP,P,M,G,MG,A);
    	
    	System.out.println("FIN DE LA EJECUCION");
    	
    }
	
	public static void main(String[] args) throws IOException {
		
		System.out.println("Insertar la ruta de lectura de la lista" +
  		" con formato 'unidadDeDisco:\\nombre.txt'");
		
		String a=elegirRuta();
		
		leyendo(a);
		
		calcular();
	}

}
