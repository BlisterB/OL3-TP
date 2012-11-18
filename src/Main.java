
public class Main {
	
	public static void main(String[] arg){
		Formule x1 = new Formule(0, 1);
		Formule x2 = new Formule(0, 2);
		Formule x3 = new Formule(0, 3);
		
		Formule form = new Formule(3, new Formule(2, x1, x2), new Formule(1, x3));
		
		/*boolean[] b = Formule.affectation(3);*/
		System.out.println("La formule est elle satisfiable ? " + form.isSatisfiable());
		
		Formule formule2 = new Formule(2, new Formule (0, 0), new Formule(1,new Formule(0,0)));
		System.out.println("La formule est : " + formule2.toString() + " Est elle satisfiable ? " + formule2.isSatisfiable());
	}
}