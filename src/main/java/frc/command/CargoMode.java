package frc.command;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.subsystems.BoxManipulator;
import frc.robot.subsystems.TankDrive;
import frc.command.CargoShooting;

public class CargoMode extends CommandGroup {

  public CargoMode(Robot robot,BoxManipulator manip, Joystick stick, TankDrive tankDrive) {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    addSequential(new MoveMotionMagic(robot, tankDrive, 2200, 2200));
    addSequential(new CargoShooting(manip));
  }
}