package game;

import javax.swing.JFrame;

public class SnakeMain extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SnakeMain() {
		SnakeWindow windows = new SnakeWindow();
		add(windows);
		setTitle("Ã∞≥‘…ﬂ-SNAKE  by oy_xw");
		setSize(435, 390);
		setLocation(200, 200);
		setVisible(true);
	}

	public static void main(String[] args) {
		new SnakeMain();
	}

}
