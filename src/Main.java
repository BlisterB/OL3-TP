
public class Main {
	
	public static void main(String[] arg){
		Formule x1 = new Formule (0, 1);
		Formule x2 = new Formule (0, 2);
		Formule x3 = new Formule (0, 3);
		Formule formule = new Formule(3, new Formule(2, x1, x2), new Formule(1, x3));
		System.out.println(formule.toString());
		
		int[] i = {1, 2, 3, 4, 5};
		Formule formule2 = new Formule(2, i);
		System.out.println(formule2.toString());
	}
}
