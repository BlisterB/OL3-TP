
public class Main {
	
	public static void main(String[] arg){
		//Formule non non X
		Formule pasBonne1 = new Formule(1, 
										new Formule(1, 
												new Formule(0, 0)));
		//Formule non(X et Y)
		Formule pasBonne2 = new Formule(1,
										new Formule(2,
												new Formule(0, 0)
												,new Formule(0, 1)));
		//Pas Bonne 3
		Formule pasBonne3 = new Formule(3, 
											new Formule(2, new Formule(0, 1), new Formule(0, 2)),
											new Formule(2, new Formule(0, 3), new Formule(0, 4))
										);
		//Mise en NNF :
				Formule trozarb = new Formule(2, new Formule(1, new Formule(0, 1)),
												new Formule(1, new Formule(3, new Formule(0, 1), new Formule(0,2))));
				
		/*Formule x1 = new Formule(0, 1);
		Formule x2 = new Formule(0, 2);
		Formule x3 = new Formule(0, 3);
		
		Formule form = new Formule(3, new Formule(2, x1, x2), new Formule(1, x3));
		
		boolean[] b = Formule.affectation(3);
		System.out.println("La formule est elle satisfiable ? " + form.isSatisfiable());
		
		Formule formule2 = new Formule(2, new Formule (0, 0), new Formule(1,new Formule(0,0)));
		System.out.println("La formule est : " + formule2.toString() + " Est elle satisfiable ? " + formule2.isSatisfiable() + " Est elle en NFG " + formule2.isNnf());
		*/
		
		
		//System.out.println("La formule est : " + pasBonne1.toString() + " Est elle en NFG " + pasBonne1.isNnf() + "Appliquons lui une regle : " + pasBonne1.miseEnNnf().toString());
		
		
		//System.out.println("La formule est : " + pasBonne2.toString() + " Est elle en NFG " + pasBonne2.isNnf() + "Appliquons lui une regle : " + pasBonne2.miseEnNnf().toString());
		
		
		//System.out.println("La Formule est : "+trozarb.toString()+"\n Est elle en NNF : " + trozarb.isNnf() +"\n Sa forme en NNF est : "+ trozarb.miseEnNnf().toString());
		
		
		//Mise en CNF :
		//pasBonne1
		//System.out.println("La formule est : " + pasBonne1.toString() + " Est elle en CNF " + pasBonne1.isCnf() + "Appliquons lui une regle : " + pasBonne1.miseEnNnf().miseEnCnf().toString());
		//Pas Bonne 2
		//System.out.println("La formule est : " + pasBonne2.toString() + " Est elle en CNF " + pasBonne2.isCnf() + "Appliquons lui une regle : " + pasBonne2.miseEnNnf().miseEnCnf().toString());
		
		System.out.println(pasBonne3.appliqueReglesCnf().toString());
		Formule pasBonne3Corrigee = pasBonne3.miseEnNnf().miseEnCnf();
		System.out.println("La formule est : " + pasBonne3.toString() + " Est elle en CNF : " + pasBonne3.isCnf() + " Appliquons lui une regle : " + pasBonne3Corrigee.toString());
	}
}
