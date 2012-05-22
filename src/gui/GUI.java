package gui;

import javax.swing.JFrame;

import logic.Logic;

public class GUI extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private Logic logic;

	public GUI(Logic logic) {
		this.logic = logic;
		this.add(new Board());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(420,440);
		this.setVisible(true);
	}	
}
