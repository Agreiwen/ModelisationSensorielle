package td2;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Modele {

	private File fichierSelect;
	private ImagePGM imagePGM;
	private int width;
	private int height;
	double A, B, a, b, tau, nbIteration;
	double[][] carte;
	double[][] entree;

	public Modele() {
		imagePGM = new ImagePGM(this);
		try {
			fileChooserOpen();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initialisationCarte();
		entree = new double[height][width];
		imagePGM.copieImageEntree();

		A = 1.0;
		B = 0.7;
		a = (height / 4.0) / 40.0;
		b = (width / 2.0) / 40.0;
		tau = 0.05;
		nbIteration = 10000;

		evolution();
		affichageCarte();
		imagePGM.copieCarteImage();
		imagePGM.writepgm("Test.pgm");
	}

	public void setFichierSelect(File f) {
		fichierSelect = f;
	}

	public File getFichierSelect() {
		return fichierSelect;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void fileChooserOpen() throws IOException {
		JFileChooser dialogue = new JFileChooser(new File("."));// "~")); a
																// décommenté
																// version final
		dialogue.setDialogTitle("Ouvrir un fichier PGM");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Image PGM", "pgm");
		dialogue.setFileFilter(filter);
		File fichier = null;

		if (dialogue.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			fichier = dialogue.getSelectedFile();
			setFichierSelect(fichier);
			String fichierSelect = dialogue.getSelectedFile().toString();

			// System.out.println("Nom du fichier : " + fichierSelect);

			if (fichierSelect.lastIndexOf(".") > 0) {
				// On récupère l'extension du fichier
				String ext = fichierSelect.substring(fichierSelect.lastIndexOf("."));
				// Si le fichier est un pgm
				if (ext.equals(".pgm")) {
					// System.out.println("extension: " + ext);
					imagePGM.lireFichier();
					// seamCarving(fichier);
				} else {
					// System.out.println("Choisissez un fichier pgm.");
				}
			} else {
				// System.out.println("Aucun fichier selectionné.");
			}
		}
	}

	private void initialisationCarte() {
		carte = new double[height][width];
		for (int i = 0; i < carte.length; i++) {
			for (int j = 0; j < carte[i].length; j++) {
				carte[i][j] = 0;
			}
		}
	}

	public double distanceEuclidienne(double xA, double yA, double xB, double yB) {
		double res = 0;
		res = (xA - yA) * (xA - yA) + (xB - yB) * (xB - yB);
		res = Math.sqrt(res);
		return res;
	}

	public double excitation(double distance) {
		double num = distance * distance;
		double ecart = a * a;
		return A * Math.exp(-(num / ecart));
	}

	public double inhibition(double distance) {
		double num = distance * distance;
		double ecart = b * b;
		return B * Math.exp(-(num / ecart));
	}

	public double mexicaine(double distance) {
		return excitation(distance) - inhibition(distance);
	}

	public void miseAJourNeurone(int i0, int j0) {
		double lateral = 0.0;
		double x0 = i0 / (1.0 * height);
		double y0 = j0 / (1.0 * width);
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				double x = i / (1.0 * height);
				double y = j / (1.0 * width);
				lateral += mexicaine(distanceEuclidienne(x0, y0, x, y) * carte[i][j]) / (width * width);
			}
		}
		System.out.println("Lateral : "+lateral+" entree : "+entree[i0][j0]);
		carte[i0][j0] -= tau * carte[i0][j0];
		carte[i0][j0] += tau * entree[i0][j0];
		carte[i0][j0] += tau * lateral;
	}

	public void affichageEntree() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < entree.length; i++) {
			for (int j = 0; j < entree[i].length; j++) {
				sb.append(entree[i][j] + " ");
			}
			sb.append("\n");
		}
		System.out.println(sb);
	}

	public void affichageCarte() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < carte.length; i++) {
			for (int j = 0; j < carte[i].length; j++) {
				sb.append(carte[i][j] + " ");
			}
			sb.append("\n");
		}
		System.out.println(sb);
	}

	public void evolution() {
		for (int k = 0; k < nbIteration; k++) {
			int i = (int) (Math.random() * 40);
			int j = (int) (Math.random() * 40);
			miseAJourNeurone(i, j);
		}
	}

}
