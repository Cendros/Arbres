package arbre;

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
		arbre.dessinerArbre();
	}
}
