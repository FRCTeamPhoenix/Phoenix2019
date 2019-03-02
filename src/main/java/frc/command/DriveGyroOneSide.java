/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.command;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Gyro;
import frc.robot.subsystems.TankDrive;
import frc.util.PIDLoop;

public class DriveGyroOneSide extends Command {

  private double angle;

  private TankDrive m_tankDrive;

  private String m_side;

  private PIDLoop pidLoop;

  private boolean reached;
  private long reachedTime;

  public DriveGyroOneSide(TankDrive tankDrive, double angle, String side) {
    m_tankDrive = tankDrive;
    this.angle = angle;
    m_side = side;
    reached = false;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    m_tankDrive.zeroEncoders();
    //pidLoop = new PIDLoop(SmartDashboard.getNumber("DB/Slider 1",0), SmartDashboard.getNumber("DB/Slider 2", 0), SmartDashboard.getNumber("DB/Slider 3",0), 20, angle);
    if(m_side.equals("left"))
      m_tankDrive.setMotionMagic(angle / 4096, 0);
    else if(m_side.equals("right"))
      m_tankDrive.setMotionMagic(0, angle / 4096);
    Gyro.reset();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    boolean startFinishing = true;
    if(m_side.equals("left"))
      startFinishing = Math.abs(m_tankDrive.talonFL.getSelectedSensorPosition() - angle) < 20;
    else if(m_side.equals("right"))
      startFinishing = Math.abs(m_tankDrive.talonFR.getSelectedSensorPosition() - angle) < 20;

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
