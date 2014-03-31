package controllers.evolutive;

import java.util.LinkedList;
import java.util.Random;

public class EE {

	public int TAMANYO_POBLACION;
	public int TAMANYO_INDIVIDUO;
	int EVALUACION;
	int GENERACION;
	int RELEVANCIA_WAYPOINTS;
	
	final int ultimasNgen = 30;
	final double Ci = 1.18;
	final double Cd = 0.82;
	public double [] tasaDeCambio;
	LinkedList<LinkedList<Boolean>> listaDeCambios;
	
	private int contador_ind;
	private int cInd_intermedia;
	private int contador_fit;
	private int cInd_fitness;
		
	public double fitness [];
	public double fitness_intermedia [];
	public double [][] poblacion_varianzas;
	public double [][] poblacion;
	public double [][] pob_intermedia;
	public double [][] varianza_intermedia;
	
	public double [][] pob_ord;
	public double [][] varianzas_ord;
	public double [] fitness_ord;	
	
	
	public EE (){
		TAMANYO_POBLACION = 20;
		TAMANYO_INDIVIDUO = 10;		
		EVALUACION = 0;
		GENERACION = 0;
		RELEVANCIA_WAYPOINTS = 1500;
		contador_ind = 0;
		cInd_intermedia = 0;
		contador_fit = 0;
		cInd_fitness = 0;
		listaDeCambios = new LinkedList();
		tasaDeCambio = new double [TAMANYO_POBLACION];

		for (int j = 0; j < TAMANYO_POBLACION; j++) {
			LinkedList<Boolean> a = new LinkedList<Boolean>();
			a.add(true);
			listaDeCambios.add(a);
			tasaDeCambio [j] = 1;
		}
		
		poblacion = new double [TAMANYO_POBLACION][TAMANYO_INDIVIDUO];
		pob_intermedia = new double [TAMANYO_POBLACION][TAMANYO_INDIVIDUO];
		fitness = new double [TAMANYO_POBLACION];	
		fitness_intermedia = new double [TAMANYO_POBLACION];	
		poblacion_varianzas = new double [TAMANYO_POBLACION][TAMANYO_INDIVIDUO];
		
		pob_ord = new double [TAMANYO_POBLACION][TAMANYO_INDIVIDUO];
		fitness_ord = new double [TAMANYO_POBLACION];	
		varianzas_ord = new double [TAMANYO_POBLACION][TAMANYO_INDIVIDUO];
	}
	
	public double[] siguienteIndividuo() {	
		if((contador_ind+1)==TAMANYO_POBLACION)
			contador_ind = 0;

		return poblacion[contador_ind++];
	}
	
	public double[] sigInd_intermedia(){
		if((cInd_intermedia+1)==pob_intermedia.length)
			cInd_intermedia = 0;

		return pob_intermedia[cInd_intermedia++];	
	}
	
	public void setFitness(double[] ev){
        //ev [0] -> waypoints cruzados
        //ev [1] -> tiempo consumido
		int fit = (int) (ev[0] * RELEVANCIA_WAYPOINTS + (ev[0]*1000 - ev[1]));
		if (fit < 0)
			fit = 0;
		fitness [contador_fit++] = fit;
		if((contador_fit)==fitness.length)
			contador_fit = 0;
	}
	
	public void setFitIntermedia(double[] ev){
        //ev [0] = m_game.getWaypoints().size()-m_game.getWaypointsLeft () -> waypoints cruzados
		//ev [1] = m_game.getTotalTime () -> tiempo consumido	
		fitness_intermedia [cInd_fitness] = ev[0] * RELEVANCIA_WAYPOINTS + (ev[0]*1000 - ev[1]);
		if(fitness_intermedia [cInd_fitness] < 0)
			fitness_intermedia [cInd_fitness] = 0;
		cInd_fitness++;
		if((cInd_fitness)==fitness_intermedia.length)
			cInd_fitness = 0;
	}
	
	/**
	 * Inicializa la poblacion de forma de aleatoria
	 * */
	public void inicializa_rdm () {	
		
		System.out.println("\nInicializamos algoritmo (NU + LAMBDA)-EE:");

		for(int k = 0; k < poblacion.length; k++){
			for(int i = 0; i < poblacion[0].length ;i++){
				if(i>3){
					poblacion [k][i] = 60*Math.random()+20;
					poblacion_varianzas [k][i] = 20*Math.random();
					
				}else{
					poblacion [k][i] = 150*Math.random()+50;
					poblacion_varianzas [k][i] = 50*Math.random();
					
				}/*if(Math.random() < 0.5){
					poblacion_varianzas [k][i] *= -1;
				}*/
			}	
		}
	}
	
	public void ordenar (double [][] pob, double [][] varianzas, double [] fitness_pob) {
		double aux_i [], aux_d [];
		double aux_j [], aux_f [];
		double aux_k, aux_m;		
		
		for (int i = 0 ; i<fitness_pob.length ; i++){
			for (int j = i + 1 ; j < fitness_pob.length  ; j++){
				if (fitness_pob[i]<fitness_pob[j]){
					aux_i = pob[j];
					aux_j = pob[i];
					aux_d = varianzas[j];
					aux_f = varianzas[i];
					aux_k = fitness_pob[j];
					aux_m = fitness_pob[i];
					pob[i] = aux_i;
					pob[j] = aux_j;
					varianzas[i] = aux_d;
					varianzas[j] = aux_f;
					fitness_pob[i] = aux_k;
					fitness_pob[j] = aux_m;					
				}
			}
			
			pob_ord = pob;
			varianzas_ord = varianzas;
			fitness_ord = fitness_pob;			
		}
	}
	
	
	/**
	 * Seleccion por torneo
	 * */
	public void torneo(double presion_selectiva){
		
		//Limpiamos las poblaciones intermedias
		pob_intermedia = new double [TAMANYO_POBLACION][TAMANYO_INDIVIDUO];
		fitness_intermedia = new double [TAMANYO_POBLACION];
		varianza_intermedia = new double [TAMANYO_POBLACION][TAMANYO_INDIVIDUO];
		
		double mayor = 0;
		int pos_mayor = 0;		
		double torneo [][] = new double [(int)presion_selectiva][TAMANYO_INDIVIDUO];		
		double fitness_torneo [] = new double [(int)presion_selectiva];
		double rdn = 0;

		for (int rellena_pob = 0;rellena_pob<pob_intermedia.length;rellena_pob++){
			for(int i = 0; i<torneo.length;i++){
				rdn = Math.random()*TAMANYO_POBLACION;
				torneo [i] = poblacion[(int)rdn].clone();
				fitness_torneo[i]= fitness [(int)rdn];
				
				if(i==0){
					mayor=fitness_torneo[i];
				
				}else{
					if(fitness_torneo[i]>mayor){
						mayor=fitness_torneo[i];
						pos_mayor=i;
					}
				}			
			}pob_intermedia [rellena_pob] = torneo[pos_mayor].clone();		
		}
	}
	
	/**
	 * Cruce
	 *  (NU,LAMBA)-EE.
	 * @return 
	 * */
	public void cruce (double lambda){
		double [][] pob_lambda = new double [(int)((double)lambda*(double)TAMANYO_POBLACION)][TAMANYO_INDIVIDUO];
		double [][] var_lambda = new double [(int)((double)lambda*(double)TAMANYO_POBLACION)][TAMANYO_INDIVIDUO];
		
		for (int i = 0; i < pob_lambda.length; i++) {
			//Se genera la poblacion intermedia con sobrecruzamiento proporcional al fitness
			for(int k = 0; k<TAMANYO_INDIVIDUO ;k++){
				//pob_lambda[i][k]=(pob_intermedia[i*2][k]+pob_intermedia[i*((int)1/(int)lambda)+1][k])/2;
				//poblacion_varianzas[i][k]=(poblacion_varianzas[i*(int)1/(int)lambda][k]+poblacion_varianzas[i*(int)1/(int)lambda+1][k])/2;

				if(i+1==pob_intermedia.length){
					pob_lambda[i][k]=(pob_intermedia[i][k]+pob_intermedia[0][k])/2;
					var_lambda[i][k]=(poblacion_varianzas[i][k]+poblacion_varianzas[0][k])/2;
					//pob_lambda[i][k]=(pob_intermedia[i*(int)1/(int)lambda][k]+pob_intermedia[0][k])/2;
					//poblacion_varianzas[i][k]=(poblacion_varianzas[i*(int)1/(int)lambda][k]+poblacion_varianzas[0][k])/2;
					}
				else{
					pob_lambda[i][k]=(pob_intermedia[i][k]+pob_intermedia[i+1][k])/2;
					var_lambda[i][k]=(poblacion_varianzas[i][k]+poblacion_varianzas[i+1][k])/2;
					//pob_lambda[i][k]=(pob_intermedia[i*(int)1/(int)(lambda*TAMANYO_POBLACION)][k]+pob_intermedia[i*((int)1/(int)(lambda*TAMANYO_POBLACION))+1][k])/2;
					//poblacion_varianzas[i][k]=(poblacion_varianzas[i*(int)1/(int)(lambda*TAMANYO_POBLACION)][k]+poblacion_varianzas[i*(int)1/(int)(lambda*TAMANYO_POBLACION)+1][k])/2;
				}
			}
		
		}pob_intermedia = pob_lambda;
		varianza_intermedia = var_lambda;	
	}
	
	/**
	 * */
	public void mutacion (){
		
		Random rnd = new Random(System.currentTimeMillis()); 			
		for(int j = 0; j<pob_intermedia.length;j++){	
			
			//Decrementamos la varianza siguiendo un esquema gaussiano		
			for(int i = 0;i<varianza_intermedia[j].length;i++){
				pob_intermedia[j][i]=pob_intermedia[j][i]+rnd.nextGaussian()*varianza_intermedia[j][i];
				if(pob_intermedia[j][i] < 0)
					pob_intermedia[j][i] = 0;
			}
		}
		
		fitness_intermedia = new double [pob_intermedia.length];		

	}
	
	public void muta_varianzas (){
		
		for (int j = 0; j < tasaDeCambio.length; j++) {
			if(tasaDeCambio[j] > 1/5){
					for (int k = 0; k < poblacion_varianzas[0].length; k++) 
						poblacion_varianzas[j][k] = poblacion_varianzas[j][k] * Ci;				
									
			}else{		
					for (int k = 0; k < poblacion_varianzas[0].length; k++)
						poblacion_varianzas[j][k] = poblacion_varianzas[j][k] * Cd;	
			}
		}	
	} 
	
	public void reemplazo(double [][] pob, double [] fitness_pob, double [][] var){
		
		double [][] nueva_pob = new double[TAMANYO_POBLACION][TAMANYO_INDIVIDUO];
		double [][] nueva_var = new double[TAMANYO_POBLACION][TAMANYO_INDIVIDUO];
		double [] nuevo_fit = new double[TAMANYO_POBLACION];
		double cambios = 0.0;

		for (int i = 0, j = 0, h = 0; i < TAMANYO_POBLACION && j < pob.length && h < TAMANYO_POBLACION ; h++) {
			
			if(((LinkedList) listaDeCambios.get(i)).size() >= ultimasNgen)
				((LinkedList) listaDeCambios.get(i)).remove(0);
			
			if ((fitness_pob[j] >= fitness[i]) && j < pob.length) {
						
				nueva_pob[h] = pob[j].clone();
				nuevo_fit[h] = fitness_pob[j];
				nueva_var[h] = var[j];
				
				if(fitness_pob[j] > fitness[i]){
					((LinkedList) listaDeCambios.get(h)).add(true);
					j++;
					
				}if(fitness_pob[j] == fitness[i]){				
					((LinkedList) listaDeCambios.get(h)).add(false);
					i++;
					j++;
				}
				
			} else {
				
				if(h>0){
					if(fitness[i]==nuevo_fit[h-1])
						i++;
					
				}nueva_pob[h] = poblacion[i].clone();
				nuevo_fit[h] = fitness[i];
				nueva_var[h] = poblacion_varianzas[i].clone();
				((LinkedList) listaDeCambios.get(h)).add(false);
				i++;				
			}
				
			cambios = 0;
			for (int k = 0; k < ((LinkedList) listaDeCambios.get(h)).size(); k++) {
				if ((Boolean) ((LinkedList) listaDeCambios.get(h)).get(k)){
					++cambios;
				}
			}tasaDeCambio[h] = cambios / (double)((LinkedList) listaDeCambios.get(h)).size();
		}
		
		poblacion = nueva_pob.clone();
		fitness = nuevo_fit.clone();
		poblacion_varianzas = nueva_var.clone();
		
	}
	
}
