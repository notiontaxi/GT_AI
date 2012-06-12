package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import logic.Logic;
import main.Config;

public class GUI extends JFrame {
	private static final long serialVersionUID = 1L;

	private Board board;
	private JPanel buttonPanel;

	private Config config;
	private Logic logic;

	private void initBoard() {
		this.board = new Board(config, logic);
		this.add(this.board, BorderLayout.CENTER);
	}

	private void initButtons() {
		this.buttonPanel = new JPanel();

		JButton startButton = new JButton("Perform Move");
		startButton.setActionCommand("move");
		
		JButton smallStartButton = new JButton("Perform Move Only First It");
		smallStartButton.setActionCommand("smallMove");
		
		// ... Add Listeners
		startButton.addActionListener(this.board);
		smallStartButton.addActionListener(this.board);

		// ... Layout inner panel with two buttons horizontally
		this.buttonPanel.setLayout(new FlowLayout());
		this.buttonPanel.add(startButton);
		this.buttonPanel.add(smallStartButton);

		this.add(this.buttonPanel, BorderLayout.NORTH);
	}

	public GUI(Config config, Logic logic) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
			// handle exception
		} catch (ClassNotFoundException e) {
			// handle exception
		} catch (InstantiationException e) {
			// handle exception
		} catch (IllegalAccessException e) {
			// handle exception
		}

		this.config = config;
		this.logic = logic;

		this.initBoard();
		this.initButtons();

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(410, 460);
		this.setVisible(true);
	}
}
