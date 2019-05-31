package frc.command;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.subsystems.TankDrive;
import frc.util.Constants;
import frc.robot.Robot;

public class Driver extends Command {
    
    private Robot m_robot;

    private Joystick m_driverJoystick;
    private TankDrive m_tankDrive;

    public Driver(Robot robot, TankDrive tankDrive,Joystick driverJoystick) {
        m_robot = robot;
        m_tankDrive = tankDrive;
        m_driverJoystick = driverJoystick;
    }

    protected void initialize(){

    }

    protected void execute(){
        
        m_tankDrive.setPercentage(m_driverJoystick.getRawAxis(Constants.XBOX_AXIS_LEFT_Y), m_driverJoystick.getRawAxis(Constants.XBOX_AXIS_RIGHT_Y));
    }

    protected void end(){
        m_tankDrive.setPercentage(0.0, 0.0);
    }

    protected boolean isFinished(){
        return false;
    }
    
    protected void interrupted(){
        end();
    }
}