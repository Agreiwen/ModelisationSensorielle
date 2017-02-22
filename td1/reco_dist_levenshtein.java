package td1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

public class reco_dist_levenshtein {
	
	LecteurDonnees ld;
	Levenshtein td;
	
	public reco_dist_levenshtein(String fichierTest, String lexique){
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
		td = new Levenshtein(ld);
		System.out.println();
		td.reconnaissanceLevenshtein();
		//System.out.println(td.distanceLevenshtein("m o a", "a m o a"));
	}

	public static void main(String[] args) {
		String fichierTest = args[0];
		String lexique = args[1];
		new reco_dist_levenshtein(fichierTest, lexique);
	}

}
