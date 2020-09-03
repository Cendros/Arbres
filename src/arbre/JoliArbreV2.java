package arbre;

import java.awt.Color;
import java.util.Random;

public class JoliArbreV2 {

	// Les caractéristiques à changer.
	static double profondeurInit; // La profondeur de l'arbre
	static double nbArbres; // Nombre de lignes d'arbres
	static int nbBranchesMax; // Nombre de branches max qui partent d'une branche
	static int nbBranchesMin = 2; // Nombre de branches min qui partent d'une branche
	static int type; // Change le type d'arbre : 1-Feuilles vertes, 2-Feuilles roses, 3-Feuilles
						// jaunes, 4-Mode hiver
	static int typeFeuille;
	static double niveauAutomne; // Probabilité de perdre les feuilles
	static int superposition; // Augmente l'eppaisseur de l'arbre
	static double radiusInit;
	static double tailleFeuille;
	static Color tronc = new Color(116, 71, 48);
	static Color couleurFeuilles;

	static int nbBranches = 0, nbFeuilles = 0;

	public JoliArbreV2() {

	}

	public void dessinerArbre() {
		double x0 = 0.5;
		double y0 = 0.0;
		double a0 = 90;
		double step = Math.sqrt(3) / 12;
		step = step / nbArbres;
		double pas = 1 / nbArbres;
		radiusInit = step * radiusInit * pas;
		tailleFeuille = tailleFeuille / nbArbres;
		Turtle turtle = new Turtle(x0, y0, a0);
		for (int n = 0; n < superposition; n++) {
			for (int i = 1; i <= nbArbres; i++) {
				for (int j = 0; j < nbArbres; j++) {
					turtle.jumpToNewTree(pas * i - pas * x0, pas * j + pas * y0, a0);
					turtle.setPenRadius(radiusInit * 1.5);
					turtle.setPenColor(new Color(116, 71, 48));
					turtle.goForward(step);
					turtle.setPenRadius(radiusInit);
					dessiner(turtle, step, profondeurInit);
				}
			}
		}
		turtle.draw();
	}

	private static void dessiner(Turtle turtle, double step, double profondeur) {
		boolean bout = true;
		if (profondeur > 0) {
			Random random = new Random();
			int nbBranches = random.nextInt(nbBranchesMax - 1) + nbBranchesMin;
			for (int i = 1; i <= nbBranches; i++) {
				if (random.nextDouble() < 0.25 * profondeur) {
					bout = false;
					int plus = 7 * ((int) profondeurInit - (int) profondeur);
					double angle = random.nextInt(80 + plus) - (40 + (double) plus / 2);
					double branche = (double) (random.nextInt(13) + 80) / 100;
					turtle.turnLeft(angle);
					trait(turtle, step, profondeur - 1, branche);
					turtle.turnLeft(-angle);
				}
			}
		} else {
			if (bout && type != 4)
				feuille(turtle, step);
		}

	}

	private static void trait(Turtle turtle, double step, double profondeur, double ratio) {
		nbBranches++;
		double radius = radiusInit * (profondeur / profondeurInit);
		radius = radius == 0 ? radiusInit * 0.1 : radius;
		turtle.setPenRadius(radius);
		turtle.goForward(step * ratio);
		dessiner(turtle, step * ratio, profondeur);
		turtle.moveForward(-step * ratio);
	}

	private static void feuille(Turtle turtle, double step) {
		switch (type) {
		case 2:
			dessinerFeuille(Color.PINK, turtle);
			break;
		case 3:
			dessinerFeuilleAutomne(turtle);
			break;
		case 4:
			break;
		default:
			dessinerFeuille(couleurFeuilles, turtle);
			break;
		}

	}

	private static void dessinerFeuilleAutomne(Turtle turtle) {
		Random random = new Random();
		int couleur = random.nextInt(3);
		Color couleurTurtle = null;
		switch (couleur) {
		case 0:
			couleurTurtle = new Color(248, 100, 50);
			break;
		case 1:
			couleurTurtle = new Color(252, 153, 46);
			break;
		case 2:
			couleurTurtle = new Color(252, 194, 73);
			break;
		default:
			break;
		}

		double x = 0, y = 0, automne = random.nextDouble(), proba = random.nextDouble();
		double probaFeuille = 0.4;

		if (automne < niveauAutomne && proba < probaFeuille) {
			x = turtle.getX();
			y = turtle.getY();
			double diff = (random.nextInt((int) (2 * 0.05 / nbArbres * 1000.0)) / 1000.0) - 0.05 / nbArbres;
			turtle.jumpTo(x, turtle.getYTronc() + diff, turtle.getAngle());
			dessinerFeuille(couleurTurtle, turtle);
		}
		if (automne > niveauAutomne)
			dessinerFeuille(couleurTurtle, turtle);
		if (automne < niveauAutomne && proba < probaFeuille)
			turtle.jumpTo(x, y, turtle.getAngle());
	}

	private static void dessinerFeuille(Color couleurFeuille, Turtle turtle) {
		nbFeuilles++;
		turtle.setPenColor(couleurFeuille);
		switch (typeFeuille) {
		case 1:
			turtle.particules(tailleFeuille);
			break;
		case 3:
			turtle.fillTriangle(tailleFeuille);
			break;
		default:
			turtle.fillcircle(tailleFeuille);
			break;
		}
		turtle.setPenColor(tronc);
	}
}
