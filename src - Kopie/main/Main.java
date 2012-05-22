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
		Config config;
		if(args.length != 0){
			config = new Config(args[0]);
		} else {
			config = new Config();
		}
		Logic logic = new Logic(config);
		GUI gui = new GUI(logic);
		gui.setVisible(true);
	}
}
