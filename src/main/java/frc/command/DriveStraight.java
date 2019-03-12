/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.subsystems.TankDrive;

public class DriveStraight extends Command {

  private TankDrive m_tankDrive;
  private double amount;
  
  private boolean reached;
  private long reachedTime;

  public DriveStraight(TankDrive tankDrive, double amount) {
    m_tankDrive = tankDrive;
    this.amount = amount;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    m_tankDrive.zeroEncoders();
    m_tankDrive.setMotionMagic(amount / 4096, amount / 4096);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    boolean startFinishing = Math.abs(m_tankDrive.talonFL.getSelectedSensorPosition() - amount) < 20;
    if(startFinishing && !reached) {
      reached = true;
      reachedTime = System.currentTimeMillis();
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return reached && System.currentTimeMillis() - reachedTime > 300;
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
