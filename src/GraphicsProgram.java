import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;

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

	// Distance from the eye to the center of the screen
	int eye = 1000;

	Image frameImage;

	private boolean backface = false;
	private int polyfillindex = 0;
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
	Vector<Trangle> polyList = new Vector<Trangle>();

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
		Trangle mangle = new Trangle();
		polyList.add(mangle);
		setVisible(true);
	}

	/**
	 * Polygon class for graphics program
	 */
	class Trangle {

		// x y z positions of the a figure
		double[] defXPos = new double[5];
		double[] defYPos = new double[5];
		double[] defZPos = new double[5];

		// normals for the current triangle and each face
		double[] normX = new double[5];
		double[] normY = new double[5];
		double[] normZ = new double[5];

		boolean[] isVisible = new boolean[5];

		// plane offset "D" normal of the thing and a point on poly
		double[] normOffset = new double[5];
		
		double yMax = defYPos[0];
		double yMin = defYPos[0];
		double dx;
		double xnext;

		// logical midpoint of the polygon
		double midXPnt;
		double midYPnt;
		double midZPnt;

		// actual 2d dimensions of the midpoint
		double currMidXPnt;
		double currMidYPnt;

		boolean isFocused = false;

		// actual 2d dimensions of the polygon
		double[] currXPos = new double[5];
		double[] currYPos = new double[5];

		/**
		 * Trangle constructor
		 */
		public Trangle() {
			setDefault(this);
			setFocusedTrangle(true);

			// Different polys for the figure are
			// Triangle 1 - p0, p1, p2
			// Triangle 2 - p0, p3, p1
			// Triangle 3 - p0, p3, p4
			// Triangle 4 - p0, p4, p2
			// SquareBase - p1, p3, p4, p2
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

	/**
	 * KeyListener class for the graphics program that takes the keyboard input
	 */
	public class myKeyListener extends KeyAdapter {

		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_INSERT: // press "insert" key to create a new
										// object and select it
				polyList.add(new Trangle());
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
			case KeyEvent.VK_U: // press "u" key on the keyboard to move
								// pyramid up
				moveUp(polyList.get(currentIndex));
				break;
			case KeyEvent.VK_D: // press "d" key on the keyboard to move
								// pyramid down
				moveDwn(polyList.get(currentIndex));
				break;
			case KeyEvent.VK_C: // Back face culling stuff goes here
				backface = !backface;
				break;
			case KeyEvent.VK_X:
				if(polyfillindex > 3){
					polyfillindex = 0;
				} else { 
					polyfillindex++;
				}
				break;
			case KeyEvent.VK_Z:
				zbuffertoggle = !zbuffertoggle;
				break;
			case KeyEvent.VK_R: // press "r" key on the keyboard to move
								// pyramid right
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
																// along x
																// axis
				}
				break;
			case KeyEvent.VK_DOWN:
				if (e.isShiftDown()) {// press shift + down arrow to scale
										// the
					// pyramid down in size
					scaleDwn(polyList.get(currentIndex));
				} else {
					rotateX(polyList.get(currentIndex), false);// rotate DOWN
																// along
																// x axis
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
			case KeyEvent.VK_PERIOD: // press the ">" key to rotate the
										// pyramid right along the z axis
				rotateZ(polyList.get(currentIndex), false);
				break;
			case KeyEvent.VK_COMMA: // press the "<" key to rortate the
									// pyramid left along the z axis
				rotateZ(polyList.get(currentIndex), true);
				break;

			case KeyEvent.VK_F:
				moveForward(polyList.get(currentIndex));
				break;
			case KeyEvent.VK_B:
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
	 * Method that takes a trangle and sets its value to a default position
	 * 
	 * @param Trangle
	 *            trng
	 */
	public void setDefault(Trangle trng) {
		// p0
		trng.defXPos[0] = 0;
		trng.defYPos[0] = 150;
		trng.defZPos[0] = 350;
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
		trng.defZPos[3] = 600;
		// p4
		trng.defXPos[4] = 150;
		trng.defYPos[4] = -150;
		trng.defZPos[4] = 600;

		trng.midXPnt = 0;
		trng.midYPnt = 0;
		trng.midZPnt = 350;

	}

	
	public void calcPolystuff(Trangle trng){
		
	}
	
	public void calcSurfaceNormals(Trangle trng) {

		// Triangle 1 normals
		//vector 1 
		double vect1x = trng.defXPos[1] - trng.defXPos[0];
		double vect1y = trng.defYPos[1] - trng.defYPos[0];
		double vect1z = trng.defZPos[1] - trng.defZPos[0];
		
		//vector 2
		double vect2x = trng.defXPos[2] - trng.defXPos[0];
		double vect2y = trng.defYPos[2] - trng.defYPos[0];
		double vect2z = trng.defZPos[2] - trng.defZPos[0];

		//normal vector from the two vectors of the triangle
		trng.normX[0] = (vect1y * vect2z) - (vect1z * vect2y);
		trng.normY[0] = -(vect1z * vect2x) - (vect1x * vect2z);
		trng.normZ[0] = (vect1x * vect2y) - (vect1y * vect2x);

		//"plane offset"  D  used in calc
		trng.normOffset[0] = (trng.defXPos[0] * trng.normX[0]) - (trng.defYPos[0] * trng.normY[0]) + (trng.defZPos[0] * trng.normZ[0]);

		//final equation to compute visibility
		double plsbeover = (0 * trng.normX[0]) - (0 * trng.normY[0]) + (-eye * trng.normZ[0]) - trng.normOffset[0];
		
		//set visible
		if (plsbeover <= 0) {
			trng.isVisible[0] = true;
		} else {
			trng.isVisible[0] = false;
		}
		
		//rinse, repeat

		// Triangle 2 normals
		vect1x = trng.defXPos[3] - trng.defXPos[0];
		vect1y = trng.defYPos[3] - trng.defYPos[0];
		vect1z = trng.defZPos[3] - trng.defZPos[0];

		vect2x = trng.defXPos[1] - trng.defXPos[0];
		vect2y = trng.defYPos[1] - trng.defYPos[0];
		vect2z = trng.defZPos[1] - trng.defZPos[0];

		trng.normX[1] = (vect1y * vect2z) - (vect1z * vect2y);
		trng.normY[1] = -(vect1z * vect2x) - (vect1x * vect2z);
		trng.normZ[1] = (vect1x * vect2y) - (vect1y * vect2x);

		trng.normOffset[1] = (trng.defXPos[3] * trng.normX[1]) - (trng.defYPos[3] * trng.normY[1]) + (trng.defZPos[3] * trng.normZ[1]);
		
		plsbeover = (0 * trng.normX[1]) - (0 * trng.normY[1]) + (-eye * trng.normZ[1]) - trng.normOffset[1];
		
		if (plsbeover <= 0) {
			trng.isVisible[1] = true;
		} else {
			trng.isVisible[1] = false;
		}

		// Triangle 3 normals
		vect1x = trng.defXPos[4] - trng.defXPos[0];
		vect1y = trng.defYPos[4] - trng.defYPos[0];
		vect1z = trng.defZPos[4] - trng.defZPos[0];

		vect2x = trng.defXPos[3] - trng.defXPos[0];
		vect2y = trng.defYPos[3] - trng.defYPos[0];
		vect2z = trng.defZPos[3] - trng.defZPos[0];

		trng.normX[2] = (vect1y * vect2z) - (vect1z * vect2y);
		trng.normY[2] = -(vect1z * vect2x) - (vect1x * vect2z);
		trng.normZ[2] = (vect1x * vect2y) - (vect1y * vect2x);

		trng.normOffset[2] = (trng.defXPos[4] * trng.normX[2]) - (trng.defYPos[4] * trng.normY[2]) + (trng.defZPos[4] * trng.normZ[2]);
		
		plsbeover = (0 * trng.normX[2]) - (0 * trng.normY[2]) + (-eye * trng.normZ[2]) - trng.normOffset[2];
		
		if (plsbeover <= 0) {
			trng.isVisible[2] = true;
		} else {
			trng.isVisible[2] = false;
		}

		// Triangle 4 normals
		vect1x = trng.defXPos[2] - trng.defXPos[0];
		vect1y = trng.defYPos[2] - trng.defYPos[0];
		vect1z = trng.defZPos[2] - trng.defZPos[0];

		vect2x = trng.defXPos[4] - trng.defXPos[0];
		vect2y = trng.defYPos[4] - trng.defYPos[0];
		vect2z = trng.defZPos[4] - trng.defZPos[0];

		trng.normX[3] = (vect1y * vect2z) - (vect1z * vect2y);
		trng.normY[3] = -(vect1z * vect2x) - (vect1x * vect2z);
		trng.normZ[3] = (vect1x * vect2y) - (vect1y * vect2x);

		trng.normOffset[3] = (trng.defXPos[2] * trng.normX[3]) - (trng.defYPos[2] * trng.normY[3]) + (trng.defZPos[2] * trng.normZ[3]);
		
		plsbeover = (0 * trng.normX[3]) - (0 * trng.normY[3]) + (-eye * trng.normZ[3]) - trng.normOffset[3];
		
		if (plsbeover <= 0) {
			trng.isVisible[3] = true;
		} else {
			trng.isVisible[3] = false;
		}

		// Triangle Base normals
		vect1x = trng.defXPos[4] - trng.defXPos[1];
		vect1y = trng.defYPos[4] - trng.defYPos[1];
		vect1z = trng.defZPos[4] - trng.defZPos[1];

		vect2x = trng.defXPos[2] - trng.defXPos[1];
		vect2y = trng.defYPos[2] - trng.defYPos[1];
		vect2z = trng.defZPos[2] - trng.defZPos[1];

		
		trng.normX[4] = (vect1y * vect2z) - (vect1z * vect2y);
		trng.normY[4] = -(vect1z * vect2x) - (vect1x * vect2z);
		trng.normZ[4] = (vect1x * vect2y) - (vect1y * vect2x);

		trng.normOffset[4] = (trng.defXPos[2] * trng.normX[4]) - (trng.defYPos[2] * trng.normY[4]) + (trng.defZPos[2] * trng.normZ[4]);
		
		plsbeover = (0 * trng.normX[4]) - (0 * trng.normY[4]) + (-eye * trng.normZ[4]) - trng.normOffset[4];
		
		if (plsbeover <= 0) {
			trng.isVisible[4] = true;
		} else {
			trng.isVisible[4] = false;
		}
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

		for (int i = 0; i < 5; i++) {
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

	public void calcYmaxmin(Trangle trng){
		
		for(int i = 0; i < 5; i++){
			if(trng.defYPos[i] > trng.yMax){
				trng.yMax = trng.defYPos[i];
			}
			if(trng.defYPos[i] < trng.yMin){
				trng.yMin = trng.defYPos[i];
			}
		}
	}
	
	/**
	 * Method that draws all of the possible trangles on the screen
	 * 
	 * @param g
	 */
	private void drawTrangle(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;

		int xoff = width / 2;
		int yoff = height / 2;

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

			for (int k = 0; k < 5; k++) {
				polyList.get(i).currXPos[k] = perspective(
						polyList.get(i).defXPos[k], polyList.get(i).defZPos[k]);
				polyList.get(i).currYPos[k] = perspective(
						polyList.get(i).defYPos[k], polyList.get(i).defZPos[k]);
			}

			g2d.setColor(Color.BLACK);
			g2d.drawLine((int) (polyList.get(i).currXPos[0] + xoff),
					(int) (yoff - polyList.get(i).currYPos[0]),
					(int) (polyList.get(i).currXPos[1] + xoff),
					(int) (yoff - polyList.get(i).currYPos[1]));
			// top to outside bottom left
			g2d.drawLine((int) (polyList.get(i).currXPos[0] + xoff),
					(int) (yoff - polyList.get(i).currYPos[0]),
					(int) (polyList.get(i).currXPos[2] + xoff),
					(int) (yoff - polyList.get(i).currYPos[2]));
			// top to outside bottom right
			g2d.drawLine((int) (polyList.get(i).currXPos[0] + xoff),
					(int) (yoff - polyList.get(i).currYPos[0]),
					(int) (polyList.get(i).currXPos[3] + xoff),
					(int) (yoff - polyList.get(i).currYPos[3]));
			// top to inside bottom left
			g2d.drawLine((int) (polyList.get(i).currXPos[4] + xoff),
					(int) (yoff - polyList.get(i).currYPos[4]),
					(int) (polyList.get(i).currXPos[0] + xoff),
					(int) (yoff - polyList.get(i).currYPos[0]));
			// inside bottom right to top
			g2d.setColor(Color.GREEN); // front side
			g2d.drawLine((int) (polyList.get(i).currXPos[1] + xoff),
					(int) (yoff - polyList.get(i).currYPos[1]),
					(int) (polyList.get(i).currXPos[2] + xoff),
					(int) (yoff - polyList.get(i).currYPos[2]));
			// outside bottom left to outside bottom right
			g2d.setColor(Color.RED); // back side
			g2d.drawLine((int) (polyList.get(i).currXPos[3] + xoff),
					(int) (yoff - polyList.get(i).currYPos[3]),
					(int) (polyList.get(i).currXPos[4] + xoff),
					(int) (yoff - polyList.get(i).currYPos[4]));
			// inside bottom left to inside bottom right
			g2d.setColor(Color.YELLOW); // left side
			g2d.drawLine((int) (polyList.get(i).currXPos[1] + xoff),
					(int) (yoff - polyList.get(i).currYPos[1]),
					(int) (polyList.get(i).currXPos[3] + xoff),
					(int) (yoff - polyList.get(i).currYPos[3]));
			// outside bottom left to inside bottom left
			g2d.setColor(Color.BLUE); // right side
			g2d.drawLine((int) (polyList.get(i).currXPos[4] + xoff),
					(int) (yoff - polyList.get(i).currYPos[4]),
					(int) (polyList.get(i).currXPos[2] + xoff),
					(int) (yoff - polyList.get(i).currYPos[2]));
			// inside bottom right to outside bottom right
			g2d.setColor(Color.BLACK);

			if (polyList.get(i).getFocusedTrangle()) {
				g2d.setStroke(new BasicStroke(1));
			}
		}
	}
	
	public void drawTriangleWithPolyFill(Graphics g){
		
	}
	
	public void drawTriangleWithZBuffer(Graphics g){
		
	}

	// guess
	public void drawTrangleWithBackFaceCulling(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;

		int xoff = width / 2;
		int yoff = height / 2;

		for (int p = 0; p < polyList.size(); p++) {
			if (currentIndex == p) {
				polyList.get(p).setFocusedTrangle(true);
			} else {
				polyList.get(p).setFocusedTrangle(false);
			}
		}

		for (int i = 0; i < polyList.size(); i++) {

			calcSurfaceNormals(polyList.get(i));

			// finds the currently used trangle and makes its lines wider
			if (polyList.get(i).getFocusedTrangle()) {
				g2d.setStroke(new BasicStroke(5));
			}

			for (int k = 0; k < 5; k++) {
				polyList.get(i).currXPos[k] = perspective(
						polyList.get(i).defXPos[k], polyList.get(i).defZPos[k]);
				polyList.get(i).currYPos[k] = perspective(
						polyList.get(i).defYPos[k], polyList.get(i).defZPos[k]);
			}

			g2d.setColor(Color.BLACK);
			
			if (polyList.get(i).isVisible[0] == true) {
				// p0 to p1
				g2d.drawLine((int) (polyList.get(i).currXPos[0] + xoff),
						(int) (yoff - polyList.get(i).currYPos[0]),
						(int) (polyList.get(i).currXPos[1] + xoff),
						(int) (yoff - polyList.get(i).currYPos[1]));
				// p0 to p2
				g2d.drawLine((int) (polyList.get(i).currXPos[0] + xoff),
						(int) (yoff - polyList.get(i).currYPos[0]),
						(int) (polyList.get(i).currXPos[2] + xoff),
						(int) (yoff - polyList.get(i).currYPos[2]));
				g2d.setColor(Color.GREEN); // front side
				// p1 to p2
				g2d.drawLine((int) (polyList.get(i).currXPos[1] + xoff),
						(int) (yoff - polyList.get(i).currYPos[1]),
						(int) (polyList.get(i).currXPos[2] + xoff),
						(int) (yoff - polyList.get(i).currYPos[2]));
				g2d.setColor(Color.BLACK);
			}

			if (polyList.get(i).isVisible[1] == true) {
				// p0 to p1
				g2d.drawLine((int) (polyList.get(i).currXPos[0] + xoff),
						(int) (yoff - polyList.get(i).currYPos[0]),
						(int) (polyList.get(i).currXPos[1] + xoff),
						(int) (yoff - polyList.get(i).currYPos[1]));
				// p0 to p3
				g2d.drawLine((int) (polyList.get(i).currXPos[0] + xoff),
						(int) (yoff - polyList.get(i).currYPos[0]),
						(int) (polyList.get(i).currXPos[3] + xoff),
						(int) (yoff - polyList.get(i).currYPos[3]));
				g2d.setColor(Color.YELLOW); // left side
				// p1 to p3
				g2d.drawLine((int) (polyList.get(i).currXPos[1] + xoff),
						(int) (yoff - polyList.get(i).currYPos[1]),
						(int) (polyList.get(i).currXPos[3] + xoff),
						(int) (yoff - polyList.get(i).currYPos[3]));
				g2d.setColor(Color.BLACK);
			}

			if (polyList.get(i).isVisible[2] == true) {
				// p0 to p3
				g2d.drawLine((int) (polyList.get(i).currXPos[0] + xoff),
						(int) (yoff - polyList.get(i).currYPos[0]),
						(int) (polyList.get(i).currXPos[3] + xoff),
						(int) (yoff - polyList.get(i).currYPos[3]));
				// p4 to p0
				g2d.drawLine((int) (polyList.get(i).currXPos[4] + xoff),
						(int) (yoff - polyList.get(i).currYPos[4]),
						(int) (polyList.get(i).currXPos[0] + xoff),
						(int) (yoff - polyList.get(i).currYPos[0]));
				g2d.setColor(Color.RED); // back side
				// p3 to p4
				g2d.drawLine((int) (polyList.get(i).currXPos[3] + xoff),
						(int) (yoff - polyList.get(i).currYPos[3]),
						(int) (polyList.get(i).currXPos[4] + xoff),
						(int) (yoff - polyList.get(i).currYPos[4]));
				g2d.setColor(Color.BLACK);
			}

			if (polyList.get(i).isVisible[3] == true) {
				// p4 to p0
				g2d.drawLine((int) (polyList.get(i).currXPos[4] + xoff),
						(int) (yoff - polyList.get(i).currYPos[4]),
						(int) (polyList.get(i).currXPos[0] + xoff),
						(int) (yoff - polyList.get(i).currYPos[0]));
				// p0 to p2
				g2d.drawLine((int) (polyList.get(i).currXPos[0] + xoff),
						(int) (yoff - polyList.get(i).currYPos[0]),
						(int) (polyList.get(i).currXPos[2] + xoff),
						(int) (yoff - polyList.get(i).currYPos[2]));
				g2d.setColor(Color.BLUE); // right side
				// p4 to p2
				g2d.drawLine((int) (polyList.get(i).currXPos[4] + xoff),
						(int) (yoff - polyList.get(i).currYPos[4]),
						(int) (polyList.get(i).currXPos[2] + xoff),
						(int) (yoff - polyList.get(i).currYPos[2]));
				g2d.setColor(Color.BLACK);
			}

			if (polyList.get(i).isVisible[4] == true) {
				g2d.setColor(Color.BLUE); // right side
				// p4 to p2
				g2d.drawLine((int) (polyList.get(i).currXPos[4] + xoff),
						(int) (yoff - polyList.get(i).currYPos[4]),
						(int) (polyList.get(i).currXPos[2] + xoff),
						(int) (yoff - polyList.get(i).currYPos[2]));
				g2d.setColor(Color.RED); // back side
				// p3 to p4
				g2d.drawLine((int) (polyList.get(i).currXPos[3] + xoff),
						(int) (yoff - polyList.get(i).currYPos[3]),
						(int) (polyList.get(i).currXPos[4] + xoff),
						(int) (yoff - polyList.get(i).currYPos[4]));
				g2d.setColor(Color.YELLOW); // left side
				// p1 to p3
				g2d.drawLine((int) (polyList.get(i).currXPos[1] + xoff),
						(int) (yoff - polyList.get(i).currYPos[1]),
						(int) (polyList.get(i).currXPos[3] + xoff),
						(int) (yoff - polyList.get(i).currYPos[3]));
				g2d.setColor(Color.GREEN); // front side
				// p1 to p2
				g2d.drawLine((int) (polyList.get(i).currXPos[1] + xoff),
						(int) (yoff - polyList.get(i).currYPos[1]),
						(int) (polyList.get(i).currXPos[2] + xoff),
						(int) (yoff - polyList.get(i).currYPos[2]));
				g2d.setColor(Color.BLACK);
			}

			g2d.setColor(Color.BLACK);

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
		for (int i = 0; i < 5; i++) {
			if (!(trng.currYPos[i] >= (height / 2))) {
				actionBool = true;
			} else {
				actionBool = false;
				break;
			}
		}

		calcMidPoints(trng);

		if (actionBool == true) {
			for (int i = 0; i < 5; i++) {
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
		for (int i = 0; i < 5; i++) {
			if (!(trng.currYPos[i] <= -height / 2)) {
				actionBool = true;
			} else {
				actionBool = false;
				break;
			}
		}

		calcMidPoints(trng);

		if (actionBool == true) {
			for (int i = 0; i < 5; i++) {
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
		for (int i = 0; i < 5; i++) {
			if (!(trng.currXPos[i] >= width / 2)) {
				actionBool = true;
			} else {
				actionBool = false;
				break;
			}
		}

		calcMidPoints(trng);

		if (actionBool == true) {
			for (int i = 0; i < 5; i++) {
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
		for (int i = 0; i < 5; i++) {
			if (!(trng.currXPos[i] <= -(width / 2))) {
				actionBool = true;
			} else {
				actionBool = false;
				break;
			}
		}

		calcMidPoints(trng);

		if (actionBool == true) {
			for (int i = 0; i < 5; i++) {
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
		for (int i = 0; i < 5; i++) {
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
			for (int i = 0; i < 5; i++) {
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

		for (int i = 0; i < 5; i++) {
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

		for (int i = 0; i < 5; i++) {
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

		for (int i = 0; i < 5; i++) {
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

		double theta = 25.0;
		if (bool) {
			theta = -25.0;
		}

		calcMidPoints(trng);

		for (int i = 0; i < 5; i++) {
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

		double theta = 25.0;
		if (bool) {
			theta = -25.0;
		}

		calcMidPoints(trng);

		for (int i = 0; i < 5; i++) {
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

		double theta = 25.0;
		if (bool) {
			theta = -25.0;
		}

		calcMidPoints(trng);
		for (int i = 0; i < 5; i++) {
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
		if (backface) {
			drawTrangleWithBackFaceCulling(g);
		} else if(polyfillindex != 0) {
			drawTriangleWithPolyFill(g);
		} else if(zbuffertoggle) {
			drawTriangleWithZBuffer(g);
		} else {
			drawTrangle(g);
		}
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
