package main;


public abstract class Config {

	public static final double TAU = 0.9;

	public static final double MAX_V = 3;

	private static double SIM_STEP_SIZE = 0.04;
	
	private static double  STOP_TIME = 100;
	
	public static void setSimStepSize(double simStepSize) {
		SIM_STEP_SIZE = simStepSize;
	}
	
	public static double getSimStepSize() {
		return SIM_STEP_SIZE;
	}
	
	public static void setStopTime(double time) {
		STOP_TIME = time;
	}
	
	public static double getStopTime() {
		return STOP_TIME;
	}
}
