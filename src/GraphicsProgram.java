import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
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

	//Dimensions for the screen
	int width = 800;
	int height = 800;

	//Distance from the eye to the center of the screen
	int eye = 1000;

	/**
	 * Method taking a point and a zdistance that returns the prospective projection of a point in the program
	 * 
	 * @param point
	 * @param zdis
	 * @return int
	 */
	public int perspective(double point, double zdis) {
		return (int) ((eye * point) / (eye + zdis) + 50);
	}
	
	//Vector list of all of the current trangle objects in the program
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
		setBackground(Color.WHITE);
		Trangle mangle = new Trangle();
		polyList.add(mangle);
		setVisible(true);
		doStuff(getGraphics());
	}

	/**
	 * Polygon class for graphics program
	 * 
	 * @author Landon
	 *
	 */
	class Trangle {

		//x y z positions of the a figure
		double[] defXPos = new double[5];
		double[] defYPos = new double[5];
		double[] defZPos = new double[5];

		//logical midpoint of the polygon
		double midXPnt;
		double midYPnt;
		double midZPnt;

		//actual 2d dimensions of the midpoint
		int currMidXPnt;
		int currMidYPnt;

		//actual 2d dimensions of the polygon
		int[] currXPos = new int[5];
		int[] currYPos = new int[5];

		/**
		 * Trangle constructor
		 */
		public Trangle() {
			setDefault(this);
			setFocusedTrangle(true);
		}

		/**
		 * Sets the current Trangle to be focused by the program
		 * @param boolean bool
		 */
		public void setFocusedTrangle(boolean bool) {
			setFocusable(bool);
		}

		/**
		 * returns wether or not the current trangle is focused
		 * @return boolean
		 */
		public boolean getFocusedTrangle() {
			return isFocusOwner();
		}

	}

	/**
	 * KeyListener class for the graphics program that takes the keyboard input
	 */
	public class myKeyListener extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_U: // press "u" key on the keyboard to move
								// pyramid up
				moveUp(polyList.get(0));
				paintComponent(getGraphics());
				break;
			case KeyEvent.VK_D: // press "d" key on the keyboard to move
								// pyramid down
				moveDwn(polyList.get(0));
				paintComponent(getGraphics());
				break;
			case KeyEvent.VK_R: // press "r" key on the keyboard to move
								// pyramid right
				moveRght(polyList.get(0));
				paintComponent(getGraphics());
				break;
			case KeyEvent.VK_L: // press "l" key on keyboard to move pyramid
								// left
				moveLft(polyList.get(0));
				paintComponent(getGraphics());
				break;
			case KeyEvent.VK_UP:
				if (e.isShiftDown()) {// press shift + up arrow to scale the
					// pyramid up in size
					scaleUp(polyList.get(0));
					paintComponent(getGraphics());
				} else {
					rotateXUp(polyList.get(0)); // rotate UP along x axis
					paintComponent(getGraphics());
				}
				break;
			case KeyEvent.VK_DOWN:
				if (e.isShiftDown()) {// press shift + down arrow to scale
										// the
					// pyramid down in size
					scaleDwn(polyList.get(0));
					paintComponent(getGraphics());
				} else {
					rotateXDown(polyList.get(0));// rotate DOWN along x axis
					paintComponent(getGraphics());
				}
				break;
			case KeyEvent.VK_RIGHT: // press right arrow key to rotate right
									// around the y axis
				rotateYRight(polyList.get(0));
				paintComponent(getGraphics());
				break;
			case KeyEvent.VK_LEFT: // press right arrow key to rotate right
									// around the y axis
				rotateYLeft(polyList.get(0));
				paintComponent(getGraphics());
				break;
			case KeyEvent.VK_PERIOD: // press the ">" key to rotate the
										// pyramid right along the z axis
				rotateZRight(polyList.get(0));
				paintComponent(getGraphics());
				break;
			case KeyEvent.VK_COMMA: // press the "<" key to rortate the
									// pyramid left along the z axis
				rotateZLeft(polyList.get(0));
				paintComponent(getGraphics());
				break;

			case KeyEvent.VK_F:
				moveForward(polyList.get(0));
				paintComponent(getGraphics());
				break;
			case KeyEvent.VK_B:
				moveBackward(polyList.get(0));
				paintComponent(getGraphics());
				break;
			case KeyEvent.VK_ENTER: // press enter to return to the default
									// position
				setDefault(polyList.get(0));
				paintComponent(getGraphics());
				break;
			}
			repaint();
		}
	}
	
	/**
	 * Method that takes a trangle and sets its value to a default position
	 * @param Trangle trng
	 */
	public void setDefault(Trangle trng) {
		// 250,100 the top vertex
		trng.defXPos[0] = 0;
		trng.defYPos[0] = 150;
		trng.defZPos[0] = 350;
		// 100,400 outside bottom left
		trng.defXPos[1] = -150;
		trng.defYPos[1] = -150;
		trng.defZPos[1] = 100;
		// 400,400 outside bottom right
		trng.defXPos[2] = 150;
		trng.defYPos[2] = -150;
		trng.defZPos[2] = 100;
		// 175,350 inside bottom left
		trng.defXPos[3] = -150;
		trng.defYPos[3] = -150;
		trng.defZPos[3] = 600;
		// 325,350 inside bottom right
		trng.defXPos[4] = 150;
		trng.defYPos[4] = -150;
		trng.defZPos[4] = 600;

		System.out.println(perspective(trng.defXPos[0], trng.defZPos[0]));

		for (int i = 0; i < 5; i++) {
			trng.currXPos[i] = perspective(trng.defXPos[i], trng.defZPos[i]);
			trng.currYPos[i] = perspective(trng.defYPos[i], trng.defZPos[i]);
		}
	}

	/**
	 * Method that draws all of the possible trangles on the screen
	 * @param g
	 */
	private void drawTrangle(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;

		int off = height / 2;

		for (int i = 0; i < polyList.size(); i++) {

			//finds the currently used trangle and makes its lines wider
			if (polyList.get(i).getFocusedTrangle()) {
				g2d.setStroke(new BasicStroke(5));
			}

			g2d.setColor(Color.BLACK);
			g2d.drawLine(polyList.get(i).currXPos[0] + off,
					off - polyList.get(i).currYPos[0],
					polyList.get(i).currXPos[1] + off, off
							- polyList.get(i).currYPos[1]);
			// top to outside bottom left
			g2d.drawLine(polyList.get(i).currXPos[0] + off,
					off - polyList.get(i).currYPos[0],
					polyList.get(i).currXPos[2] + off, off
							- polyList.get(i).currYPos[2]);
			// top to outside bottom right
			g2d.drawLine(polyList.get(i).currXPos[0] + off,
					off - polyList.get(i).currYPos[0],
					polyList.get(i).currXPos[3] + off, off
							- polyList.get(i).currYPos[3]);
			// top to inside bottom left
			g2d.drawLine(polyList.get(i).currXPos[4] + off,
					off - polyList.get(i).currYPos[4],
					polyList.get(i).currXPos[0] + off, off
							- polyList.get(i).currYPos[0]);
			// inside bottom right to top
			g2d.setColor(Color.GREEN); // front side
			g2d.drawLine(polyList.get(i).currXPos[1] + off,
					off - polyList.get(i).currYPos[1],
					polyList.get(i).currXPos[2] + off, off
							- polyList.get(i).currYPos[2]);
			// outside bottom left to outside bottom right
			g2d.setColor(Color.RED); // back side
			g2d.drawLine(polyList.get(i).currXPos[3] + off,
					off - polyList.get(i).currYPos[3],
					polyList.get(i).currXPos[4] + off, off
							- polyList.get(i).currYPos[4]);
			// inside bottom left to inside bottom right
			g2d.setColor(Color.YELLOW); // left side
			g2d.drawLine(polyList.get(i).currXPos[1] + off,
					off - polyList.get(i).currYPos[1],
					polyList.get(i).currXPos[3] + off, off
							- polyList.get(i).currYPos[3]);
			// outside bottom left to inside bottom left
			g2d.setColor(Color.BLUE); // right side
			g2d.drawLine(polyList.get(i).currXPos[4] + off,
					off - polyList.get(i).currYPos[4],
					polyList.get(i).currXPos[2] + off, off
							- polyList.get(i).currYPos[2]);
			// inside bottom right to outside bottom right
			g2d.setColor(Color.BLACK);

			if (polyList.get(i).getFocusedTrangle()) {
				g2d.setStroke(new BasicStroke(1));
			}

			for (int j = 0; j < 5; j++) {
				System.out.println("p" + j + " = " + "("
						+ polyList.get(i).currXPos[j] + ","
						+ polyList.get(i).currYPos[j] + ")");
			}
		}
	}

	/**
	 * Method that moves all of the Y points of the selected polygon in the positive y position
	 * @param Trangle trng
	 */
	public void moveUp(Trangle trng) {
		Boolean actionBool = false;
		for (int i = 0; i < 5; i++) {
			if (!(trng.currYPos[i] <= -550)) {
				actionBool = true;
			} else {
				actionBool = false;
				break;
			}
		}

		if (actionBool == true) {
			for (int i = 0; i < 5; i++) {
				trng.defYPos[i] = trng.defYPos[i] - 10;
				trng.currYPos[i] = perspective(trng.defYPos[i], trng.defZPos[i]);
			}
		}
	}

	/**
	 * Method that move all of the Y points of the selected polygon in the negative y position
	 * @param Trangle trng
	 */
	public void moveDwn(Trangle trng) {
		Boolean actionBool = false;
		for (int i = 0; i < 5; i++) {
			if (!(trng.currYPos[i] >= 550)) {
				actionBool = true;
			} else {
				actionBool = false;
				break;
			}
		}

		if (actionBool == true) {
			for (int i = 0; i < 5; i++) {
				trng.defYPos[i] = trng.defYPos[i] + 10;
				trng.currYPos[i] = perspective(trng.defYPos[i], trng.defZPos[i]);
			}
		}
	}

	/**
	 * Method that moves all of the x points of the selected polygon in the positive x position
	 * @param Trangle trng
	 */
	public void moveRght(Trangle trng) {
		Boolean actionBool = false;
		for (int i = 0; i < 5; i++) {
			if (!(trng.currXPos[i] >= 550)) {
				actionBool = true;
			} else {
				actionBool = false;
				break;
			}
		}

		if (actionBool == true) {
			for (int i = 0; i < 5; i++) {
				trng.defXPos[i] = trng.defXPos[i] + 10;
				trng.currXPos[i] = perspective(trng.defXPos[i], trng.defZPos[i]);
			}
		}
	}

	/**
	 * Method that moves all of the x points of the selected polygon in the negative x positon
	 * @param Trangle trng
	 */
	public void moveLft(Trangle trng) {
		Boolean actionBool = false;
		for (int i = 0; i < 5; i++) {
			if (!(trng.currXPos[i] <= -550)) {
				actionBool = true;
			} else {
				actionBool = false;
				break;
			}
		}

		if (actionBool == true) {
			for (int i = 0; i < 5; i++) {
				trng.defXPos[i] = trng.defXPos[i] - 10;
				trng.currXPos[i] = perspective(trng.defXPos[i], trng.defZPos[i]);
			}
		}
	}

	/**
	 * Method that increases the size of selected polygon by increasing the distance of all the points from one another
	 * @param Trangle trng
	 */
	public void scaleUp(Trangle trng) {
		Boolean actionBool = false;
		for (int i = 0; i < 5; i++) {
			if (!(trng.currXPos[i] >= 550 || trng.currYPos[i] >= 550
					|| trng.currYPos[i] <= -550 || trng.currXPos[i] <= -550)) {
				actionBool = true;
			} else {
				actionBool = false;
				break;
			}
		}

		if (actionBool == true) {
			for (int i = 0; i < 5; i++) {
				trng.defXPos[i] = (trng.defXPos[i] * 1.05);
				trng.defYPos[i] = (trng.defYPos[i] * 1.05);
				trng.currXPos[i] = perspective(trng.defXPos[i], trng.defZPos[i]);
				trng.currYPos[i] = perspective(trng.defYPos[i], trng.defZPos[i]);
			}
		}
	}

	/**
	 * Method that decreases the size of a polygon by decreasing the distance of all the points from one another
	 * @param Trangle trng
	 */
	public void scaleDwn(Trangle trng) {
		for (int i = 0; i < 5; i++) {
			trng.defXPos[i] = (trng.defXPos[i] * .95);
			trng.defYPos[i] = (trng.defYPos[i] * .95);
			trng.currXPos[i] = perspective(trng.defXPos[i], trng.defZPos[i]);
			trng.currYPos[i] = perspective(trng.defYPos[i], trng.defZPos[i]);
		}
	}

	/**
	 * Method that moves all of the z points of the selected polygon into the positive z direction (right hand rule)
	 * @param Trangle trng
	 */
	public void moveForward(Trangle trng) {
		for (int i = 0; i < 5; i++) {
			trng.defZPos[i] = trng.defZPos[i] - 100;
			trng.currXPos[i] = perspective(trng.defXPos[i], trng.defZPos[i]);
		}
	}

	/**
	 * Method that moves all of the z points of the selected polygon into the negative z direction (right hand rule)
	 * @param Trangle trng
	 */
	public void moveBackward(Trangle trng) {
		for (int i = 0; i < 5; i++) {
			trng.defZPos[i] = trng.defZPos[i] + 100;
			trng.currXPos[i] = perspective(trng.defXPos[i], trng.defZPos[i]);
		}
	}

	/**
	 * Method that rotates the selected polygon around the z axis clockwise
	 * @param Trangle trng
	 */
	public void rotateZRight(Trangle trng) { // clockwise
		double theta = -25.0;

		for (int i = 0; i < 5; i++) {
			trng.defXPos[i] = (trng.defXPos[i] * Math.cos(theta) - trng.defYPos[i]
					* Math.sin(theta));
			trng.defYPos[i] = (trng.defXPos[i] * Math.sin(theta) + trng.defYPos[i]
					* Math.cos(theta));
			trng.currXPos[i] = perspective(trng.defXPos[i], trng.defZPos[i]);
			trng.currYPos[i] = perspective(trng.defYPos[i], trng.defZPos[i]);
		}

	}

	/**
	 * Method that rotates the selected polygon around the z axis counter-clockwise
	 * @param Trangle trng
	 */
	public void rotateZLeft(Trangle trng) { // counter clockwise

		double theta = 25.0;

		for (int i = 0; i < 5; i++) {
			trng.defXPos[i] = (trng.defXPos[i] * Math.cos(theta) - trng.defYPos[i]
					* Math.sin(theta));
			trng.defYPos[i] = (trng.defXPos[i] * Math.sin(theta) + trng.defYPos[i]
					* Math.cos(theta));
			trng.currXPos[i] = perspective(trng.defXPos[i], trng.defZPos[i]);
			trng.currYPos[i] = perspective(trng.defYPos[i], trng.defZPos[i]);
		}

	}

	/**
	 * Method that rotates the selected polygon around the y axis counter-clockwise
	 * @param Trangle trng
	 */
	public void rotateYRight(Trangle trng) {

		double theta = 25.0;

		for (int i = 0; i < 5; i++) {
			trng.defXPos[i] = (trng.defXPos[i] * Math.cos(theta) - trng.defZPos[i]
					* Math.sin(theta));
			trng.defZPos[i] = (trng.defXPos[i] * Math.sin(theta) + trng.defZPos[i]
					* Math.cos(theta));
			trng.currXPos[i] = perspective(trng.defXPos[i], trng.defZPos[i]);
			trng.currYPos[i] = perspective(trng.defYPos[i], trng.defZPos[i]);
		}

	}

	/**
	 * Method that rotates the selected polygon around the y axis clockwise
	 * @param Trangle trng
	 */
	public void rotateYLeft(Trangle trng) {

		double theta = -25.0;

		for (int i = 0; i < 5; i++) {
			trng.defXPos[i] = (trng.defXPos[i] * Math.cos(theta) - trng.defZPos[i]
					* Math.sin(theta));
			trng.defZPos[i] = (trng.defXPos[i] * Math.sin(theta) + trng.defZPos[i]
					* Math.cos(theta));
			trng.currXPos[i] = perspective(trng.defXPos[i], trng.defZPos[i]);
			trng.currYPos[i] = perspective(trng.defYPos[i], trng.defZPos[i]);
		}

	}

	/**
	 * Method that rotates the selected polygon around the x axis clockwise 
	 * @param Trangle trng
	 */
	public void rotateXUp(Trangle trng) {

		double theta = 25.0;

		for (int i = 0; i < 5; i++) {
			trng.defYPos[i] = (trng.defYPos[i] * Math.cos(theta) - trng.defZPos[i]
					* Math.sin(theta));
			trng.defZPos[i] = (trng.defYPos[i] * Math.sin(theta) + trng.defZPos[i]
					* Math.cos(theta));
			trng.currXPos[i] = perspective(trng.defXPos[i], trng.defZPos[i]);
			trng.currYPos[i] = perspective(trng.defYPos[i], trng.defZPos[i]);
		}

	}

	/**
	 * Method that rotates the selected polygon around the x axis counter-clockwise
	 * @param Trangle trng
	 */
	public void rotateXDown(Trangle trng) {

		double theta = -25.0;

		for (int i = 0; i < 5; i++) {
			trng.defYPos[i] = (trng.defYPos[i] * Math.cos(theta) - trng.defZPos[i]
					* Math.sin(theta));
			trng.defZPos[i] = (trng.defYPos[i] * Math.sin(theta) + trng.defZPos[i]
					* Math.cos(theta));
			trng.currXPos[i] = perspective(trng.defXPos[i], trng.defZPos[i]);
			trng.currYPos[i] = perspective(trng.defYPos[i], trng.defZPos[i]);
		}

	}

	/**
	 * Method that draws the lines on the page
	 * @param g
	 */
	private void doStuff(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;

		g2d.drawLine(width / 2, 0, width / 2, height);
		g2d.drawLine(0, height / 2, width, height / 2);
	}

	/**
	 * Method that does the main drawing of the components on the screen
	 * @param g
	 */
	public void paintComponent(Graphics g) {
		drawTrangle(g);
	}

	/**
	 * Main method of the program
	 * @param args
	 */
	public static void main(String[] args) {
				new GraphicsProgram();
	}
}
