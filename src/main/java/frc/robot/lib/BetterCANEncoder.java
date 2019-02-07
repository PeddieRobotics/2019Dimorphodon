package frc.robot.lib;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;

public class BetterCANEncoder extends CANEncoder {

    private double distancePerPulse;
    private double offset;
	
	public BetterCANEncoder(CANSparkMax spark) {
		super(spark);
	}
    
    public void setDistancePerPulse(double setpoint) {
        distancePerPulse = setpoint;
    }

    public void reset() {
        offset = this.getPosition();
    }

    public double getDistance() {
        return (this.getPosition() - offset) * distancePerPulse;
    }
}
