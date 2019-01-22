package frc.robot.framework;

import java.util.Vector;
import edu.wpi.first.wpilibj.Notifier;

public class Looper {
    private final Notifier notifier;
    private final Vector<Runnable> methods;
    private final double kPeriod;

	/**
	 * Creates a new Looper object
	 * 
	 * @param period The period (in ms) between updates
	 */
    public Looper(double period) {
        notifier = new Notifier(this::update);
        methods = new Vector<Runnable>();
        kPeriod = period/1000.0;
    }


	/**
	 * Adds a method to the list to be periodically called
	 * 
	 * @param method The method to be called
	 */
	public synchronized void add(Runnable method) {
		methods.add(method);
	}
    
	/**
	 * Start updating
	 */
    public synchronized void start() {
        notifier.startPeriodic(kPeriod);
    }
    
    private synchronized void update() {
    	for (Runnable subsystem : methods) {
			subsystem.run();
		}
    }
}
