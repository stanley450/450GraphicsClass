import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * Program Assignment for Computer Graphics
 * 
 * Graphics engine that creates and messes with objects
 * 
 * @author Landon Stanley
 * 
 */
@SuppressWarnings("serial")
public class GraphicsProgram extends JFrame {

	// Dimensions for the screen
	final int width = 1000;
	final int height = 800;

	// Distance from the eye to the center of the screen
	private int eye = 1000;

	Image frameImage;

	private boolean backface = false;
	private int polyfillindex = 0;
	private boolean polyfilledges = false;
	private boolean polyfillnoedges = false;
	private boolean zbuffertoggle = false;

	private int currentIndex = 0;

	public class Point2d {
		double x, y;

		Point2d(double xpoint, double ypoint) {
			this.x = xpoint;
			this.y = ypoint;
		}
	}

	public Point2d subtractPoints2d(Point2d pointA, Point2d pointB) {
		Point2d temp = new Point2d(0, 0);

		temp.x = pointA.x - pointB.x;
		temp.y = pointA.y - pointB.y;

		return temp;
	}

	public Point2d addPoints2d(Point2d pointA, Point2d pointB) {
		Point2d temp = new Point2d(0, 0);

		temp.x = pointA.x + pointB.x;
		temp.y = pointA.y + pointB.y;

		return temp;
	}

	public Point2d multiPoints2d(Point2d point, double scale) {
		Point2d temp = new Point2d(0, 0);

		temp.x = point.x * scale;
		temp.y = point.y * scale;

		return temp;
	}

	public class Point3d {
		double x, y, z;

		Point3d(double xpoint, double ypoint, double zpoint) {
			this.x = xpoint;
			this.y = ypoint;
			this.z = zpoint;
		}
	}

	public Point3d subtractPoints(Point3d pointA, Point3d pointB) {
		Point3d temp = new Point3d(0, 0, 0);

		temp.x = pointA.x - pointB.x;
		temp.y = pointA.y - pointB.y;
		temp.z = pointA.z - pointB.z;

		return temp;
	}

	public Point3d addPoints(Point3d pointA, Point3d pointB) {
		Point3d temp = new Point3d(0, 0, 0);

		temp.x = pointA.x + pointB.x;
		temp.y = pointA.y + pointB.y;
		temp.z = pointA.z + pointB.z;

		return temp;
	}

	public Point3d multiPoints(Point3d point, double scale) {
		Point3d temp = new Point3d(0, 0, 0);

		temp.x = point.x * scale;
		temp.y = point.y * scale;
		temp.z = point.z * scale;

		return temp;
	}

	// function used with the keylistener to choose the current polygon you are
	// using
	private void currentIndexChecker(boolean bool) {
		if (bool) {// "page up" key press - go through poly list from 0 to end
			if (polyList.size() - 2 >= currentIndex) {
				currentIndex++;
			} else {
				currentIndex = 0;
			}
		} else {// "page down" key press - go through poly list from end to 0
			if (currentIndex > 0) {
				currentIndex--;
			} else {
				currentIndex = polyList.size() - 1;
			}
		}
	}

	private void deleteLastPoly() {// press delete key to delete the last
									// polygon
		if (currentIndex == polyList.size() - 1) {
			currentIndex = currentIndex - 1;
			polyList.remove(polyList.size() - 1);
		} else {
			polyList.remove(polyList.size() - 1);
		}

	}

	/**
	 * Method taking a point and a zdistance that returns the prospective
	 * projection of a point in the program
	 * 
	 * @param point
	 * @param zdis
	 * @return int
	 */
	private double perspective(double point, double zdis) {
		return ((eye * point) / (eye + zdis));
	}

	// Vector list of all of the current Polygon objects in the program
	Vector<Polygon> polyList = new Vector<Polygon>();

	/**
	 * Constructor for the main graphics engine
	 */
	public GraphicsProgram() {
		setPreferredSize(new Dimension(width, height));
		setMinimumSize(new Dimension(width, height));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Please Work");
		addKeyListener(new myKeyListener());
		Polygon mangle = new Polygon();
		polyList.add(mangle);
		setVisible(true);
	}

	public class Triangle2D {

		Point2d p0, p1, p2;

		Triangle2D(Triangle3D trng) {
			this.p0 = new Point2d(perspective(trng.p0.x, trng.p0.z), perspective(trng.p0.y, trng.p0.z));
			this.p1 = new Point2d(perspective(trng.p1.x, trng.p1.z), perspective(trng.p1.y, trng.p1.z));
			this.p2 = new Point2d(perspective(trng.p2.x, trng.p2.z), perspective(trng.p2.y, trng.p2.z));
		}
	}

	public class Triangle3D {

		Point3d p0, p1, p2;

		Triangle3D(Point3d point1, Point3d point2, Point3d point3) {
			this.p0 = point1;
			this.p1 = point2;
			this.p2 = point3;
		}

		Edge edge1 = new Edge(p0, p1);
		Edge edge2 = new Edge(p1, p2);
		Edge edge3 = new Edge(p2, p1);

	}
	
	public class Edge {
		
		Point3d p1, p2;

		Edge(Point3d pointA, Point3d pointB) {

			this.p1 = pointA;
			this.p2 = pointB;

//			if (p1.y > p2.y) {
//				ymax = p1.y;
//				xformax = p1.x;
//				ymin = p2.y;
//				xformin = p2.x;
//			} else {
//				ymax = p2.y;
//				xformax = p2.x;
//				ymin = p1.y;
//				xformin = p1.x;
//			}
//
//			dx = -((xformax - xformin) / (ymax - ymin));
//			x = xformax + (dx / 2);

		}

		boolean edgebool = false;

		double ymax;
		double xformax;
		double ymin;
		double xformin;
		double x;
		double dx;

	}

	public void drawTriangle(Graphics g, Triangle2D trng, int xoff, int yoff) {
		Graphics2D g2d = (Graphics2D) g;

		g2d.drawLine((int) (trng.p0.x + xoff), (int) (yoff - trng.p0.y),
				(int) (trng.p1.x + xoff), (int) (yoff - trng.p1.y));
		g2d.drawLine((int) (trng.p1.x + xoff), (int) (yoff - trng.p1.y),
				(int) (trng.p2.x + xoff), (int) (yoff - trng.p2.y));
		g2d.drawLine((int) (trng.p0.x + xoff), (int) (yoff - trng.p0.y),
				(int) (trng.p2.x + xoff), (int) (yoff - trng.p2.y));
	}

	/**
	 * Polygon class for graphics program
	 */
	class Polygon {

		// x y z positions of the a figure
		Point3d[] polyPosition = new Point3d[5];

		// normals for the current triangle and each face
		Point3d[] normalPoints = new Point3d[6];

		boolean[] isVisible = new boolean[6];

		// plane offset "D" normal of the thing and a point on poly
		double[] normOffset = new double[6];

		// logical midpoint of the polygon
		Point3d polyMidPoint;

		// actual 2d dimensions of the midpoint
		Point2d perspectivePolygonMidpoint;

		boolean isFocused = false;

		Triangle3D[] triangleList = new Triangle3D[6];

		// actual 2d dimensions of the polygon
		Point2d[] perspectivePolygonPoints = new Point2d[5];

		/**
		 * Polygon constructor
		 */
		public Polygon() {
			setDefault(this);
			setFocusedPolygon(true);
			setTriangles();
		}

		public void setTriangles() {
			// Different polys for the figure are
			// Triangle 1 - p0, p1, p2
			triangleList[0] = new Triangle3D(polyPosition[0], polyPosition[1],
					polyPosition[2]);
			// Triangle 2 - p0, p3, p1
			triangleList[1] = new Triangle3D(polyPosition[0], polyPosition[3],
					polyPosition[1]);
			// Triangle 3 - p0, p3, p4
			triangleList[2] = new Triangle3D(polyPosition[0], polyPosition[4],
					polyPosition[3]);
			// Triangle 4 - p0, p4, p2
			triangleList[3] = new Triangle3D(polyPosition[0], polyPosition[2],
					polyPosition[4]);
			// Triangle 5 - p1, p3, p2
			triangleList[4] = new Triangle3D(polyPosition[1], polyPosition[3],
					polyPosition[2]);
			// Triangle 6 - p4, p2, p3
			triangleList[5] = new Triangle3D(polyPosition[4], polyPosition[2],
					polyPosition[3]);
		}

		/**
		 * Sets the current Polygon to be focused by the program
		 * 
		 * @param boolean bool
		 */
		public void setFocusedPolygon(boolean bool) {
			isFocused = bool;
		}

		/**
		 * returns wether or not the current Polygon is focused
		 * 
		 * @return boolean
		 */
		public boolean getFocusedPolygon() {
			return isFocused;
		}
	}

	public void closeOnEscape() {
		this.dispose();
	}

	/**
	 * KeyListener class for the graphics program that takes the keyboard input
	 */
	public class myKeyListener extends KeyAdapter {

		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_ESCAPE:
				closeOnEscape();
			case KeyEvent.VK_INSERT: // press "insert" key to create a new
										// object and select it
				polyList.add(new Polygon());
				currentIndex = polyList.size() - 1;
				break;
			case KeyEvent.VK_PAGE_UP: // press "page up" key to cycle through
										// objects from the first object and
										// each one after that
				currentIndexChecker(true);
				break;
			case KeyEvent.VK_PAGE_DOWN: // press "page down" key to cycle
										// through objects from the last object
										// and each one after that
				currentIndexChecker(false);
				break;
			case KeyEvent.VK_DELETE:
				deleteLastPoly();
				break;
			case KeyEvent.VK_U: // press "u" key on the keyboard to move pyramid
								// up
				moveUp(polyList.get(currentIndex));
				break;
			case KeyEvent.VK_D: // press "d" key on the keyboard to move pyramid
								// down
				moveDwn(polyList.get(currentIndex));
				break;
			case KeyEvent.VK_C: // Back face culling stuff goes here
				backface = !backface;
				break;
			case KeyEvent.VK_X:
				if (polyfillindex > 1) {
					polyfillindex = 0;
				} else {
					polyfillindex++;
				}
				if (polyfillindex == 1) {
					polyfilledges = true;
				} else if (polyfillindex == 2) {
					polyfillnoedges = true;
				} else {
					polyfilledges = false;
					polyfillnoedges = false;
				}
				System.out.println("polyfillindex = " + polyfillindex);
				break;
			case KeyEvent.VK_Z:
				zbuffertoggle = !zbuffertoggle;
				break;
			case KeyEvent.VK_R: // press "r" key on the keyboard to move pyramid
								// right
				moveRght(polyList.get(currentIndex));
				break;
			case KeyEvent.VK_L: // press "l" key on keyboard to move pyramid
								// left
				moveLft(polyList.get(currentIndex));
				break;
			case KeyEvent.VK_UP:
				if (e.isShiftDown()) {// press shift + up arrow to scale the
										// pyramid up in size
					scaleUp(polyList.get(currentIndex));
				} else {
					rotateX(polyList.get(currentIndex), true); // rotate UP
																// along x axis
				}
				break;
			case KeyEvent.VK_DOWN:
				if (e.isShiftDown()) {// press shift + down arrow to scale the
										// pyramid down in size
					scaleDwn(polyList.get(currentIndex));
				} else {
					rotateX(polyList.get(currentIndex), false);// rotate DOWN
																// along x axis
				}
				break;
			case KeyEvent.VK_RIGHT: // press right arrow key to rotate right
									// around the y axis
				rotateY(polyList.get(currentIndex), true);
				break;
			case KeyEvent.VK_LEFT: // press right arrow key to rotate right
									// around the y axis
				rotateY(polyList.get(currentIndex), false);
				break;
			case KeyEvent.VK_PERIOD: // press the ">" key to rotate the pyramid
										// right along the z axis
				rotateZ(polyList.get(currentIndex), false);
				break;
			case KeyEvent.VK_COMMA: // press the "<" key to rotate the pyramid
									// left along the z axis
				rotateZ(polyList.get(currentIndex), true);
				break;
			case KeyEvent.VK_F: // press the "f" key to move the pyramid in the
								// positive z direction
				moveForward(polyList.get(currentIndex));
				break;
			case KeyEvent.VK_B:// press the "b" key to move the pyramid in the
								// negative z direction
				moveBackward(polyList.get(currentIndex));
				break;
			case KeyEvent.VK_ENTER: // press enter to return to the default
									// position
				setDefault(polyList.get(currentIndex));
				break;
			}
			repaint();
		}

	}

	/**
	 * Method that takes a Polygon and sets its value to a default position
	 * 
	 * @param Polygon
	 *            poly
	 */
	public void setDefault(Polygon poly) {
		System.out.println("set default");
		// p0
		poly.polyPosition[0] = new Point3d(0, 150, 200);

		// p1
		poly.polyPosition[1] = new Point3d(-150, -150, 100);

		// p2
		poly.polyPosition[2] = new Point3d(150, -150, 100);
		
		// p3
		poly.polyPosition[3] = new Point3d(-150, -150, 300);

		// p4
		poly.polyPosition[4] = new Point3d(150, -150, 300);

		poly.polyMidPoint = new Point3d(0, 0, 200);

	}

	public void calcSurfaceNormals(Polygon poly) {

		// Triangle 1 normals
		// vector 1
		Point3d vect1 = subtractPoints(poly.polyPosition[1],
				poly.polyPosition[0]);

		// vector 2
		Point3d vect2 = subtractPoints(poly.polyPosition[2],
				poly.polyPosition[0]);

		// normal vector from the two vectors of the triangle
		poly.normalPoints[0] = new Point3d((vect1.y * vect2.z)
				- (vect1.z * vect2.y), -(vect1.z * vect2.x)
				- (vect1.x * vect2.z), (vect1.x * vect2.y)
				- (vect1.y * vect2.x));

		// "plane offset" D used in calc
		poly.normOffset[0] = (poly.polyPosition[0].x * poly.normalPoints[0].x)
				- (poly.polyPosition[0].y * poly.normalPoints[0].y)
				+ (poly.polyPosition[0].z * poly.normalPoints[0].z);

		// final equation to compute visibility
		double visible = (-eye * poly.normalPoints[0].z) - poly.normOffset[0];

		// set visible
		if (visible < 0) {
			poly.isVisible[0] = true;
		} else {
			poly.isVisible[0] = false;
		}

		// rinse, repeat

		// Triangle 2 normals
		// vector 1
		vect1 = subtractPoints(poly.polyPosition[3], poly.polyPosition[0]);

		// vector 2
		vect2 = subtractPoints(poly.polyPosition[1], poly.polyPosition[0]);

		// normal vector from the two vectors of the triangle
		poly.normalPoints[1] = new Point3d((vect1.y * vect2.z)
				- (vect1.z * vect2.y), -(vect1.z * vect2.x)
				- (vect1.x * vect2.z), (vect1.x * vect2.y)
				- (vect1.y * vect2.x));

		// "plane offset" D used in calc
		poly.normOffset[1] = (poly.polyPosition[3].x * poly.normalPoints[1].x)
				- (poly.polyPosition[3].y * poly.normalPoints[1].y)
				+ (poly.polyPosition[3].z * poly.normalPoints[1].z);

		// final equation to compute visibility
		visible = (-eye * poly.normalPoints[1].z) - poly.normOffset[1];

		// set visible
		if (visible < 0) {
			poly.isVisible[1] = true;
		} else {
			poly.isVisible[1] = false;
		}

		// Triangle 3 normals
		// vector 1
		vect1 = subtractPoints(poly.polyPosition[4], poly.polyPosition[0]);

		// vector 2
		vect2 = subtractPoints(poly.polyPosition[3], poly.polyPosition[0]);

		// normal vector from the two vectors of the triangle
		poly.normalPoints[2] = new Point3d((vect1.y * vect2.z)
				- (vect1.z * vect2.y), -(vect1.z * vect2.x)
				- (vect1.x * vect2.z), (vect1.x * vect2.y)
				- (vect1.y * vect2.x));

		// "plane offset" D used in calc
		poly.normOffset[2] = (poly.polyPosition[4].x * poly.normalPoints[2].x)
				- (poly.polyPosition[4].y * poly.normalPoints[2].y)
				+ (poly.polyPosition[4].z * poly.normalPoints[2].z);

		// final equation to compute visibility
		visible = (-eye * poly.normalPoints[2].z) - poly.normOffset[2];

		// set visible
		if (visible < 0) {
			poly.isVisible[2] = true;
		} else {
			poly.isVisible[2] = false;
		}

		// Triangle 4 normals
		// vector 1
		vect1 = subtractPoints(poly.polyPosition[2], poly.polyPosition[0]);

		// vector 2
		vect2 = subtractPoints(poly.polyPosition[4], poly.polyPosition[0]);

		// normal vector from the two vectors of the triangle
		poly.normalPoints[3] = new Point3d((vect1.y * vect2.z)
				- (vect1.z * vect2.y), -(vect1.z * vect2.x)
				- (vect1.x * vect2.z), (vect1.x * vect2.y)
				- (vect1.y * vect2.x));

		// "plane offset" D used in calc
		poly.normOffset[3] = (poly.polyPosition[2].x * poly.normalPoints[3].x)
				- (poly.polyPosition[2].y * poly.normalPoints[3].y)
				+ (poly.polyPosition[2].z * poly.normalPoints[3].z);

		// final equation to compute visibility
		visible = (-eye * poly.normalPoints[3].z) - poly.normOffset[3];

		// set visible
		if (visible < 0) {
			poly.isVisible[3] = true;
		} else {
			poly.isVisible[3] = false;
		}

		// Triangle 5 normals
		// vector 1
		vect1 = subtractPoints(poly.polyPosition[3], poly.polyPosition[1]);

		// vector 2
		vect2 = subtractPoints(poly.polyPosition[2], poly.polyPosition[1]);

		// normal vector from the two vectors of the triangle
		poly.normalPoints[4] = new Point3d((vect1.y * vect2.z)
				- (vect1.z * vect2.y), -(vect1.z * vect2.x)
				- (vect1.x * vect2.z), (vect1.x * vect2.y)
				- (vect1.y * vect2.x));

		// "plane offset" D used in calc
		poly.normOffset[4] = (poly.polyPosition[3].x * poly.normalPoints[4].x)
				- (poly.polyPosition[3].y * poly.normalPoints[4].y)
				+ (poly.polyPosition[3].z * poly.normalPoints[4].z);

		// final equation to compute visibility
		visible = (-eye * poly.normalPoints[4].z) - poly.normOffset[4];

		// set visible
		if (visible < 0) {
			poly.isVisible[4] = true;
		} else {
			poly.isVisible[4] = false;
		}

		// Triangle 6 normals
		// vector 1
		vect1 = subtractPoints(poly.polyPosition[2], poly.polyPosition[4]);

		// vector 2
		vect2 = subtractPoints(poly.polyPosition[3], poly.polyPosition[4]);

		// normal vector from the two vectors of the triangle
		poly.normalPoints[5] = new Point3d((vect1.y * vect2.z)
				- (vect1.z * vect2.y), -(vect1.z * vect2.x)
				- (vect1.x * vect2.z), (vect1.x * vect2.y)
				- (vect1.y * vect2.x));

		// "plane offset" D used in calc
		poly.normOffset[5] = (poly.polyPosition[3].x * poly.normalPoints[5].x)
				- (poly.polyPosition[3].y * poly.normalPoints[5].y)
				+ (poly.polyPosition[3].z * poly.normalPoints[5].z);

		// final equation to compute visibility
		visible = (-eye * poly.normalPoints[5].z) - poly.normOffset[5];

		// set visible
		if (visible < 0) {
			poly.isVisible[5] = true;
		} else {
			poly.isVisible[5] = false;
		}

	}

	public void calcMidPoints(Polygon poly) {

		Point3d min, max;

		min = poly.polyPosition[0];
		max = poly.polyPosition[0];

		for (int i = 0; i < 5; i++) {
			if (min.x > poly.polyPosition[i].x)
				min.x = poly.polyPosition[i].x;
			if (min.y > poly.polyPosition[i].y)
				min.x = poly.polyPosition[i].y;
			if (min.z > poly.polyPosition[i].z)
				min.x = poly.polyPosition[i].z;
			if (max.x > poly.polyPosition[i].x)
				max.x = poly.polyPosition[i].x;
			if (max.y > poly.polyPosition[i].y)
				max.y = poly.polyPosition[i].y;
			if (max.z > poly.polyPosition[i].z)
				max.z = poly.polyPosition[i].z;
		}
		poly.polyMidPoint.x = (min.x + max.x) / 2;
		poly.polyMidPoint.y = (min.y + max.y) / 2;
		poly.polyMidPoint.z = (min.z + max.z) / 2;
	}

	public void fillPolygon(Polygon poly) {

	}

	/**
	 * Method that draws all of the possible Polygons on the screen
	 * 
	 * @param g
	 */
	public void drawPolygon(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;

		int xoff = width / 2;
		int yoff = height / 2;

		for (int p = 0; p < polyList.size(); p++) {
			if (currentIndex == p) {
				polyList.get(p).setFocusedPolygon(true);
			} else {
				polyList.get(p).setFocusedPolygon(false);
			}
		}

		for (int i = 0; i < polyList.size(); i++) {

			// finds the currently used Polygon and makes its lines wider
			if (polyList.get(i).getFocusedPolygon()) {
				g2d.setStroke(new BasicStroke(5));
			}

			 for (int k = 0; k < 5; k++) {
			// polyList.get(i).currXPos[k] = perspective(
			// polyList.get(i).defXPos[k], polyList.get(i).defZPos[k]);
			// polyList.get(i).currYPos[k] = perspective(
			// polyList.get(i).defYPos[k], polyList.get(i).defZPos[k]);
				 
				 polyList.get(i).perspectivePolygonPoints[k] = new Point2d(
						 perspective(polyList.get(i).polyPosition[k].x, polyList.get(i).polyPosition[k].z), 
						 perspective(polyList.get(i).polyPosition[k].y, polyList.get(i).polyPosition[k].z));
			 }

			Triangle2D[] templist = new Triangle2D[polyList.get(i).triangleList.length];

			g2d.setColor(Color.BLACK);

			for (int q = 0; q < polyList.get(i).triangleList.length; q++) {
				templist[q] = new Triangle2D(polyList.get(i).triangleList[q]);
				drawTriangle(g, templist[q], xoff, yoff);
			}

			// g2d.setColor(Color.BLACK);
			// g2d.drawLine((int) (polyList.get(i).currXPos[0] + xoff),
			// (int) (yoff - polyList.get(i).currYPos[0]),
			// (int) (polyList.get(i).currXPos[1] + xoff),
			// (int) (yoff - polyList.get(i).currYPos[1]));
			// // top to outside bottom left
			// g2d.drawLine((int) (polyList.get(i).currXPos[0] + xoff),
			// (int) (yoff - polyList.get(i).currYPos[0]),
			// (int) (polyList.get(i).currXPos[2] + xoff),
			// (int) (yoff - polyList.get(i).currYPos[2]));
			// // top to outside bottom right
			// g2d.drawLine((int) (polyList.get(i).currXPos[0] + xoff),
			// (int) (yoff - polyList.get(i).currYPos[0]),
			// (int) (polyList.get(i).currXPos[3] + xoff),
			// (int) (yoff - polyList.get(i).currYPos[3]));
			// // top to inside bottom left
			// g2d.drawLine((int) (polyList.get(i).currXPos[4] + xoff),
			// (int) (yoff - polyList.get(i).currYPos[4]),
			// (int) (polyList.get(i).currXPos[0] + xoff),
			// (int) (yoff - polyList.get(i).currYPos[0]));
			// if (polyfilledges) {
			// fillPolygon(polyList.get(i));
			//
			// }
			// // inside bottom right to top
			// g2d.setColor(Color.GREEN); // front side
			// g2d.drawLine((int) (polyList.get(i).currXPos[1] + xoff),
			// (int) (yoff - polyList.get(i).currYPos[1]),
			// (int) (polyList.get(i).currXPos[2] + xoff),
			// (int) (yoff - polyList.get(i).currYPos[2]));
			// // outside bottom left to outside bottom right
			// g2d.setColor(Color.RED); // back side
			// g2d.drawLine((int) (polyList.get(i).currXPos[3] + xoff),
			// (int) (yoff - polyList.get(i).currYPos[3]),
			// (int) (polyList.get(i).currXPos[4] + xoff),
			// (int) (yoff - polyList.get(i).currYPos[4]));
			// // inside bottom left to inside bottom right
			// g2d.setColor(Color.YELLOW); // left side
			// g2d.drawLine((int) (polyList.get(i).currXPos[1] + xoff),
			// (int) (yoff - polyList.get(i).currYPos[1]),
			// (int) (polyList.get(i).currXPos[3] + xoff),
			// (int) (yoff - polyList.get(i).currYPos[3]));
			// // outside bottom left to inside bottom left
			// g2d.setColor(Color.BLUE); // right side
			// g2d.drawLine((int) (polyList.get(i).currXPos[4] + xoff),
			// (int) (yoff - polyList.get(i).currYPos[4]),
			// (int) (polyList.get(i).currXPos[2] + xoff),
			// (int) (yoff - polyList.get(i).currYPos[2]));
			// // inside bottom right to outside bottom right
			// g2d.setColor(Color.BLACK);

			if (polyList.get(i).getFocusedPolygon()) {
				g2d.setStroke(new BasicStroke(1));
			}
		}
	}

	// guess
	// public void drawPolygonWithBackFaceCulling(Graphics g) {
	//
	// Graphics2D g2d = (Graphics2D) g;
	//
	// int xoff = width / 2;
	// int yoff = height / 2;
	//
	// for (int p = 0; p < polyList.size(); p++) {
	// if (currentIndex == p) {
	// polyList.get(p).setFocusedPolygon(true);
	// } else {
	// polyList.get(p).setFocusedPolygon(false);
	// }
	// }
	//
	// for (int i = 0; i < polyList.size(); i++) {
	//
	// calcSurfaceNormals(polyList.get(i));
	//
	// // finds the currently used Polygon and makes its lines wider
	// if (polyList.get(i).getFocusedPolygon()) {
	// g2d.setStroke(new BasicStroke(5));
	// }
	//
	// // for (int k = 0; k < 5; k++) {
	// // polyList.get(i).currXPos[k] = perspective(
	// // polyList.get(i).defXPos[k], polyList.get(i).defZPos[k]);
	// // polyList.get(i).currYPos[k] = perspective(
	// // polyList.get(i).defYPos[k], polyList.get(i).defZPos[k]);
	// // }
	//
	// g2d.setColor(Color.BLACK);
	//
	// if (polyList.get(i).isVisible[0] == true) {
	// // p0 to p1
	// g2d.drawLine((int) (polyList.get(i).currXPos[0] + xoff),
	// (int) (yoff - polyList.get(i).currYPos[0]),
	// (int) (polyList.get(i).currXPos[1] + xoff),
	// (int) (yoff - polyList.get(i).currYPos[1]));
	// // p0 to p2
	// g2d.drawLine((int) (polyList.get(i).currXPos[0] + xoff),
	// (int) (yoff - polyList.get(i).currYPos[0]),
	// (int) (polyList.get(i).currXPos[2] + xoff),
	// (int) (yoff - polyList.get(i).currYPos[2]));
	// g2d.setColor(Color.GREEN); // front side
	// // p1 to p2
	// g2d.drawLine((int) (polyList.get(i).currXPos[1] + xoff),
	// (int) (yoff - polyList.get(i).currYPos[1]),
	// (int) (polyList.get(i).currXPos[2] + xoff),
	// (int) (yoff - polyList.get(i).currYPos[2]));
	// g2d.setColor(Color.BLACK);
	// }
	//
	// if (polyList.get(i).isVisible[1] == true) {
	// // p0 to p1
	// g2d.drawLine((int) (polyList.get(i).currXPos[0] + xoff),
	// (int) (yoff - polyList.get(i).currYPos[0]),
	// (int) (polyList.get(i).currXPos[1] + xoff),
	// (int) (yoff - polyList.get(i).currYPos[1]));
	// // p0 to p3
	// g2d.drawLine((int) (polyList.get(i).currXPos[0] + xoff),
	// (int) (yoff - polyList.get(i).currYPos[0]),
	// (int) (polyList.get(i).currXPos[3] + xoff),
	// (int) (yoff - polyList.get(i).currYPos[3]));
	// g2d.setColor(Color.YELLOW); // left side
	// // p1 to p3
	// g2d.drawLine((int) (polyList.get(i).currXPos[1] + xoff),
	// (int) (yoff - polyList.get(i).currYPos[1]),
	// (int) (polyList.get(i).currXPos[3] + xoff),
	// (int) (yoff - polyList.get(i).currYPos[3]));
	// g2d.setColor(Color.BLACK);
	// }
	//
	// if (polyList.get(i).isVisible[2] == true) {
	// // p0 to p3
	// g2d.drawLine((int) (polyList.get(i).currXPos[0] + xoff),
	// (int) (yoff - polyList.get(i).currYPos[0]),
	// (int) (polyList.get(i).currXPos[3] + xoff),
	// (int) (yoff - polyList.get(i).currYPos[3]));
	// // p4 to p0
	// g2d.drawLine((int) (polyList.get(i).currXPos[4] + xoff),
	// (int) (yoff - polyList.get(i).currYPos[4]),
	// (int) (polyList.get(i).currXPos[0] + xoff),
	// (int) (yoff - polyList.get(i).currYPos[0]));
	// g2d.setColor(Color.RED); // back side
	// // p3 to p4
	// g2d.drawLine((int) (polyList.get(i).currXPos[3] + xoff),
	// (int) (yoff - polyList.get(i).currYPos[3]),
	// (int) (polyList.get(i).currXPos[4] + xoff),
	// (int) (yoff - polyList.get(i).currYPos[4]));
	// g2d.setColor(Color.BLACK);
	// }
	//
	// if (polyList.get(i).isVisible[3] == true) {
	// // p4 to p0
	// g2d.drawLine((int) (polyList.get(i).currXPos[4] + xoff),
	// (int) (yoff - polyList.get(i).currYPos[4]),
	// (int) (polyList.get(i).currXPos[0] + xoff),
	// (int) (yoff - polyList.get(i).currYPos[0]));
	// // p0 to p2
	// g2d.drawLine((int) (polyList.get(i).currXPos[0] + xoff),
	// (int) (yoff - polyList.get(i).currYPos[0]),
	// (int) (polyList.get(i).currXPos[2] + xoff),
	// (int) (yoff - polyList.get(i).currYPos[2]));
	// g2d.setColor(Color.BLUE); // right side
	// // p4 to p2
	// g2d.drawLine((int) (polyList.get(i).currXPos[4] + xoff),
	// (int) (yoff - polyList.get(i).currYPos[4]),
	// (int) (polyList.get(i).currXPos[2] + xoff),
	// (int) (yoff - polyList.get(i).currYPos[2]));
	// g2d.setColor(Color.BLACK);
	// }
	//
	// if (polyList.get(i).isVisible[4] == true) {
	// g2d.setColor(Color.BLUE); // right side
	// // p4 to p2
	// g2d.drawLine((int) (polyList.get(i).currXPos[4] + xoff),
	// (int) (yoff - polyList.get(i).currYPos[4]),
	// (int) (polyList.get(i).currXPos[2] + xoff),
	// (int) (yoff - polyList.get(i).currYPos[2]));
	// g2d.setColor(Color.RED); // back side
	// // p3 to p4
	// g2d.drawLine((int) (polyList.get(i).currXPos[3] + xoff),
	// (int) (yoff - polyList.get(i).currYPos[3]),
	// (int) (polyList.get(i).currXPos[4] + xoff),
	// (int) (yoff - polyList.get(i).currYPos[4]));
	// g2d.setColor(Color.YELLOW); // left side
	// // p1 to p3
	// g2d.drawLine((int) (polyList.get(i).currXPos[1] + xoff),
	// (int) (yoff - polyList.get(i).currYPos[1]),
	// (int) (polyList.get(i).currXPos[3] + xoff),
	// (int) (yoff - polyList.get(i).currYPos[3]));
	// g2d.setColor(Color.GREEN); // front side
	// // p1 to p2
	// g2d.drawLine((int) (polyList.get(i).currXPos[1] + xoff),
	// (int) (yoff - polyList.get(i).currYPos[1]),
	// (int) (polyList.get(i).currXPos[2] + xoff),
	// (int) (yoff - polyList.get(i).currYPos[2]));
	// g2d.setColor(Color.BLACK);
	// }
	//
	// g2d.setColor(Color.BLACK);
	//
	// if (polyList.get(i).getFocusedPolygon()) {
	// g2d.setStroke(new BasicStroke(1));
	// }
	// }
	//
	// }

	/**
	 * Method that moves all of the Y points of the selected polygon in the
	 * positive y position
	 * 
	 * @param Polygon
	 *            poly
	 */
	public void moveUp(Polygon poly) {
		Boolean actionBool = false;
		for (int i = 0; i < 5; i++) {
			if (!(poly.perspectivePolygonPoints[i].y >= (height / 2))) {
				actionBool = true;
			} else {
				actionBool = false;
				break;
			}
		}

		calcMidPoints(poly);

		if (actionBool == true) {
			for (int i = 0; i < 5; i++) {
				poly.polyPosition[i].y += 10;
			}
			poly.polyMidPoint.y += 10;
		}
	}

	/**
	 * Method that move all of the Y points of the selected polygon in the
	 * negative y position
	 * 
	 * @param Polygon
	 *            poly
	 */
	public void moveDwn(Polygon poly) {
		Boolean actionBool = false;
		for (int i = 0; i < 5; i++) {
			if (!(poly.perspectivePolygonPoints[i].y <= -height / 2)) {
				actionBool = true;
			} else {
				actionBool = false;
				break;
			}
		}

		calcMidPoints(poly);

		if (actionBool == true) {
			for (int i = 0; i < 5; i++) {
				poly.polyPosition[i].y -= 10;
			}
			poly.polyMidPoint.y -= 10;
		}
	}

	/**
	 * Method that moves all of the x points of the selected polygon in the
	 * positive x position
	 * 
	 * @param Polygon
	 *            poly
	 */
	public void moveRght(Polygon poly) {
		Boolean actionBool = false;
		for (int i = 0; i < 5; i++) {
			if (!(poly.perspectivePolygonPoints[i].x >= width / 2)) {
				actionBool = true;
			} else {
				actionBool = false;
				break;
			}
		}

		calcMidPoints(poly);

		if (actionBool == true) {
			for (int i = 0; i < 5; i++) {
				poly.polyPosition[i].x += 10;
			}
			poly.polyMidPoint.x += 10;
		}
	}

	/**
	 * Method that moves all of the x points of the selected polygon in the
	 * negative x positon
	 * 
	 * @param Polygon
	 *            poly
	 */
	public void moveLft(Polygon poly) {
		Boolean actionBool = false;
		for (int i = 0; i < 5; i++) {
			if (!(poly.perspectivePolygonPoints[i].x <= -(width / 2))) {
				actionBool = true;
			} else {
				actionBool = false;
				break;
			}
		}

		calcMidPoints(poly);

		if (actionBool == true) {
			for (int i = 0; i < 5; i++) {
				poly.polyPosition[i].x -= 10;
			}
			poly.polyMidPoint.x -= 10;
		}
	}

	/**
	 * Method that increases the size of selected polygon by increasing the
	 * distance of all the points from one another
	 * 
	 * @param Polygon
	 *            poly
	 */
	public void scaleUp(Polygon poly) {
		Boolean actionBool = false;
		for (int i = 0; i < 5; i++) {
			if (!(poly.perspectivePolygonPoints[i].x >= width / 2
					|| poly.perspectivePolygonPoints[i].y >= height / 2
					|| poly.perspectivePolygonPoints[i].y <= -height / 2 || poly.perspectivePolygonPoints[i].x <= -(width / 2))) {
				actionBool = true;
			} else {
				actionBool = false;
				break;
			}
		}

		calcMidPoints(poly);

		if (actionBool == true) {
			for (int i = 0; i < 5; i++) {
				poly.polyPosition[i] = subtractPoints(poly.polyPosition[i],
						poly.polyMidPoint);
				poly.polyPosition[i] = multiPoints(poly.polyPosition[i], 1.05);
				poly.polyPosition[i] = addPoints(poly.polyPosition[i],
						poly.polyMidPoint);
			}
		}
	}

	/**
	 * Method that decreases the size of a polygon by decreasing the distance of
	 * all the points from one another
	 * 
	 * @param Polygon
	 *            poly
	 */
	public void scaleDwn(Polygon poly) {

		calcMidPoints(poly);

		for (int i = 0; i < 5; i++) {
			poly.polyPosition[i] = subtractPoints(poly.polyPosition[i],
					poly.polyMidPoint);
			poly.polyPosition[i] = multiPoints(poly.polyPosition[i], .95);
			poly.polyPosition[i] = addPoints(poly.polyPosition[i],
					poly.polyMidPoint);
		}
	}

	/**
	 * Method that moves all of the z points of the selected polygon into the
	 * positive z direction (right hand rule)
	 * 
	 * @param Polygon
	 *            poly
	 */
	public void moveForward(Polygon poly) {

		calcMidPoints(poly);

		for (int i = 0; i < 5; i++) {
			poly.polyPosition[i].z -= 100;
		}
		poly.polyMidPoint.z -= 100;
	}

	/**
	 * Method that moves all of the z points of the selected polygon into the
	 * negative z direction (right hand rule)
	 * 
	 * @param Polygon
	 *            poly
	 */
	public void moveBackward(Polygon poly) {

		calcMidPoints(poly);

		for (int i = 0; i < 5; i++) {
			poly.polyPosition[i].z += 100;
		}
		poly.polyMidPoint.z += 100;
	}

	/**
	 * Method that rotates the selected polygon around the z axis
	 * 
	 * @param Polygon
	 *            poly
	 */
	public void rotateZ(Polygon poly, boolean bool) { // counter clockwise

		double theta = 25.0;
		if (bool) {
			theta = -25.0;
		}

		calcMidPoints(poly);

		for (int i = 0; i < 5; i++) {
			double currX = poly.polyPosition[i].x - poly.polyMidPoint.x;
			double currY = poly.polyPosition[i].y - poly.polyMidPoint.y;
			double tempX = (currX * Math.cos(theta) - currY * Math.sin(theta));
			double tempY = (currX * Math.sin(theta) + currY * Math.cos(theta));
			tempX = tempX + poly.polyPosition[i].x;
			tempY = tempY + poly.polyPosition[i].y;
			poly.polyPosition[i].x = tempX;
			poly.polyPosition[i].y = tempY;
		}

	}

	/**
	 * Method that rotates the selected polygon around the y axis
	 * 
	 * @param Polygon
	 *            poly
	 */
	public void rotateY(Polygon poly, boolean bool) {

		double theta = 25.0;
		if (bool) {
			theta = -25.0;
		}

		calcMidPoints(poly);

		for (int i = 0; i < 5; i++) {
			double currX = poly.polyPosition[i].x - poly.polyMidPoint.x;
			double currZ = poly.polyPosition[i].z - poly.polyMidPoint.z;
			double tempX = (currX * Math.cos(theta) - currZ * Math.sin(theta));
			double tempZ = (currX * Math.sin(theta) + currZ * Math.cos(theta));
			tempX = tempX + poly.polyPosition[i].x;
			tempZ = tempZ + poly.polyPosition[i].z;
			poly.polyPosition[i].x = tempX;
			poly.polyPosition[i].z = tempZ;
		}

	}

	/**
	 * Method that rotates the selected polygon around the x axis
	 * 
	 * @param Polygon
	 *            poly
	 */
	public void rotateX(Polygon poly, boolean bool) {

		double theta = 25.0;
		if (bool) {
			theta = -25.0;
		}

		calcMidPoints(poly);
		for (int i = 0; i < 5; i++) {
			double currY = poly.polyPosition[i].y - poly.polyMidPoint.y;
			double currZ = poly.polyPosition[i].z - poly.polyMidPoint.z;
			double tempY = (currY * Math.cos(theta) - currZ * Math.sin(theta));
			double tempZ = (currY * Math.sin(theta) + currZ * Math.cos(theta));
			tempY = tempY + poly.polyPosition[i].y;
			tempZ = tempZ + poly.polyPosition[i].z;
			poly.polyPosition[i].y = tempY;
			poly.polyPosition[i].z = tempZ;
		}

	}

	/**
	 * Method that draws the lines on the page
	 * 
	 * @param g
	 */
	private void drawBackground(Graphics g) {

		setBackground(Color.WHITE);

		Graphics2D g2d = (Graphics2D) g;

		g2d.drawLine((width / 2), 0, (width / 2), height);
		g2d.drawLine(0, (height / 2), width, (height / 2));
	}

	public void paint(Graphics g) {
		frameImage = createImage(getWidth(), getHeight());
		Graphics frameGraphics = frameImage.getGraphics();
		paintComponent(frameGraphics);
		g.drawImage(frameImage, 0, 0, this);
	}

	/**
	 * Method that does the main drawing of the components on the screen
	 * 
	 * @param g
	 */
	public void paintComponent(Graphics g) {
		drawBackground(g);
		// if (backface) {
		// drawPolygonWithBackFaceCulling(g);
		// } else {
		drawPolygon(g);
		// }
		repaint();
	}

	/**
	 * Main method of the program
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new GraphicsProgram();
	}
}
