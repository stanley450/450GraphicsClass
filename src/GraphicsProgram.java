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
		
		private int[] currXPos = new int[5];
		private int[] currYPos = new int[5];
		
		private int midXPnt;
		private int midYPnt;
		
		private int z = 50;//distance from eye to screen in z axis
		private int coordz = 250;
		private int dis = 100;//distance from screen to center point in z axis

		private double[] scaleArrayX = new double[5];
		private double[] scaleArrayY = new double[5];

		private Boolean drawTrangle = true;
		
		public int perspective(int a, int b){
			return z*(a - coordz)/(z + b) + a;
		}

		public Trangle() {
			setBackground(Color.WHITE);
			setDefault();

			// scale array top p1
			scaleArrayX[0] = 0;
			scaleArrayY[0] = -150;
			// scale array bottom left p2
			scaleArrayX[1] = -150;
			scaleArrayY[1] = 150;
			// scale array bottom right p3
			scaleArrayX[2] = 150;
			scaleArrayY[2] = 150;
			// scale array inner left p4
			scaleArrayX[3] = -75;
			scaleArrayY[3] = 100;
			// scale array inner right p5
			scaleArrayX[4] = 75;
			scaleArrayY[4] = 100;
		}

		public void setDefault() {
			// 250,100 the top vertex
			defXPos[0] = perspective(250, dis);
			defYPos[0] = perspective(100, dis);
			// 100,400 outside bottom left
			defXPos[1] = perspective(100, dis);
			defYPos[1] = perspective(400, dis);
			// 400,400 outside bottom right
			defXPos[2] = perspective(400, dis);
			defYPos[2] = perspective(400, dis);
			// 175,350 inside bottom left
			defXPos[3] = perspective(175, dis);
			defYPos[3] = perspective(350, dis);
			// 325,350 inside bottom right
			defXPos[4] = perspective(325, dis);
			defYPos[4] = perspective(350, dis);
			// 250,250 origin/midpoint
			midXPnt = perspective(250, dis);
			midYPnt = perspective(250, dis);

			currXPos = defXPos;
			currYPos = defYPos;

		}

		private void drawTrangle(Graphics g) {

			drawTrangle = true;

			Graphics2D g2d = (Graphics2D) g;

			g2d.setColor(Color.BLACK);
			g2d.drawLine(currXPos[0], currYPos[0], currXPos[1], currYPos[1]);// top
																				// to
																				// outside
																				// bottom
																				// left
			g2d.drawLine(currXPos[0], currYPos[0], currXPos[2], currYPos[2]);// top
																				// to
																				// outside
																				// bottom
																				// right
			g2d.drawLine(currXPos[0], currYPos[0], currXPos[3], currYPos[3]);// top
																				// to
																				// inside
																				// bottom
																				// left
			g2d.drawLine(currXPos[4], currYPos[4], currXPos[0], currYPos[0]);// inside
																				// bottom
																				// right
																				// to
																				// top
			g2d.setColor(Color.GREEN); // front side
			g2d.drawLine(currXPos[1], currYPos[1], currXPos[2], currYPos[2]);// outside
																				// bottom
																				// left
																				// to
																				// ouside
																				// bottom
																				// right
			g2d.setColor(Color.RED); // back side
			g2d.drawLine(currXPos[3], currYPos[3], currXPos[4], currYPos[4]);// inside
																				// bottom
																				// left
																				// to
																				// inside
																				// bottom
																				// right
			g2d.setColor(Color.YELLOW); // left side
			g2d.drawLine(currXPos[1], currYPos[1], currXPos[3], currYPos[3]);// outside
																				// bottom
																				// left
																				// to
																				// inside
																				// bottom
																				// left
			g2d.setColor(Color.BLUE); // right side
			g2d.drawLine(currXPos[4], currYPos[4], currXPos[2], currYPos[2]);// inside
																				// bottom
																				// right
																				// to
																				// outside
																				// bottom
																				// right
			g2d.setColor(Color.BLACK);

			for (int i = 0; i < 5; i++) {
				System.out.println("p" + i + " = " + "(" + currXPos[i] + ","
						+ currYPos[i] + ")");
			}
			System.out.println("object center = (" + midXPnt + "," + midYPnt
					+ ")");

		}

		public void moveUp() {
			Boolean actionBool = false;
			for (int i = 0; i < 5; i++) {
				if (!(currYPos[i] <= 0)) {
					actionBool = true;
				} else {
					actionBool = false;
					break;
				}
			}

			if (actionBool == true) {
				for (int i = 0; i < 5; i++) {
					currYPos[i] = currYPos[i] - 10;
				}
				midYPnt = midYPnt - 10;
			}
		}

		public void moveDwn() {
			Boolean actionBool = false;
			for (int i = 0; i < 5; i++) {
				if (!(currYPos[i] >= 500)) {
					actionBool = true;
				} else {
					actionBool = false;
					break;
				}
			}

			if (actionBool == true) {
				for (int i = 0; i < 5; i++) {
					currYPos[i] = currYPos[i] + 10;
				}
				midYPnt = midYPnt + 10;
			}
		}

		public void moveRght() {
			Boolean actionBool = false;
			for (int i = 0; i < 5; i++) {
				if (!(currXPos[i] >= 500)) {
					actionBool = true;
				} else {
					actionBool = false;
					break;
				}
			}

			if (actionBool == true) {
				for (int i = 0; i < 5; i++) {
					currXPos[i] = currXPos[i] + 10;
				}
				midXPnt = midXPnt + 10;
			}
		}

		public void moveLft() {
			Boolean actionBool = false;
			for (int i = 0; i < 5; i++) {
				if (!(currXPos[i] <= 0)) {
					actionBool = true;
				} else {
					actionBool = false;
					break;
				}
			}

			if (actionBool == true) {
				for (int i = 0; i < 5; i++) {
					currXPos[i] = currXPos[i] - 10;
				}
				midXPnt = midXPnt - 10;
			}
		}

		public void scaleUp() {
			double[] tempX = scaleArrayX;
			double[] tempY = scaleArrayY;

			Boolean actionBool = false;
			for (int i = 0; i < 5; i++) {
				if (!(currXPos[i] >= 500 || currYPos[i] >= 500
						|| currYPos[i] <= 0 || currXPos[i] <= 0)) {
					actionBool = true;
				} else {
					actionBool = false;
					break;
				}
			}

			if (actionBool == true) {
				for (int i = 0; i < 5; i++) {
					tempX[i] = (tempX[i] * (1.05));
					currXPos[i] = (int) tempX[i] + midXPnt;
					tempY[i] = (tempY[i] * (1.05));
					currYPos[i] = (int) tempY[i] + midYPnt;
				}
			}
		}

		public void scaleDwn() {
			double[] tempX = scaleArrayX;
			double[] tempY = scaleArrayY;

			for (int i = 0; i < 5; i++) {
				tempX[i] = (tempX[i] * (.95));
				currXPos[i] = (int) tempX[i] + midXPnt;
				tempY[i] = (tempY[i] * (.95));
				currYPos[i] = (int) tempY[i] + midYPnt;
			}
		}

		public void rotateZRight() { // clockwise
			double theta = -25.0;

			double[] tempX = scaleArrayX;
			double[] tempY = scaleArrayY;

			for (int i = 0; i < 5; i++) {
				tempX[i] = ((tempX[i] * Math.cos(theta)) - (tempY[i] * Math
						.sin(theta)));
				currXPos[i] = (int) tempX[i] + midXPnt;
				tempY[i] = ((tempX[i] * Math.sin(theta)) + (tempY[i] * Math
						.cos(theta)));
				currYPos[i] = (int) tempY[i] + midYPnt;
			}

		}

		public void rotateZLeft() { // counter clockwise

			double theta = 25.0;

			double[] tempX = scaleArrayX;
			double[] tempY = scaleArrayY;

			for (int i = 0; i < 5; i++) {
				tempX[i] = ((tempX[i] * Math.cos(theta)) - (tempY[i] * Math
						.sin(theta)));
				currXPos[i] = (int) tempX[i] + midXPnt;
				tempY[i] = ((tempX[i] * Math.sin(theta)) + (tempY[i] * Math
						.cos(theta)));
				currYPos[i] = (int) tempY[i] + midYPnt;
			}

		}

		public void rotateYUp() {

		}

		public void rotateYDown() {

		}
		
		public void rotateXRight(){
			
		}
		
		public void rotateXLeft(){
			
		}

		private void doStuff(Graphics g) {

			Graphics2D g2d = (Graphics2D) g;

			g2d.drawLine(250, 0, 250, 500);
			g2d.drawLine(0, 250, 500, 250);
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			doStuff(g);
			if (drawTrangle) {
				drawTrangle(g);
			}
		}
	}

	Trangle mangle = new Trangle();

	public GraphicsProgram() {

		setPreferredSize(new Dimension(500, 500));
		setMinimumSize(new Dimension(520, 540));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Please Work");

		add(mangle);

	}

	public void keyListener() {
		mangle.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_U:
					mangle.moveUp();
					mangle.paintComponent(getGraphics());
					break;
				case KeyEvent.VK_D:
					mangle.moveDwn();
					mangle.paintComponent(getGraphics());
					break;
				case KeyEvent.VK_R:
					mangle.moveRght();
					mangle.paintComponent(getGraphics());
					break;
				case KeyEvent.VK_L:
					mangle.moveLft();
					mangle.paintComponent(getGraphics());
					break;
				case KeyEvent.VK_UP:
					if (e.isShiftDown()) {
						mangle.scaleUp();
						mangle.paintComponent(getGraphics());
					}
					break;
				case KeyEvent.VK_DOWN:
					if (e.isShiftDown()) {
						mangle.scaleDwn();
						mangle.paintComponent(getGraphics());
					}
					break;
				case KeyEvent.VK_PERIOD:
					mangle.rotateZRight();
					mangle.paintComponent(getGraphics());
					break;
				case KeyEvent.VK_COMMA:
					mangle.rotateZLeft();
					mangle.paintComponent(getGraphics());
					break;
				case KeyEvent.VK_ENTER:
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
