package frc.robot;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Shoulder extends Subsystem {

 private CANSparkMax spark;
 private CANPIDController pidController;
 private CANEncoder encoder;

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
   spark = new CANSparkMax(7, MotorType.kBrushless);
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

//    int smartMotionSlot = 0;
//    pidController.setSmartMotionMaxVelocity(maxVel, smartMotionSlot);
//    pidController.setSmartMotionMinOutputVelocity(minVel, smartMotionSlot);
//    pidController.setSmartMotionMaxAccel(maxAcc, smartMotionSlot);
//    pidController.setSmartMotionAllowedClosedLoopError(allowedErr, smartMotionSlot);
 }

 public void setShoulder(double setpoint){
   setPoint = setpoint*distancePerPulse;
 }

 public void update() {
     pidController.setReference(setPoint, ControlType.kPosition);

     DriverStation.reportError(" " + encoder.getPosition() / distancePerPulse, false);
     DriverStation.reportError(" " + setPoint, false);
 }

 public void initDefaultCommand() {
 }
}