import java.util.Scanner;


public class Formule {
	int opPrinc;
	Formule sousFormule1;
	Formule sousFormule2;
	int numeroDeVariable = -1;
	//Champs d'opPrinc
	public static final int VAR = 0;
	public static final int NEG = 1;
	public static final int CONJ = 2;
	public static final int DISJ = 3;
	
	
	/**Constructeurs**/
	public Formule(int op, int numeroDeVariable){
		if(op == 0 && numeroDeVariable >= 0){
			opPrinc = 0;
			this.numeroDeVariable = numeroDeVariable;
		}
		else throw new MonException("Probleme d'initialisation de variable");
	}
	public Formule(int op, Formule f1){
		if(op == 1 && f1 != null){
			opPrinc = 1;
			this.sousFormule1 = f1;
		}
		else throw new MonException("Probleme d'initialisation de variable");
	}
	public Formule(int op, Formule f1, Formule f2){
		if(f1 != null && f2 != null){
			if(op == 2 || op == 3){
				opPrinc = op;
				sousFormule1 = f1;
				sousFormule2 = f2;
			}
			else throw new MonException("Probleme d'initialisation de variable");
		}
		else throw new MonException("Probleme d'initialisation de variable");
	}
	public Formule(int op, int[]t){
		if(op == 2 || op == 3){
			Formule curseur = new Formule(0, t[0]);
			for(int i = 1; i < t.length; i ++){
				curseur = new Formule(op, curseur, new Formule(0, t[i]));
			}
			opPrinc = curseur.opPrinc;
			sousFormule1 = curseur.sousFormule1;
			sousFormule2 = curseur.sousFormule2;
		}
		else throw new MonException("Probleme d'initialisation de variable");
	}
	
	
	/** Classe toString : renvoit la chaine de caracteres reprÃ©sentant la formule **/
	public String toString(){
		if(opPrinc == 0)
			return "x"+numeroDeVariable;
		if(opPrinc == 1)
			return ("non " + sousFormule1.toString());
		if(opPrinc == 2)
			return "("+ sousFormule1.toString() + " et " + sousFormule2.toString() + ")";
		if(opPrinc == 3)
			return "(" + sousFormule1.toString() + " ou " + sousFormule2.toString() + ")";
		else throw new MonException("Variable opPrinc non conforme");
	}
	
	  /////////////////////////////////////////////////
	 //                    TP2                      //
	/////////////////////////////////////////////////
	
	public static boolean[] affectation (int nbAffectation){
		Scanner sc = new Scanner(System.in);
		boolean[] b = new boolean[nbAffectation];
		for(int i = 0; i < b.length; i ++){
			System.out.println("Valeurs associÃ©es Ã  "+i+" : ");
			b[i] = sc.nextBoolean();
		}
		return b;
	}
	
	public boolean eval(boolean[] s){
		if(isVariable())
			if(numeroDeVariable >= 0 && numeroDeVariable < s.length)
				return s[numeroDeVariable];
			else return false;//Cas des variables qui n'apparaissent pas dans l'affectation
		if(isNegation())
			return !sousFormule1.eval(s);
		if(isConjonction())
			return (sousFormule1.eval(s) && sousFormule2.eval(s));
		if(isDisjonction())
			return (sousFormule1.eval(s) || sousFormule2.eval(s));
		else return false;
	}
	
	public int maxVar(){
		if(isVariable())
			return numeroDeVariable;
		if(isNegation())
			return sousFormule1.maxVar();
		if(isConjonction() || isDisjonction()){
			int a = sousFormule1.maxVar();
			int b = sousFormule2.maxVar();
			if (a >= b)
				return a;
			else return b;
		}
		else return -1;
	}
	
	/**Inititialise un tableau de false de longueur Ã©gale au numeroDeVariable maximal de la formule, en sachant que x0 est une variable**/
	boolean[] initAffectation(){
		boolean[] t = new boolean[this.maxVar() + 1];
		return t;
	}
	
	/**Fonction qui ajoute 1 Ã  un tableau de nombre binaire **/
	static boolean affSuivante(boolean[] t){
		if(t[t.length - 1] == false){
			t[t.length - 1] = true;
			return true;
		}
		else{
			t[t.length - 1] = false;
			for(int i = t.length - 2; i >= 0; i--){
				if(t[i] == false){
					t[i] = true;
					return true;
				}
				else{
					t[i] = false;
				}
			}
			return false;
		}
		
	}
	
	/**Fonction qui calcule si une formule est satisfiable ou non**/
	boolean isSatisfiable(){
		boolean[] t = initAffectation();
		if(this.eval(t))
			return true;
		while(affSuivante(t)){
			if(this.eval(t))
				return true;
		}
		return false;
	}
	
	
	  /**/////////////////////////////////////////////////
	 /**                    TP3                        //
	/**//////////////////////////////////////////////**/
	
	/**Fonction qui renvoit True si la formule est en forme normale de négation **/
	boolean isNnf(){
		/*Alors on va parcourir la fonction de maniere récursive
		 * Si on trouve une négation et que la sous formule est une autre négation : false
		 * Si on trouve une négation et que la sous formule est une conjonction/disjonction : false
		 * Sinon on continue
		 * Si on trouve une variable, alors il n'y a eu aucun soucis : true
		 */
		if(isVariable())
			return true;
		if(isConjonction() || isDisjonction())
			return (sousFormule1.isNnf() && sousFormule2.isNnf());
		if(isNegation()){
			if(sousFormule1.isVariable())
				return true;
			else
				return false;
			
		}
		else return false;
	}
	
	/**Fonction qui applique UNE SEULE fois une des regles de mise en forme normale de négation
	 * Elle l'applique seulement si necessaire à la BASE de la formule donnée
	 */
	
	public Formule appliqueReglesNnf(){
		/*Regle des mise en forme Nnf :
		 * non non X -> X
		 * non(X et Y) -> (non X ou non Y) 
		 * non(X ou Y) -> (non X et non Y) 
		 */
		if(isNegation()){
			if (sousFormule1.isNegation())
				return sousFormule1.sousFormule1;
			if(sousFormule1.isConjonction())
				return new Formule(DISJ, new Formule(NEG, sousFormule1.sousFormule1)
										,new Formule(NEG, sousFormule1.sousFormule2));
			if(sousFormule1.isDisjonction())
				return new Formule(CONJ, new Formule(NEG, sousFormule1.sousFormule1)
										,new Formule(NEG, sousFormule1.sousFormule2));
			else return this;
		}
		else return this;
	}
	
	
	/**Fonction qui retourne une formule équivalent en Nnf**/
	public Formule miseEnNnf(){
		if(isVariable()) //On renvoit directement la sous formule
			return this;
		if(isConjonction() || isDisjonction()){ // On continue a dérouler la formule
			sousFormule1.miseEnNnf();
			sousFormule2.miseEnNnf();
			return this;
		}
		else{//Cas d'une négation, on applique une regle puis on continue à dérouler le programme
			/*On a deux cas :
			 * _Soit on a une négation de variable, donc on renvoit directement this (ça sert de terminaison de recursion)
			 * _Soit on a une négation d'autre chose, dans ce cas on applique les regle puis on renvoit la mise en forme de la fonction créée pour dérouler la nouvelle formule 
			 */
			if(sousFormule1.isVariable())
				return this;
			else{
				return this.appliqueReglesNnf().miseEnNnf();
			}
			
		}
		
	}
	
	/**Fonction revoyant true si la formule est une clause disjonctive**/
	public boolean isClauseDisjonctive(){
		/*Une clause disjonctive est :
		 * _la conjonction de zéro ou plus littéraux
		 * Vérifions ça :
		 */
		if(isVariable())
			return true;
		if(isNegation())
			return sousFormule1.isClauseDisjonctive();
		if(isConjonction())
			return sousFormule1.isClauseDisjonctive() && sousFormule2.isClauseDisjonctive();
		else //Cad d'une disjonction : l'opération interdite dans une clause disjonctive
			return false;
	}
	  /**/////////////////////////////////////////////////
	 /**                    ACCESSEUR                  //
	/**//////////////////////////////////////////////**/
	public Formule getSousFormule1(){
		return sousFormule1;
	}
	public Formule getSousFormule2(){
		return sousFormule2;
	}
	public int getNumeroDeVariable(){
		return numeroDeVariable;
	}
	public boolean isNegation(){
		return (opPrinc == NEG);
	}
	public boolean isConjonction(){
		return (opPrinc == CONJ);
	}
	public boolean isDisjonction(){
		return (opPrinc == DISJ);
	}
	public boolean isVariable(){
		return (opPrinc == VAR);
	}
}
