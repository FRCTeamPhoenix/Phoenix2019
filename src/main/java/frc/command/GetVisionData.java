/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class GetVisionData extends Command {

  private Robot m_robot;

  public GetVisionData(Robot robot) {
    m_robot = robot;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    m_robot.targetCenterX = 94; //obviously not actually 94 lol
    m_robot.targetDistance = 1248; //obviously not actually 1248 lol
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return true;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}