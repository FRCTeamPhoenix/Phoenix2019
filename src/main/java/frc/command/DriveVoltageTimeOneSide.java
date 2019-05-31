/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.subsystems.TankDrive;

public class DriveVoltageTimeOneSide extends Command {

  private double driveTime;
  private double startTime;
  private boolean backward;

  private TankDrive m_tankDrive;

  private String m_side;

  public DriveVoltageTimeOneSide(TankDrive tankDrive, double driveTime, String side) {
    m_tankDrive = tankDrive;
    this.driveTime = 1000 * Math.abs(driveTime);
    this.backward = (driveTime < 0);
    m_side = side;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    startTime = System.currentTimeMillis();
    if(m_side.equals("left")) {
      if(this.backward)
        m_tankDrive.setPercentage(0.5, 0);
      else
        m_tankDrive.setPercentage(-0.5, 0);
    } else if(m_side.equals("right")) {
      if(this.backward)
        m_tankDrive.setPercentage(0, 0.5);
      else
        m_tankDrive.setPercentage(0, -0.5);
    }
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {

  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return System.currentTimeMillis() - startTime >= driveTime;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    m_tankDrive.setPercentage(0, 0);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
