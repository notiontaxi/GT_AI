/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alex
 */
public class ThreadObsever {
	
	private int threadCount;
	private Logic logic;
	
	public ThreadObsever(Logic logic, int threadCount){
		this.logic = logic;
		this.threadCount = threadCount;
	}
	
	public Coordinate runMinimax(){
		ArrayList<Coordinate> emptyFields = logic.getBoard().getEmptyFields();
		int fieldsPerThread = threadCount > 0 ? (int) (emptyFields.size() / threadCount) : 0;
		Vector<MiniMaxRunner> runners = new Vector();
		Vector<Thread> threads = new Vector();
		try {
			for (int i = 0; i < threadCount - 1 &&  i < emptyFields.size() - 1; ++i){
				MiniMaxRunner m = new MiniMaxRunner(logic, emptyFields.subList(i * fieldsPerThread, (i+1) * fieldsPerThread), i);
				Thread t = new Thread(m);
				runners.add(m);
				threads.add(t);
			}
			MiniMaxRunner m = new MiniMaxRunner(logic, emptyFields.subList(((threadCount - 1) * fieldsPerThread), (emptyFields.size())), threadCount - 1);
			runners.add(m);
			threads.add(new Thread(m));
		} catch (CloneNotSupportedException ex) {
			Logger.getLogger(ThreadObsever.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		for (Thread t : threads){
			t.start();
		}
			
		for (Thread t : threads){
			try {
				t.join();
			} catch (InterruptedException ex) {
				Logger.getLogger(ThreadObsever.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		
		int bestUtility = -9999999;
		Coordinate bestMove = null;
		for (MiniMaxRunner runner : runners){
			if (runner.getFinalUtility() > bestUtility){
				bestUtility = runner.getFinalUtility();
				bestMove = runner.getFinalCoordinate();
			}
		}
		return bestMove;
	}
}
