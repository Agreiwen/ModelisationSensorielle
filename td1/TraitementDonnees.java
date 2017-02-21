package td1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class TraitementDonnees {

	HashMap<String, ArrayList<String>> motsTest;
	HashMap<String, ArrayList<String>> motsLexique;

	public TraitementDonnees(LecteurDonnees ld) {
		motsTest = ld.motsTest;
		motsLexique = ld.motsLexique;
	}

	public int distanceLevenshtein(String mot1, String f1, String mot2, String f2) {

		System.out.print(mot1+" ["+f1+"]");
		System.out.print(" => "+mot2+" ["+f2+"] ");

		String[] t1 = f1.split(" ");
		String[] t2 = f2.split(" ");
		return levenshteinCalcul(t1, t2);

	}

	public int levenshteinCalcul(String[] f1, String[] f2) {
		int size1 = f1.length;
		int size2 = f2.length;

		int[][] tmp = new int[size1 + 1][size2 + 1];
		String[][] action = new String[size1+1][size2+1];

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

				tmp[i][j] = Math.min(Math.min(tmp[i - 1][j] + 1, tmp[i][j - 1] + 1), tmp[i - 1][j - 1] + m);
				
				//faux de faire ça, on l'utilise plus d'ailleurs
				if(tmp[i][j]==0)action[i][j] = "correct";
				else if (tmp[i][j]==omi)action[i][j] = "omi";
				else if(tmp[i][j]==inser)action[i][j] = "inser";
				else if(tmp[i][j]==sub)action[i][j] = "sub";
			}
		}

		// ALGO UN
		/*if (size1 >= size2) {
			int previous = 0;
			for (int i = 1; i <= size1; i++) {
				int min = Integer.MAX_VALUE;
				String actionMin = "";
				for (int j = 1; j <= size2; j++) {
					if (tmp[i][j] < min){
						min = tmp[i][j];
						actionMin = action[i][j];
					}
				}
				if(min == previous) actionMin = "correct";
				previous = min;
				System.out.println(min+" "+actionMin);
			}
		}else{
			int previous = 0;
			for (int i = 1; i <= size2; i++) {
				int min = Integer.MAX_VALUE;
				String actionMin = "";
				for (int j = 1; j <= size1; j++) {
					if (tmp[j][i] < min){
						min = tmp[j][i];
						actionMin = action[j][i];
					}
				}
				if(min == previous) actionMin = "correct";
				previous = min;
				System.out.println(min+" "+actionMin);
			}
			
		}*/
		
		
		
		// ALGO DEUX
		/*if(size1>=size2){
			for (int i = 1; i <= size1; i++) {
				int min = Integer.MAX_VALUE;
				int x = 0;
				int y = 0;
				for (int j = 1; j <= size2; j++) {
					if(tmp[i][j]<min){
						min = tmp[i][j];
						x = i;
						y = j;
					}
					
				}
					if(action[x][y].equals("correct"))System.out.print(" c("+f1[x-1]+"=>"+f2[y-1]+")");
					else if(action[x][y].equals("sub"))System.out.print(" s("+f1[x-1]+"=>"+f2[y-1]+")");
					else if(action[x][y].equals("omi"))System.out.print(" o(=>"+f1[x-1]+")");
					else if(action[x][y].equals("inser"))System.out.print(" i(=>"+f2[y-1]+")");
				
				
			}
		}
		System.out.println("");*/
		if(tmp[size1][size2]>0)System.out.print("Erreur "+tmp[size1][size2]+" <=> ");
		else System.out.print("Correct "+tmp[size1][size2]+" <=> ");
		
		//ALGO TROIS QUI MARCHE
		int j =0;
		int i =0;
		boolean fini = false;
		while(!fini){
			int haut=0;
			int droite=0;
			int diago=0;
			if(i==size1 && j==size2)break;
			if(i<size1 && j<size2)diago=tmp[i+1][j+1];
			else diago = 500;
			if(i<=size1 && j<size2)haut=tmp[i][j+1];
			else haut = 500;
			if(i<size1 && j<=size2)droite=tmp[i+1][j];
			else droite = 500;
		
			
			
			int min = Math.min(haut, Math.min(diago, droite));
			//System.out.print("| diago:"+diago+" haut:"+haut+" droite:"+droite+" ");
			if(min == diago){
				j++;
				i++;
				System.out.print(" s("+f1[i-1]+"=>"+f2[j-1]+")");
				
			}
			else if(min == haut){
				j++;
				System.out.print(" i(=>"+f2[j-1]+")");
				
			}
			else if(min == droite){
				i++;
				System.out.print(" o(=>"+f1[i-1]+")");
				
			}
			else System.out.println("erreur");
			
		}
		System.out.println("");
		
		
		/*
		for (int i = 1; i <= size2; i++) {
			for (int j = 1; j <= size1; j++) {
			}
		}*/
		
			
		System.out.println("------------");
		
		return tmp[size1][size2];
	}

	public void reconnaissanceLevenshtein() {
		StringBuilder sb;
		int distanceLevenshtein;
		int minDistanceLevenshtein;
		String motReconnu;
		String phonemesMotReconnu;
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
						/*
						 * sb = new StringBuilder(motTest+" ["
						 * +phonemesMotTest.get(i)+"] => "+motLexique+" ["
						 * +phonemesMotLexique.get(j)+"] "); distanceLevenshtein
						 * = distanceLevenshtein(phonemesMotTest.get(i),
						 * phonemesMotLexique.get(j)); if(distanceLevenshtein >
						 * 0){ sb.append("Erreur"); }else{ sb.append("Correct");
						 * } sb.append(" "
						 * +Integer.toString(distanceLevenshtein));
						 * System.out.println(sb.toString());
						 */
						distanceLevenshtein = distanceLevenshtein(motTest,phonemesMotTest.get(i), motLexique,phonemesMotLexique.get(j));
						if (distanceLevenshtein < minDistanceLevenshtein) {
							minDistanceLevenshtein = distanceLevenshtein;
							motReconnu = motLexique;
							phonemesMotReconnu = phonemesMotLexique.get(j);
						}
					}
				}
				/*sb = new StringBuilder(motTest + " [" + phonemesMotTest.get(i) + "] => " + motReconnu + " ["
						+ phonemesMotReconnu + "] ");
				if (!motReconnu.equals(motTest)) {
					sb.append("Erreur");
					nombreErreur++;
				} else {
					sb.append("Correct");
					nombreCorrect++;
				}
				sb.append(" " + Integer.toString(minDistanceLevenshtein));
				sb.append(" <=> ");
				String[] separatedPhonemesMotTest = phonemesMotTest.get(i).split(" ");
				String[] separatedPhonemesMotReconnu = phonemesMotReconnu.split(" ");
				if (separatedPhonemesMotTest.length == separatedPhonemesMotReconnu.length) {
					for (int j = 0; j < separatedPhonemesMotTest.length; j++) {
						if (!separatedPhonemesMotReconnu[j].equals(separatedPhonemesMotTest[j])) {
							sb.append("(" + separatedPhonemesMotReconnu[j] + "=>" + separatedPhonemesMotTest[j] + ")");
						} else {
							sb.append(separatedPhonemesMotReconnu[j]);
						}
						sb.append(" ");
					}
				} else if (separatedPhonemesMotTest.length < separatedPhonemesMotReconnu.length) {
					for (int j = 0; j < separatedPhonemesMotTest.length; j++) {
						if (!separatedPhonemesMotReconnu[j].equals(separatedPhonemesMotTest[j])) {
							sb.append("(" + separatedPhonemesMotReconnu[j] + "=>" + separatedPhonemesMotTest[j] + ")");
						} else {
							sb.append(separatedPhonemesMotReconnu[j]);
						}
						sb.append(" ");
					}
					for (int j = separatedPhonemesMotTest.length; j < separatedPhonemesMotReconnu.length; j++) {
						sb.append("(" + separatedPhonemesMotReconnu[j] + "=>) ");
					}
				} else if (separatedPhonemesMotTest.length > separatedPhonemesMotReconnu.length) {
					for (int j = 0; j < separatedPhonemesMotReconnu.length; j++) {
						if (!separatedPhonemesMotReconnu[j].equals(separatedPhonemesMotTest[j])) {
							sb.append("(" + separatedPhonemesMotReconnu[j] + "=>" + separatedPhonemesMotTest[j] + ")");
						} else {
							sb.append(separatedPhonemesMotReconnu[j]);
						}
						sb.append(" ");
					}
					for (int j = separatedPhonemesMotReconnu.length; j < separatedPhonemesMotTest.length; j++) {
						sb.append("(=>" + separatedPhonemesMotTest[j] + ") ");
					}
				}
				System.out.println(sb.toString());*/
			}
		}
		System.out.println();
		System.out.println("Nombre d'erreur = " + nombreErreur + " et nombre correct = " + nombreCorrect);
		double tauxReconnaissance = ((double) nombreCorrect / ((double) nombreCorrect + (double) nombreErreur)) * 100.0;
		System.out.println("Taux de reconnaissance = " + tauxReconnaissance + " %");
	}

}
