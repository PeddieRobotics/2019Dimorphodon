/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

import java.sql.Driver;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Subsystem;


/**
 * Add your docs here.
 * Here we connect to the Data that the lime light outputs in limeLightTable
 * tx access the x offset from the crosshair 
 */
public class LimeLight extends Subsystem {

  NetworkTable limeLightTable = NetworkTableInstance.getDefault().getTable("limelight");
  NetworkTableEntry tx = limeLightTable.getEntry("tx");

  //don't know if we need this 
  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
  /**
  *Create a variable that stores our dx(x displacement) called x
  * Report the x to the driverstation 
  */
  public void update(){
    double x = tx.getDouble(0.0);
    DriverStation.reportError("X displacement " + x, true);
    
  }
}
