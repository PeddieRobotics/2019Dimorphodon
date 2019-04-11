package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;


import edu.wpi.first.wpilibj.command.Subsystem;

public class ShoulderV2 extends Subsystem {
  private CANSparkMax spark;
  private CANPIDController pidController;
  private CANEncoder encoder;
  private Solenoid brake;

  private enum Mode_Type {
    MOVING, BRAKING, DISENGAGING, DISABLED, ENGAGING, NO_BRAKE_DISENGAGING, NO_BRAKE_MOVING
  };
  private Mode_Type mode = Mode_Type.BRAKING;

  private boolean brakeOn;
  private double moveTime;
  private double brakeTime;
  private boolean brakesActive;

  private double kP = 0.00005;
  private double kI = 0.000001;
  private double kD = 0.0;
  private double kIz = 0.0;
  private double kFF = 0.000156;
  private double kMaxOutput = 1.0;
  private double kMinOutput = -1.0;
  private double maxRPM = 5700.0;

  private double maxVel = 5500.0; //rpm
  private double minVel = 0.0; 
  private double maxAcc = 7500.0; //.....?????
  private double allowedErr = 0.0;

  private double setPoint;  
  private double processVar;
  private double distancePerPulse = 0.5787;

  public ShoulderV2() {
    spark = new CANSparkMax(ElectricalLayout.MOTOR_CARGO_SHOULDER, MotorType.kBrushless); 
    spark.setIdleMode(IdleMode.kCoast);
    spark.restoreFactoryDefaults();

    pidController = spark.getPIDController();
    encoder = spark.getEncoder();
    encoder.setPosition(0.0);

    pidController.setP(kP);
    pidController.setI(kI);
    pidController.setD(kD);
    pidController.setIZone(kIz);
    pidController.setFF(kFF);
    pidController.setOutputRange(kMinOutput, kMaxOutput);

    int smartMotionSlot = 0;
    pidController.setSmartMotionMaxVelocity(maxVel, smartMotionSlot);
    pidController.setSmartMotionMinOutputVelocity(minVel, smartMotionSlot);
    pidController.setSmartMotionMaxAccel(maxAcc, smartMotionSlot);
    pidController.setSmartMotionAllowedClosedLoopError(allowedErr, smartMotionSlot);

    brake = new Solenoid(ElectricalLayout.SOLENOID_SHOULDER_BRAKE);
    brakesActive = true;
  }

  public void setShoulder(double setpoint) {
    if ( brakesActive ) {
      setPoint = setpoint * distancePerPulse;
      moveTime = Timer.getFPGATimestamp();
      mode = Mode_Type.DISENGAGING;
    } else {
      setPoint = setpoint * distancePerPulse;
      moveTime = Timer.getFPGATimestamp();
      mode = Mode_Type.NO_BRAKE_DISENGAGING;
    }
  }
  public void setIdleBrakeMode(boolean idleMode) {
    if(idleMode) {
      spark.setIdleMode(IdleMode.kBrake);
    } else {
      spark.setIdleMode(IdleMode.kCoast);
    }
  }
  public void setBrakes( boolean brakes ) {
    brakesActive = brakes;
  }

  public boolean atTarget() {
    return (Math.abs((encoder.getPosition() - setPoint)) < 1.5) && (Math.abs(encoder.getVelocity()) < 25.0);
  }

  public double setPoint(){
    return this.setPoint();
  }

  public void update() {
    switch (mode) {

      case MOVING:
        //DriverStation.reportError("moving", false);
        brakeOn = false;
        pidController.setReference(setPoint, ControlType.kSmartMotion);
        processVar = encoder.getPosition();
        if(atTarget()) {
          DriverStation.reportError("moving",false);
          brakeTime = Timer.getFPGATimestamp();
          mode = Mode_Type.ENGAGING;
        }
      break;

      case NO_BRAKE_MOVING:
        //DriverStation.reportError("moving", false);
        brakeOn = false;
        pidController.setReference(setPoint, ControlType.kPosition);
      break;

      case ENGAGING:
        brakeOn = true;
        pidController.setReference(setPoint,ControlType.kPosition);
        if(Timer.getFPGATimestamp() - brakeTime > 0.1) {
          mode = Mode_Type.BRAKING;
        }
      break;

      case NO_BRAKE_DISENGAGING:
        //DriverStation.reportError("disengaging", false);
        brakeOn = false;
        if(Timer.getFPGATimestamp() - moveTime > 0.1) {
          mode = Mode_Type.NO_BRAKE_MOVING;
        }
      break;

      case DISENGAGING:
        //DriverStation.reportError("disengaging", false);
        brakeOn = false;
//        spark.set(0.0);
        if(encoder.getPosition() / distancePerPulse > 65 && encoder.getPosition() / distancePerPulse < 71) {
          pidController.setReference(65.0 * distancePerPulse, ControlType.kSmartMotion);
        }
        if(encoder.getPosition() / distancePerPulse > 30 && encoder.getPosition() / distancePerPulse < 40) {
          pidController.setReference(32.0 * distancePerPulse, ControlType.kSmartMotion);
        }
        if(Timer.getFPGATimestamp() - moveTime > 0.3) {
          mode = Mode_Type.MOVING;
        }
      break;

      case BRAKING:
       // DriverStation.reportError("braking", false);
        brakeOn = true;
        spark.set(0);
      break; 

      case DISABLED:
        brakeOn = false;
        spark.set(0);
      break;

    }
    brake.set(!brakeOn);
    SmartDashboard.putNumber("encoder values " ,encoder.getPosition() / distancePerPulse);
  }

  public void initDefaultCommand() {
    
  }
}
