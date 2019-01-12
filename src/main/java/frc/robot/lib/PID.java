package org.usfirst.frc.team5895.robot.lib;

import edu.wpi.first.wpilibj.Timer;

public class PID{
	
	private final boolean RESET_ZERO_CROSS;
	private final boolean RESET_I;

	private final double Kp;
	private final double Ki;
	private final double Kd;
	private final double dV;
	
	private double setpoint;
	private double capI;
	private double lastTime = 0;
	private double errorSum = 0;
	private double lastError = 0;
	private double lastSetpoint = 0;
	private double lastOutput = 0;
	
	/**
	 * Initializes a new PID controller
	 * 
	 * @param Kp The proportional gain
	 * @param Ki The integral gain
	 * @param Kd The derivative gain
	 * @param dV The maximum amount the voltage can change per ms
	 * @param reset_zero_cross If true, resets the integral term whenever the error crosses zero
	 * @param capI The limit on how high the integral term can grow to
	 * @param resetI To reset I on setpoint change or not
	 */
	public PID(double Kp, double Ki, double Kd, double dV, boolean reset_zero_cross, double capI, boolean resetI) {
		this.Kp = Kp;
		this.Ki = Ki;
		this.Kd = Kd;
		this.dV = dV;
		this.capI = capI;
		RESET_ZERO_CROSS = reset_zero_cross;
		setpoint = 0;
		lastTime = 0;
		errorSum = 0;
		lastError = 0;
		lastSetpoint = 0;
		lastOutput = 0;
		RESET_I = resetI;
	}
	
	/**
	 * Initializes a new PID controller
	 * 
	 * @param Kp The proportional gain
	 * @param Ki The integral gain
	 * @param Kd The derivative gain
	 * @param resetI To reset I on setpoint change or not
	 */
	public PID(double Kp, double Ki, double Kd, boolean resetI) {
		this(Kp, Ki, Kd, 1, true, Double.MAX_VALUE, true);
	}
	
	/**
	 * Initializes a new PID controller
	 * 
	 * @param Kp The proportional gain
	 * @param Ki The integral gain
	 * @param Kd The derivative gain
	 * @param dV The maximum amount the voltage can change per ms
	 */
	public PID(double Kp, double Ki, double Kd, double dV) {
		this(Kp, Ki, Kd, dV, true, Double.MAX_VALUE, true);
	}
	
	/**
	 * Initializes a new PID controller
	 * 
	 * @param Kp The proportional gain
	 * @param Ki The integral gain
	 * @param Kd The derivative gain
	 * @param dV The maximum amount the voltage can change per ms
	 * @param capI The limit on how high the integral term can grow to
	 */
	public PID(double Kp, double Ki, double Kd, double dV, double capI) {
		this(Kp, Ki, Kd, dV, true, capI, true);
	}
	
	/**
	 * Initializes a new PID controller
	 * 
	 * @param Kp The proportional gain
	 * @param Ki The integral gain
	 * @param Kd The derivative gain
	 * @param dV The maximum amount the voltage can change per ms
	 * @param reset_zero_cross If true, resets the integral term whenever the error crosses zero
	 */
	public PID(double Kp, double Ki, double Kd, double dV, boolean reset_zero_cross) {
		this(Kp, Ki, Kd, dV, reset_zero_cross, Double.MAX_VALUE, true);
	}
	
	/**
	 * Resets the integral term
	 */
	public void resetIntegral() {
		errorSum = 0;
	}

	/**
	 * Changes the target point to be setpoint
	 * 
	 * @param setpoint Where the mechanism controlled should go to
	 */
	public void set(double setpoint) {
		this.setpoint = setpoint;
	}
	
	/**
	 * Returns the target position
	 * 
	 * @return The setpoint
	 */
	public double getSetpoint() {
		return setpoint;
	}

	/**
	 * Returns the output for the mechanism (should be called periodically)
	 * 
	 * @param proccessVar The current location of the mechanism
	 * @return The output to the motor controlling the mechanism
	 */
	public double getOutput(double proccessVar) {

		double error = setpoint - proccessVar;
		
		//did the setpoint change?
		if (RESET_I && (setpoint != lastSetpoint)) {
			errorSum = 0;
			lastError = error;
			lastTime = Timer.getFPGATimestamp() * 1000;
		}
		else if (RESET_ZERO_CROSS && (lastError * error < 0)) {
			errorSum = 0;  //reset if lastError and error have different signs
		}
		
		double time = Timer.getFPGATimestamp() * 1000;
		double dt = time - lastTime;
		
		errorSum += error * dt; //always integrate error
		
		if (Ki * errorSum > capI) {
			errorSum = capI/Ki;    //limit I term from being too high
		} else if (Ki * errorSum < -capI) {
			errorSum = -capI/Ki;    //limit I term from being too low
		}
		
		double dError = (error - lastError) / dt;  //should this be smoothed over
		                                           //multiple measurements?
		
		double output = Kp * error + Ki * errorSum + Kd * dError;
		
		//limit the amount of voltage the output can change per ms
		if (output - lastOutput > (dt * dV)) {
			output = lastOutput + (dt * dV);
		}
		if (output - lastOutput < -(dt * dV)) {
			output = lastOutput - (dt * dV);
		}
		
		//set variables for next run through loop
		lastTime = time;
		lastSetpoint = setpoint;
		lastError = error;
		lastOutput = output;
		
		return output;
	}

}
  public PID(double Kp, double Ki, double Kd, double dV) {
    this(Kp, Ki, Kd, dV, true, Double.MAX_VALUE, true);
  }
}
