package td1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

public class MainClass {
	
	LecteurDonnees ld;
	TraitementDonnees td;
	Hmm hmmdiscret;
	
	public MainClass(String fichierTest, String lexique, String hmm){
		try {
			System.setOut(new PrintStream(new File("reconnaissance.txt")));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		System.out.println("Lecture des fichiers suivants : "+fichierTest+" et "+lexique+" et "+hmm);
		
		try {
			//ld = new LecteurDonnees(fichierTest, lexique);
			hmmdiscret = new Hmm(hmm);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//td = new TraitementDonnees(ld);
		System.out.println();
		//td.reconnaissanceLevenshtein();
		//System.out.println(td.distanceLevenshtein("m o a", "a m o a"));
	}

	public static void main(String[] args) {
		String fichierTest = args[0];
		String lexique = args[1];
		String hmminit = args[2];
		new MainClass(fichierTest, lexique, hmminit);
	}

}
