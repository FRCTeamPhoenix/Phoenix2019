/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.subsystems.TankDrive;

public class DriveVoltageTime extends Command {

  private double driveTime;
  private double startTime;
  private boolean backward;
  
  private TankDrive m_tankDrive;

  public DriveVoltageTime(TankDrive tankDrive, double driveTime) {
    m_tankDrive = tankDrive;
    this.driveTime = 1000 * Math.abs(driveTime);
    this.backward = (driveTime < 0);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    this.startTime = System.currentTimeMillis();
    m_tankDrive.autonomousConfig();
    if(this.backward)
      m_tankDrive.setPercentage(0.5, 0.5);
    else
      m_tankDrive.setPercentage(-0.5, -0.5);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {

  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    //System.out.println(System.currentTimeMillis() - startTime);
    return System.currentTimeMillis() - startTime >= driveTime;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    //System.out.println("ending");
    m_tankDrive.setPercentage(0, 0);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
