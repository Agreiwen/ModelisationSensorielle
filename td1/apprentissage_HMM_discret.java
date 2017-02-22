package td1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class apprentissage_HMM_discret {
	public String fichierDest;
	public ArrayList<String> motsApp;
	public ArrayList<String> motsRef;
	public ArrayList<String> motsTest;
	String affichageDistLeven = "";
	protected HashMap<String, HashMap<String, Double>> matriceTransition;
	protected double pSub;
	protected double pIns;
	protected double pOmi;
	//protected ArrayList<ArrayList<ArrayList<String>>> baseapp;
	
	
	public apprentissage_HMM_discret(String modeleinit, String donneesApp, String modeleapp){
		motsApp = new ArrayList<>();
		motsRef = new ArrayList<>();
		motsTest = new ArrayList<>();
		this.fichierDest = modeleapp;
		try {
			lectureFichierModele(modeleinit);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("Lecture des fichiers suivants : "+modeleinit+" et "+donneesApp+" et "+modeleapp);
		try {
			lectureApp(donneesApp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		for(int i = 0; i<motsApp.size();i++){
			String motTest = motsTest.get(i);
			String motRef = motsRef.get(i);
			String[] f1 = motTest.split(" ");
			String[] f2 = motRef.split(" ");
			double a = levenshteinCalcul(f1, f2);
			System.out.println(motTest+" ->"+motRef+" = "+a);
			
		}
		
		
	}

	private void lectureApp(String donneesApp) throws IOException {
		String st = "";
		String[] separated;
		BufferedReader br;
		String tab = "	";
		//ArrayList<String> nouvelleEntree;
		// @SuppressWarnings("unused")
		// Pattern pattern = Pattern.compile(tab);
		br = new BufferedReader(new FileReader(donneesApp));
		// for (int i = 0; i < separated.length; i++) {

		// }
		System.out.print("Lecture des donnees app... \n");

		while ((st = br.readLine()) != null) {
			// st = br.readLine();
			//ArrayList<String> testPossible = new ArrayList<>();
		//	HashMap<String, ArrayList<String>> reference = new HashMap<>();
			
			separated = st.split(tab);
			String motinfo = separated[0];

			List<String> matchList = new ArrayList<String>();
			List<String> matchList2 = new ArrayList<String>();
			Pattern regex = Pattern.compile("\\[(.*?)\\]");
			Matcher ref = regex.matcher(separated[1]);
			Matcher test = regex.matcher(separated[2]);

			while (ref.find()) {// Finds Matching Pattern in String
				matchList.add(ref.group(1));// Fetching Group from String
			}
			while (test.find()) {// Finds Matching Pattern in String
				matchList2.add(test.group(1));// Fetching Group from String
			}
			
			String motref = matchList.get(0);
			String mottest = matchList2.get(0);
			this.motsApp.add(motinfo);
			this.motsRef.add(motref);
			this.motsTest.add(mottest);
		}
		System.out.println("Termine.");
		br.close();
	}

	public static void main(String[] args) {
		String modeleinit = args[0];
		String donneesApp = args[1];
		String modeleapp = args[2];
		new apprentissage_HMM_discret(modeleinit, donneesApp, modeleapp);
	}
	
	private double getCsub(String phonemetest, String phonemeref) {
		return -Math.log(pSub) - Math.log(matriceTransition.get(phonemetest).get(phonemeref));
	}

	private double getCins(String phoneme1test) {
		return -Math.log(pIns) - Math.log(matriceTransition.get(phoneme1test).get("<ins>"));
	}

	private double getComi() {
		return -Math.log(pOmi);
	}

	public double distanceLevenshtein(String mot1, String f1, String mot2, String f2) {
		affichageDistLeven = "";
		affichageDistLeven += mot1 + " [" + f1 + "]";
		affichageDistLeven += " => " + mot2 + " [" + f2 + "] ";

		String[] t1 = f1.split(" ");
		String[] t2 = f2.split(" ");
		return levenshteinCalcul(t1, t2);
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
		for (int i = 1; i <= size1; i++) {
			for (int j = 1; j <= size2; j++) {
				double m = getCsub(f1[i - 1], f2[j - 1]);
				double omi = tmp[i - 1][j] + getComi();
				double inser = tmp[i][j - 1] + getCins(f1[i - 1]);
				double sub = tmp[i - 1][j - 1] + m;
				tmp[i][j] = Math.min(Math.min(omi, inser), sub);
			}
		}

		if (tmp[size1][size2] > 0) {
			affichageDistLeven += "Erreur " + tmp[size1][size2] + " <=> ";
		} else {
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
	
	private void lectureFichierModele(String fichierModele) throws IOException {
		String st = "";
		String[] separated;
		BufferedReader br;
		String tab = ";";
		br = new BufferedReader(new FileReader(fichierModele));
		System.out.print("Lecture du fichier modele HMM... ");

		br.readLine();
		String valeurproba = br.readLine();
		separated = valeurproba.split(tab);
		String psub = separated[0];
		String pins = separated[1];
		String pomi = separated[2];
		this.pSub = Double.parseDouble(psub);
		this.pIns = Double.parseDouble(pins);
		this.pOmi = Double.parseDouble(pomi);
		br.readLine();
		String coltableau = br.readLine();
		String[] separated2 = coltableau.split(tab);

		matriceTransition = new HashMap<>();

		for (int i = 1; i < separated2.length; i++) {
			HashMap<String, Double> matricetmp = new HashMap<>();
			for (int j = 1; j < separated2.length; j++) {
				matricetmp.put(separated2[j], 0.);
			}
			matriceTransition.put(separated2[i], matricetmp);
		}

		while ((st = br.readLine()) != null) {
			separated = st.split(tab);
			for (int i = 1; i < separated.length; i++) {
				matriceTransition.get(separated2[i]).put(separated[0], Double.parseDouble(separated[i]));
			}
		}
		System.out.println("Termine.");
		br.close();
	}
	
	

}
