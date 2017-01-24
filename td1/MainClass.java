package td1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

public class MainClass {
	
	LecteurDonnees ld;
	TraitementDonnees td;
	
	public MainClass(String fichierTest, String lexique){
		try {
			System.setOut(new PrintStream(new File("reconnaissance.txt")));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		System.out.println("Lecture des fichiers suivants : "+fichierTest+" et "+lexique);
		
		try {
			ld = new LecteurDonnees(fichierTest, lexique);
		} catch (IOException e) {
			e.printStackTrace();
		}
		td = new TraitementDonnees(ld);
		System.out.println();
		td.reconnaissanceLevenshtein();
		
	}

	public static void main(String[] args) {
		String fichierTest = args[0];
		String lexique = args[1];
		new MainClass(fichierTest, lexique);
	}

}
