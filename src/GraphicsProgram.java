import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Graphics engine developed for 470 class
 * 
 * @author Landon Stanley
 */
public class GraphicsProgram extends JFrame {

	int WIDTH = 800;
	int HEIGHT = 800;

	// List of the polygons on the screen
	Vector<Trangle> polyList = new Vector<Trangle>();

	// initialization of the trangle ids
	int trangId = 1;

	int currIndex;

	/**
	 * Searches through the polygon list and returns the trangle with the
	 * current index
	 * 
	 * @param index
	 */
	private Trangle getById(int index) {
		for (int i = 0; i < polyList.size(); i++) {
			if (polyList.get(i).id == index) {
				return polyList.get(i);
			}
		}
		return null;
	}

	/**
	 * Draws the polygons on the screen and updates
	 * 
	 * @param g
	 */
	private void drawTrangle(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;

		int offY = HEIGHT / 2;
		int off = WIDTH / 2;

		for (int i = 0; i < polyList.size(); i++) {

			int[] currXPos = new int[5];
			int[] currYPos = new int[5];

			for (int j = 0; j < 5; j++) {
				currXPos[j] = (int) polyList.get(i).currXPos[j];
				currYPos[j] = (int) polyList.get(i).currYPos[j];
			}

			g2d.setColor(Color.BLACK);
			g2d.drawLine(currXPos[0] + off, offY - currYPos[0], currXPos[1]
					+ off, offY - currYPos[1]);
			// top to outside bottom left
			g2d.drawLine(currXPos[0] + off, offY - currYPos[0], currXPos[2]
					+ off, offY - currYPos[2]);
			// top to outside bottom right
			g2d.drawLine(currXPos[0] + off, offY - currYPos[0], currXPos[3]
					+ off, offY - currYPos[3]);
			// top to inside bottom left
			g2d.drawLine(currXPos[4] + off, offY - currYPos[4], currXPos[0]
					+ off, offY - currYPos[0]);
			// inside bottom right to top
			g2d.setColor(Color.GREEN); // front side
			g2d.drawLine(currXPos[1] + off, offY - currYPos[1], currXPos[2]
					+ off, offY - currYPos[2]);
			// outside bottom left to outside bottom right
			g2d.setColor(Color.RED); // back side
			g2d.drawLine(currXPos[3] + off, offY - currYPos[3], currXPos[4]
					+ off, offY - currYPos[4]);
			// inside bottom left to inside bottom right
			g2d.setColor(Color.YELLOW); // left side
			g2d.drawLine(currXPos[1] + off, offY - currYPos[1], currXPos[3]
					+ off, offY - currYPos[3]);
			// outside bottom left to inside bottom left
			g2d.setColor(Color.BLUE); // right side
			g2d.drawLine(currXPos[4] + off, offY - currYPos[4], currXPos[2]
					+ off, offY - currYPos[2]);
			// inside bottom right to outside bottom right
			g2d.setColor(Color.BLACK);

		}

	}

	/**
	 * Trangle class which is the actual polygon class that is drawn
	 * 
	 * @author Landon
	 */
	class Trangle extends JPanel {

		int id;

		private double[] defXPos = new double[5];
		private double[] defYPos = new double[5];
		private double[] defZPos = new double[5];

		private double[] currXPos = new double[5];
		private double[] currYPos = new double[5];

		private double eye = 1000;

		private Boolean drawTrangle = true;

		public double perspective(double point, double zdis) {
			return (eye * point) / (eye + zdis) + 50;
		}

		/**
		 * Constructor for Trangle class, gives the polygon an ID, gives it
		 * basic values and sets it to focusable
		 */
		public Trangle() {

			id = trangId;
			trangId = trangId + 1;
			setDefault();
			setFocusable(true);
		}

		/**
		 * Sets the current trangle to focusable
		 */
		public void setFocus() {
			setFocusable(true);
		}

		/**
		 * Sets the default values of the pyramid on creation or pressing the
		 * Reset/Enter key
		 */
		public void setDefault() {
			// 250,100 the top vertex
			defXPos[0] = 0;
			defYPos[0] = 150;
			defZPos[0] = 350;
			// 100,400 outside bottom left
			defXPos[1] = -150;
			defYPos[1] = -150;
			defZPos[1] = 100;
			// 400,400 outside bottom right
			defXPos[2] = 150;
			defYPos[2] = -150;
			defZPos[2] = 100;
			// 175,350 inside bottom left
			defXPos[3] = -150;
			defYPos[3] = -150;
			defZPos[3] = 600;
			// 325,350 inside bottom right
			defXPos[4] = 150;
			defYPos[4] = -150;
			defZPos[4] = 600;

			System.out.println(perspective(defXPos[0], defZPos[0]));

			for (int i = 0; i < 5; i++) {
				currXPos[i] = perspective(defXPos[i], defZPos[i]);
				currYPos[i] = perspective(defYPos[i], defZPos[i]);
			}

		}

		/**
		 * Translates the polygon into the positive Y direction
		 */
		public void moveUp() {
			Boolean actionBool = false;
			for (int i = 0; i < 5; i++) {
				if (!(currYPos[i] <= -550)) {
					actionBool = true;
				} else {
					actionBool = false;
					break;
				}
			}

			if (actionBool == true) {
				for (int i = 0; i < 5; i++) {
					defYPos[i] = defYPos[i] - 10;
					currYPos[i] = perspective(defYPos[i], defZPos[i]);
				}
			}
		}

		/**
		 * Translates the polygon into the negative Y direction
		 */
		public void moveDwn() {
			Boolean actionBool = false;
			for (int i = 0; i < 5; i++) {
				if (!(currYPos[i] >= 550)) {
					actionBool = true;
				} else {
					actionBool = false;
					break;
				}
			}

			if (actionBool == true) {
				for (int i = 0; i < 5; i++) {
					defYPos[i] = defYPos[i] + 10;
					currYPos[i] = perspective(defYPos[i], defZPos[i]);
				}
			}
		}

		/**
		 * Translates the polygon into the positive X direction
		 */
		public void moveRght() {
			Boolean actionBool = false;
			for (int i = 0; i < 5; i++) {
				if (!(currXPos[i] >= 550)) {
					actionBool = true;
				} else {
					actionBool = false;
					break;
				}
			}

			if (actionBool == true) {
				for (int i = 0; i < 5; i++) {
					defXPos[i] = defXPos[i] + 10;
					currXPos[i] = perspective(defXPos[i], defZPos[i]);
				}
			}
		}

		/**
		 * Translates the polygon into the negative X direction
		 */
		public void moveLft() {
			Boolean actionBool = false;
			for (int i = 0; i < 5; i++) {
				if (!(currXPos[i] <= -550)) {
					actionBool = true;
				} else {
					actionBool = false;
					break;
				}
			}

			if (actionBool == true) {
				for (int i = 0; i < 5; i++) {
					defXPos[i] = defXPos[i] - 10;
					currXPos[i] = perspective(defXPos[i], defZPos[i]);
				}
			}
		}

		/**
		 * Increases the size of the polygon by moving the points away from a
		 * center point
		 */
		public void scaleUp() {
			Boolean actionBool = false;
			for (int i = 0; i < 5; i++) {
				if (!(currXPos[i] >= 550 || currYPos[i] >= 550
						|| currYPos[i] <= -550 || currXPos[i] <= -550)) {
					actionBool = true;
				} else {
					actionBool = false;
					break;
				}
			}

			if (actionBool == true) {
				for (int i = 0; i < 5; i++) {
					defXPos[i] = (int) (defXPos[i] * 1.05);
					defYPos[i] = (int) (defYPos[i] * 1.05);
					currXPos[i] = perspective(defXPos[i], defZPos[i]);
					currYPos[i] = perspective(defYPos[i], defZPos[i]);
				}
			}
		}

		/**
		 * Decreases the size of the polygon by moving the points towards a
		 * center point
		 */
		public void scaleDwn() {
			for (int i = 0; i < 5; i++) {
				defXPos[i] = (int) (defXPos[i] * .95);
				defYPos[i] = (int) (defYPos[i] * .95);
				currXPos[i] = perspective(defXPos[i], defZPos[i]);
				currYPos[i] = perspective(defYPos[i], defZPos[i]);
			}
		}

		/**
		 * Translates the polgyon by moving in the positive Z direction
		 */
		public void moveForward() {
			for (int i = 0; i < 5; i++) {
				defZPos[i] = defZPos[i] - 100;
				currXPos[i] = perspective(defXPos[i], defZPos[i]);
			}
		}

		/**
		 * Translates the polygon by moving in the negative direction
		 */
		public void moveBackward() {
			for (int i = 0; i < 5; i++) {
				defZPos[i] = defZPos[i] + 100;
				currXPos[i] = perspective(defXPos[i], defZPos[i]);
			}
		}

		/**
		 * Rotate clockwise along the Z axis
		 */
		public void rotateZRight() {
			double theta = -25.0;

			for (int i = 0; i < 5; i++) {
				defXPos[i] = (int) (defXPos[i] * Math.cos(theta) - defYPos[i]
						* Math.sin(theta));
				defYPos[i] = (int) (defXPos[i] * Math.sin(theta) + defYPos[i]
						* Math.cos(theta));
				currXPos[i] = perspective(defXPos[i], defZPos[i]);
				currYPos[i] = perspective(defYPos[i], defZPos[i]);
			}

		}

		/**
		 * Rotate counter-clockwise along the Z axis
		 */
		public void rotateZLeft() {

			double theta = 25.0;

			for (int i = 0; i < 5; i++) {
				defXPos[i] = (int) (defXPos[i] * Math.cos(theta) - defYPos[i]
						* Math.sin(theta));
				defYPos[i] = (int) (defXPos[i] * Math.sin(theta) + defYPos[i]
						* Math.cos(theta));
				currXPos[i] = perspective(defXPos[i], defZPos[i]);
				currYPos[i] = perspective(defYPos[i], defZPos[i]);
			}

		}

		/**
		 * Rotate right along the Y axis
		 */
		public void rotateYRight() {

			double theta = 25.0;

			for (int i = 0; i < 5; i++) {
				defXPos[i] = (int) (defXPos[i] * Math.cos(theta) - defZPos[i]
						* Math.sin(theta));
				defZPos[i] = (int) (defXPos[i] * Math.sin(theta) + defZPos[i]
						* Math.cos(theta));
				currXPos[i] = perspective(defXPos[i], defZPos[i]);
				currYPos[i] = perspective(defYPos[i], defZPos[i]);
			}

		}

		/**
		 * Rotate left along the Y axis
		 */
		public void rotateYLeft() {

			double theta = -25.0;

			for (int i = 0; i < 5; i++) {
				defXPos[i] = (int) (defXPos[i] * Math.cos(theta) - defZPos[i]
						* Math.sin(theta));
				defZPos[i] = (int) (defXPos[i] * Math.sin(theta) + defZPos[i]
						* Math.cos(theta));
				currXPos[i] = perspective(defXPos[i], defZPos[i]);
				currYPos[i] = perspective(defYPos[i], defZPos[i]);
			}

		}

		/**
		 * Rotate clockwise along the X axis
		 */
		public void rotateXUp() {

			double theta = 25.0;

			for (int i = 0; i < 5; i++) {
				defYPos[i] = (int) (defYPos[i] * Math.cos(theta) - defZPos[i]
						* Math.sin(theta));
				defZPos[i] = (int) (defYPos[i] * Math.sin(theta) + defZPos[i]
						* Math.cos(theta));
				currXPos[i] = perspective(defXPos[i], defZPos[i]);
				currYPos[i] = perspective(defYPos[i], defZPos[i]);
			}

		}

		/**
		 * Rotate counter-clockwise along the X axis
		 */
		public void rotateXDown() {

			double theta = -25.0;

			for (int i = 0; i < 5; i++) {
				defYPos[i] = (int) (defYPos[i] * Math.cos(theta) - defZPos[i]
						* Math.sin(theta));
				defZPos[i] = (int) (defYPos[i] * Math.sin(theta) + defZPos[i]
						* Math.cos(theta));
				currXPos[i] = perspective(defXPos[i], defZPos[i]);
				currYPos[i] = perspective(defYPos[i], defZPos[i]);
			}

		}

		/**
		 * Overrides the paintComponent function to draw all of the screen
		 * 
		 * @param Graphics
		 *            g
		 */
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			if (drawTrangle) {
				drawTrangle(g);
			}
		}
	}

	/**
	 * Constructor for the graphics engine, sets the default size
	 */
	public GraphicsProgram() {

		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBackground(Color.WHITE);
		setTitle("Please Work");
		Trangle mangle = new Trangle();
		polyList.add(mangle);
		Trangle one = new Trangle();
		Trangle two = new Trangle();
		Trangle three = new Trangle();
		Trangle four = new Trangle();
		polyList.add(one);
		polyList.add(two);
		polyList.add(three);
		polyList.add(four);

	}

	/**
	 * Draws the X and Y line on the screen
	 */
	private void drawCoordinates() {

		Graphics2D g2d = (Graphics2D) getGraphics();

		g2d.drawLine(this.HEIGHT / 2, 0, this.HEIGHT / 2, this.WIDTH);
		g2d.drawLine(0, this.WIDTH / 2, this.HEIGHT, this.WIDTH / 2);
	}

	public void keyListener() {
		this.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_0:
					currIndex = 0;
					break;
				case KeyEvent.VK_1:
					currIndex = 1;
					break;
				case KeyEvent.VK_2:
					currIndex = 2;
					break;
				case KeyEvent.VK_3:
					currIndex = 3;
					break;
				case KeyEvent.VK_4:
					currIndex = 4;
					break;
				}
			}
		});
		
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				getById(currIndex).setFocus();
				switch (e.getKeyCode()) {
				case KeyEvent.VK_U: // press "u" key on the keyboard to move
									// pyramid up
					getById(currIndex).moveUp();
					getById(currIndex).paintComponent(getGraphics());
					break;
				case KeyEvent.VK_D: // press "d" key on the keyboard to move
									// pyramid down
					getById(currIndex).moveDwn();
					getById(currIndex).paintComponent(getGraphics());
					break;
				case KeyEvent.VK_R: // press "r" key on the keyboard to move
									// pyramid right
					getById(currIndex).moveRght();
					getById(currIndex).paintComponent(getGraphics());
					break;
				case KeyEvent.VK_L: // press "l" key on keyboard to move
									// pyramid
									// left
					getById(currIndex).moveLft();
					getById(currIndex).paintComponent(getGraphics());
					break;
				case KeyEvent.VK_UP:
					if (e.isShiftDown()) {// press shift + up arrow to scale
											// the
						// pyramid up in size
						getById(currIndex).scaleUp();
						getById(currIndex).paintComponent(getGraphics());
					} else {
						getById(currIndex).rotateXUp(); // rotate UP along x
														// axis
						getById(currIndex).paintComponent(getGraphics());
					}
					break;
				case KeyEvent.VK_DOWN:
					if (e.isShiftDown()) {// press shift + down arrow to
											// scale
											// the
						// pyramid down in size
						getById(currIndex).scaleDwn();
						getById(currIndex).paintComponent(getGraphics());
					} else {
						getById(currIndex).rotateXDown();// rotate DOWN
															// along x axis
						getById(currIndex).paintComponent(getGraphics());
					}
					break;
				case KeyEvent.VK_RIGHT: // press right arrow key to rotate
										// right
										// around the y axis
					getById(currIndex).rotateYRight();
					getById(currIndex).paintComponent(getGraphics());
					break;
				case KeyEvent.VK_LEFT: // press right arrow key to rotate
										// right
										// around the y axis
					getById(currIndex).rotateYLeft();
					getById(currIndex).paintComponent(getGraphics());
					break;
				case KeyEvent.VK_PERIOD: // press the ">" key to rotate the
											// pyramid right along the z
											// axis
					getById(currIndex).rotateZRight();
					getById(currIndex).paintComponent(getGraphics());
					break;
				case KeyEvent.VK_COMMA: // press the "<" key to rortate the
										// pyramid left along the z axis
					getById(currIndex).rotateZLeft();
					getById(currIndex).paintComponent(getGraphics());
					break;

				case KeyEvent.VK_F:
					getById(currIndex).moveForward();
					getById(currIndex).paintComponent(getGraphics());
					break;
				case KeyEvent.VK_B:
					getById(currIndex).moveBackward();
					getById(currIndex).paintComponent(getGraphics());
					break;
				case KeyEvent.VK_ENTER: // press enter to return to the
										// default
										// position
					getById(currIndex).setDefault();
					getById(currIndex).paintComponent(getGraphics());
					break;
				}
				getById(currIndex).repaint();
			}
		});
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				GraphicsProgram lines = new GraphicsProgram();
				lines.keyListener();
				lines.setVisible(true);
				lines.drawCoordinates();
			}
		});
	}
}
