package arbre;

/*************************************************************************
 *  Compilation:  javac StdDraw.java
 *  Execution:    java StdDraw
 *
 *  Standard drawing library. This class provides a basic capability for
 *  creating drawings with your programs. It uses a simple graphics model that
 *  allows you to create drawings consisting of points, lines, and curves
 *  in a window on your computer and to save the drawings to a file.
 *
 *  Todo
 *  ----
 *    -  Add support for gradient fill, etc.
 *
 *  Remarks
 *  -------
 *    -  don't use AffineTransform for rescaling since it inverts
 *       images and strings
 *    -  careful using setFont in inner loop within an animation -
 *       it can cause flicker
 *
 *************************************************************************/
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.awt.image.DirectColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/**
 * <i>Standard draw</i>. This class provides a basic capability for creating
 * drawings with your programs. It uses a simple graphics model that allows you
 * to create drawings consisting of points, lines, and curves in a window on
 * your computer and to save the drawings to a file.
 * <p>
 * For additional documentation, see
 * <a href="http://introcs.cs.princeton.edu/15inout">Section 1.5</a> of
 * <i>Introduction to Programming in Java: An Interdisciplinary Approach</i> by
 * Robert Sedgewick and Kevin Wayne.
 */
public final class StdDraw implements ActionListener {

	// pre-defined colors
	public static final Color BLACK = Color.BLACK;
	public static final Color BLUE = Color.BLUE;
	public static final Color CYAN = Color.CYAN;
	public static final Color DARK_GRAY = Color.DARK_GRAY;
	public static final Color GRAY = Color.GRAY;
	public static final Color GREEN = Color.GREEN;
	public static final Color LIGHT_GRAY = Color.LIGHT_GRAY;
	public static final Color MAGENTA = Color.MAGENTA;
	public static final Color ORANGE = Color.ORANGE;
	public static final Color PINK = Color.PINK;
	public static final Color RED = Color.RED;
	public static final Color WHITE = Color.WHITE;
	public static final Color YELLOW = Color.YELLOW;

	/**
	 * Shade of blue used in Introduction to Programming in Java. It is Pantone
	 * 300U. The RGB values are approximately (9, 90, 266).
	 */
	public static final Color BOOK_BLUE = new Color(9, 90, 166);
	public static final Color BOOK_LIGHT_BLUE = new Color(103, 198, 243);

	/**
	 * Shade of red used in Algorithms 4th edition. It is Pantone 1805U. The RGB
	 * values are approximately (150, 35, 31).
	 */
	public static final Color BOOK_RED = new Color(150, 35, 31);

	// default colors
	private static final Color DEFAULT_PEN_COLOR = BLACK;
	private static final Color DEFAULT_CLEAR_COLOR = WHITE;

	// current pen color
	private static Color penColor;

	// default canvas size is DEFAULT_SIZE-by-DEFAULT_SIZE
	private static final int DEFAULT_SIZE = 512;
	private static int width = 100;
	private static int height = 100;

	// default pen radius
	private static final double DEFAULT_PEN_RADIUS = 0.002;

	// current pen radius
	private static double penRadius;

	// show we draw immediately or wait until next show?
	private static boolean defer = false;

	// boundary of drawing canvas, 5% border
	private static final double BORDER = 0.05;
	private static final double DEFAULT_XMIN = 0.0;
	private static final double DEFAULT_XMAX = 1.0;
	private static final double DEFAULT_YMIN = 0.0;
	private static final double DEFAULT_YMAX = 1.0;
	private static double xmin, ymin, xmax, ymax;

	// double buffered graphics
	private static BufferedImage offscreenImage, onscreenImage;
	private static Graphics2D offscreen, onscreen;

	// singleton for callbacks: avoids generation of extra .class files
	private static StdDraw std = new StdDraw();

	// the frame for drawing to the screen
	private static JFrame frame;

	// not instantiable
	private StdDraw() {
	}

	// static initializer
	static {
		setCanvasSize();
	}

	/**
	 * Set the window size to the default size 512-by-512 pixels.
	 */
	public static void setCanvasSize() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setCanvasSize((int) screenSize.getHeight(), (int) screenSize.getHeight() - 100);
	}

	/**
	 * Set the window size to w-by-h pixels.
	 *
	 * @param w the width as a number of pixels
	 * @param h the height as a number of pixels
	 * @throws RuntimeException if the width or height is 0 or negative
	 */
	public static void setCanvasSize(int w, int h) {
		if (w < 1 || h < 1)
			throw new RuntimeException("width and height must be positive");
		width = w;
		height = h;
		init();
	}

	public static int getWidth() {
		return width;
	}

	public static int getHeight() {
		return height;
	}

	// init
	private static void init() {
		if (frame != null)
			frame.setVisible(false);
		frame = new JFrame();
		offscreenImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		onscreenImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		offscreen = offscreenImage.createGraphics();
		onscreen = onscreenImage.createGraphics();
		setXscale();
		setYscale();
		offscreen.setColor(DEFAULT_CLEAR_COLOR);
		offscreen.fillRect(0, 0, width, height);
		setPenColor();
		setPenRadius();
		clear();

		// add antialiasing
		RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		offscreen.addRenderingHints(hints);

		// frame stuff
		ImageIcon icon = new ImageIcon(onscreenImage);
		JLabel draw = new JLabel(icon);

		frame.setContentPane(draw);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // closes all windows
		// frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // closes only
		// current window
		frame.setTitle("Standard Draw");
		frame.setJMenuBar(createMenuBar());
		frame.pack();
		frame.requestFocusInWindow();
		frame.setVisible(true);
	}

	// create the menu bar (changed to private)
	private static JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("File");
		menuBar.add(menu);
		JMenuItem menuItem1 = new JMenuItem(" Save...   ");
		menuItem1.addActionListener(std);
		menuItem1.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		menu.add(menuItem1);
		return menuBar;
	}

	/*************************************************************************
	 * User and screen coordinate systems
	 *************************************************************************/

	/**
	 * Set the x-scale to be the default (between 0.0 and 1.0).
	 */
	public static void setXscale() {
		setXscale(DEFAULT_XMIN, DEFAULT_XMAX);
	}

	/**
	 * Set the y-scale to be the default (between 0.0 and 1.0).
	 */
	public static void setYscale() {
		setYscale(DEFAULT_YMIN, DEFAULT_YMAX);
	}

	/**
	 * Set the x-scale (a 10% border is added to the values)
	 * 
	 * @param min the minimum value of the x-scale
	 * @param max the maximum value of the x-scale
	 */
	public static void setXscale(double min, double max) {
		double size = max - min;
		xmin = min - BORDER * size;
		xmax = max + BORDER * size;
	}

	/**
	 * Set the y-scale (a 10% border is added to the values).
	 * 
	 * @param min the minimum value of the y-scale
	 * @param max the maximum value of the y-scale
	 */
	public static void setYscale(double min, double max) {
		double size = max - min;
		ymin = min - BORDER * size;
		ymax = max + BORDER * size;
	}

	// helper functions that scale from user coordinates to screen coordinates and
	// back
	private static double scaleX(double x) {
		return width * (x - xmin) / (xmax - xmin);
	}

	private static double scaleY(double y) {
		return height * (ymax - y) / (ymax - ymin);
	}

	private static double factorX(double w) {
		return w * width / Math.abs(xmax - xmin);
	}

	private static double factorY(double h) {
		return h * height / Math.abs(ymax - ymin);
	}

	/**
	 * Clear the screen to the default color (white).
	 */
	public static void clear() {
		clear(DEFAULT_CLEAR_COLOR);
	}

	/**
	 * Clear the screen to the given color.
	 * 
	 * @param color the Color to make the background
	 */
	public static void clear(Color color) {
		offscreen.setColor(color);
		offscreen.fillRect(0, 0, width, height);
		offscreen.setColor(penColor);
		draw();
	}

	/**
	 * Get the current pen radius.
	 * 
	 * @return the pen radius
	 */
	public static double getPenRadius() {
		return penRadius;
	}

	/**
	 * Set the pen size to the default (.002).
	 */
	public static void setPenRadius() {
		setPenRadius(DEFAULT_PEN_RADIUS);
	}

	/**
	 * Set the radius of the pen to the given size.
	 * 
	 * @param r the radius of the pen
	 * @throws RuntimeException if r is negative
	 */
	public static void setPenRadius(double r) {
		if (r < 0)
			throw new RuntimeException("pen radius must be positive");
		penRadius = r * DEFAULT_SIZE;
		BasicStroke stroke = new BasicStroke((float) penRadius, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		// BasicStroke stroke = new BasicStroke((float) penRadius);
		offscreen.setStroke(stroke);
	}

	/**
	 * Set the pen color to the default color (black).
	 */
	public static void setPenColor() {
		setPenColor(DEFAULT_PEN_COLOR);
	}

	/**
	 * Set the pen color to the given color. The available pen colors are BLACK,
	 * BLUE, CYAN, DARK_GRAY, GRAY, GREEN, LIGHT_GRAY, MAGENTA, ORANGE, PINK, RED,
	 * WHITE, and YELLOW.
	 * 
	 * @param color the Color to make the pen
	 */
	public static void setPenColor(Color color) {
		penColor = color;
		offscreen.setColor(penColor);
	}

	/*************************************************************************
	 * Drawing geometric shapes.
	 *************************************************************************/

	/**
	 * Draw a line from (x0, y0) to (x1, y1).
	 * 
	 * @param x0 the x-coordinate of the starting point
	 * @param y0 the y-coordinate of the starting point
	 * @param x1 the x-coordinate of the destination point
	 * @param y1 the y-coordinate of the destination point
	 */
	public static void line(double x0, double y0, double x1, double y1) {
		offscreen.draw(new Line2D.Double(scaleX(x0), scaleY(y0), scaleX(x1), scaleY(y1)));
		// draw();
	}

	/**
	 * Draw one pixel at (x, y).
	 * 
	 * @param x the x-coordinate of the pixel
	 * @param y the y-coordinate of the pixel
	 */
	public static void pixel(double x, double y) {
		offscreen.fillRect((int) Math.round(scaleX(x)), (int) Math.round(scaleY(y)), 1, 1);
	}

	/**
	 * Draw a point at (x, y).
	 * 
	 * @param x the x-coordinate of the point
	 * @param y the y-coordinate of the point
	 */
	public static void point(double x, double y) {
		double xs = scaleX(x);
		double ys = scaleY(y);
		double r = penRadius;
		// double ws = factorX(2*r);
		// double hs = factorY(2*r);
		// if (ws <= 1 && hs <= 1) pixel(x, y);
		if (r <= 1)
			pixel(x, y);
		else
			offscreen.fill(new Ellipse2D.Double(xs - r / 2, ys - r / 2, r, r));
		// draw();
	}

	/**
	 * Draw a circle of radius r, centered on (x, y).
	 * 
	 * @param x the x-coordinate of the center of the circle
	 * @param y the y-coordinate of the center of the circle
	 * @param r the radius of the circle
	 * @throws RuntimeException if the radius of the circle is negative
	 */
	public static void circle(double x, double y, double r) {
		if (r < 0)
			throw new RuntimeException("circle radius can't be negative");
		double xs = scaleX(x);
		double ys = scaleY(y);
		double ws = factorX(2 * r);
		double hs = factorY(2 * r);
		if (ws <= 1 && hs <= 1)
			pixel(x, y);
		else
			offscreen.draw(new Ellipse2D.Double(xs - ws / 2, ys - hs / 2, ws, hs));
		draw();
	}

	/**
	 * Draw filled circle of radius r, centered on (x, y).
	 * 
	 * @param x the x-coordinate of the center of the circle
	 * @param y the y-coordinate of the center of the circle
	 * @param r the radius of the circle
	 * @throws RuntimeException if the radius of the circle is negative
	 */
	public static void filledCircle(double x, double y, double r) {
		if (r < 0)
			throw new RuntimeException("circle radius can't be negative");
		double xs = scaleX(x);
		double ys = scaleY(y);
		double ws = factorX(2 * r);
		double hs = factorY(2 * r);
		if (ws <= 1 && hs <= 1)
			pixel(x, y);
		else
			offscreen.fill(new Ellipse2D.Double(xs - ws / 2, ys - hs / 2, ws, hs));
		// draw();
	}

	/**
	 * Draw a filled polygon with the given (x[i], y[i]) coordinates.
	 * 
	 * @param x an array of all the x-coordindates of the polygon
	 * @param y an array of all the y-coordindates of the polygon
	 */
	public static void filledPolygon(double[] x, double[] y) {
		int N = x.length;
		GeneralPath path = new GeneralPath();
		path.moveTo((float) scaleX(x[0]), (float) scaleY(y[0]));
		for (int i = 0; i < N; i++)
			path.lineTo((float) scaleX(x[i]), (float) scaleY(y[i]));
		path.closePath();
		offscreen.fill(path);
	}

	/**
	 * Display on screen, pause for t milliseconds, and turn on <em>animation
	 * mode</em>: subsequent calls to drawing methods such as <tt>line()</tt>,
	 * <tt>circle()</tt>, and <tt>square()</tt> will not be displayed on screen
	 * until the next call to <tt>show()</tt>. This is useful for producing
	 * animations (clear the screen, draw a bunch of shapes, display on screen for a
	 * fixed amount of time, and repeat). It also speeds up drawing a huge number of
	 * shapes (call <tt>show(0)</tt> to defer drawing on screen, draw the shapes,
	 * and call <tt>show(0)</tt> to display them all on screen at once).
	 * 
	 * @param t number of milliseconds
	 */
	@SuppressWarnings("static-access")
	public static void show(int t) {
		defer = false;
		draw();
		try {
			Thread.currentThread().sleep(t);
		} catch (InterruptedException e) {
			System.out.println("Error sleeping");
		}
		defer = true;
	}

	/**
	 * Display on-screen and turn off animation mode: subsequent calls to drawing
	 * methods such as <tt>line()</tt>, <tt>circle()</tt>, and <tt>square()</tt>
	 * will be displayed on screen when called. This is the default.
	 */
	public static void show() {
		defer = false;
		draw();
	}

	// draw onscreen if defer is false
	public static void draw() {
		if (defer)
			return;
		onscreen.drawImage(offscreenImage, 0, 0, null);
		frame.repaint();
	}

	/*************************************************************************
	 * Save drawing to a file.
	 *************************************************************************/

	/**
	 * Save onscreen image to file - suffix must be png, jpg, or gif.
	 * 
	 * @param filename the name of the file with one of the required suffixes
	 */
	public static void save(String filename) {
		File file = new File(filename);
		String suffix = filename.substring(filename.lastIndexOf('.') + 1);

		// png files
		if (suffix.toLowerCase().equals("png")) {
			try {
				ImageIO.write(onscreenImage, suffix, file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// need to change from ARGB to RGB for jpeg
		// reference:
		// http://archives.java.sun.com/cgi-bin/wa?A2=ind0404&L=java2d-interest&D=0&P=2727
		else if (suffix.toLowerCase().equals("jpg")) {
			WritableRaster raster = onscreenImage.getRaster();
			WritableRaster newRaster;
			newRaster = raster.createWritableChild(0, 0, width, height, 0, 0, new int[] { 0, 1, 2 });
			DirectColorModel cm = (DirectColorModel) onscreenImage.getColorModel();
			DirectColorModel newCM = new DirectColorModel(cm.getPixelSize(), cm.getRedMask(), cm.getGreenMask(),
					cm.getBlueMask());
			BufferedImage rgbBuffer = new BufferedImage(newCM, newRaster, false, null);
			try {
				ImageIO.write(rgbBuffer, suffix, file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		else {
			System.out.println("Invalid image file type: " + suffix);
		}
	}

	/**
	 * This method cannot be called directly.
	 */
	public void actionPerformed(ActionEvent e) {
		FileDialog chooser = new FileDialog(StdDraw.frame, "Use a .png or .jpg extension", FileDialog.SAVE);
		chooser.setVisible(true);
		String filename = chooser.getFile();
		if (filename != null) {
			StdDraw.save(chooser.getDirectory() + File.separator + chooser.getFile());
		}
	}

}
