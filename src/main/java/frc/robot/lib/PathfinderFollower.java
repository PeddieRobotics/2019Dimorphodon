package frc.robot.lib;

import jaci.pathfinder.Trajectory;

/**
 * The DistanceFollower is an object designed to follow a trajectory based on distance covered input. This class can be used
 * for Tank or Swerve drive implementations.
 *
 * @author Jaci
 */

import frc.robot.lib.pathfinder.TankModifier;

public class PathfinderFollower {

    private double kp, ki, kd, kv, ka, kTurn;

    private double[] last_error = new double[2]; 
    private double[] error_sum = new double[2]; 
    private double goalHeading;

    private int[] segment = new int[2];
    private Trajectory trajectory;
    private Trajectory left_trajectory, right_trajectory;
    private TankModifier modifier; 
    private final double wheelbase_width = 27.0/12; // Basan uses 27.0/12, Cygnus uses 25.0/12 
    private final double direction = 1.0;
    

    /**
     * Configure the PID/VA Variables for the Follower
     * @param traj The trajectory to follow
     * @param kp The proportional term. This is usually quite high (0.8 - 1.0 are common values)
     * @param kv The velocity ratio. This should be 1 over your maximum velocity @ 100% throttle.
     *           This converts m/s given by the algorithm to a scale of -1..1 to be used by your
     *           motor controllers
     * @param ka The acceleration term. Adjust this if you want to reach higher or lower speeds faster. 0.0 is the default
     */
    public PathfinderFollower(Trajectory trajectory, double kp, double ki, double kd, double kv, double ka, double kTurn) {
        this.trajectory = trajectory;
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;
        this.kv = kv;
        this.ka = ka;
        this.kTurn = kTurn;  
        makeTankModifiedTrajectory();
    }
    
    //default constructor for P control-loop
    public PathfinderFollower(Trajectory trajectory, double kp, double kv, double ka, double kTurn) {
        this(trajectory, kp, 0, 0, kv, ka, kTurn);  
        makeTankModifiedTrajectory();
    }
    
    //default constructor for PI control-loop
    public PathfinderFollower(Trajectory trajectory, double kp, double ki, double kv, double ka, double kTurn) {
        this(trajectory, kp, ki, 0, kv, ka, kTurn);  
        makeTankModifiedTrajectory();
    }
    
    public void makeTankModifiedTrajectory() {
    	
    // use modifier to make trajectory for left and right side
    modifier = new TankModifier(trajectory).modify(wheelbase_width);
    left_trajectory = modifier.getLeftTrajectory();
    right_trajectory = modifier.getRightTrajectory();
        
    }

    /**
     * Reset the follower to start again. Encoders must be reconfigured.
     */
    public void reset() {
        segment[0] = 0;
        
    }

    /**
     * Calculate the desired output for the motors, based on the distance the robot has covered.
     * This does not account for heading of the robot. To account for heading, add some extra terms in your control
     * loop for realignment based on gyroscope input and the desired heading given by this object.
     * @param leftDistance, rightDistance, observed_heading		distance in meters
     * @return                  The desired output for your motor controller
     */
    public double [] getOutput(double leftDistance, double rightDistance, double observed_heading) {
    	double[] output = new double[2];
    	
    	if (isFinished()) {
			  output[0] = 0;
			  output[1] = 0;
			  return output;
		  } else  {

   			double distanceL = direction * leftDistance;
   			double distanceR = direction * rightDistance;

   			double speedLeft = direction * calculate(left_trajectory, distanceL, 0); //save last_error and error_sum in slot 0
   			double speedRight = direction * calculate(right_trajectory, distanceR, 1); // save last_error and error_sum in slot 1
              
            double observedHeading = Math.toRadians(observed_heading);
              
            double angleDiff = getAngleDiff(goalHeading - observedHeading);
            		
            double turn = kTurn * angleDiff;

  			output[0] = speedLeft + turn;
  			output[1] = speedRight - turn;
  			
  			return output;
   	    }
   	}
    
    public double getAngleDiff(double angle) {
        while (angle >= 180.0) {
          angle -= 360.0;
        }
        while (angle < -180.0) {
          angle += 360.0;
        }
        return Math.toDegrees(angle);
    }

    public double calculate(Trajectory trajectory_in_use, double distance_covered, int error_control) {
    	int i = error_control;
    	
    	if (segment[i] < trajectory_in_use.length()) {
            Trajectory.Segment seg = trajectory_in_use.get(segment[i]);
            double error = seg.position - distance_covered;
            double calculated_value =
                    kp * error +                                    // Proportional
                    ki * error_sum[i] +								// Integral 
                    kd * ((error - last_error[i]) / seg.dt) +		// Derivative
                    (kv * seg.velocity + ka * seg.acceleration);    // V and A Terms
            
            error_sum[i] += error * seg.dt; // integrate error
            last_error[i] = error; // save last_error
            goalHeading = seg.heading;
            segment[i]++;
            
            return calculated_value;
        }
    	else {
    		return 0;
    	}
    }   

    /**
     * @return the desired heading of the current point in the trajectory
     */
    public double getHeading() {
        return goalHeading;
    }

    /**
     * @return the current segment being operated on`
     */
    public Trajectory.Segment getSegment() {
        return trajectory.get(segment[0]);
    }

    /**
     * @return whether we have finished tracking this trajectory or not.
     */
    public boolean isFinished() {
        return segment[0] >= trajectory.length();
    }
}