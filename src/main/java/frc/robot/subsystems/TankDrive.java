package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.ParamEnum;

import edu.wpi.first.wpilibj.command.Subsystem;

import frc.util.Constants;

public class TankDrive extends Subsystem {

    public WPI_TalonSRX talonFL;
	public WPI_TalonSRX talonFR;
	private WPI_TalonSRX talonBL;
    private WPI_TalonSRX talonBR;
    
    public TankDrive(WPI_TalonSRX talonFL, WPI_TalonSRX talonFR, WPI_TalonSRX talonBL, WPI_TalonSRX talonBR) {
        
        this.talonFL = talonFL;
        this.talonFR = talonFR;
        this.talonBL = talonBL;
        this.talonBR = talonBR;

        configureTalons();

    }

    public void setMotionMagic(double left, double right) {
        talonFL.set(ControlMode.MotionMagic, left);
        talonFR.set(ControlMode.MotionMagic, right);
    }

    //configures base talons
    private void configureTalons() {

        talonFL.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,Constants.VELOCITY_PID_INDEX,Constants.TIMEOUT);
        talonFR.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,Constants.VELOCITY_PID_INDEX,Constants.TIMEOUT);
        
        talonFL.setSensorPhase(false);
        talonFR.setSensorPhase(false);
        
        talonFL.setInverted(false);
        talonFR.setInverted(true);
        talonBL.setInverted(false);
        talonBR.setInverted(true);

        talonFL.configNominalOutputForward(0, Constants.TIMEOUT);
		talonFL.configNominalOutputReverse(0, Constants.TIMEOUT);
		talonFL.configPeakOutputForward(1.0, Constants.TIMEOUT);
		talonFL.configPeakOutputReverse(-1.0, Constants.TIMEOUT);
		talonFR.configNominalOutputForward(0, Constants.TIMEOUT);
		talonFR.configNominalOutputReverse(0, Constants.TIMEOUT);
		talonFR.configPeakOutputForward(1.0, Constants.TIMEOUT);
        talonFR.configPeakOutputReverse(-1.0, Constants.TIMEOUT);
        
    }

    public void teleopConfig() {

        talonFR.configSetParameter(ParamEnum.eOnBoot_BrakeMode, 0.0, 0, 0, Constants.TIMEOUT);
        talonFL.configSetParameter(ParamEnum.eOnBoot_BrakeMode, 0.0, 0, 0, Constants.TIMEOUT);
        
    }

    public void autonomousConfig() {
        zeroEncoders();

        talonFR.configSetParameter(ParamEnum.eOnBoot_BrakeMode, 1.0, 1, 0, Constants.TIMEOUT);
		talonFL.configSetParameter(ParamEnum.eOnBoot_BrakeMode, 1.0, 1, 0, Constants.TIMEOUT);

    }
    


    public void zeroEncoders() {

		talonFL.setSelectedSensorPosition(0, Constants.VELOCITY_PID_INDEX, Constants.TIMEOUT);
		talonFR.setSelectedSensorPosition(0, Constants.VELOCITY_PID_INDEX, Constants.TIMEOUT);
		talonBL.setSelectedSensorPosition(0, Constants.VELOCITY_PID_INDEX, Constants.TIMEOUT);
		talonBR.setSelectedSensorPosition(0, Constants.VELOCITY_PID_INDEX, Constants.TIMEOUT);
    }

    public void setPercentage(double left,double right) {

        
		talonFL.set(ControlMode.PercentOutput, left);
        talonFR.set(ControlMode.PercentOutput, right);

    }

    public void setMotionMagic(double left, double right) {



		    talonFL.set(ControlMode.PercentOutput, Math.abs(left) >= Constants.JOYSTICK_DEADZONE ? left : 0);
        talonFR.set(ControlMode.PercentOutput, Math.abs(right) >= Constants.JOYSTICK_DEADZONE ? right : 0);
        talonBL.follow(talonFL);
        talonBR.follow(talonFR);

    }
    
    protected void initDefaultCommand() {

    }
}