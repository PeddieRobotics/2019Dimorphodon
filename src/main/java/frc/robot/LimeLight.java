package frc.robot;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

import java.sql.Driver;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Subsystem;


public class LimeLight extends Subsystem {

  NetworkTable limeLightTable = NetworkTableInstance.getDefault().getTable("limelight");
  NetworkTableEntry tx = limeLightTable.getEntry("tx");


  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
  public double getAngle(){
    double angleToTarget = tx.getDouble(0.0);
    DriverStation.reportError("Angle to target is: " + angleToTarget, true);
    return(angleToTarget); 
  }
}
