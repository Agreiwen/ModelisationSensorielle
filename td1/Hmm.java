package td1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Hmm {
	
	protected double psub;
	protected double pins;
	protected double pomi;

	protected HashMap<String, HashMap<String,Double>> matriceTransition;
	
	public Hmm(String fichierTest) throws IOException{

		lectureFichierTest(fichierTest);
	}

	private double getCsub(String phonemetest, String phonemeref) {
		return -Math.log(psub)-Math.log(matriceTransition.get(phonemetest).get(phonemeref));
		
	}
	
	private double getCins(String phoneme1test) {
		return -Math.log(pins)-Math.log(matriceTransition.get(phoneme1test).get("<ins>"));
		
	}

	private double getComi() {
		return -Math.log(pomi);
		
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
        System.out.println("Lecture du fichier test... ");
        
        String nomproba = br.readLine();
        String valeurproba = br.readLine();
        separated = valeurproba.split(tab);
        String psub = separated[0];
        String pins = separated[1];
        String pomi = separated[2];
        this.psub = Double.parseDouble(psub);
        this.pins = Double.parseDouble(pins);
        this.pomi = Double.parseDouble(pomi);
        String commentaire = br.readLine();
        String coltableau = br.readLine();
        String[] separated2 = coltableau.split(tab);

        matriceTransition = new HashMap<>();
        
        for (int i = 1; i < separated2.length; i++) {
        	HashMap<String,Double> matricetmp = new HashMap<>();
        	for (int j = 1; j < separated2.length; j++) {
        		matricetmp.put(separated2[j], 0.);
        	}
        	matriceTransition.put(separated2[i], matricetmp);
		}

        while ((st = br.readLine()) != null) {
        	separated = st.split(tab);
        	for (int i = 1; i < separated.length; i++) {
        		
        		    matriceTransition.get(separated2[i]).put(separated[0],Double.parseDouble(separated[i]));
        		    System.out.println("Abscisse :"+separated2[i]+" Ordonnee :"+separated[0]+"  valeur :"+separated[i]);
   				
			}
    	}
        System.out.println("Termine.");
        br.close();
		
	}

}
