package gui;

import javax.swing.JFrame;

import logic.Logic;

public class GUI extends JFrame {
	private static final long serialVersionUID = 1L;

	public GUI(Logic logic) {
		this.add(new Board(logic));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(420,440);
		this.setVisible(true);
	}	
}
