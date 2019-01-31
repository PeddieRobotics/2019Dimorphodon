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
    }

    /**
     * Make sure we are constantly updating our network table values
     */
    public void update() {
        double hasTarget = tv.getDouble(0.0);
        if (hasTarget == 1) {
            yAngle = ty.getDouble(0.0);
            xAngle = tx.getDouble(0.0);
            SmartDashboard.putNumber("tx", xAngle);
            SmartDashboard.putNumber("Distance", calcDist());
            SmartDashboard.putNumber("ty", yAngle);
            // SmartDashboard.putNumber("radYAngle", Math.toRadians(yAngle));
        }
    }

    public double calcDist() {
        double distance = (targetHeight - mountHeight) / Math.tan(Math.toRadians(yAngle));
        return distance;
    }

    public double calcAngle() {
        return xAngle;
    }

}
