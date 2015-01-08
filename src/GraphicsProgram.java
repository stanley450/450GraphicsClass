import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GraphicsProgram extends JFrame {

	class Trangle extends JPanel {

		private int[] defXPos = new int[5];
		private int[] defYPos = new int[5];
		private int[] defZPos = new int[5];

		private int[] currXPos = new int[5];
		private int[] currYPos = new int[5];

		private int eye = 1000;

		private Boolean drawTrangle = true;

		public int perspective(int point, int zdis) {
			return (eye * point) / (eye + zdis) + 50;
		}

		public Trangle() {
			setBackground(Color.WHITE);
			setDefault();
			setFocusable(true);
		}

		public void setDefault() {
			// 250,100 the top vertex
			defXPos[0] = 0;
			defYPos[0] = -150;
			defZPos[0] = 350;
			// 100,400 outside bottom left
			defXPos[1] = -150;
			defYPos[1] = 150;
			defZPos[1] = 100;
			// 400,400 outside bottom right
			defXPos[2] = 150;
			defYPos[2] = 150;
			defZPos[2] = 100;
			// 175,350 inside bottom left
			defXPos[3] = -150;
			defYPos[3] = 150;
			defZPos[3] = 600;
			// 325,350 inside bottom right
			defXPos[4] = 150;
			defYPos[4] = 150;
			defZPos[4] = 600;

			System.out.println(perspective(defXPos[0], defZPos[0]));

			for (int i = 0; i < 5; i++) {
				currXPos[i] = perspective(defXPos[i], defZPos[i]);
				currYPos[i] = perspective(defYPos[i], defZPos[i]);
			}

		}

		private void drawTrangle(Graphics g) {

			drawTrangle = true;

			Graphics2D g2d = (Graphics2D) g;
			
			int off = 450;

			g2d.setColor(Color.BLACK);
			g2d.drawLine(currXPos[0] + off, currYPos[0] + off,
					currXPos[1] + off, currYPos[1] + off);
			// top to outside bottom left
			g2d.drawLine(currXPos[0] + off, currYPos[0] + off,
					currXPos[2] + off, currYPos[2] + off);
			// top to outside bottom right
			g2d.drawLine(currXPos[0] + off, currYPos[0] + off,
					currXPos[3] + off, currYPos[3] + off);
			// top to inside bottom left
			g2d.drawLine(currXPos[4] + off, currYPos[4] + off,
					currXPos[0] + off, currYPos[0] + off);
			// inside bottom right to top
			g2d.setColor(Color.GREEN); // front side
			g2d.drawLine(currXPos[1] + off, currYPos[1] + off,
					currXPos[2] + off, currYPos[2] + off);
			// outside bottom left to outside bottom right
			g2d.setColor(Color.RED); // back side
			g2d.drawLine(currXPos[3] + off, currYPos[3] + off,
					currXPos[4] + off, currYPos[4] + off);
			// inside bottom left to inside bottom right
			g2d.setColor(Color.YELLOW); // left side
			g2d.drawLine(currXPos[1] + off, currYPos[1] + off,
					currXPos[3] + off, currYPos[3] + off);
			// outside bottom left to inside bottom left
			g2d.setColor(Color.BLUE); // right side
			g2d.drawLine(currXPos[4] + off, currYPos[4] + off,
					currXPos[2] + off, currYPos[2] + off);
			// inside bottom right to outside bottom right
			g2d.setColor(Color.BLACK);

			for (int i = 0; i < 5; i++) {
				System.out.println("p" + i + " = " + "(" + currXPos[i] + ","
						+ currYPos[i] + ")");
			}

		}

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

		public void scaleDwn() {
			for (int i = 0; i < 5; i++) {
				defXPos[i] = (int) (defXPos[i] * .95);
				defYPos[i] = (int) (defYPos[i] * .95);
				currXPos[i] = perspective(defXPos[i], defZPos[i]);
				currYPos[i] = perspective(defYPos[i], defZPos[i]);
			}
		}
		
		public void moveForward() {
			for(int i = 0; i < 5; i++){
				defZPos[i] = defZPos[i] - 100;
				currXPos[i] = perspective(defXPos[i], defZPos[i]);
			}
		}
		
		public void moveBackward() {
			for(int i = 0; i < 5; i++){
				defZPos[i] = defZPos[i] + 100;
				currXPos[i] = perspective(defXPos[i], defZPos[i]);
			}
		}

		public void rotateZRight() { // clockwise
			double theta = -25.0;

			for (int i = 0; i < 5; i++) {
				defXPos[i] = (int) (defXPos[i] * Math.cos(theta) - defYPos[i] * Math.sin(theta));
				defYPos[i] = (int) (defXPos[i] * Math.sin(theta) + defYPos[i] * Math.cos(theta));
				currXPos[i] = perspective(defXPos[i], defZPos[i]);
				currYPos[i] = perspective(defYPos[i], defZPos[i]);
			}

		}

		public void rotateZLeft() { // counter clockwise

			double theta = 25.0;

			for (int i = 0; i < 5; i++) {
				defXPos[i] = (int) (defXPos[i] * Math.cos(theta) - defYPos[i] * Math.sin(theta));
				defYPos[i] = (int) (defXPos[i] * Math.sin(theta) + defYPos[i] * Math.cos(theta));
				currXPos[i] = perspective(defXPos[i], defZPos[i]);
				currYPos[i] = perspective(defYPos[i], defZPos[i]);
			}

		}

		public void rotateYRight() {

			double theta = 25.0;
			
			for (int i = 0; i < 5; i++) {
				defXPos[i] = (int) (defXPos[i] * Math.cos(theta) - defZPos[i] * Math.sin(theta));
				defZPos[i] = (int) (defXPos[i] * Math.sin(theta) + defZPos[i] * Math.cos(theta));
				currXPos[i] = perspective(defXPos[i], defZPos[i]);
				currYPos[i] = perspective(defYPos[i], defZPos[i]);
			}

		}

		public void rotateYLeft() {

			double theta = -25.0;

			for (int i = 0; i < 5; i++) {
				defXPos[i] = (int) (defXPos[i] * Math.cos(theta) - defZPos[i] * Math.sin(theta));
				defZPos[i] = (int) (defXPos[i] * Math.sin(theta) + defZPos[i] * Math.cos(theta));
				currXPos[i] = perspective(defXPos[i], defZPos[i]);
				currYPos[i] = perspective(defYPos[i], defZPos[i]);
			}

		}

		public void rotateXUp() {
			
			double theta = 25.0;

			for (int i = 0; i < 5; i++) {
				defYPos[i] = (int) (defYPos[i] * Math.cos(theta) - defZPos[i] * Math.sin(theta));
				defZPos[i] = (int) (defYPos[i] * Math.sin(theta) + defZPos[i] * Math.cos(theta));
				currXPos[i] = perspective(defXPos[i], defZPos[i]);
				currYPos[i] = perspective(defYPos[i], defZPos[i]);
			}

		}

		public void rotateXDown() {
			
			double theta = -25.0;

			for (int i = 0; i < 5; i++) {
				defYPos[i] = (int) (defYPos[i] * Math.cos(theta) - defZPos[i] * Math.sin(theta));
				defZPos[i] = (int) (defYPos[i] * Math.sin(theta) + defZPos[i] * Math.cos(theta));
				currXPos[i] = perspective(defXPos[i], defZPos[i]);
				currYPos[i] = perspective(defYPos[i], defZPos[i]);
			}

		}


		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			
			if (drawTrangle) {
				drawTrangle(g);
			}
		}
	}

	Trangle mangle = new Trangle();

	public GraphicsProgram() {

		setPreferredSize(new Dimension(1000, 1000));
		setMinimumSize(new Dimension(1000, 1000));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Please Work");
		add(mangle);
		
	}
	
	private void drawCoordinates() {

		Graphics2D g2d = (Graphics2D) getGraphics();

		g2d.drawLine(500, 0, 500, 1000);
		g2d.drawLine(0, 500, 1000, 500);
	}

	public void keyListener() {
		drawCoordinates();
		mangle.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_U: // press "u" key on the keyboard to move
									// pyramid up
					mangle.moveUp();
					mangle.paintComponent(getGraphics());
					break;
				case KeyEvent.VK_D: // press "d" key on the keyboard to move
									// pyramid down
					mangle.moveDwn();
					mangle.paintComponent(getGraphics());
					break;
				case KeyEvent.VK_R: // press "r" key on the keyboard to move
									// pyramid right
					mangle.moveRght();
					mangle.paintComponent(getGraphics());
					break;
				case KeyEvent.VK_L: // press "l" key on keyboard to move pyramid
									// left
					mangle.moveLft();
					mangle.paintComponent(getGraphics());
					break;
				case KeyEvent.VK_UP:
					if (e.isShiftDown()) {// press shift + up arrow to scale the
						// pyramid up in size
						mangle.scaleUp();
						mangle.paintComponent(getGraphics());
					} else {
						mangle.rotateXUp(); //rotate UP along x axis
						mangle.paintComponent(getGraphics());
					}
					break;
				case KeyEvent.VK_DOWN:
					if (e.isShiftDown()) {// press shift + down arrow to scale the
						// pyramid down in size
						mangle.scaleDwn();
						mangle.paintComponent(getGraphics());
					} else {
						mangle.rotateXDown();//rotate DOWN along x axis
						mangle.paintComponent(getGraphics());
					}
					break;
				case KeyEvent.VK_RIGHT: // press right arrow key to rotate right
										// around the y axis
					mangle.rotateYRight();
					mangle.paintComponent(getGraphics());
					break;
				case KeyEvent.VK_LEFT: // press right arrow key to rotate right
										// around the y axis
					mangle.rotateYLeft();
					mangle.paintComponent(getGraphics());
					break;
				case KeyEvent.VK_PERIOD: // press the ">" key to rotate the
											// pyramid right along the z axis
					mangle.rotateZRight();
					mangle.paintComponent(getGraphics());
					break;
				case KeyEvent.VK_COMMA: // press the "<" key to rortate the
										// pyramid left along the z axis
					mangle.rotateZLeft();
					mangle.paintComponent(getGraphics());
					break;
					
				case KeyEvent.VK_F:
					mangle.moveForward();
					mangle.paintComponent(getGraphics());
					break;
				case KeyEvent.VK_B:
					mangle.moveBackward();
					mangle.paintComponent(getGraphics());
					break;
				case KeyEvent.VK_ENTER: // press enter to return to the default
										// position
					mangle.setDefault();
					mangle.paintComponent(getGraphics());
					break;
				}
				mangle.repaint();
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
			}
		});
	}
}
