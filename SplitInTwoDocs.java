import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.LinkedList;


/**
 * @descripcion Convierte los datasets Weather y Elec2 en Weather y Elec2 Chunks.
 * @author Andres Leon Suarez Cetrulo
 * */

public class SplitInTwoDocs {

	static LinkedList<String> pares = new LinkedList<String>();
	static LinkedList<String> impares = new LinkedList<String>();

	public static void leyendo(String a) throws IOException
	{	
	      File archivo = null;
	      FileReader fr = null;
	      BufferedReader br = null;
	      
	      try {
	    	  
	         archivo = new File (a);
	         fr = new FileReader (archivo);
	         br = new BufferedReader(fr);

	         // Lectura del fichero
	         String lineaLeida;
	         for(int i = 0;(lineaLeida=br.readLine())!=null; i++)
	         { 
	        	 
	        	 if(i%2==1){//linea par (como empieza numerando por 0, se supone que 0 es fila 1 = impar)
	        		 pares.add(lineaLeida);
	        	 }else{//linea impar
	        		 impares.add(lineaLeida);
	        	 }

	         }
	         
	      }
	      catch(Exception e){
	         e.printStackTrace();

	      }
	}
	
	public static void escribiendo(String linea, String r) throws IOException
	{    
		
		FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
        	//Impresion del fichero
            fichero = new FileWriter(r);
            pw = new PrintWriter(fichero);   
            
        	if(linea.equals("pares")){
        		for(int i = 0; i < pares.size();i++){
                    pw.println(pares.get(i));

        		}
        		
        	}else if(linea.equals("impares")){
        		for(int i = 0; i < impares.size();i++){
                    pw.println(impares.get(i));

        		}
        	}
         
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
	
    public static String elegirRuta()throws IOException
    {
    	BufferedReader elijo= new BufferedReader(new
    			InputStreamReader(System.in));
    	String e=elijo.readLine();

    	return e;
    	
    }
	
	public static void main(String[] args) throws IOException {
		
		System.out.println("Insertar la ruta de lectura de la lista" +
  		" con formato 'unidadDeDisco:\\nombre.txt'");
		
		String lectura=elegirRuta();		
		leyendo(lectura);
		
		System.out.println("Insertar la ruta de escritura de filas impares" +
		  		" con formato 'unidadDeDisco:\\nombre.txt'");
		String impares=elegirRuta();
		escribiendo("impares",impares);

		System.out.println("Insertar la ruta de escritura de filas pares" +
		  		" con formato 'unidadDeDisco:\\nombre.txt'");
		String pares=elegirRuta();
		escribiendo("pares",pares);

	}
	
}
