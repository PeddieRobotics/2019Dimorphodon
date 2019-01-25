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
    final double targetHeight = 31.5;
    final double mountHeight = 8.75;
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
            SmartDashboard.putNumber("Distance", calcDist());
            SmartDashboard.putNumber("yAngle", yAngle);
            SmartDashboard.putNumber("radYAngle", Math.toRadians(yAngle));
        }
    }

    /**
     * Assuming we know the mount height, mount angle, and the height of the target,
     * and we are pointed straight at the target Then our distance is the tangent of
     * our targetHeight - mountHeight/yAngle + mountAngle In order to make this work
     * we need to be alligned with the target so our x angle is 0
     * 
     * @return the distance to the target
     */
    public double calcDist() {
        double distance = (targetHeight - mountHeight) / Math.tan(Math.toRadians(yAngle + cy0.getDouble(0.0) * 20.5));
        return distance;
    }

    /**
     * this angle is just our crosshair angle(i think)
     * 
     * @return
     */
    public double calcAngle() {
        return xAngle;
    }

}
