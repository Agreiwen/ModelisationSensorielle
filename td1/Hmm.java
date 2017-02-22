package td1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class Hmm {
	
	HashMap<String, ArrayList<String>> motsTest;
	HashMap<String, ArrayList<String>> motsLexique;
	String affichageDistLeven = "";
	String affichageMotReconnu = "";
	protected double psub;
	protected double pins;
	protected double pomi;

	protected HashMap<String, HashMap<String,Double>> matriceTransition;
	
	public Hmm(String fichierTest, LecteurDonnees ld) throws IOException{

		motsTest = ld.motsTest;
		motsLexique = ld.motsLexique;
		lectureFichierTest(fichierTest);
	}

	private double getCsub(String phonemetest, String phonemeref) {
		//System.out.println("Phoneme ref recu : "+phonemeref+" phoneme test recu :"+phonemetest);
		return -Math.log(psub)-Math.log(matriceTransition.get(phonemetest).get(phonemeref));
	}
	
	private double getCins(String phoneme1test) {
		return -Math.log(pins)-Math.log(matriceTransition.get(phoneme1test).get("<ins>"));
	}

	private double getComi() {
		return -Math.log(pomi);
	}
	
	public double distanceLevenshtein(String mot1, String f1, String mot2, String f2) {
		affichageDistLeven = "";
		affichageDistLeven += mot1 + " [" + f1 + "]";
		affichageDistLeven += " => " + mot2 + " [" + f2 + "] ";

		String[] t1 = f1.split(" ");
		String[] t2 = f2.split(" ");
		return levenshteinCalcul(t1, t2);
	}

	private void lectureFichierTest(String fichierTest) throws IOException {
		String st = "";
		String partie1 = "";
		String partie2 = "";
		String[] separated;
		BufferedReader br;
		String tab = ";";
		ArrayList<String> nouvelleEntree;
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
        		 //   System.out.println("Abscisse :"+separated2[i]+" Ordonnee :"+separated[0]+"  valeur :"+separated[i]);
   				
			}
    	}
        System.out.println("Termine.");
        br.close();
		
	}
	
	
	public double levenshteinCalcul(String[] f1, String[] f2) {
		int size1 = f1.length;
		int size2 = f2.length;
		
		double[][] tmp = new double[size1 + 1][size2 + 1];

		for (int i = 0; i <= size1; i++) {
			tmp[i][0] = i;
		}
		for (int j = 0; j <= size2; j++) {
			tmp[0][j] = j;
		}
	/*	System.out.println("");
		System.out.print("On compare: ");
		for (int i = 1; i <= size1; i++) {
			System.out.println("Taille"+size1);
			System.out.print(f1[i-1]);
		}
		System.out.print(" et ");
		for (int j = 1; j <= size2; j++) {
			System.out.print(f2[j-1]);
		}*/
		System.out.print("\n");
		for (int i = 1; i <= size1; i++) {
			for (int j = 1; j <= size2; j++) {
				double m = getCsub(f1[i-1],f2[j-1]);
				double omi = tmp[i - 1][j] + getComi();
				double inser = tmp[i][j - 1] + getCins(f1[i-1]);
				double sub = tmp[i - 1][j - 1] + m;
				tmp[i][j] = Math.min(Math.min(omi, inser), sub);
			}
		}

		if (tmp[size1][size2] > 0){
			affichageDistLeven += "Erreur " + tmp[size1][size2] + " <=> ";
		}
		else{
			affichageDistLeven += "Correct " + tmp[size1][size2] + " <=> ";
		}

		int j = 0;
		int i = 0;
		boolean fini = false;
		while (!fini) {
			double haut = 0;
			double droite = 0;
			double diago = 0;
			if (i == size1 && j == size2)
				break;
			if (i < size1 && j < size2)
				diago = tmp[i + 1][j + 1];
			else
				diago = 500;
			if (i <= size1 && j < size2)
				haut = tmp[i][j + 1];
			else
				haut = 500;
			if (i < size1 && j <= size2)
				droite = tmp[i + 1][j];
			else
				droite = 500;

			double min = Math.min(haut, Math.min(diago, droite));
			if (min == diago) {
				j++;
				i++;
				affichageDistLeven += " s(" + f1[i - 1] + "=>" + f2[j - 1] + ")";
			} else if (min == haut) {
				j++;
				affichageDistLeven += " i(=>" + f2[j - 1] + ")";
			} else if (min == droite) {
				i++;
				affichageDistLeven += " o(=>" + f1[i - 1] + ")";
			} else
				System.out.println("erreur");
		}
		affichageDistLeven += "\n";
		return tmp[size1][size2];
	}

	
	public void reconnaissanceLevenshtein() {
		double distanceLevenshtein;
		double minDistanceLevenshtein;
		String motReconnu;
		String phonemesMotReconnu = "";
		int nombreErreur = 0;
		int nombreCorrect = 0;
		System.out.println("pour chaque action : s = substitution o = omission i = insertion \n");
		for (String motTest : this.motsTest.keySet()) {
			ArrayList<String> phonemesMotTest = new ArrayList<>();
			phonemesMotTest = this.motsTest.get(motTest);
			for (int i = 0; i < phonemesMotTest.size(); i++) {
				motReconnu = new String();
				phonemesMotReconnu = new String();
				minDistanceLevenshtein = Integer.MAX_VALUE;
				for (String motLexique : this.motsLexique.keySet()) {
					ArrayList<String> phonemesMotLexique = new ArrayList<>();
					phonemesMotLexique = this.motsLexique.get(motLexique);
					for (int j = 0; j < phonemesMotLexique.size(); j++) {
						if(!phonemesMotTest.get(i).isEmpty() && phonemesMotTest.get(i).length() != 0 ){
							distanceLevenshtein = distanceLevenshtein(motTest, phonemesMotTest.get(i), motLexique,
									phonemesMotLexique.get(j));
							if (distanceLevenshtein < minDistanceLevenshtein) {
								minDistanceLevenshtein = distanceLevenshtein;
								motReconnu = motLexique;
								phonemesMotReconnu = phonemesMotLexique.get(j);
								affichageMotReconnu = affichageDistLeven;
							}
						}
						
					}
				}
				System.out.println(affichageMotReconnu);
				if (!motReconnu.equals(motTest)) {
					nombreErreur++;
				} else {
					nombreCorrect++;
				}
			}
		}
		System.out.println();
		System.out.println("Nombre d'erreur = " + nombreErreur + " et nombre correct = " + nombreCorrect);
		double tauxReconnaissance = ((double) nombreCorrect / ((double) nombreCorrect + (double) nombreErreur)) * 100.0;
		System.out.println("Taux de reconnaissance = " + tauxReconnaissance + " %");
	}

}
