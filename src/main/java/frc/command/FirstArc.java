/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.command;

import edu.wpi.first.wpilibj.command.ConditionalCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.TankDrive;
import frc.robot.Robot;

public class FirstArc extends ConditionalCommand {

  private Robot robot;

  public FirstArc(Robot robot, TankDrive tankDrive, double multiplier) {
    super(new MoveMotionMagicOneSide(robot, tankDrive, multiplier, "left"),
          new MoveMotionMagicOneSide(robot, tankDrive, multiplier, "right"));
    this.robot = robot;
  }

  @Override
  public boolean condition() {
    return robot.targetCenterX > 2;
  }
}
