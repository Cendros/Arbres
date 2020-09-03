package arbre;
/*************************************************************************
 *  Compilation:  javac Turtle.java
 *  Execution:    java Turtle
 *
 *  Data type for turtle graphics using standard draw.
 * 
 * Copyright  2000–2011, Robert Sedgewick and Kevin Wayne. 
 * http://introcs.cs.princeton.edu/java/32class/Turtle.java.html
 * 
 * Small modifications brought by Paul Dorbec on 2018-10-29.
 *************************************************************************/

import java.awt.Color;
import java.util.Random;

/** Allows for drawing with a simple Turtle */
public class Turtle {
	private double x, y, xTronc, yTronc; // turtle is at (x, y)
	private double angle; // facing this many degrees counterclockwise from the x-axis

	/**
	 * Creates the turtle at the specified pose.
	 * 
	 * @param x0 initial X-coordinate of the turtle
	 * @param y0 initial Y-coordinate of the turtle
	 * @param a0 initial orientation of the turtle : degrees counterclockwise from
	 *           the x-axis.
	 */
	public Turtle(double x0, double y0, double a0) {
		x = x0;
		y = y0;
		xTronc = x0;
		yTronc = y0;
		angle = a0;
	}

	/**
	 * Rotates the turtle.
	 * 
	 * @param delta degrees for turning counterclockwise
	 */
	public void turnLeft(double delta) {
		angle += delta;
	}

	/**
	 * Moves the turtle forward, with the pen down.
	 * 
	 * @param step the length of the straight move
	 */
	public void goForward(double step) {
		double oldx = x;
		double oldy = y;
		x += step * Math.cos(Math.toRadians(angle));
		y += step * Math.sin(Math.toRadians(angle));
		StdDraw.line(oldx, oldy, x, y);
	}

	/**
	 * Waits for some time.
	 * 
	 * @param t milliseconds to wait
	 */
	public void pause(int t) {
		StdDraw.show(t);
	}

	/**
	 * Chooses the drawing color.
	 * 
	 * @param color the color to draw with
	 */
	public void setPenColor(Color color) {
		StdDraw.setPenColor(color);
	}

	/**
	 * Chooses the pen radius.
	 * 
	 * @param radius the pen radius
	 */
	public void setPenRadius(double radius) {
		StdDraw.setPenRadius(radius);
	}

	/**
	 * Chooses the on-screen canvas dimension.
	 * 
	 * @param width  the width of the drawing area in pixels
	 * @param height the height of the drawing area in pixels
	 */
	public void setCanvasSize(int width, int height) {
		StdDraw.setCanvasSize(width, height);
	}

	/**
	 * Draws a circle
	 * 
	 * @param r the radius
	 */
	public void circle(double r) {
		StdDraw.circle(x, y, r);
	}

	public void fillcircle(double r) {
		StdDraw.filledCircle(x, y, r);
	}

	/**
	 * Moves the turtle forward, with the pen up.
	 * 
	 * @param step the length of the straight move
	 */

	public void moveForward(double step) {
		x += step * Math.cos(Math.toRadians(angle));
		y += step * Math.sin(Math.toRadians(angle));
	}

	public void jumpToNewTree(double x, double y, double angle) {
		this.x = x;
		this.y = y;
		this.xTronc = x;
		this.yTronc = y;
		this.angle = angle;
	}

	public void jumpTo(double x, double y, double angle) {
		this.x = x;
		this.y = y;
		this.angle = angle;
	}

	public double getPenRadius() {
		return StdDraw.getPenRadius();
	}

	public void pointA(double x, double y, double x0, double y0) {
		x = x / StdDraw.getWidth() + x0;
		y = y / StdDraw.getHeight() + y0;
		StdDraw.point(x, y);
	}

	public void draw() {
		StdDraw.draw();

	}

	public void fillTriangle(double d) {
		Random random = new Random();
		double[] pointsX = new double[3], pointY = new double[3], angle = new double[3];
		angle[0] = (double) random.nextInt(100) + 70;
		angle[1] = (double) random.nextInt(100) + 70;
		angle[2] = 360 - (angle[0] + angle[1]);
		for (int i = 0; i < 3; i++) {
			this.goForward(d);
			pointsX[i] = this.x;
			pointY[i] = this.y;
			this.goForward(-d);
			turnLeft(angle[i]);
		}
		StdDraw.filledPolygon(pointsX, pointY);
	}

	public void particules(double tailleFeuille) {
		double radius = this.getPenRadius();
		this.setPenRadius(tailleFeuille);
		Random random = new Random();
		for (int i = 0; i < 25; i++) {
			double x = (random.nextInt((int) (2 * tailleFeuille * 0.9 * 1000.0)) / 1000.0) - tailleFeuille * 0.9;
			double y = (random.nextInt((int) (2 * tailleFeuille * 0.9 * 1000.0)) / 1000.0) - tailleFeuille * 0.9;
			StdDraw.point(this.x + x, this.y + y);

		}
		this.setPenRadius(radius);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getAngle() {
		return angle;
	}

	public double getXTronc() {
		return xTronc;
	}

	public double getYTronc() {
		return yTronc;
	}

} // class Turtle
