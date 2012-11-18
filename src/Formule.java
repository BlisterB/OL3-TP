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
		if(opPrinc == 0)
			if(numeroDeVariable >= 0 && numeroDeVariable < s.length)
				return s[numeroDeVariable];
			else return false;//Cas des variables qui n'apparaissent pas dans l'affectation
		if(opPrinc == 1)
			return !sousFormule1.eval(s);
		if(opPrinc == 2)
			return (sousFormule1.eval(s) && sousFormule2.eval(s));
		if(opPrinc == 3)
			return (sousFormule1.eval(s) || sousFormule2.eval(s));
		else return false;
	}
	
	public int maxVar(){
		if(opPrinc == 0)
			return numeroDeVariable;
		if(opPrinc == 1)
			return sousFormule1.maxVar();
		if(opPrinc == 2 || opPrinc == 3){
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
