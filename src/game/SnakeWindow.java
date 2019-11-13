package game;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SnakeWindow extends JPanel implements Runnable, KeyListener, ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int score = 0, speed = 0;
	boolean start = false;
	int rx = 0, ry = 0;
	int eat1 = 0, eat2 = 0;
	JDialog dialog = new JDialog();
	JLabel label = new JLabel("game over ! you score:" + score);
	JButton okButton = new JButton(".>_<.");
	Random random = new Random();
	JButton stopButton, newButton;
	List<snakeAct> list = new ArrayList<snakeAct>();
	int temp = 0;
	Thread newThread;

	public SnakeWindow() {
		newButton = new JButton("BEGIN");
		stopButton = new JButton("STOP");
		newButton.addActionListener(this);
		stopButton.addActionListener(this);
		this.addKeyListener(this);
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.add(newButton);
		this.add(stopButton);
		dialog.setLayout(new GridLayout(2, 1));
		dialog.add(label);
		dialog.add(okButton);
		dialog.setSize(200, 200);
		dialog.setLocation(200, 200);
		dialog.setVisible(false);
		okButton.addActionListener(this);
	}

	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		graphics.drawRect(10, 40, 400, 300);
		graphics.drawString("score: " + score, 150, 15);
		graphics.drawString("speed: " + speed, 150, 35);
		graphics.setColor(new Color(0, 255, 0));
		if (start) {
			graphics.fillRect(10 + rx * 10, 40 + ry * 10, 10, 10);
			for (int i = 0; i < list.size(); i++) {
				graphics.setColor(new Color(0, 0, 255));
				graphics.fillRect(10 + list.get(i).getX() * 10, 40 + list.get(i).getY() * 10, 10, 10);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == newButton) {
			newButton.setEnabled(false);
			start = true;
			rx = random.nextInt(40);
			ry = random.nextInt(30);
			snakeAct tempAct = new snakeAct();
			tempAct.setX(20);
			tempAct.setY(15);
			list.add(tempAct);
			this.requestFocus();
			newThread = new Thread(this);
			newThread.start();
			repaint();
		}
		if (event.getSource() == stopButton) {
			System.exit(0);
		}
		if (event.getSource() == okButton) {
			list.clear();
			start = false;
			newButton.setEnabled(true);
			dialog.setVisible(false);
			score = 0;
			speed = 0;
			repaint();
		}
	}

	private void eat() {
		if (rx == list.get(0).getX() && ry == list.get(0).getY()) {
			rx = random.nextInt(40);
			ry = random.nextInt(30);
			snakeAct tempAct = new snakeAct();
			tempAct.setX(list.get(list.size() - 1).getX());
			tempAct.setY(list.get(list.size() - 1).getY());
			list.add(tempAct);
			score = score + 100 * speed + 10;
			eat1++;
			if (eat1 - eat2 >= 4) {
				eat2 = eat1;
				speed++;
			}
		}
	}

	public void otherMove() {
		snakeAct tempAct = new snakeAct();
		for (int i = 0; i < list.size(); i++) {
			if (i == 1) {
				list.get(i).setX(list.get(0).getX());
				list.get(i).setY(list.get(0).getY());
			} else if (i > 1) {
				tempAct = list.get(i - 1);
				list.set(i - 1, list.get(i));
				list.set(i, tempAct);
			}
		}
	}

	@Override
	public void move(int x, int y) {
		if (minYes(x, y)) {
			otherMove();
			list.get(0).setX(list.get(0).getX() + x);
			list.get(0).setY(list.get(0).getY() + y);
			eat();
			repaint();
		} else {
			newThread = null;
			label.setText("game over you score:" + score);
			dialog.setVisible(true);
		}
	}

	public boolean minYes(int x, int y) {
		if (!maxYes(list.get(0).getX() + x, list.get(0).getY() + y)) {
			return false;
		}
		return true;
	}

	public boolean maxYes(int x, int y) {
		if (x < 0 || x >= 40 || y < 0 || y >= 30) {
			return false;
		}
		for (int i = 0; i < list.size(); i++) {
			if (i > 1 && list.get(0).getX() == list.get(i).getX() && list.get(0).getY() == list.get(i).getY()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void keyPressed(KeyEvent event) {
		if (start) {
			switch (event.getKeyCode()) {
			case KeyEvent.VK_UP:
				move(0, -1);
				temp = 1;
				break;
			case KeyEvent.VK_DOWN:
				move(0, 1);
				temp = 2;
				break;
			case KeyEvent.VK_LEFT:
				move(-1, 0);
				temp = 3;
				break;
			case KeyEvent.VK_RIGHT:
				move(1, 0);
				temp = 4;
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent event) {

	}

	@Override
	public void keyTyped(KeyEvent event) {

	}

	@Override
	public void run() {
		while (start) {
			switch (temp) {
			case 1:
				move(0, -1);
				break;
			case 2:
				move(0, 1);
				break;
			case 3:
				move(-1, 0);
				break;
			case 4:
				move(1, 0);
				break;
			default:
				break;
			}
			repaint();
			try {
				Thread.sleep(300 - 30 * speed);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
