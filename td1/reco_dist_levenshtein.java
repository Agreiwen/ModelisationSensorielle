package td1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class reco_dist_levenshtein {

	public HashMap<String, ArrayList<String>> motsTest;
	public HashMap<String, ArrayList<String>> motsLexique;
	String affichageDistLeven = "";
	String affichageMotReconnu = "";

	public reco_dist_levenshtein(String fichierTest, String lexique) {
		try {
			System.setOut(new PrintStream(new File("reco_dist_levenshtein.txt")));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		motsTest = new HashMap<>();
		motsLexique = new HashMap<>();
		try {
			lectureFichierTest(fichierTest);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			lectureLexique(lexique);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Lecture des fichiers suivants : " + fichierTest + " et " + lexique);

		System.out.println();
		reconnaissanceLevenshtein();
		// System.out.println(td.distanceLevenshtein("m o a", "a m o a"));
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
			if (separated.length != 2) {
				partie2 = "";
			} else {
				partie2 = separated[1];
			}
			if (motsLexique.containsKey(partie1)) {
				motsLexique.get(partie1).add(partie2);
			} else {
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
			if (separated.length != 2) {
				partie2 = "";
			} else {
				partie2 = separated[1];
			}
			if (motsTest.containsKey(partie1)) {
				motsTest.get(partie1).add(partie2);
			} else {
				nouvelleEntree = new ArrayList<>();
				nouvelleEntree.add(partie2);
				motsTest.put(partie1, nouvelleEntree);
			}
		}
		System.out.println("Termine.");
		br.close();
	}

	public int distanceLevenshtein(String mot1, String f1, String mot2, String f2) {
		affichageDistLeven = "";
		affichageDistLeven += mot1 + " [" + f1 + "]";
		affichageDistLeven += " => " + mot2 + " [" + f2 + "] ";

		String[] t1 = f1.split(" ");
		String[] t2 = f2.split(" ");
		return levenshteinCalcul(t1, t2);
	}

	public int levenshteinCalcul(String[] f1, String[] f2) {
		int size1 = f1.length;
		int size2 = f2.length;

		int[][] tmp = new int[size1 + 1][size2 + 1];

		for (int i = 0; i <= size1; i++) {
			tmp[i][0] = i;
		}
		for (int j = 0; j <= size2; j++) {
			tmp[0][j] = j;
		}

		for (int i = 1; i <= size1; i++) {
			for (int j = 1; j <= size2; j++) {
				int m = (f1[i - 1].equals(f2[j - 1])) ? 0 : 1;
				int omi = tmp[i - 1][j] + 1;
				int inser = tmp[i][j - 1] + 1;
				int sub = tmp[i - 1][j - 1] + m;
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
			int haut = 0;
			int droite = 0;
			int diago = 0;
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

			int min = Math.min(haut, Math.min(diago, droite));
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

	@SuppressWarnings("unused")
	public void reconnaissanceLevenshtein() {
		int distanceLevenshtein;
		int minDistanceLevenshtein;
		String motReconnu;
		String phonemesMotReconnu = "";
		int nombreErreur = 0;
		int nombreCorrect = 0;
		System.out.println("\nPour chaque action : s = substitution o = omission i = insertion \n");
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

	public static void main(String[] args) {
		String lexique = args[0];
		String fichierTest = args[1];
		new reco_dist_levenshtein(fichierTest, lexique);
	}

}
