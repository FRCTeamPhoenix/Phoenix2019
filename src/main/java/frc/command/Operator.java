package frc.command;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.BoxManipulator;
import frc.util.Constants;

public class Operator extends Command {

    private Robot m_robot;
    private Joystick m_operatorJoystick;
    private BoxManipulator m_manipulator;

    public Operator(Robot robot, Joystick OperatorJoystick, BoxManipulator manipulator) {
        m_robot = robot;
        m_manipulator = manipulator;
        m_operatorJoystick = OperatorJoystick;
    }
    
    protected void initialize(){

    }

    protected void execute(){
        //push and pull intakes
        if(m_operatorJoystick.getRawButton(Constants.XBOX_BUTTON_LEFT_BUMPER)){
            m_manipulator.pushBox();
        }else if(m_operatorJoystick.getRawButton(Constants.XBOX_BUTTON_RIGHT_BUMPER)){
            m_manipulator.pullBox();
        }else{
            m_manipulator.stop();
        }
        
        //move the manipulator up and down based off button presses
        if(m_operatorJoystick.getRawButton(Constants.XBOX_BUTTON_A)){
            m_manipulator.openManipulator();
        }else if(m_operatorJoystick.getRawButton(Constants.XBOX_BUTTON_B)){
            m_manipulator.closeManipulator();
        }

        //makes manipulator go to position
        if(Math.abs(m_operatorJoystick.getRawAxis(Constants.XBOX_AXIS_LEFT_Y)) > 0.1){
            m_manipulator.goPercentOutput(m_operatorJoystick.getRawAxis(Constants.XBOX_AXIS_LEFT_Y));
            //m_manipulator.goToPosition(Math.max(m_operatorJoystick.getRawAxis(Constants.XBOX_AXIS_LEFT_Y), 0.0) * -1000.0);
        }
    }

    protected void end(){
        m_manipulator.goToPosition(0);
    }

    protected boolean isFinished(){
        return false;
    }
    
    protected void interrupted(){
        end();
    }
}