package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.*;

import java.util.ArrayList;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

import frc.robot.lib.PID;

import edu.wpi.first.wpilibj.DriverStation;

public class LimeLight {
    final double vertIterations = 0;
    NetworkTable table;
    NetworkTableEntry tx;
    NetworkTableEntry ty;
    NetworkTableEntry tv;
    NetworkTableEntry ts; 
    NetworkTableEntry tl;
    NetworkTableEntry tvert;
    NetworkTableEntry camtran; 
    NetworkTableEntry lightState; 
    NetworkTableEntry pipeline; 
    final double mountAngle = 0.0;
    final double targetHeight = 29;
    final double mountHeight = 32.75;
    private final double TX_P = 0.01; 
    private final double TX_I = 0.0;
    private final double TX_D = 0.00;
    PID TX_PID;
    private boolean atAngle = true;
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
        ts = table.getEntry("ts");
        tl = table.getEntry("tl");
        pipeline = table.getEntry("pipeline");
        lightState = table.getEntry("ledMode");
        TX_PID = new PID(TX_P,TX_I,TX_D,6);
        tvert = table.getEntry("tvert");
        atAngle = true;
    }
    public void setPID(){
        atAngle = false;
        TX_PID.set(0);

    }
    public double getSign(double number){
        if(number>0){
            return 1; 
        }return -1;
    }
    public double generateOutput(){
    return TX_PID.getOutput(xAngle);
       
    
    
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
    public double getPipeline(){
        return pipeline.getDouble(-1);
    }
    public boolean hasTarget(){
        return(tv.getDouble(0.0)==1);
    }
    public void rightPipeline(){
        pipeline.setValue(2);
    }
    public void leftPipeline(){
        pipeline.setValue(1);
    }
    public void center(){
        pipeline.setValue(0);
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
    public void solid(){
        lightState.setValue(3);
    }
    public void defaultValue(){
        lightState.setValue(4);
    }
	
	
}