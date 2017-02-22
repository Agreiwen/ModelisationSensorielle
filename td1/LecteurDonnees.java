package td1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class LecteurDonnees {
	
	public HashMap<String, ArrayList<String>> motsTest;
	public HashMap<String, ArrayList<String>> motsLexique;
	
	public LecteurDonnees(String fichierTest, String lexique) throws IOException{
		motsTest = new HashMap<>();
		motsLexique = new HashMap<>();
		lectureFichierTest(fichierTest);
		lectureLexique(lexique);
	}

	private void lectureLexique(String lexique) throws IOException {
		String st = "";
		String partie1 = "";
		String partie2 = "";
		String[] separated;
		BufferedReader br;
		String tab = "	";
		ArrayList<String> nouvelleEntree;
        br = new BufferedReader(new FileReader(lexique));
 
        System.out.print("Lecture du lexique... ");
        while ((st = br.readLine()) != null) {
        	separated = st.split(tab);
        	partie1 = separated[0];
        	if(separated.length != 2){
        		partie2 = "";
        	}else{
        		partie2 = separated[1];
        	}
        	if(motsLexique.containsKey(partie1)){
        		motsLexique.get(partie1).add(partie2);
        	}else{
        		nouvelleEntree = new ArrayList<>();
        		nouvelleEntree.add(partie2);
        		motsLexique.put(partie1, nouvelleEntree);
        	}
        }
        System.out.println("Termine.");
        br.close();
	}

	private void lectureFichierTest(String fichierTest) throws IOException {
		String st = "";
		String partie1 = "";
		String partie2 = "";
		String[] separated;
		BufferedReader br;
		String tab = "	";
		ArrayList<String> nouvelleEntree;
        @SuppressWarnings("unused")
		Pattern pattern = Pattern.compile(tab);
        br = new BufferedReader(new FileReader(fichierTest));
 
        System.out.print("Lecture du fichier test... ");
        while ((st = br.readLine()) != null) {
        	separated = st.split(tab);
        	partie1 = separated[0];
        	if(separated.length != 2){
        		partie2 = "";
        	}else{
        		partie2 = separated[1];
        	}
        	if(motsTest.containsKey(partie1)){
        		motsTest.get(partie1).add(partie2);
        	}else{
        		nouvelleEntree = new ArrayList<>();
        		nouvelleEntree.add(partie2);
        		motsTest.put(partie1, nouvelleEntree);
        	}
        }
        System.out.println("Termine.");
        br.close();
	}


}
