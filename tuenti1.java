
public class tuenti1 {

	/**
	 * @author Andres Leon Suarez Cetrulo
	 * Bitcoin buy-sell problem for Tuenti Challenge 3 (2013)
	 */
	public static void main(String[] args) {

		/*eg 1
		 int saldo = 2;
		 int bitcoins = 0;
		 int[] b = {1,2,10,4,1,10};*/
		 
		 //eg 2
		 int saldo = 5;
		 int bitcoins = 0;
		 int[] b = {1,2,4,20,5,30,4,25,7};
		
		for(int i = 1; i<b.length; i++){
			if (b[i]>b[i-1]&&saldo>0){
				//buy
				bitcoins = saldo/b[i-1];
				saldo = 0;

			}if(b[i]<b[i-1]&&bitcoins > 0){
				//sell
				saldo = bitcoins*b[i-1];
				bitcoins = 0;
				
			}if(i == b.length-1 && bitcoins > 0){
				//sell
				saldo = bitcoins*b[i];
				bitcoins = 0;
			}

		} System.out.println(saldo);

	}

}
