package arbre;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class App {

	public static void main(String[] args) {
		try {
			String look = UIManager.getSystemLookAndFeelClassName();
			if (look.equals("com.sun.java.swing.plaf.windows.WindowsLookAndfeel"))
				UIManager.setLookAndFeel(UIManager.createLookAndFeel("look"));
		} catch (UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		JoliArbreV2 arbre = new JoliArbreV2();
		@SuppressWarnings("unused")
		Options options = new Options(null, "Options de l'arbre", true, arbre);
		arbre.dessinerArbre();
	}
}
