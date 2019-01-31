package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.*;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class LimeLight {

    NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
    NetworkTableEntry tx;
    NetworkTableEntry ty;
    NetworkTableEntry tv;
    NetworkTableEntry cy0;
    NetworkTableEntry cx0;
    final double mountAngle = 0.0;
    final double targetHeight = 28.75;
    final double mountHeight = 7.25;
    double yAngle = 0;
    double xAngle = 0;

    public LimeLight() {
        table = NetworkTableInstance.getDefault().getTable("limelight");
        tx = table.getEntry("tx");
        ty = table.getEntry("ty");
        tv = table.getEntry("tv");
        cy0 = table.getEntry("cy0");
        cx0 = table.getEntry("cx0");

    }

    /**
     * Make sure we are constantly updating our network table values
     */
    public void update() {
        double hasTarget = tv.getDouble(0.0);
        if (hasTarget == 1) {
            yAngle = ty.getDouble(0.0);
            xAngle = tx.getDouble(0.0);
            SmartDashboard.putNumber("xAngle", xAngle);
            SmartDashboard.putNumber("YDistance", calcYDist());
            SmartDashboard.putNumber("yAngle", yAngle);
            // SmartDashboard.putNumber("radYAngle", Math.toRadians(yAngle));
            SmartDashboard.putNumber("XDistance", calcXDist());
        }
    }

    /**
     * Assuming we know the mount height, mount angle, and the height of the target,
     * and we are pointed straight at the target Then our distance is the tangent of
     * our targetHeight - mountHeight/yAngle + mountAngle In order to make this work
     * we need to be alligned with the target so our x angle is 0
     * 
     * @return the x distance to the target
     */
    public double getTX(){
        SmartDashboard.putNumber("X angle", xAngle);
        return xAngle;
    }
    /**
     * Returns our y angle
     */
    public double getTY(){
        SmartDashboard.putNumber("Y Angle", yAngle);
        return yAngle;
    }
    /**
     * Tentative Y dist calculations 
     * They are accuarate to an inch if we are pointed perpendicular to the target 
     */
    public double calcYDist() {
        
        double distance = (targetHeight - mountHeight) / Math.tan(Math.toRadians(yAngle));
        SmartDashboard.putNumber("Y distance ", distance);
        return distance;
    }
    /**
     * Same as above accept tentative calculations for x distance 
     * Assumes we have all the variables we need 
     */
    public double calcXDist() {
        double distance = (calcYDist() * Math.tan(Math.toRadians(xAngle)));
        SmartDashboard.putNumber("X distance", distance);
        return distance;
    }
