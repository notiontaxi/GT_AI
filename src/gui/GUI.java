package gui;

import javax.swing.JFrame;

public class Main extends JFrame {
	
	public Main() {
		this.add(new Board());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(420,440);
		this.setVisible(true);
	}

	
	public static void main(String[] args) {
		Main main = new Main();
		main.setVisible(true);
	}
	
}
