package td2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Scanner;

public class ImagePGM {

	private int[][] image;
	private Modele modele;

	public ImagePGM(Modele m) {
		this.modele = m;
	}

	public void lireFichier() {
		image = readpgm();
	}

	public int[][] readpgm() {
		try {
			InputStream f = new FileInputStream(modele.getFichierSelect());
			BufferedReader d = new BufferedReader(new InputStreamReader(f));
			@SuppressWarnings("unused")
			String magic = d.readLine();
			String line = d.readLine();
			while (line.startsWith("#")) {
				line = d.readLine();
			}
			@SuppressWarnings("resource")
			Scanner s = new Scanner(line);
			int width = s.nextInt();
			int height = s.nextInt();
			modele.setWidth(width);
			modele.setHeight(height);
			line = d.readLine();
			s = new Scanner(line);
			@SuppressWarnings("unused")
			int maxVal = s.nextInt();
			int[][] im = new int[height][width];
			s = new Scanner(d);
			int count = 0;
			while (count < height * width) {
				im[count / width][count % width] = s.nextInt();
				count++;
			}
			return im;
		}

		catch (Throwable t) {
			t.printStackTrace(System.err);
			return null;
		}
	}

	public void writepgm(String test) {
		int width = image[0].length;
		int height = image.length;
		try {
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(test)));
			pw.println("P2");
			pw.print(width);
			pw.print(" ");
			pw.println(height);
			pw.println("255");
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					pw.print(image[i][j]);
					pw.print(" ");
				}
				pw.println("\n");
			}
			pw.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public void copieImageEntree() {
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[i].length; j++) {
				modele.entree[i][j] = image[i][j];
			}
		}
	}

	public void copieCarteImage() {
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[i].length; j++) {
				image[i][j] = (int)modele.carte[i][j];
			}
		}
		
	}

}
