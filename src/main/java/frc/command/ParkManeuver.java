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

public class ParkManeuver extends CommandGroup {

  public ParkManeuver(Robot robot, Joystick stick, TankDrive tankDrive) {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    addSequential(new MoveMotionMagic(robot, tankDrive, -8000, -8000));
    double multiplier = SmartDashboard.getNumber("DB/Slider 0", 0);
    addSequential(new GetVisionData(robot));
    addSequential(new FirstArc(robot, tankDrive, multiplier));
    addSequential(new SecondArc(robot, tankDrive, multiplier));
  }
}


