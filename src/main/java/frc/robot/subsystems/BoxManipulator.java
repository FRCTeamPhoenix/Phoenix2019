package frc.robot.subsystems;

import frc.robot.PCMHandler;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class BoxManipulator extends Subsystem {
	public WPI_VictorSPX talonIntakeRight;
	public WPI_VictorSPX talonIntakeLeft;
	public WPI_TalonSRX talonTip;
	private PCMHandler pcm;
	Timer timer = new Timer();
	
	public static final int PULL = 0;
	public static final int PUSH = 1;
	
	private final int PidLoopIndex = 0;
	private final int PidTimeOutMs = 10;
	private final boolean SensorPhase = true;
	private final boolean InvertMotor = false;
	
	
	public BoxManipulator(WPI_VictorSPX talonIntakeRight, WPI_VictorSPX talonIntakeLeft, WPI_TalonSRX talonTip, PCMHandler pcm) {
		this.talonIntakeRight = talonIntakeRight;
		this.talonIntakeLeft = talonIntakeLeft;
		this.talonTip = talonTip;
		
		talonTip.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, PidLoopIndex, PidTimeOutMs);
		talonTip.setSensorPhase(SensorPhase);
		talonTip.setInverted(InvertMotor);
		talonTip.configNominalOutputForward(0, PidTimeOutMs);
		talonTip.configNominalOutputReverse(0, PidTimeOutMs);
		talonTip.configPeakOutputForward(1, PidTimeOutMs);
		talonTip.configPeakOutputReverse(-1, PidTimeOutMs);
		talonTip.configAllowableClosedloopError(0, PidLoopIndex, PidTimeOutMs);
		
		talonTip.config_kF(PidLoopIndex, SmartDashboard.getNumber("DB/Slider 0",0), PidTimeOutMs);
		talonTip.config_kP(PidLoopIndex, SmartDashboard.getNumber("DB/Slider 1",0), PidTimeOutMs);
		talonTip.config_kI(PidLoopIndex, SmartDashboard.getNumber("DB/Slider 2",0), PidTimeOutMs);
		talonTip.config_kD(PidLoopIndex, SmartDashboard.getNumber("DB/Slider 3",0), PidTimeOutMs);

		talonTip.config_kF(1, SmartDashboard.getNumber("DB/Slider 0",0), PidTimeOutMs);
		talonTip.config_kP(1, SmartDashboard.getNumber("DB/Slider 1",0), PidTimeOutMs);
		talonTip.config_kI(1, SmartDashboard.getNumber("DB/Slider 2",0), PidTimeOutMs);
		talonTip.config_kD(1, SmartDashboard.getNumber("DB/Slider 3",0), PidTimeOutMs);
		
		this.talonIntakeLeft.follow(this.talonIntakeRight);

		//set pid for talon tip
		
		//Need equivalent for solenoids
		this.pcm = pcm;
		
	}
	
	public void initialize() {
		talonTip.set(ControlMode.PercentOutput, 0.1);
	}
	
	public void setTipToZero() {
		talonTip.set(ControlMode.Current, 0.0);
	}
	
	public void closeManipulator() {
		pcm.closeManipulator();
	}
	
	public void openManipulator() {
		pcm.openManipulator();
	}
	
	public void goToPosition(double position) {
		talonTip.set(ControlMode.Position, position);
	}

	public void goPercentOutput(double percent){
		talonTip.set(ControlMode.PercentOutput, percent);
	}

	public double getPosition(){
		return talonTip.getSelectedSensorPosition();
	}
	
	public void pullBox() {
		talonIntakeRight.set(ControlMode.PercentOutput, -0.5);
		talonIntakeLeft.set(ControlMode.PercentOutput, 0.5);
		
	}
	
	public void pushBox() {
		pushBox(1);
	}
	
	public void pushBox(double power) {
		talonIntakeRight.set(ControlMode.PercentOutput, power);
		talonIntakeLeft.set(ControlMode.PercentOutput, power);
	}
	
	public void outputToSmartDashboard() {
		SmartDashboard.putString("DB/String 0", "Motor Output: " + (talonIntakeRight.getMotorOutputPercent()*100) + "%");
		SmartDashboard.putString("DB/String 1", "Position: " + talonIntakeRight.getSelectedSensorPosition(0));
		//Need equivalent for solenoids
	}
	public boolean atUpperLimit() {
		return talonTip.getSensorCollection().isRevLimitSwitchClosed();
	}
	
	public void end(int t) {
		talonTip.set(ControlMode.PercentOutput, 0.4 * Math.exp(-t / 50));
	}

	public void stop() {
		talonIntakeRight.set(ControlMode.PercentOutput, 0.0);
		talonIntakeLeft.set(ControlMode.PercentOutput, 0.0);
	}

	protected void initDefaultCommand() {
		// TODO Auto-generated method stub
		
	}
	
}