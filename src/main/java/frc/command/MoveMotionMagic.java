/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.command;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.subsystems.TankDrive;
import frc.util.Constants;
import frc.robot.Robot;

public class MoveMotionMagic extends Command {

  public static final double THRESHOLD    = 200;
  public static final int    OSC_TIME_MS  = 100;

  private double left;
  private double right;
  private boolean holdAfter;

  private TankDrive tankDrive;

  private long finishTime;
  private boolean isFinishing;

  private Robot robot;

  public MoveMotionMagic(Robot robot, TankDrive tankDrive, double left, double right, boolean holdAfter) {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(tankDrive);
    this.tankDrive = tankDrive;
    this.left = left;
    this.right = right;
    this.holdAfter = holdAfter;
    this.robot = robot;
  }

  public MoveMotionMagic(Robot robot, TankDrive tankDrive, double left, double right) {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    this(robot, tankDrive, left, right, false);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    tankDrive.zeroEncoders();
    tankDrive.setMotionMagic(left, right);
    this.isFinishing = false;
    System.out.println("start motion Magic");
    robot.isCommand = true;
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    boolean startFinishing = Math.abs(tankDrive.talonFL.getSelectedSensorPosition(0) - left) < THRESHOLD || Math.abs(tankDrive.talonFR.getSelectedSensorPosition(0) - right) < THRESHOLD;
    if(startFinishing && !isFinishing) {
      System.out.println("Start finishing");
      finishTime = System.currentTimeMillis();
      isFinishing = true;
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return (isFinishing && (System.currentTimeMillis() - finishTime) >= OSC_TIME_MS) || robot.driverJoystick.getRawButton(Constants.XBOX_BUTTON_B);
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    robot.isCommand = false;
    System.out.println("Finish finishing");
    if(!holdAfter)
      tankDrive.setPercentage(0, 0);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}
