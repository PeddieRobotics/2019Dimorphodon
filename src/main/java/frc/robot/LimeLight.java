package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;


public class Limelight{
    
    NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
    NetworkTableEntry tx;
    NetworkTableEntry ty;
    final double mountAngle = 0.0;
    final double targetHeight = 5;
    final double mountHeight = 2;
    double yAngle = 0;
    double xAngle= 0;
    
    public Limelight(){
        table = NetworkTableInstance.getDefault().getTable("limelight");
        tx = table.getEntry("tx");
        ty = table.getEntry("ty");
        
    }
    /**
     * Make sure we are constantly updating our network table values 
     */
    public void update(){
        yAngle = ty.getDouble(0.0);
        xAngle = tx.getDouble(0.0);
    }
    /**
     * Assuming we know the mount height, mount angle, and the height of the target, and we are pointed straight at the target 
        *Then our distance is the tangent of our targetHeight - mountHeight/yAngle + mountAngle  
     *In order to make this work we need to be alligned with the target so our x angle is 0 
     * @return the distance to the target 
     */
    public double calcDist(){
        double distance = (targetHeight-mountHeight)/Math.tan(Math.toRadians(yAngle+mountAngle));
        return distance;
    }
    /**
     * this angle is just our crosshair angle(i think) 
     * @return
     */
    public double calcAngle(){
        return xAngle;
    }

}
