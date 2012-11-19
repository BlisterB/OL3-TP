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
	
	
	/** Classe toString : renvoit la chaine de caracteres représentant la formule **/
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
			System.out.println("Valeurs associées à "+i+" : ");
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
	
	/**Inititialise un tableau de false de longueur égale au numeroDeVariable maximal de la formule, en sachant que x0 est une variable**/
	boolean[] initAffectation(){
		boolean[] t = new boolean[this.maxVar() + 1];
		return t;
	}
	
	/**Fonction qui ajoute 1 à un tableau de nombre binaire **/
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
	
	/**Fonction qui renvoit True si la formule est en forme normale de n�gation **/
	boolean isNnf(){
		/*Alors on va parcourir la fonction de maniere r�cursive
		 * Si on trouve une n�gation et que la sous formule est une autre n�gation : false
		 * Si on trouve une n�gation et que la sous formule est une conjonction/disjonction : false
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
	
	/**Fonction qui applique UNE SEULE fois une des regles de mise en forme normale de n�gation
	 * Elle l'applique seulement si necessaire � la BASE de la formule donn�e
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
	
	
	/**Fonction qui retourne une formule �quivalent en Nnf**/
	public Formule miseEnNnf(){
		if(isVariable()) //On renvoit directement la sous formule
			return this;
		if(isConjonction() || isDisjonction()){ // On continue a d�rouler la formule
			sousFormule1 = sousFormule1.miseEnNnf();
			sousFormule2 = sousFormule2.miseEnNnf();
			return this;
		}
		else{//Cas d'une n�gation, on applique une regle puis on continue � d�rouler le programme
			/*On a deux cas :
			 * _Soit on a une n�gation de variable, donc on renvoit directement this (�a sert de terminaison de recursion)
			 * _Soit on a une n�gation d'autre chose, dans ce cas on applique les regle puis on renvoit la mise en forme de la fonction cr��e pour d�rouler la nouvelle formule 
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
		 * _la conjonction de z�ro ou plus litt�raux
		 * V�rifions �a :
		 */
		if(isVariable())
			return true;
		if(isNegation())
			if(sousFormule1.isVariable())
				return true;
			else return false;
		if(isDisjonction())
			return sousFormule1.isClauseDisjonctive() && sousFormule2.isClauseDisjonctive();
		else //Cad d'une conjonction : l'op�ration interdite dans une clause disjonctive
			return false;
	}
	
	public boolean isCnf(){
		/*Une formule est en forme conjonctive normale si :
		 * _elle est la conjonction de z�ro ou plus clause disjonctive
		 */
		if(isClauseDisjonctive())
			return true;
		if(isConjonction()){
			return sousFormule1.isCnf() && sousFormule2.isCnf();//On d�roule la formule pour obtenir eventuellement des clauses disjonctive
		}
		else return false;
	}
	
	Formule appliqueReglesCnf(){
		/*On va v�rfier si une des regles 4.6, 4.7, 4.15, 4,16 est applicable, puis l'appliquer */
		//4.6 (X et Y) et Z -> X et (Y et Z)
		if(isConjonction() && sousFormule1.isConjonction())
			return new Formule(CONJ, sousFormule1.sousFormule1
									,new Formule(CONJ, sousFormule1.sousFormule2
													, sousFormule2)
								);
		//4.7 (X ou Y) ou Z -> X ou (Y ou Z)
		if(isDisjonction() && sousFormule1.isDisjonction())
			return new Formule(DISJ, sousFormule1.sousFormule1
									,new Formule(DISJ, sousFormule1.sousFormule2
													, sousFormule2)
								);
		//4.15 X ou (Y et Z) -> (X ou Y) et (X ou Z)
		if(isDisjonction() && sousFormule2.isConjonction())
			return new Formule(CONJ,
									new Formule(DISJ, sousFormule1, sousFormule2.sousFormule1),
									new Formule(DISJ, sousFormule1, sousFormule2.sousFormule2)
								);
		
		//4.16(X et Y) ou Z -> (X ou Z) et (Y ou Z)
		if(isDisjonction() && sousFormule1.isConjonction()){
			return new Formule(CONJ,
									new Formule(DISJ, sousFormule1.sousFormule1, sousFormule2),
									new Formule(DISJ, sousFormule1.sousFormule2, sousFormule2)
								);
		}
		else return this;
	}
	
	/** Fonction renvoyant une formule en forme CNF, attention, il faut que cette formule soit en NNF **/
	Formule miseEnCnf(){
		/*Pour mettre une formule en Cnf on va effectuer les op�rations suivantes :
		 * _Supposer que la formule est en Cnf car si on appelle miseEnCnf ici elle sera appell�e a chaque fois
		 * _Appliquer les regles de mise en Cnf
		 */
		if(isCnf()){
			return this;
		}
		else{//Donc l� on est soit une conjonction, soit une disjonction
			/*L� on a 2 cas : 
			 * _Soit c'est une conjonction, dans ce cas le probleme ne se situe pas dans ce noeud de la formule
			 * _Soit c'est une disjonction, dans ce cas il faut appliquer les regle de mise en DNF jusqu'� obtenir
			 * soit un litteral soit une conjonction
			 */
			if(isDisjonction()){
				return appliqueReglesCnf().miseEnCnf();//On applique donc un des regles puis on rempli la fonction sur cette meme formule car il peut arriver qu'on ait encore une disjonction au final (par exemple pour ((x1 et x2) ou (x3 et x4))
			}
			else{//Donc c'est une conjonction
				sousFormule1 = sousFormule1.miseEnCnf();
				sousFormule2 = sousFormule2.miseEnCnf();
				return this;
			}
			
		}
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
