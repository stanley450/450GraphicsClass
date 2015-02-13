import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JFrame;

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

	public double[][] zBuffArray = new double[width + 100][height + 100];

	double inf = Double.POSITIVE_INFINITY;

	int polyNum = 0;

	// Distance from the eye to the center of the screen
	int eye = 1000;

	Image frameImage;

	private boolean backface = false;
	private int polyfillindex = 0;
	private boolean polyfilledges = false;
	private boolean polyfillnoedges = false;
	private boolean zbuffertoggle = false;

	int currentIndex = 0;

	// function used with the keylistener to choose the current polygon you are
	// using
	public void currentIndexChecker(boolean bool) {
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
	public double perspective(double point, double zdis) {
		return ((eye * point) / (eye + zdis));
	}

	// Vector list of all of the current trangle objects in the program
	ArrayList<Trangle> polyList = new ArrayList<Trangle>();

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
		Trangle mangle = new Trangle(true);
		polyList.add(mangle);
		setVisible(true);

		for (int i = 0; i < width; i++) {
			for (int k = 0; k < height; k++) {
				zBuffArray[i][k] = inf;
			}
		}

	}

	/**
	 * Polygon class for graphics program
	 */
	class Trangle {
		int thisNum;
		boolean isPyrm = true;

		// x y z positions of the a figure
		double[] defXPos;
		double[] defYPos;
		double[] defZPos;

		// actual 2d dimensions of the polygon
		double[] currXPos;
		double[] currYPos;
		double[] currZPos;

		// logical midpoint of the polygon
		double midXPnt;
		double midYPnt;
		double midZPnt;

		// actual 2d dimensions of the midpoint
		double currMidXPnt;
		double currMidYPnt;

		boolean isFocused = false;

		/**
		 * Trangle constructor
		 */
		public Trangle(boolean bool) {
			isPyrm = bool;
			if (isPyrm) {
				defXPos = new double[5];
				defYPos = new double[5];
				defZPos = new double[5];
				currXPos = new double[5];
				currYPos = new double[5];
				currZPos = new double[5];
				setDefault(this);
				setFocusedTrangle(true);
				thisNum = polyNum;
				polyNum++;
			} else {
				defXPos = new double[16];
				defYPos = new double[16];
				defZPos = new double[16];
				currXPos = new double[16];
				currYPos = new double[16];
				currZPos = new double[16];
				setDefaultCyl(this);
				setFocusedTrangle(true);
				thisNum = polyNum;
				polyNum++;
			}
		}

		/**
		 * Sets the current Trangle to be focused by the program
		 * 
		 * @param boolean bool
		 */
		public void setFocusedTrangle(boolean bool) {
			isFocused = bool;
		}

		/**
		 * returns wether or not the current trangle is focused
		 * 
		 * @return boolean
		 */
		public boolean getFocusedTrangle() {
			return isFocused;
		}
	}

	public void closeonescape() {
		this.dispose();
	}

	/**
	 * KeyListener class for the graphics program that takes the keyboard input
	 */
	public class myKeyListener extends KeyAdapter {

		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_ESCAPE:
				closeonescape();
			case KeyEvent.VK_INSERT: // press "insert" key to create a new
										// object and select it
				if (e.isShiftDown()) {
					polyList.add(new Trangle(false));
					currentIndex = polyList.size() - 1;
				} else {
					polyList.add(new Trangle(true));
					currentIndex = polyList.size() - 1;
				}
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
				if (polyList.get(currentIndex).isPyrm) {
					setDefault(polyList.get(currentIndex));
				} else {
					setDefaultCyl(polyList.get(currentIndex));
				}
				break;
			}
			repaint();
		}

	}

	/**
	 * Method that takes a trangle and sets its value to a default position
	 * 
	 * @param Trangle
	 *            trng
	 */
	public void setDefault(Trangle trng) {
		// p0
		trng.defXPos[0] = 0;
		trng.defYPos[0] = 150;
		trng.defZPos[0] = 250;
		// p1
		trng.defXPos[1] = -150;
		trng.defYPos[1] = -150;
		trng.defZPos[1] = 100;
		// p2
		trng.defXPos[2] = 150;
		trng.defYPos[2] = -150;
		trng.defZPos[2] = 100;
		// p3
		trng.defXPos[3] = -150;
		trng.defYPos[3] = -150;
		trng.defZPos[3] = 400;
		// p4
		trng.defXPos[4] = 150;
		trng.defYPos[4] = -150;
		trng.defZPos[4] = 400;

		trng.midXPnt = 0;
		trng.midYPnt = 0;
		trng.midZPnt = 250;

	}

	public void setDefaultCyl(Trangle trng) {
		// p0
		trng.defXPos[0] = -25;
		trng.defYPos[0] = 50;
		trng.defZPos[0] = 100;
		// p1
		trng.defXPos[1] = 25;
		trng.defYPos[1] = 50;
		trng.defZPos[1] = 100;
		// p2
		trng.defXPos[2] = 50;
		trng.defYPos[2] = 25;
		trng.defZPos[2] = 100;
		// p3
		trng.defXPos[3] = 50;
		trng.defYPos[3] = -25;
		trng.defZPos[3] = 100;
		// p4
		trng.defXPos[4] = 25;
		trng.defYPos[4] = -50;
		trng.defZPos[4] = 100;
		// p5
		trng.defXPos[5] = -25;
		trng.defYPos[5] = -50;
		trng.defZPos[5] = 100;
		// p6
		trng.defXPos[6] = -50;
		trng.defYPos[6] = -25;
		trng.defZPos[6] = 100;
		// p7
		trng.defXPos[7] = -50;
		trng.defYPos[7] = 25;
		trng.defZPos[7] = 100;
		// p8
		trng.defXPos[8] = -25;
		trng.defYPos[8] = 50;
		trng.defZPos[8] = 300;
		// p9
		trng.defXPos[9] = 25;
		trng.defYPos[9] = 50;
		trng.defZPos[9] = 300;
		// p10
		trng.defXPos[10] = 50;
		trng.defYPos[10] = 25;
		trng.defZPos[10] = 300;
		// p11
		trng.defXPos[11] = 50;
		trng.defYPos[11] = -25;
		trng.defZPos[11] = 300;
		// p12
		trng.defXPos[12] = 25;
		trng.defYPos[12] = -50;
		trng.defZPos[12] = 300;
		// p13
		trng.defXPos[13] = -25;
		trng.defYPos[13] = -50;
		trng.defZPos[13] = 300;
		// p14
		trng.defXPos[14] = -50;
		trng.defYPos[14] = -25;
		trng.defZPos[14] = 300;
		// p15
		trng.defXPos[15] = -50;
		trng.defYPos[15] = 25;
		trng.defZPos[15] = 300;

		trng.midXPnt = 0;
		trng.midYPnt = 0;
		trng.midZPnt = 200;

	}

	public double calcTriangleNormal(double x1, double y1, double z1,
			double x2, double y2, double z2, double x3, double y3, double z3) {
		double vect1x, vect1y, vect1z;

		double vect2x, vect2y, vect2z;

		double normX, normY, normZ, normOffset;

		// point 3 will be the top x3,y3,z3

		// vector 1
		vect1x = x1 - x3;
		vect1y = y1 - y3;
		vect1z = z1 - z3;

		// vector 2
		vect2x = x2 - x3;
		vect2y = y2 - y3;
		vect2z = z2 - z3;

		// normal vector from the two vectors of the triangle
		normX = (vect1y * vect2z) - (vect1z * vect2y);
		normY = -(vect2z * vect1x) + (vect2x * vect1z);
		normZ = (vect1x * vect2y) - (vect1y * vect2x);

		// "plane offset" D used in calc
		normOffset = (x3 * normX) - (y3 * normY) + (x3 * normZ);

		// final equation to compute visibility
		if (((eye * normZ) - normOffset) < 0) {
			return -1;
		}
		return (eye * normZ) - normOffset;
	}

	public void calcMidPoints(Trangle trng) {

		double minX, maxX;
		double minY, maxY;
		double minZ, maxZ;

		minX = trng.defXPos[0];
		maxX = trng.defXPos[0];
		minY = trng.defYPos[0];
		maxY = trng.defYPos[0];
		minZ = trng.defZPos[0];
		maxZ = trng.defZPos[0];

		for (int i = 0; i < trng.defXPos.length; i++) {
			if (minX > trng.defXPos[i])
				minX = trng.defXPos[i];
			if (maxX < trng.defXPos[i])
				maxX = trng.defXPos[i];
			if (minY > trng.defYPos[i])
				minY = trng.defYPos[i];
			if (maxY < trng.defYPos[i])
				maxY = trng.defYPos[i];
			if (minZ > trng.defZPos[i])
				minZ = trng.defZPos[i];
			if (maxZ < trng.defZPos[i])
				maxZ = trng.defZPos[i];
		}

		trng.midXPnt = (minX + maxX) / 2;
		trng.midYPnt = (minY + maxY) / 2;
		trng.midZPnt = (minZ + maxZ) / 2;
	}

	public void fillTrangle(double defx1, double defy1, double defz1,
			double defx2, double defy2, double defz2, double defx3,
			double defy3, double defz3, Graphics2D g2d, Trangle trng) {

		double normalvalue = calcTriangleNormal(defx1, defy1, defz1, defx2,
				defy2, defz2, defx3, defy3, defz3);

		double x1 = perspective(defx1, defz1);
		double y1 = perspective(defy1, defz1);
		double z1 = defz1 / (eye + defz1);
		double x2 = perspective(defx2, defz2);
		double y2 = perspective(defy2, defz2);
		double z2 = defz2 / (eye + defz2);
		double x3 = perspective(defx3, defz3);
		double y3 = perspective(defy3, defz3);
		double z3 = defz3 / (eye + defz3);

		if (!backface || !(normalvalue <= 0)) {

			int xoff = width / 2;
			int yoff = height / 2;

			// start point of edge
			double[][] startpoints = new double[3][3];
			// end point of edge
			double[][] endpoints = new double[3][3];

			// edge p1 to p2
			startpoints[0][0] = x1;
			startpoints[0][1] = y1;
			startpoints[0][2] = z1;
			endpoints[0][0] = x2;
			endpoints[0][1] = y2;
			endpoints[0][2] = z2;

			// edge p2 to p3
			startpoints[1][0] = x2;
			startpoints[1][1] = y2;
			startpoints[1][2] = z2;
			endpoints[1][0] = x3;
			endpoints[1][1] = y3;
			endpoints[1][2] = z3;

			// edge p3 to p1
			startpoints[2][0] = x3;
			startpoints[2][1] = y3;
			startpoints[2][2] = z3;
			endpoints[2][0] = x1;
			endpoints[2][1] = y1;
			endpoints[2][2] = z1;

			double[][] ymax = new double[3][2];
			double[][] ymin = new double[3][2];
			double[] dx = new double[3];
			double[] x = new double[3];

			// max function
			for (int i = 0; i < 3; i++) {
				if (startpoints[i][1] > endpoints[i][1]) {
					ymax[i] = startpoints[i];
					ymin[i] = endpoints[i];

				} else {
					ymin[i] = startpoints[i];
					ymax[i] = endpoints[i];

				}
			}

			double[][] tempmax = new double[3][2];
			double[][] tempmin = new double[3][2];

			if (ymax[0][1] >= ymax[1][1] && ymax[0][1] >= ymax[2][1]
					&& ymin[0][1] >= ymin[1][1] && ymin[0][1] >= ymin[2][1]) {
				tempmax[0] = ymax[0];
				tempmin[0] = ymin[0];
				if (ymax[1][1] >= ymax[2][1] && ymin[1][1] >= ymin[2][1]) {
					tempmax[1] = ymax[1];
					tempmin[1] = ymin[1];
					tempmax[2] = ymax[2];
					tempmin[2] = ymin[2];
				} else {
					tempmax[1] = ymax[2];
					tempmin[1] = ymin[2];
					tempmax[2] = ymax[1];
					tempmin[2] = ymin[1];
				}
			} else {
				tempmax[2] = ymax[0];
				tempmin[2] = ymin[0];
				if (ymax[1][1] >= ymax[2][1] && ymin[1][1] >= ymin[2][1]) {
					tempmax[0] = ymax[1];
					tempmin[0] = ymin[1];
					tempmax[1] = ymax[2];
					tempmin[1] = ymin[2];
				} else {
					tempmax[0] = ymax[2];
					tempmin[0] = ymin[2];
					tempmax[1] = ymax[1];
					tempmin[1] = ymin[1];
				}
			}

			ymax = tempmax;
			ymin = tempmin;

			// edge sorting
			if (ymax[0][1] >= ymax[1][1] && ymax[0][1] >= ymax[2][1]
					&& ymin[0][1] >= ymin[1][1] && ymin[0][1] >= ymin[2][1]) {
				// if the ymax is the largest in its groups and the ymin with it
				// is
				// the biggest also set them as the first edge
				startpoints[0] = ymax[0];
				endpoints[0] = ymin[0];

				if (ymax[1][1] >= ymax[2][1] && ymin[1][1] >= ymin[2][1]) {
					startpoints[1] = ymax[1];
					endpoints[1] = ymin[1];
					startpoints[2] = ymax[2];
					endpoints[2] = ymin[2];
				} else {
					startpoints[2] = ymax[1];
					endpoints[2] = ymin[1];
					startpoints[1] = ymax[2];
					endpoints[1] = ymin[2];
				}
			} else {
				startpoints[2] = ymax[0];
				endpoints[2] = ymin[0];

				if (ymax[1][1] >= ymax[2][1] && ymin[1][1] >= ymin[2][1]) {
					startpoints[0] = ymax[1];
					endpoints[0] = ymin[1];
					startpoints[1] = ymax[2];
					endpoints[1] = ymin[2];
				} else {
					startpoints[1] = ymax[1];
					endpoints[1] = ymin[1];
					startpoints[0] = ymax[2];
					endpoints[0] = ymin[2];
				}
			}

			for (int i = 0; i < 3; i++) {
				dx[i] = -((endpoints[i][0] - startpoints[i][0]) / (endpoints[i][1] - startpoints[i][1]));
				x[i] = startpoints[i][0];
			}

			double ystart = ymax[0][1];
			double yend = ymin[1][1];

			double zbuff1 = startpoints[0][2];
			double zbuff2 = startpoints[2][2];
			double zbuff3 = endpoints[1][2];
			double zedge1 = (zbuff2 - zbuff1)
					/ (startpoints[2][1] - startpoints[1][1]);
			double zedge2 = (zbuff3 - zbuff1)
					/ (endpoints[2][1] - startpoints[1][1]);
			double zedge3 = (zbuff3 - zbuff2)
					/ (startpoints[1][1] - startpoints[2][1]);
			double ztemp1 = zbuff1;
			double ztemp2 = zbuff2;
			double ztemp3 = zbuff3;

			for (int i = (int) ystart; i > yend; i--) {
				if (zbuffertoggle) {

					if ((i <= startpoints[1][1] && i >= endpoints[1][1])
							&& (i <= startpoints[0][1] && i >= endpoints[0][1])) {
						if (x[0] >= x[1]) {

							double temp1 = ztemp1;
							double temp1edge = (ztemp2 - ztemp1)
									/ (x[0] - x[1]);
							for (int k = (int) x[1]; k < x[0]; k++) {

								if (temp1 <= zBuffArray[k + xoff][yoff - i]) {
									zBuffArray[k + xoff][yoff - i] = temp1;

									if (polyfillnoedges) {
										if (trng.thisNum == 1) {
											g2d.setColor(Color.BLUE);
										} else {
											g2d.setColor(Color.RED);
										}
										g2d.drawLine((k + xoff), (yoff - i),
												(k + xoff), (yoff - i));
									} else {
										if (k <= (int) x[1] || k >= (int) x[0]) {
											g2d.setColor(Color.BLACK);
											g2d.drawLine((k + xoff),
													(yoff - i), (k + xoff),
													(yoff - i));
										} else {
											g2d.setColor(Color.BLUE);
											if (polyfilledges) {
												g2d.drawLine((k + xoff),
														(yoff - i), (k + xoff),
														(yoff - i));
											}
										}
									}
								} else {
									zBuffArray[k + xoff][yoff - i] = inf;
								}

								temp1 = temp1 + temp1edge;
							}
						} else {

							double temp1 = ztemp1;
							double temp1edge = (ztemp2 - ztemp1)
									/ (x[1] - x[0]);
							for (int k = (int) x[0]; k < x[1]; k++) {

								if (temp1 <= zBuffArray[k + xoff][yoff - i]) {
									zBuffArray[k + xoff][yoff - i] = temp1;

									if (polyfillnoedges) {
										if (trng.thisNum == 1) {
											g2d.setColor(Color.BLUE);
										} else {
											g2d.setColor(Color.RED);
										}
										g2d.drawLine((k + xoff), (yoff - i),
												(k + xoff), (yoff - i));
									} else {
										if (k <= (int) x[0] || k >= (int) x[1]) {
											g2d.setColor(Color.BLACK);
											g2d.drawLine((k + xoff),
													(yoff - i), (k + xoff),
													(yoff - i));
										} else {
											g2d.setColor(Color.BLUE);
											if (polyfilledges) {
												g2d.drawLine((k + xoff),
														(yoff - i), (k + xoff),
														(yoff - i));
											}
										}
									}
								} else {
									zBuffArray[k + xoff][yoff - i] = inf;
								}
								temp1 = temp1 + temp1edge;
							}
						}
						x[0] = x[0] + (dx[0]);
						x[1] = x[1] + (dx[1]);

						ztemp1 = ztemp1 + zedge1;
						ztemp2 = ztemp2 + zedge2;

					} else if ((i < startpoints[1][1] && i >= endpoints[1][1])
							&& (i < startpoints[2][1] && i >= endpoints[2][1])) {

						if (x[2] >= x[1]) {

							double temp1 = ztemp3;
							double temp1edge = (ztemp3 - ztemp2)
									/ (x[2] - x[1]);

							for (int k = (int) x[1]; k < x[2]; k++) {

								if (temp1 <= zBuffArray[k + xoff][yoff - i]) {
									zBuffArray[k + xoff][yoff - i] = temp1;

									if (polyfillnoedges) {
										if (trng.thisNum == 1) {
											g2d.setColor(Color.BLUE);
										} else {
											g2d.setColor(Color.RED);
										}
										g2d.drawLine((k + xoff), (yoff - i),
												(k + xoff), (yoff - i));
									} else {
										if (k <= (int) x[1] || k >= (int) x[2]) {
											g2d.setColor(Color.BLACK);
											g2d.drawLine((k + xoff),
													(yoff - i), (k + xoff),
													(yoff - i));
										} else {
											g2d.setColor(Color.BLUE);
											if (polyfilledges) {
												g2d.drawLine((k + xoff),
														(yoff - i), (k + xoff),
														(yoff - i));
											}
										}

									}
								} else {
									zBuffArray[k + xoff][yoff - i] = inf;
								}
								temp1 = temp1 + temp1edge;
							}
						} else {

							double temp1 = ztemp3;
							double temp1edge = (ztemp3 - ztemp2)
									/ (x[1] - x[2]);

							for (int k = (int) x[2]; k < x[1]; k++) {

								if (temp1 <= zBuffArray[k + xoff][yoff - i]) {
									zBuffArray[k + xoff][yoff - i] = temp1;

									if (polyfillnoedges) {
										if (trng.thisNum == 1) {
											g2d.setColor(Color.BLUE);
										} else {
											g2d.setColor(Color.RED);
										}
										g2d.drawLine((k + xoff), (yoff - i),
												(k + xoff), (yoff - i));
									} else {
										if (k <= (int) x[2] || k >= (int) x[1]) {
											g2d.setColor(Color.BLACK);
											g2d.drawLine((k + xoff),
													(yoff - i), (k + xoff),
													(yoff - i));
										} else {
											g2d.setColor(Color.BLUE);
											if (polyfilledges) {
												g2d.drawLine((k + xoff),
														(yoff - i), (k + xoff),
														(yoff - i));
											}
										}

									}
								} else {
									zBuffArray[k + xoff][yoff - i] = inf;
								}
								temp1 = temp1 + temp1edge;
							}
						}

						x[2] = x[2] + (dx[2]);
						x[1] = x[1] + (dx[1]);

						ztemp3 = ztemp3 + zedge3;
						ztemp2 = ztemp2 + zedge2;

					}

				} else {

					if (!polyfillnoedges) {
						g2d.setColor(Color.BLACK);
						g2d.drawLine((int) (startpoints[0][0] + xoff),
								(int) (yoff - startpoints[0][1]),
								(int) (endpoints[0][0] + xoff),
								(int) (yoff - endpoints[0][1]));
						g2d.drawLine((int) (startpoints[1][0] + xoff),
								(int) (yoff - startpoints[1][1]),
								(int) (endpoints[1][0] + xoff),
								(int) (yoff - endpoints[1][1]));
						g2d.drawLine((int) (startpoints[2][0] + xoff),
								(int) (yoff - startpoints[2][1]),
								(int) (endpoints[2][0] + xoff),
								(int) (yoff - endpoints[2][1]));
					}

					if ((i <= startpoints[1][1] && i >= endpoints[1][1])
							&& (i <= startpoints[0][1] && i >= endpoints[0][1])) {

						if (x[0] >= x[1]) {
							for (int k = (int) x[1]; k < x[0]; k++) {

								if (polyfillnoedges) {
									g2d.setColor(Color.BLUE);
									g2d.drawLine((k + xoff), (yoff - i),
											(k + xoff), (yoff - i));
								} else {
									if (k <= (int) x[1] || k >= (int) x[0]) {
										g2d.setColor(Color.BLACK);
										g2d.drawLine((k + xoff), (yoff - i),
												(k + xoff), (yoff - i));
									} else {
										g2d.setColor(Color.BLUE);
										if (polyfilledges) {
											g2d.drawLine((k + xoff),
													(yoff - i), (k + xoff),
													(yoff - i));
										}
									}
								}
							}
						} else {
							for (int k = (int) x[0]; k < x[1]; k++) {
								if (polyfillnoedges) {
									g2d.setColor(Color.BLUE);
									g2d.drawLine((k + xoff), (yoff - i),
											(k + xoff), (yoff - i));
								} else {
									if (k <= (int) x[0] || k >= (int) x[1]) {
										g2d.setColor(Color.BLACK);
										g2d.drawLine((k + xoff), (yoff - i),
												(k + xoff), (yoff - i));
									} else {
										g2d.setColor(Color.BLUE);
										if (polyfilledges) {
											g2d.drawLine((k + xoff),
													(yoff - i), (k + xoff),
													(yoff - i));
										}
									}
								}
							}
						}
						x[0] = x[0] + (dx[0]);
						x[1] = x[1] + (dx[1]);

					} else if ((i < startpoints[1][1] && i >= endpoints[1][1])
							&& (i < startpoints[2][1] && i >= endpoints[2][1])) {

						if (x[2] >= x[1]) {
							for (int k = (int) x[1]; k < x[2]; k++) {
								if (polyfillnoedges) {
									g2d.setColor(Color.BLUE);
									g2d.drawLine((k + xoff), (yoff - i),
											(k + xoff), (yoff - i));
								} else {
									if (k <= (int) x[1] || k >= (int) x[2]) {
										g2d.setColor(Color.BLACK);
										g2d.drawLine((k + xoff), (yoff - i),
												(k + xoff), (yoff - i));
									} else {
										g2d.setColor(Color.BLUE);
										if (polyfilledges) {
											g2d.drawLine((k + xoff),
													(yoff - i), (k + xoff),
													(yoff - i));
										}
									}

								}
							}
						} else {
							for (int k = (int) x[2]; k < x[1]; k++) {
								if (polyfillnoedges) {
									g2d.setColor(Color.BLUE);
									g2d.drawLine((k + xoff), (yoff - i),
											(k + xoff), (yoff - i));
								} else {
									if (k <= (int) x[2] || k >= (int) x[1]) {
										g2d.setColor(Color.BLACK);
										g2d.drawLine((k + xoff), (yoff - i),
												(k + xoff), (yoff - i));
									} else {
										g2d.setColor(Color.BLUE);
										if (polyfilledges) {
											g2d.drawLine((k + xoff),
													(yoff - i), (k + xoff),
													(yoff - i));
										}
									}

								}
							}
						}

						x[2] = x[2] + (dx[2]);
						x[1] = x[1] + (dx[1]);

					}
				}
			}
		}

		g2d.setColor(Color.BLACK); // front side
	}

	/**
	 * Method that draws all of the possible trangles on the screen
	 * 
	 * @param g
	 */
	public void drawStuff(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;

		for (int p = 0; p < polyList.size(); p++) {
			if (currentIndex == p) {
				polyList.get(p).setFocusedTrangle(true);
			} else {
				polyList.get(p).setFocusedTrangle(false);
			}
		}

		for (int i = 0; i < polyList.size(); i++) {

			// finds the currently used trangle and makes its lines wider
			if (polyList.get(i).getFocusedTrangle()) {
				g2d.setStroke(new BasicStroke(5));
			}

			g2d.setColor(Color.BLACK); // front side

			if (polyList.get(i).isPyrm == true) {
				fillTrangle(polyList.get(i).defXPos[1],
						polyList.get(i).defYPos[1], polyList.get(i).defZPos[1],
						polyList.get(i).defXPos[2], polyList.get(i).defYPos[2],
						polyList.get(i).defZPos[2], polyList.get(i).defXPos[0],
						polyList.get(i).defYPos[0], polyList.get(i).defZPos[0],
						g2d, polyList.get(i));
				fillTrangle(polyList.get(i).defXPos[3],
						polyList.get(i).defYPos[3], polyList.get(i).defZPos[3],
						polyList.get(i).defXPos[1], polyList.get(i).defYPos[1],
						polyList.get(i).defZPos[1], polyList.get(i).defXPos[0],
						polyList.get(i).defYPos[0], polyList.get(i).defZPos[0],
						g2d, polyList.get(i));
				fillTrangle(polyList.get(i).defXPos[4],
						polyList.get(i).defYPos[4], polyList.get(i).defZPos[4],
						polyList.get(i).defXPos[3], polyList.get(i).defYPos[3],
						polyList.get(i).defZPos[3], polyList.get(i).defXPos[0],
						polyList.get(i).defYPos[0], polyList.get(i).defZPos[0],
						g2d, polyList.get(i));
				fillTrangle(polyList.get(i).defXPos[2],
						polyList.get(i).defYPos[2], polyList.get(i).defZPos[2],
						polyList.get(i).defXPos[4], polyList.get(i).defYPos[4],
						polyList.get(i).defZPos[4], polyList.get(i).defXPos[0],
						polyList.get(i).defYPos[0], polyList.get(i).defZPos[0],
						g2d, polyList.get(i));
				fillTrangle(polyList.get(i).defXPos[3],
						polyList.get(i).defYPos[3], polyList.get(i).defZPos[3],
						polyList.get(i).defXPos[2], polyList.get(i).defYPos[2],
						polyList.get(i).defZPos[2], polyList.get(i).defXPos[1],
						polyList.get(i).defYPos[1], polyList.get(i).defZPos[1],
						g2d, polyList.get(i));
				fillTrangle(polyList.get(i).defXPos[2],
						polyList.get(i).defYPos[2], polyList.get(i).defZPos[2],
						polyList.get(i).defXPos[3], polyList.get(i).defYPos[3],
						polyList.get(i).defZPos[3], polyList.get(i).defXPos[4],
						polyList.get(i).defYPos[4], polyList.get(i).defZPos[4],
						g2d, polyList.get(i));
			} else {
				//1 - 1 
				fillTrangle(polyList.get(i).defXPos[0],
						polyList.get(i).defYPos[0], polyList.get(i).defZPos[0],
						polyList.get(i).defXPos[1], polyList.get(i).defYPos[1],
						polyList.get(i).defZPos[1], polyList.get(i).defXPos[8],
						polyList.get(i).defYPos[8], polyList.get(i).defZPos[8],
						g2d, polyList.get(i));
				//1 - 2
				fillTrangle(polyList.get(i).defXPos[1],
						polyList.get(i).defYPos[1], polyList.get(i).defZPos[1],
						polyList.get(i).defXPos[8], polyList.get(i).defYPos[8],
						polyList.get(i).defZPos[8], polyList.get(i).defXPos[9],
						polyList.get(i).defYPos[9], polyList.get(i).defZPos[9],
						g2d, polyList.get(i));
				//2 - 1
				fillTrangle(polyList.get(i).defXPos[2],
						polyList.get(i).defYPos[2], polyList.get(i).defZPos[2],
						polyList.get(i).defXPos[1], polyList.get(i).defYPos[1],
						polyList.get(i).defZPos[1], polyList.get(i).defXPos[9],
						polyList.get(i).defYPos[9], polyList.get(i).defZPos[9],
						g2d, polyList.get(i));
				//2 - 2
				fillTrangle(polyList.get(i).defXPos[2],
						polyList.get(i).defYPos[2], polyList.get(i).defZPos[2],
						polyList.get(i).defXPos[10], polyList.get(i).defYPos[10],
						polyList.get(i).defZPos[10], polyList.get(i).defXPos[9],
						polyList.get(i).defYPos[9], polyList.get(i).defZPos[9],
						g2d, polyList.get(i));
				//3 - 1
				fillTrangle(polyList.get(i).defXPos[3],
						polyList.get(i).defYPos[3], polyList.get(i).defZPos[3],
						polyList.get(i).defXPos[2], polyList.get(i).defYPos[2],
						polyList.get(i).defZPos[2], polyList.get(i).defXPos[10],
						polyList.get(i).defYPos[10], polyList.get(i).defZPos[10],
						g2d, polyList.get(i));
				//3 - 2
				fillTrangle(polyList.get(i).defXPos[3],
						polyList.get(i).defYPos[3], polyList.get(i).defZPos[3],
						polyList.get(i).defXPos[10], polyList.get(i).defYPos[10],
						polyList.get(i).defZPos[10], polyList.get(i).defXPos[11],
						polyList.get(i).defYPos[11], polyList.get(i).defZPos[11],
						g2d, polyList.get(i));
				//4 - 1
				fillTrangle(polyList.get(i).defXPos[3],
						polyList.get(i).defYPos[3], polyList.get(i).defZPos[3],
						polyList.get(i).defXPos[4], polyList.get(i).defYPos[4],
						polyList.get(i).defZPos[4], polyList.get(i).defXPos[11],
						polyList.get(i).defYPos[11], polyList.get(i).defZPos[11],
						g2d, polyList.get(i));
				//4 - 2
				fillTrangle(polyList.get(i).defXPos[4],
						polyList.get(i).defYPos[4], polyList.get(i).defZPos[4],
						polyList.get(i).defXPos[11], polyList.get(i).defYPos[11],
						polyList.get(i).defZPos[11], polyList.get(i).defXPos[12],
						polyList.get(i).defYPos[12], polyList.get(i).defZPos[12],
						g2d, polyList.get(i));
				//5 - 1
				fillTrangle(polyList.get(i).defXPos[4],
						polyList.get(i).defYPos[4], polyList.get(i).defZPos[4],
						polyList.get(i).defXPos[5], polyList.get(i).defYPos[5],
						polyList.get(i).defZPos[5], polyList.get(i).defXPos[12],
						polyList.get(i).defYPos[12], polyList.get(i).defZPos[12],
						g2d, polyList.get(i));
				//5 - 2
				fillTrangle(polyList.get(i).defXPos[5],
						polyList.get(i).defYPos[5], polyList.get(i).defZPos[5],
						polyList.get(i).defXPos[13], polyList.get(i).defYPos[13],
						polyList.get(i).defZPos[13], polyList.get(i).defXPos[12],
						polyList.get(i).defYPos[12], polyList.get(i).defZPos[12],
						g2d, polyList.get(i));
				//6 - 1
				fillTrangle(polyList.get(i).defXPos[5],
						polyList.get(i).defYPos[5], polyList.get(i).defZPos[5],
						polyList.get(i).defXPos[6], polyList.get(i).defYPos[6],
						polyList.get(i).defZPos[6], polyList.get(i).defXPos[13],
						polyList.get(i).defYPos[13], polyList.get(i).defZPos[13],
						g2d, polyList.get(i));
				//6 - 2
				fillTrangle(polyList.get(i).defXPos[6],
						polyList.get(i).defYPos[6], polyList.get(i).defZPos[6],
						polyList.get(i).defXPos[13], polyList.get(i).defYPos[13],
						polyList.get(i).defZPos[13], polyList.get(i).defXPos[14],
						polyList.get(i).defYPos[14], polyList.get(i).defZPos[14],
						g2d, polyList.get(i));
				//7 - 1
				fillTrangle(polyList.get(i).defXPos[6],
						polyList.get(i).defYPos[6], polyList.get(i).defZPos[6],
						polyList.get(i).defXPos[7], polyList.get(i).defYPos[7],
						polyList.get(i).defZPos[7], polyList.get(i).defXPos[14],
						polyList.get(i).defYPos[14], polyList.get(i).defZPos[14],
						g2d, polyList.get(i));
				//7 - 2
				fillTrangle(polyList.get(i).defXPos[7],
						polyList.get(i).defYPos[7], polyList.get(i).defZPos[7],
						polyList.get(i).defXPos[14], polyList.get(i).defYPos[14],
						polyList.get(i).defZPos[14], polyList.get(i).defXPos[15],
						polyList.get(i).defYPos[15], polyList.get(i).defZPos[15],
						g2d, polyList.get(i));
				//8 - 1
				fillTrangle(polyList.get(i).defXPos[7],
						polyList.get(i).defYPos[7], polyList.get(i).defZPos[7],
						polyList.get(i).defXPos[0], polyList.get(i).defYPos[0],
						polyList.get(i).defZPos[0], polyList.get(i).defXPos[15],
						polyList.get(i).defYPos[15], polyList.get(i).defZPos[15],
						g2d, polyList.get(i));
				//8 - 2
				fillTrangle(polyList.get(i).defXPos[0],
						polyList.get(i).defYPos[0], polyList.get(i).defZPos[0],
						polyList.get(i).defXPos[15], polyList.get(i).defYPos[15],
						polyList.get(i).defZPos[15], polyList.get(i).defXPos[8],
						polyList.get(i).defYPos[8], polyList.get(i).defZPos[8],
						g2d, polyList.get(i));
			}

			g2d.setColor(Color.BLACK); // front side

			if (polyList.get(i).getFocusedTrangle()) {
				g2d.setStroke(new BasicStroke(1));
			}
		}

	}

	/**
	 * Method that moves all of the Y points of the selected polygon in the
	 * positive y position
	 * 
	 * @param Trangle
	 *            trng
	 */
	public void moveUp(Trangle trng) {
		Boolean actionBool = false;
		for (int i = 0; i < trng.defXPos.length; i++) {
			if (!(trng.currYPos[i] >= (height / 2))) {
				actionBool = true;
			} else {
				actionBool = false;
				break;
			}
		}

		calcMidPoints(trng);

		if (actionBool == true) {
			for (int i = 0; i < trng.defXPos.length; i++) {
				trng.defYPos[i] = trng.defYPos[i] + 10;
			}
			trng.midYPnt = trng.midYPnt + 10;
		}
	}

	/**
	 * Method that move all of the Y points of the selected polygon in the
	 * negative y position
	 * 
	 * @param Trangle
	 *            trng
	 */
	public void moveDwn(Trangle trng) {
		Boolean actionBool = false;
		for (int i = 0; i < trng.defXPos.length; i++) {
			if (!(trng.currYPos[i] <= -height / 2)) {
				actionBool = true;
			} else {
				actionBool = false;
				break;
			}
		}

		calcMidPoints(trng);

		if (actionBool == true) {
			for (int i = 0; i < trng.defXPos.length; i++) {
				trng.defYPos[i] = trng.defYPos[i] - 10;
			}
			trng.midYPnt = trng.midYPnt - 10;
		}
	}

	/**
	 * Method that moves all of the x points of the selected polygon in the
	 * positive x position
	 * 
	 * @param Trangle
	 *            trng
	 */
	public void moveRght(Trangle trng) {
		Boolean actionBool = false;
		for (int i = 0; i < trng.defXPos.length; i++) {
			if (!(trng.currXPos[i] >= width / 2)) {
				actionBool = true;
			} else {
				actionBool = false;
				break;
			}
		}

		calcMidPoints(trng);

		if (actionBool == true) {
			for (int i = 0; i < trng.defXPos.length; i++) {
				trng.defXPos[i] = trng.defXPos[i] + 10;
			}
			trng.midXPnt = trng.midXPnt + 10;
		}
	}

	/**
	 * Method that moves all of the x points of the selected polygon in the
	 * negative x positon
	 * 
	 * @param Trangle
	 *            trng
	 */
	public void moveLft(Trangle trng) {
		Boolean actionBool = false;
		for (int i = 0; i < trng.defXPos.length; i++) {
			if (!(trng.currXPos[i] <= -(width / 2))) {
				actionBool = true;
			} else {
				actionBool = false;
				break;
			}
		}

		calcMidPoints(trng);

		if (actionBool == true) {
			for (int i = 0; i < trng.defXPos.length; i++) {
				trng.defXPos[i] = trng.defXPos[i] - 10;
			}
			trng.midXPnt = trng.midXPnt - 10;
		}
	}

	/**
	 * Method that increases the size of selected polygon by increasing the
	 * distance of all the points from one another
	 * 
	 * @param Trangle
	 *            trng
	 */
	public void scaleUp(Trangle trng) {
		Boolean actionBool = false;
		for (int i = 0; i < trng.defXPos.length; i++) {
			if (!(trng.currXPos[i] >= width / 2
					|| trng.currYPos[i] >= height / 2
					|| trng.currYPos[i] <= -height / 2 || trng.currXPos[i] <= -(width / 2))) {
				actionBool = true;
			} else {
				actionBool = false;
				break;
			}
		}

		calcMidPoints(trng);

		if (actionBool == true) {
			for (int i = 0; i < trng.defXPos.length; i++) {
				trng.defXPos[i] = trng.defXPos[i] - trng.midXPnt;
				trng.defYPos[i] = trng.defYPos[i] - trng.midYPnt;
				trng.defZPos[i] = trng.defZPos[i] - trng.midZPnt;
				trng.defXPos[i] = (trng.defXPos[i] * 1.05);
				trng.defYPos[i] = (trng.defYPos[i] * 1.05);
				trng.defZPos[i] = (trng.defZPos[i] * 1.05);
				trng.defXPos[i] = trng.defXPos[i] + trng.midXPnt;
				trng.defYPos[i] = trng.defYPos[i] + trng.midYPnt;
				trng.defZPos[i] = trng.defZPos[i] + trng.midZPnt;
			}
		}
	}

	/**
	 * Method that decreases the size of a polygon by decreasing the distance of
	 * all the points from one another
	 * 
	 * @param Trangle
	 *            trng
	 */
	public void scaleDwn(Trangle trng) {

		calcMidPoints(trng);

		for (int i = 0; i < trng.defXPos.length; i++) {
			trng.defXPos[i] = trng.defXPos[i] - trng.midXPnt;
			trng.defYPos[i] = trng.defYPos[i] - trng.midYPnt;
			trng.defZPos[i] = trng.defZPos[i] - trng.midZPnt;
			trng.defXPos[i] = (trng.defXPos[i] * .95);
			trng.defYPos[i] = (trng.defYPos[i] * .95);
			trng.defZPos[i] = (trng.defZPos[i] * .95);
			trng.defXPos[i] = trng.defXPos[i] + trng.midXPnt;
			trng.defYPos[i] = trng.defYPos[i] + trng.midYPnt;
			trng.defZPos[i] = trng.defZPos[i] + trng.midZPnt;
		}
	}

	/**
	 * Method that moves all of the z points of the selected polygon into the
	 * positive z direction (right hand rule)
	 * 
	 * @param Trangle
	 *            trng
	 */
	public void moveForward(Trangle trng) {

		calcMidPoints(trng);

		for (int i = 0; i < trng.defXPos.length; i++) {
			trng.defZPos[i] = trng.defZPos[i] - 100;
		}
		trng.midZPnt = trng.midZPnt - 100;
	}

	/**
	 * Method that moves all of the z points of the selected polygon into the
	 * negative z direction (right hand rule)
	 * 
	 * @param Trangle
	 *            trng
	 */
	public void moveBackward(Trangle trng) {

		calcMidPoints(trng);

		for (int i = 0; i < trng.defXPos.length; i++) {
			trng.defZPos[i] = trng.defZPos[i] + 100;
		}
		trng.midZPnt = trng.midZPnt + 100;
	}

	/**
	 * Method that rotates the selected polygon around the z axis
	 * 
	 * @param Trangle
	 *            trng
	 */
	public void rotateZ(Trangle trng, boolean bool) { // counter clockwise

		double theta = -.10;
		if (bool) {
			theta = .10;
		}

		calcMidPoints(trng);

		for (int i = 0; i < trng.defXPos.length; i++) {
			double currX = trng.defXPos[i] - trng.midXPnt;
			double currY = trng.defYPos[i] - trng.midYPnt;
			double tempX = (currX * Math.cos(theta) - currY * Math.sin(theta));
			double tempY = (currX * Math.sin(theta) + currY * Math.cos(theta));
			tempX = tempX + trng.midXPnt;
			tempY = tempY + trng.midYPnt;
			trng.defXPos[i] = tempX;
			trng.defYPos[i] = tempY;
		}

	}

	/**
	 * Method that rotates the selected polygon around the y axis
	 * 
	 * @param Trangle
	 *            trng
	 */
	public void rotateY(Trangle trng, boolean bool) {

		double theta = -.10;
		if (bool) {
			theta = .10;
		}

		calcMidPoints(trng);

		for (int i = 0; i < trng.defXPos.length; i++) {
			double currX = trng.defXPos[i] - trng.midXPnt;
			double currZ = trng.defZPos[i] - trng.midZPnt;
			double tempX = (currX * Math.cos(theta) - currZ * Math.sin(theta));
			double tempZ = (currX * Math.sin(theta) + currZ * Math.cos(theta));
			tempX = tempX + trng.midXPnt;
			tempZ = tempZ + trng.midZPnt;
			trng.defXPos[i] = tempX;
			trng.defZPos[i] = tempZ;
		}

	}

	/**
	 * Method that rotates the selected polygon around the x axis
	 * 
	 * @param Trangle
	 *            trng
	 */
	public void rotateX(Trangle trng, boolean bool) {

		double theta = -.10;
		if (bool) {
			theta = .10;
		}

		calcMidPoints(trng);
		for (int i = 0; i < trng.defXPos.length; i++) {
			double currY = trng.defYPos[i] - trng.midYPnt;
			double currZ = trng.defZPos[i] - trng.midZPnt;
			double tempY = (currY * Math.cos(theta) - currZ * Math.sin(theta));
			double tempZ = (currY * Math.sin(theta) + currZ * Math.cos(theta));
			tempY = tempY + trng.midYPnt;
			tempZ = tempZ + trng.midZPnt;
			trng.defYPos[i] = tempY;
			trng.defZPos[i] = tempZ;
		}

	}

	/**
	 * Method that draws the lines on the page
	 * 
	 * @param g
	 */
	private void doStuff(Graphics g) {

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
		doStuff(g);
		drawStuff(g);
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