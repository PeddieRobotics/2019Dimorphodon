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
 */
public class LimeLight extends Subsystem {

  NetworkTable limeLightTable = NetworkTableInstance.getDefault().getTable("limelight");
  NetworkTableEntry tx = limeLightTable.getEntry("tx");


  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
  public void update(){
    double x = tx.getDouble(0.0);
    DriverStation.reportError("X displacement " + x, true);
    
  }
}
