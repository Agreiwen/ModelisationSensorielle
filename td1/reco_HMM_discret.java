package td1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

public class reco_HMM_discret {
	

	Hmm hmmdiscret;
	LecteurDonnees ld;
//	TraitementDonnees td;
	public reco_HMM_discret(String fichierTest, String lexique, String hmm){
		try {
			System.setOut(new PrintStream(new File("reco_HMM_discret.txt")));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		System.out.println("Lecture des fichiers suivants : "+fichierTest+", "+lexique+" et "+hmm);
		
		try {
			ld = new LecteurDonnees(fichierTest, lexique);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			hmmdiscret = new Hmm(hmm, ld);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//td = new TraitementDonnees(ld);
		hmmdiscret.reconnaissanceLevenshtein();
		System.out.println();
	}

	public static void main(String[] args) {
		String lexique = args[0];
		String hmminit = args[1];
		String fichierTest = args[2];
		new reco_HMM_discret(fichierTest, lexique, hmminit);
	}

}
