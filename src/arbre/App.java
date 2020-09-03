package arbre;

import java.text.DecimalFormat;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class App {

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		JoliArbreV2 arbre = new JoliArbreV2();
		@SuppressWarnings("unused")
		Options options = new Options(null, "Options de l'arbre", true, arbre);
		long startTime = System.currentTimeMillis();
		arbre.dessinerArbre();
		long stopTime = System.currentTimeMillis();
		long temps = stopTime - startTime;
		System.out.println("Estimation branches : " + Options.estimationBranche);
		System.out.println("Nombre de branches : " + JoliArbreV2.nbBranches);
		System.out.println("Estimation feuilles : " + Options.estimationFeuilles);
		System.out.println("Nombre de feuilles : " + JoliArbreV2.nbFeuilles);
		System.out.println("Temps : " + temps);
		double erreurBranches = (double) Math.abs(Options.estimationBranche - JoliArbreV2.nbBranches)
				/ JoliArbreV2.nbBranches;
		double erreurFeuilles = (double) Math.abs(Options.estimationFeuilles - JoliArbreV2.nbFeuilles)
				/ JoliArbreV2.nbFeuilles;
		double precisionBranches = (double) (1 - erreurBranches) * 100,
				precisionFeuilles = (double) (1 - erreurFeuilles) * 100;
		DecimalFormat numberFormat = new DecimalFormat("#.00");
		System.out.println("précision branches : " + numberFormat.format(precisionBranches) + "%");
		System.out.println("précision feuilles : " + numberFormat.format(precisionFeuilles) + "%");
	}
}
