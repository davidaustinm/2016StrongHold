package org.usfirst.frc.team4003.robot.commands;

public class TrisonicsPID {
	double kp,ki,kd;
	double totalError = 0;
	double lastError = 0;
	double alpha = .8;
	double target;
	public TrisonicsPID(double kp,double ki,double kd) {
		this.kp = kp;
		this.ki = ki;
		this.kd = kd;
	}
	public void setTarget(double t) {
		target = t;
	}
	public double getCorrection(double current) {
		double error = target-current;
		if (error*lastError<=0) totalError = 0;
		else totalError = alpha*totalError+error;
		double changeInError = error-lastError;
		double correction = kp*error+ki*totalError+kd*changeInError;
		lastError = error;
		return correction;
	}
}
