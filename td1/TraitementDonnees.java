package td1;

import java.util.ArrayList;
import java.util.HashMap;

public class TraitementDonnees {
	
	HashMap<String, ArrayList<String>> motsTest;
	HashMap<String, ArrayList<String>> motsLexique;

	public TraitementDonnees(LecteurDonnees ld) {
		motsTest = ld.motsTest;
		motsLexique = ld.motsLexique;
	}
	
	public int distanceLevenshtein(String motTest, String motLexique){
		int distance = 0;
		String[] separatedMotTest = motTest.split(" ");
		String[] separatedMotLexique = motLexique.split(" ");
		distance += Math.abs(separatedMotTest.length - separatedMotLexique.length);
		//System.out.println(distance);
		if(separatedMotTest.length >= separatedMotLexique.length){
			for (int i = 0; i < separatedMotLexique.length; i++) {
				if(!separatedMotLexique[i].equals(separatedMotTest[i])){
					//System.out.println(separatedMotTest[i]+" - "+separatedMotLexique[i]);
					distance++;
				}
			}
		}else{
			for (int i = 0; i < separatedMotTest.length; i++) {
				if(!separatedMotLexique[i].equals(separatedMotTest[i])){
					//System.out.println(separatedMotTest[i]+" - "+separatedMotLexique[i]);
					distance++;
				}
			}
		}
		return distance;
	}
	
	public void reconnaissanceLevenshtein(){
		StringBuilder sb;
		int distanceLevenshtein;
		int minDistanceLevenshtein;
		String motReconnu;
		String phonemesMotReconnu;
		int nombreErreur = 0;
		int nombreCorrect = 0;
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
						/*sb = new StringBuilder(motTest+" ["+phonemesMotTest.get(i)+"] => "+motLexique+" ["+phonemesMotLexique.get(j)+"] ");
						distanceLevenshtein = distanceLevenshtein(phonemesMotTest.get(i), phonemesMotLexique.get(j));
						if(distanceLevenshtein > 0){
							sb.append("Erreur");
						}else{
							sb.append("Correct");
						}
						sb.append(" "+Integer.toString(distanceLevenshtein));
						System.out.println(sb.toString());*/
						distanceLevenshtein = distanceLevenshtein(phonemesMotTest.get(i), phonemesMotLexique.get(j));
						if(distanceLevenshtein < minDistanceLevenshtein){
							minDistanceLevenshtein = distanceLevenshtein;
							motReconnu = motLexique;
							phonemesMotReconnu = phonemesMotLexique.get(j);
						}
					}
				}
				sb = new StringBuilder(motTest+" ["+phonemesMotTest.get(i)+"] => "+motReconnu+" ["+phonemesMotReconnu+"] ");
				if(!motReconnu.equals(motTest)){
					sb.append("Erreur");
					nombreErreur++;
				}else{
					sb.append("Correct");
					nombreCorrect++;
				}
				sb.append(" "+Integer.toString(minDistanceLevenshtein));
				sb.append(" <=> ");
				String[] separatedPhonemesMotTest = phonemesMotTest.get(i).split(" ");
				String[] separatedPhonemesMotReconnu = phonemesMotReconnu.split(" ");
				if(separatedPhonemesMotTest.length == separatedPhonemesMotReconnu.length){
					for (int j = 0; j < separatedPhonemesMotTest.length; j++) {
						if(!separatedPhonemesMotReconnu[j].equals(separatedPhonemesMotTest[j])){
							sb.append("("+separatedPhonemesMotReconnu[j]+"=>"+separatedPhonemesMotTest[j]+")");
						}else{
							sb.append(separatedPhonemesMotReconnu[j]);
						}
						sb.append(" ");
					}
				}else if(separatedPhonemesMotTest.length < separatedPhonemesMotReconnu.length){
					for (int j = 0; j < separatedPhonemesMotTest.length; j++) {
						if(!separatedPhonemesMotReconnu[j].equals(separatedPhonemesMotTest[j])){
							sb.append("("+separatedPhonemesMotReconnu[j]+"=>"+separatedPhonemesMotTest[j]+")");
						}else{
							sb.append(separatedPhonemesMotReconnu[j]);
						}
						sb.append(" ");
					}
					for (int j = separatedPhonemesMotTest.length; j < separatedPhonemesMotReconnu.length; j++) {
						sb.append("("+separatedPhonemesMotReconnu[j]+"=>) ");
					}
				}else if(separatedPhonemesMotTest.length > separatedPhonemesMotReconnu.length){
					for (int j = 0; j < separatedPhonemesMotReconnu.length; j++) {
						if(!separatedPhonemesMotReconnu[j].equals(separatedPhonemesMotTest[j])){
							sb.append("("+separatedPhonemesMotReconnu[j]+"=>"+separatedPhonemesMotTest[j]+")");
						}else{
							sb.append(separatedPhonemesMotReconnu[j]);
						}
						sb.append(" ");
					}
					for (int j = separatedPhonemesMotReconnu.length; j < separatedPhonemesMotTest.length; j++) {
						sb.append("(=>"+separatedPhonemesMotTest[j]+") ");
					}
				}
				System.out.println(sb.toString());
			}
		}
		System.out.println();
		System.out.println("Nombre d'erreur = "+nombreErreur+" et nombre correct = "+nombreCorrect);
		double tauxReconnaissance = ((double)nombreCorrect/((double)nombreCorrect+(double)nombreErreur))*100.0;
		System.out.println("Taux de reconnaissance = "+tauxReconnaissance+" %");
	}

}
