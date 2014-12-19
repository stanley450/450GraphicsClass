import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.*;
import javax.swing.JPanel;
import javax.swing.border.Border;

import java.awt.GridLayout;

public class GraphicsProgram extends JFrame {

	class Trangle extends JPanel {

		private int[] defXPos = new int[5];
		private int[] defYPos = new int[5];
		private int[] currXPos = new int[5];
		private int[] currYPos = new int[5];

		private Boolean drawTrangle = true;

		public Trangle() {
			setBackground(Color.WHITE);
			setDefault();
			setFocusable(true);

		}

		public void setDefault() {
			// 250,100 the top vertex
			defXPos[0] = 250;
			defYPos[0] = 100;
			// 100,400 outside bottom left
			defXPos[1] = 100;
			defYPos[1] = 400;
			// 400,400 outside bottom right
			defXPos[2] = 400;
			defYPos[2] = 400;
			// 175,350 inside bottom left
			defXPos[3] = 175;
			defYPos[3] = 350;
			// 325,350 inside bottom right
			defXPos[4] = 325;
			defYPos[4] = 350;

			currXPos = defXPos;
			currYPos = defYPos;
		}

		private void drawTrangle(Graphics g) {

			drawTrangle = true;

			Graphics2D g2d = (Graphics2D) g;

			g2d.setColor(Color.BLACK);
			g2d.drawLine(currXPos[0], currYPos[0], currXPos[1], currYPos[1]);// top to outside bottom left
			g2d.drawLine(currXPos[0], currYPos[0], currXPos[2], currYPos[2]);// top to outside bottom right
			g2d.drawLine(currXPos[0], currYPos[0], currXPos[3], currYPos[3]);// top to inside bottom left
			g2d.drawLine(currXPos[4], currYPos[4], currXPos[0], currYPos[0]);// inside bottom right to top
			g2d.setColor(Color.GREEN); // front side
			g2d.drawLine(currXPos[1], currYPos[1], currXPos[2], currYPos[2]);// outside bottom left to ouside bottom right
			g2d.setColor(Color.RED); // back side
			g2d.drawLine(currXPos[3], currYPos[3], currXPos[4], currYPos[4]);// inside bottom left to inside bottom right
			g2d.setColor(Color.YELLOW); // left side
			g2d.drawLine(currXPos[1], currYPos[1], currXPos[3], currYPos[3]);// outside bottom left to inside bottom left
			g2d.setColor(Color.BLUE); // right side
			g2d.drawLine(currXPos[4], currYPos[4], currXPos[2], currYPos[2]);// inside bottom right to outside bottom right
			g2d.setColor(Color.BLACK);

		}

		public void moveUp() {
			for (int i = 0; i < 5; i++) {
				currYPos[i] = currYPos[i] - 10;
			}
		}

		public void moveDwn() {
			for (int i = 0; i < 5; i++) {
				currYPos[i] = currYPos[i] + 10;
			}
		}

		public void moveRght() {
			for (int i = 0; i < 5; i++) {
				currXPos[i] = currXPos[i] + 10;
			}
		}

		public void moveLft() {
			for (int i = 0; i < 5; i++) {
				currXPos[i] = currXPos[i] - 10;
			}
		}

		public void scaleUp() {
			for (int i = 0; i < 5; i++) {
				currXPos[i] = (int) (currXPos[i] * 1.1);
				currYPos[i] = (int) (currYPos[i] * 1.1);
			}
		}

		public void scaleDwn() {
			for (int i = 0; i < 5; i++) {
				currXPos[i] = (int) (currXPos[i] * .9);
				currYPos[i] = (int) (currYPos[i] * .9);
			}
		}
		
		public void rotateZRight(){
			
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
		setMinimumSize(new Dimension(500, 500));
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
				case KeyEvent.VK_UP:
					mangle.moveUp();
					mangle.paintComponent(getGraphics());
					break;
				case KeyEvent.VK_DOWN:
					mangle.moveDwn();
					mangle.paintComponent(getGraphics());
					break;
				case KeyEvent.VK_RIGHT:
					mangle.moveRght();
					mangle.paintComponent(getGraphics());
					break;
				case KeyEvent.VK_LEFT:
					mangle.moveLft();
					mangle.paintComponent(getGraphics());
					break;
				case KeyEvent.VK_P:
					mangle.scaleUp();
					mangle.paintComponent(getGraphics());
					break;
				case KeyEvent.VK_L:
					mangle.scaleDwn();
					mangle.paintComponent(getGraphics());
					break;
				case KeyEvent.VK_R:
					mangle.setDefault();
					mangle.paintComponent(getGraphics());
					break;
				}
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
