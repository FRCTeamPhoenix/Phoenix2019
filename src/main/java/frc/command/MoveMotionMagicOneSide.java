/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.command;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.TankDrive;
import frc.util.Constants;
import frc.robot.Robot;

public class MoveMotionMagicOneSide extends Command {

  public static final double THRESHOLD    = 200;
  public static final int    OSC_TIME_MS  = 100;

  private double amount;
  private String side;
  private boolean holdAfter;

  private TankDrive tankDrive;

  private long finishTime;
  private boolean isFinishing;

  private Robot robot;

  public MoveMotionMagicOneSide(Robot robot, TankDrive tankDrive, double amount, String side, boolean holdAfter) {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    this.tankDrive = tankDrive;
    this.amount = amount;
    this.side = side;
    this.holdAfter = holdAfter;
    this.robot = robot;
  }

  public MoveMotionMagicOneSide(Robot robot, TankDrive tankDrive, double amount, String side) {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    this(robot, tankDrive, amount, side, false);
  }

  private double amount(int x) {
    int[] data = {3, -3, 9, -6, -8, 5};

    int d = data[0];
    int minDist = 9999;
    for(int i=0;i<data.length;i++) {
      if(Math.abs(data[i] - x) < minDist) {
        d = data[i];
        minDist = Math.abs(data[i] - x);
      }
    }
    System.out.println("d: " + d + " x: " + x);
    //return Math.abs(SmartDashboard.getNumber("DB/Slider 2", 0));
    switch(d) {
      case 3: return 3873;
      case -3: return 3873;
      case 9: return 5000;
      case -6: return 5000;
      case -8: return 5300;
      case 5: return 4600;
      default: return 0;
    }
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    tankDrive.zeroEncoders();
    double x = Math.abs(robot.targetCenterX);
    //amount = amount((int)x - (int)x % 2);
    amount = amount((int)x);

    System.out.println("amount " + amount);
    if(side.equals("left"))
      tankDrive.setMotionMagic(-amount, 0);
    else if(side.equals("right"))
      tankDrive.setMotionMagic(0, -amount);
    this.isFinishing = false;
    robot.isCommand = true;
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    boolean startFinishing = true;
    //System.out.println("left errror " + Math.abs(tankDrive.talonFL.getSelectedSensorPosition(0) + amount));
    //System.out.println("right errof" + Math.abs(tankDrive.talonFR.getSelectedSensorPosition(0) + amount));
    if(side.equals("left"))
      startFinishing = Math.abs(tankDrive.talonFL.getSelectedSensorPosition(0) + amount) < THRESHOLD;
    else if(side.equals("right"))
      startFinishing = Math.abs(tankDrive.talonFR.getSelectedSensorPosition(0) + amount) < THRESHOLD;
    if(startFinishing && !isFinishing) {
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
    if(!holdAfter)
      tankDrive.setPercentage(0, 0);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
