/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.command;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.subsystems.TankDrive;
import frc.util.Constants;

public class ParkManeuver extends CommandGroup {
  public ParkManeuver(Robot robot, Joystick stick, TankDrive tankDrive) {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    addSequential(new DriveStraight(tankDrive, -8000));
    addSequential(new GetVisionData(robot));
    double multiplier = SmartDashboard.getNumber("DB/Slider 0", 0);
    System.out.println(multiplier * robot.targetCenterX);
    if(robot.targetCenterX < Constants.NEAR_TARGET) {
      addSequential(new DriveGyroOneSide(tankDrive, multiplier * robot.targetCenterX, "left"));
      addSequential(new DriveGyroOneSide(tankDrive, multiplier * robot.targetCenterX, "right"));
      //addSequential(new DriveVoltageTime(tankDrive, robot.targetDistance));
    } else if(robot.targetCenterX > Constants.NEAR_TARGET) {
      addSequential(new DriveGyroOneSide(tankDrive, multiplier * robot.targetCenterX, "right"));
      addSequential(new DriveGyroOneSide(tankDrive, multiplier * robot.targetCenterX, "left"));
      //addSequential(new DriveVoltageTime(tankDrive, Constants.TARGET_DISTANCE_MUL * robot.targetDistance));
    } else {
      addSequential(new DriveStraight(tankDrive, 8000));
    }
  }
}
