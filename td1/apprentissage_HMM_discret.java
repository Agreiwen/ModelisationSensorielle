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
	public HashMap<String, ArrayList<String>> motsApp;
	protected HashMap<String, HashMap<String,ArrayList<String>>> baseapp;
	
	
	public apprentissage_HMM_discret(String modeleinit, String donneesApp, String modeleapp){
		baseapp = new HashMap<>();
		this.fichierDest = modeleapp;
		System.out.println("Lecture des fichiers suivants : "+modeleinit+" et "+donneesApp+" et "+modeleapp);
		try {
			lectureApp(donneesApp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void lectureApp(String donneesApp) throws IOException {
		String st = "";
		String[] separated;
		BufferedReader br;
		String tab = "	";
		ArrayList<String> nouvelleEntree;
		// @SuppressWarnings("unused")
		// Pattern pattern = Pattern.compile(tab);
		br = new BufferedReader(new FileReader(donneesApp));
		// for (int i = 0; i < separated.length; i++) {

		// }
		System.out.print("Lecture des donnees app... \n");

		while ((st = br.readLine()) != null) {
			// st = br.readLine();
			ArrayList<String> testPossible = new ArrayList<>();
			HashMap<String, ArrayList<String>> reference = new HashMap<>();
			
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
			if (baseapp.containsKey(motinfo)){
				if(baseapp.get(motinfo).containsKey(motref)){
					if(!baseapp.get(motinfo).get(motref).contains(mottest)){
						baseapp.get(motinfo).get(motref).add(mottest);
					}
				}else{
					testPossible.add(mottest);
					//reference.put(motref, testPossible);
					baseapp.get(motinfo).put(motref, testPossible);
				}
			}else{
				testPossible.add(mottest);
				reference.put(motref, testPossible);
				baseapp.put(motinfo,reference);
			}
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

}
