/*
 * Ecrivain.java
 *
 * Created on 15 octobre 2008, 16:23
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */


import java.util.*;
import java.io.*;

public class Ecrivain {

	private File theFile;
	private PrintWriter theWriter;
	
	/**
	 * Cree un objet Ecrivain pour le fichier en parametre
	 * le fichier en parametre est vide' de son contenu s'il existe
	 * @param nom Le fichier a ecrire
	 */
	
	public Ecrivain (String nom) {
                theFile = new File(nom);
                videLeFichier();
	}
	
	/**
	 * Vide le fichier et
	 * positionne le curseur sur la premiere ligne
	 */
	
	public void videLeFichier () {
        
            try {
		if (theFile.exists()) theFile.delete();
                theFile.createNewFile();
                theWriter = new PrintWriter(new BufferedWriter(new FileWriter(theFile)));
		}
		catch (IOException e) {
			System.out.println("Probleme de E/S");
			System.exit(-1);
		}		
	}
	
	/**
	 * Ecrit la chaine passee en parametre dans le tampon
	 * et avance le curseur a la prochaine ligne
	 * @param s la chaine a ecrire 
	 * 
	 */
	
	public void writeln (String s){
		theWriter.println(s);                    
	}

       /**
	 * Ecrit la chaine passee en parametre dans le tampon
	 * sans avancer le curseur a la prochaine ligne
	 * @param s la chaine a ecrire 
	 * 
	 */
	
	public void write (String s){
		theWriter.print(s);                    
	}
        
        /**
	 * Ecrit le tampon dans le fichier
	 * 
	 */
        
        public void flush (){
                    theWriter.flush();                    
	}
        
        /**
	 * Appele' quand l'objet est ramasse'
	 * 
	 */        

        protected void finalize () {
                    theWriter.close();
	}
}
