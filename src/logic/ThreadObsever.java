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
public class ThreadObsever extends Thread{
	
	private int threadCount;
	private Logic logic;

	private Coordinate coordinate;
	private boolean isDone;
	private int totalItterations = 0;
	
	public ThreadObsever(Logic logic, int threadCount){
		this.logic = logic;
		this.threadCount = threadCount;
	}
	
	@Override
	public void run() {
		super.run();
		this.runMinimax();
	}
	
	public Coordinate runMinimax(){
		this.isDone = false;
		
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
		
		int finishedRunners = 0;
		int runnersSize = runners.size();
		while(finishedRunners < runnersSize) {
			this.totalItterations = 0;
			finishedRunners = 0;
			for (MiniMaxRunner runner : runners){
				this.totalItterations += runner.getItteration();
				if(runner.isDone()) {
					finishedRunners++;
				}
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// make sure every thread is done
		for(Thread t : threads) {
			try {
				t.join();
			} catch (InterruptedException ex) {
				Logger.getLogger(ThreadObsever.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		this.isDone = true;
		
		int bestUtility = -9999999;
		coordinate = null;
		for (MiniMaxRunner runner : runners){
			if (runner.getFinalUtility() > bestUtility){
				bestUtility = runner.getFinalUtility();
				coordinate = runner.getFinalCoordinate();
			}
		}
		return coordinate;
	}
	
	public boolean isDone() {
		return isDone;
	}
	
	public Coordinate getCoordinate() {
		return coordinate;
	}
	
	public int getTotalItterations() {
		return totalItterations;
	}
}
