package frc.robot.lib;

import edu.wpi.first.wpilibj.command.PIDSubsystem;

public class PID {
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

  public PID(double Kp, double Ki, double Kd, double dV) {
    this(Kp, Ki, Kd, dV, true, Double.MAX_VALUE, true);
  }
}
