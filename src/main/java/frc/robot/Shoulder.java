package frc.robot;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Shoulder extends Subsystem {

  private CANSparkMax spark;
  private CANPIDController pidController;
  private CANEncoder encoder;
  private Solenoid brake;

  private enum Mode_Type {
    MOVING, BRAKING, DISENGAGING, DISABLED
  };
  private Mode_Type mode = Mode_Type.BRAKING;

  private boolean brakeOn;
  private double moveTime;

  private double kP = 0.02;
  private double kI = 0.0;
  private double kD = 0.0;
  private double kIz = 0.0;
  private double kFF = 0.0;
  private double kMaxOutput = 1.0;
  private double kMinOutput = -1.0;
  private double maxRPM = 20.0;
  private double maxVel = 2000.0;
  private double minVel = 0.0;
  private double maxAcc = 1500.0;
  private double allowedErr = 0.0;

  private double setPoint = 0;
  private double distancePerPulse = 0.715;

  public Shoulder() {
    spark = new CANSparkMax(ElectricalLayout.MOTOR_CARGO_SHOULDER, MotorType.kBrushless);
    spark.restoreFactoryDefaults();
    pidController = spark.getPIDController();
    encoder = spark.getEncoder();

    encoder.setPositionConversionFactor(1.0);
    encoder.setVelocityConversionFactor(1.0);
    encoder.setPosition(0);

    pidController.setP(kP);
    pidController.setI(kI);
    pidController.setD(kD);
    pidController.setIZone(kIz);
    pidController.setFF(kFF);
    pidController.setOutputRange(kMinOutput, kMaxOutput);

    spark.setIdleMode(IdleMode.kCoast);

    brake = new Solenoid(ElectricalLayout.SOLENOID_SHOULDER_BRAKE);

    // int smartMotionSlot = 0;
    // pidController.setSmartMotionMaxVelocity(maxVel, smartMotionSlot);
    // pidController.setSmartMotionMinOutputVelocity(minVel, smartMotionSlot);
    // pidController.setSmartMotionMaxAccel(maxAcc, smartMotionSlot);
    // pidController.setSmartMotionAllowedClosedLoopError(allowedErr,
    // smartMotionSlot);
  }

  public void setShoulder(double setpoint) {
    setPoint = setpoint * distancePerPulse;
    moveTime = Timer.getFPGATimestamp();
    mode = Mode_Type.DISENGAGING;
  }

  public boolean atTarget() {
    return (Math.abs((encoder.getPosition() - setPoint)) < 1.0) && (Math.abs(encoder.getVelocity()) < 1.0);
  }

  public void update() {
    switch (mode) {
      case MOVING:
      DriverStation.reportError("moving", false);
      brakeOn = false;
      pidController.setReference(setPoint, ControlType.kPosition);
      if(atTarget()) {
        mode = Mode_Type.BRAKING;
      }
      break;
      case DISENGAGING:
      DriverStation.reportError("disengaging", false);
      brakeOn = false;
      if(Timer.getFPGATimestamp() - moveTime > 0.1) {
        mode = Mode_Type.MOVING;
      }
      break;
      case BRAKING:
      DriverStation.reportError("braking", false);
      brakeOn = true;
      spark.set(0);
      break;
      case DISABLED:
      brakeOn = false;
      spark.set(0);
      break;
    }
    brake.set(!brakeOn);
//    DriverStation.reportError(" " + encoder.getPosition() / distancePerPulse, false);
//    DriverStation.reportError(" " + setPoint, false);
  }

  public void initDefaultCommand() {
  }
}