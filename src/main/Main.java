/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import gui.GUI;
import logic.Logic;

/**
 *
 * @author Alex
 */
public class Main {
	public static void main(String[] args) {
		Logic logic = new Logic();
		GUI gui = new GUI(logic);
		gui.setVisible(true);
	}
}
