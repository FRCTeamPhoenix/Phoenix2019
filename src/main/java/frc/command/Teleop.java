package frc.command;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.robot.subsystems.BoxManipulator;
import frc.robot.subsystems.TankDrive;
import frc.robot.Robot;


public class Teleop extends CommandGroup {

    public Teleop(Robot robot, TankDrive tankDrive, BoxManipulator manipulator, Joystick driverJoystick, Joystick operatorJoyStick) {
        addSequential(new Driver(robot,tankDrive,driverJoystick));
        addParallel(new Operator(robot,operatorJoyStick,manipulator));
    }
}