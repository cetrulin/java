package controllers.evolutive;

import java.util.LinkedList;
import java.util.Random;

public class EE1 {

	public int TAMANYO_INDIVIDUO;
	int RELEVANCIA_WAYPOINTS;
	
	public final int ultimasNgen = 30;
	final double Ci = 1.18;
	final double Cd = 0.82;
	public double tasaDeCambio;
	LinkedList<Boolean> listaDeCambios;
			
	public double fitness;
	public double fitness_intermedia;
	public double [] poblacion;
	public double [] pob_intermedia;
	public double [] poblacion_varianzas;
	public double [] varianza_intermedia;
	
	
	public EE1 (){
		TAMANYO_INDIVIDUO = 10;		
		RELEVANCIA_WAYPOINTS = 1500;
		listaDeCambios = new LinkedList<Boolean>();
		tasaDeCambio = 0.0;
		listaDeCambios.add(true);
		
		poblacion = new double [TAMANYO_INDIVIDUO];
		pob_intermedia = new double [TAMANYO_INDIVIDUO];
		fitness = 0.0;	
		fitness_intermedia = 0.0;	
		poblacion_varianzas = new double [TAMANYO_INDIVIDUO];
		
	}
	
	public double[] getIndividuo() {	

		return poblacion;
	}
	
	public double[] getPintermedia(){

		return pob_intermedia;	
	}
	
	public void setFitness(double[] ev){
        //ev [0] = m_game.getWaypoints().size()-m_game.getWaypointsLeft () -> waypoints cruzados
        //ev [1] = m_game.getTotalTime () -> tiempo consumido			
		fitness  = ev[0] * RELEVANCIA_WAYPOINTS + (ev[0]*1000 - ev[1]);
	}
	
	public void setFitIntermedia(double[] ev){
        //ev [0] = m_game.getWaypoints().size()-m_game.getWaypointsLeft () -> waypoints cruzados
		//ev [1] = m_game.getTotalTime () -> tiempo consumido	
		fitness_intermedia = ev[0] * RELEVANCIA_WAYPOINTS + (ev[0]*1000 - ev[1]);
		if(fitness_intermedia < 0)
			fitness_intermedia = 0;
	}
	
	/**
	 * Inicializa la poblacion de forma de aleatoria
	 * */
	public void inicializa_rdm () {	
		
		System.out.println("\nInicializamos algoritmo (1 + 1)-EE:");

		for(int i = 0; i < poblacion.length ;i++){
			if(i>3){
				poblacion [i] = 60*Math.random()+20;
				poblacion_varianzas [i] = 20*Math.random();
				
			}else{
				poblacion [i] = 150*Math.random()+50;
				poblacion_varianzas [i] = 50*Math.random();;
				
			}/*if(Math.random() < 0.5){
				poblacion_varianzas [i] *= -1;
			}*/
		}	
	}
	
	/**
	 * */
	public void mutacion (){	
		
		pob_intermedia = poblacion.clone();
		varianza_intermedia = poblacion_varianzas.clone();
		
		//Decrementamos la varianza siguiendo un esquema gaussiano		
		for(int i = 0;i<varianza_intermedia.length;i++){
			pob_intermedia[i]=pob_intermedia[i]+varianza_intermedia[i];
			if(pob_intermedia[i] < 0)
				pob_intermedia[i] = 0;
		}
	
	}
	
	public void muta_varianzas (){
		
		if(tasaDeCambio > 0.2){
				for (int k = 0; k < poblacion_varianzas.length; k++) 
					poblacion_varianzas[k] = poblacion_varianzas[k] * Ci;												
		}else{		
				for (int k = 0; k < poblacion_varianzas.length; k++)
					poblacion_varianzas[k] = poblacion_varianzas[k] * Cd;	
		}
	}	

	public void reemplazo(double [] pob, double fitness_pob, double [] var){		
		double cambios = 0.0;
			
		if(listaDeCambios.size() >= ultimasNgen)
			listaDeCambios.remove(0);
		
		if (fitness_pob > fitness) {
			poblacion = pob.clone();
			fitness = fitness_pob;
			poblacion_varianzas = var.clone();
			listaDeCambios.add(true);
			
		} else {
			listaDeCambios.add(false);
			
		}for (int k = 0; k <  listaDeCambios.size(); k++) {
			if ((Boolean) listaDeCambios.get(k))
				++cambios;
			
		}tasaDeCambio = cambios / (double)(listaDeCambios.size());
				
	}

}
