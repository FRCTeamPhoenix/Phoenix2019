/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.util;

import frc.robot.Gyro;

//Ians custom pid loop class
public class PIDLoop {

  private double p;
  private double i;
  private double d;

  private double dt;

  private double target;

  private double izone;

  private double accum;
  private double lastError = -9999;

  public PIDLoop(double p, double i, double d, double dt, double target) {
    this.p = p;
    this.i = i;
    this.d = d;
    this.dt = dt;
    this.target = target;

    this.accum = 0;
    this.izone = -1;
  }

  public double getP() {
    return p;
  }

  public void setP(double p) {
    this.p = p;
  }

  public double getI() {
    return i;
  }

  public void setI(double i) {
    this.i = i;
  }

  public double getD() {
    return d;
  }

  public void setD(double d) {
    this.d = d;
  }

  public double get() {
    double error = -(Gyro.angle() + target) / 360.0;

    double p_out = p * error;

    accum += error * dt;
    if(izone > 0 && Math.abs(accum) > izone) accum = 0;
    double i_out = i * accum;

    double d_out = 0;
    if(lastError > -9999)
      d_out = d * (error - lastError) / dt;
    
    lastError = error;

    System.out.println("Total pid out: " + (p_out + i_out + d_out) + " gyro angle: " + Gyro.angle() + " error: " + error + " p: " + p);
    return p_out + i_out + d_out;
  }

}
