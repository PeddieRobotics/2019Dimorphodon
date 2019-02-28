package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.*;

import java.util.ArrayList;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class LimeLight {
    final double vertIterations = 0;
    NetworkTable table;
    NetworkTableEntry tx;
    NetworkTableEntry ty;
    NetworkTableEntry tv;
    NetworkTableEntry tvert;
    NetworkTableEntry lightState; 
    final double mountAngle = 0.0;
    final double targetHeight = 29;
    final double mountHeight = 32.75;
    double yAngle = 0;
    double xAngle = 0;
    double verticalHeigth; 
    double currentTvert = 0;
    ArrayList<Double> averageTVert = new ArrayList<Double>();
    public LimeLight() {
        table = NetworkTableInstance.getDefault().getTable("limelight");
        tx = table.getEntry("tx");
        ty = table.getEntry("ty");
        tv = table.getEntry("tv");
        lightState = table.getEntry("ledMode");
        
        tvert = table.getEntry("tvert");
    }
    public double tx(){
        return this.xAngle;
    }
    public double ty(){
        return this.yAngle;
    }
    /**
     * Make sure we are constantly updating our network table values
     */
    public void update() {
        double hasTarget = tv.getDouble(0.0);
        if (hasTarget == 1) {
            yAngle = ty.getDouble(0.0);
            xAngle = tx.getDouble(0.0);
            currentTvert = tvert.getDouble(0.0);    
            if(currentTvert>0){updateTVert(currentTvert);}
            SmartDashboard.putNumber("tx", xAngle);
            SmartDashboard.putNumber("Distance", calcDist());
            SmartDashboard.putNumber("ty", yAngle);
            // SmartDashboard.putNumber("radYAngle", Math.toRadians(yAngle));
        }
    }
    public boolean hasTarget(){
        return(tv.getDouble(0.0)==1);
    }

    public double calcDist() {
        double distance = (targetHeight - mountHeight) / Math.tan(Math.toRadians(yAngle));
        return distance;
    }
    public double calcXDist(){
        double distance = calcDist()/Math.tan(Math.toRadians(xAngle));
        return distance;
    }
    public double calcAngle() {
        return xAngle;
    }
    public double toFeet(double inches){
        return inches/12;
    }
    public void updateTVert(double current){
        averageTVert.add(current);
        if(averageTVert.size()>vertIterations){
            averageTVert.remove(0);
        }
    }
    public double tvertAverage(){
        double average = 0;
        if(averageTVert.size()>0){
            for(int i =0; i < averageTVert.size();i++){
                average += averageTVert.get(i);
            }
        }
        return average/averageTVert.size();
    }
    public void blink(){
       lightState.setValue(2);
    }
    public void off(){
        lightState.setValue(1);
    }
}
