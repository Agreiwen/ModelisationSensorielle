package td1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class Hmm {
	
	public Hmm(String fichierTest) throws IOException{
		//motsTest = new HashMap<>();
		//motsLexique = new HashMap<>();
		lectureFichierTest(fichierTest);
		//lectureLexique(lexique);
	}

	private void lectureFichierTest(String fichierTest) throws IOException {
		String st = "";
		String partie1 = "";
		String partie2 = "";
		String[] separated;
		BufferedReader br;
		String tab = ";";
		ArrayList<String> nouvelleEntree;
        @SuppressWarnings("unused")
		Pattern pattern = Pattern.compile(tab);
        br = new BufferedReader(new FileReader(fichierTest));
        System.out.print("Lecture du fichier test... ");
        
        String nomproba = br.readLine();
        String valeurproba = br.readLine();
        separated = valeurproba.split(tab);
        String psub = separated[0];
        String pins = separated[1];
        String pomi = separated[2];
        
        String commentaire = br.readLine();
        while ((st = br.readLine()) != null) {
        	separated = st.split(tab);
        	for (int i = 0; i < separated.length; i++) {
        		System.out.println(separated[i]+" endmot ");
			}
        	
        	System.out.println(" endligne ");
        	
        }
        System.out.println("Termine.");
        br.close();
		
	}

}
