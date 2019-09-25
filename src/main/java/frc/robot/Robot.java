/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.command.CargoMode;
import frc.command.MoveMotionMagic;
import frc.command.ParkManeuver;
import frc.robot.subsystems.BoxManipulator;
import frc.robot.subsystems.TankDrive;
import frc.util.CameraControl;
import frc.util.Constants;
import frc.util.Vision;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  public Joystick driverJoystick;
  public Joystick operatorJoystick;

  WPI_TalonSRX talonFR;
	WPI_TalonSRX talonFL;
	WPI_TalonSRX talonBR;
  WPI_TalonSRX talonBL;
  
  WPI_VictorSPX talonIntakeLeft;
  WPI_VictorSPX talonIntakeRight;
  WPI_TalonSRX talonTip;
  WPI_TalonSRX talonHatchManip;

  TankDrive tankDrive;

  public double targetCenterX = 11;
  public double targetDistance = 0;
  public boolean targetFound = true;

  BoxManipulator manipulator;
  PCMHandler pcm;

  CameraControl cameras;
  
  DigitalInput limitSwitch;

  int presetPosition;
  boolean preset;

  boolean start = false;
  int invertDriver = -1;
  boolean lastSwitchCameraPressed = false;
  boolean lastInvertDriverPressed = false;

  public boolean isCommand = false;

  int currentCamera = 0;
  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {

    driverJoystick = new Joystick(0);
    operatorJoystick = new Joystick(1);
    pcm = new PCMHandler(11);

    talonFR = new WPI_TalonSRX(Constants.LEFT_MASTER_TALON_ID);
    talonBR = new WPI_TalonSRX(Constants.LEFT_SLAVE_TALON_ID);
    
    talonFL = new WPI_TalonSRX(Constants.RIGHT_MASTER_TALON_ID);
    talonBL = new WPI_TalonSRX(Constants.RIGHT_SLAVE_TALON_ID);
    
    talonTip = new WPI_TalonSRX(Constants.TALON_TIP);

    talonHatchManip = new WPI_TalonSRX(Constants.TALON_HATCH_MANIP);
    talonHatchManip.setSelectedSensorPosition(0);

    talonIntakeLeft = new WPI_VictorSPX(6);
    talonIntakeRight = new WPI_VictorSPX(7);

    tankDrive = new TankDrive(talonFL, talonFR, talonBL, talonBR);
    manipulator = new BoxManipulator(talonIntakeRight, talonIntakeLeft, talonTip, pcm);

    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    Gyro.init();
    cameras = new CameraControl(320, 240, 15);

    talonFL.setInverted(InvertType.InvertMotorOutput);
    talonBL.setInverted(InvertType.FollowMaster);

    talonFR.setInverted(InvertType.None);
    talonBR.setInverted(InvertType.FollowMaster);

    talonTip.setInverted(true);
    talonTip.setSensorPhase(true);

    //limit to 18 amps immediately
    talonTip.configPeakCurrentDuration(0);
    talonTip.configContinuousCurrentLimit(18);

    //limit the hath manipulator to 18 amps
    talonHatchManip.configContinuousCurrentLimit(18);
    talonHatchManip.configPeakCurrentDuration(0);
    
   
    limitSwitch = new DigitalInput(9);

  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    //pcm.turnOn();

    //tankDrive.teleopConfig();

    presetPosition = 0;


    talonFL.setSelectedSensorPosition(0);
    talonFR.setSelectedSensorPosition(0);

    tankDrive.teleopConfig();
    //Gyro.calibrate();
    Gyro.reset();
    

    talonTip.set(ControlMode.PercentOutput,0);
    tankDrive.setPercentage(0, 0);
    talonTip.setSelectedSensorPosition(1900, 0, 10);
    
    
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    
    teleopPeriodic();
  }


  public void teleopInit() {
   
    
  } 
  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    
    //Switching Camera
    if (driverJoystick.getRawButton(Constants.XBOX_BUTTON_TWO_WINDOWS) && !lastSwitchCameraPressed) {
      cameras.nextCamera();
      
    }

    if (driverJoystick.getRawButton(Constants.XBOX_BUTTON_THREE_LINES) && !lastInvertDriverPressed) {
      invertDriver *= -1;
      
    }

    // if(isCommand) {
    //   Scheduler.getInstance().run();
    //   return;
    // }

    if(driverJoystick.getRawButton(Constants.XBOX_BUTTON_LEFT_BUMPER)) {
        pcm.setLowGear(false);
        pcm.setHighGear(true);
    }
    

    if(driverJoystick.getRawButton(Constants.XBOX_BUTTON_RIGHT_BUMPER)) {
      pcm.setLowGear(true);
      pcm.setHighGear(false);
    }

    // Drivetrain
    if (invertDriver == 1) {
      if (Math.abs(driverJoystick.getRawAxis(Constants.XBOX_AXIS_LEFT_Y)) > 0.1) {
        talonFL.set(ControlMode.PercentOutput, invertDriver *-driverJoystick.getRawAxis(Constants.XBOX_AXIS_LEFT_Y));
        
      }else if (!isCommand) {
        talonFL.set(ControlMode.PercentOutput, 0);
      }
      if (Math.abs(driverJoystick.getRawAxis(Constants.XBOX_AXIS_RIGHT_Y)) > 0.1) {
        talonFR.set(ControlMode.PercentOutput, invertDriver * -driverJoystick.getRawAxis(Constants.XBOX_AXIS_RIGHT_Y));
        
      }else if (!isCommand) {
        talonFR.set(ControlMode.PercentOutput, 0);
      }
      
    } else {
      if (Math.abs(driverJoystick.getRawAxis(Constants.XBOX_AXIS_LEFT_Y)) > 0.1) {
        talonFR.set(ControlMode.PercentOutput, invertDriver *-driverJoystick.getRawAxis(Constants.XBOX_AXIS_LEFT_Y));
        
      }else if (!isCommand){
        talonFR.set(ControlMode.PercentOutput, 0);
      }
      if (Math.abs(driverJoystick.getRawAxis(Constants.XBOX_AXIS_RIGHT_Y)) > 0.1) {
        talonFL.set(ControlMode.PercentOutput, invertDriver * -driverJoystick.getRawAxis(Constants.XBOX_AXIS_RIGHT_Y));
        
      }else if (!isCommand) {
        talonFL.set(ControlMode.PercentOutput, 0);
      }
    }
    
    

    //Manipulator - LOGITECH
    if (operatorJoystick.getRawButton(Constants.LOGITECH_LEFT_TRIGGER)) {
      manipulator.pushBox(-0.3);
    } else if (operatorJoystick.getRawButton(Constants.LOGITECH_RIGHT_TRIGGER)) {
      manipulator.pushBox(0.3);
    } else {
      manipulator.pushBox(0.07);
    }

    /*auto park
    if(operatorJoystick.getRawButton(Constants.LOGITECH_BUTTON_X)) {
      Scheduler.getInstance().add(new ParkManeuver(this, operatorJoystick, tankDrive));
    }*/

    SmartDashboard.putString("DB/String 1", ""+talonTip.getSelectedSensorVelocity());

    if(talonTip.getSelectedSensorPosition()<-900  && operatorJoystick.getRawAxis(1) > 0.1){
      talonTip.set(ControlMode.PercentOutput, 0.12);
    }else if(Math.abs(operatorJoystick.getRawAxis(1)) > 0.1) {
      preset = false;
      talonTip.set(ControlMode.PercentOutput, -operatorJoystick.getRawAxis(1) * 2 / 3);
    } else if (presetPosition == 0 && talonTip.getSelectedSensorPosition() < 300 && preset) {
      talonTip.set(ControlMode.PercentOutput,0);
    } else if (presetPosition == 1900 && talonTip.getSelectedSensorPosition() > 1600 && preset) {
      talonTip.set(ControlMode.PercentOutput,0);
    } else if (!preset) {
      talonTip.set(ControlMode.PercentOutput,0);
    }
    


    //lock in hatch if up is pressed
    int povValue = operatorJoystick.getPOV();
    if(povValue == 0 || povValue == 45 || povValue == 315){
      talonHatchManip.set(ControlMode.PercentOutput, -0.5);
    }//release hatch if down and encoder value is not zero and limit switch is not triggered
    else if(povValue == 180 || povValue == 135 || povValue == 225){ 
      talonHatchManip.set(ControlMode.PercentOutput, 0.1);
    }else if(talonHatchManip.getSelectedSensorPosition() < -800 && talonHatchManip.getSelectedSensorPosition() > -1000){
      talonHatchManip.set(ControlMode.PercentOutput, -0.2);
    }else{
      talonHatchManip.set(ControlMode.PercentOutput, 0.0);
    }
    
    if(operatorJoystick.getRawButton(Constants.LOGITECH_BUTTON_LEFT_BUMPER)) {
      manipulator.closeManipulator();
    }
    if(operatorJoystick.getRawButton(Constants.LOGITECH_BUTTON_RIGHT_BUMPER)) {
      manipulator.openManipulator();
    }

    
    // if(driverJoystick.getRawButton(Constants.XBOX_BUTTON_X)) {
    //   Scheduler.getInstance().add(new ParkManeuver(this, operatorJoystick, tankDrive));
    // }

    Scheduler.getInstance().run();

    if (driverJoystick.getRawButton(Constants.XBOX_BUTTON_TWO_WINDOWS)) {
      lastSwitchCameraPressed = true;
    } else {
      lastSwitchCameraPressed = false;
    }

    if (driverJoystick.getRawButton(Constants.XBOX_BUTTON_THREE_LINES)) {
      lastInvertDriverPressed = true;
    } else {
      lastInvertDriverPressed = false;
    }

    if(driverJoystick.getRawButton(Constants.XBOX_BUTTON_X)) {
      Scheduler.getInstance().add(new MoveMotionMagic(this, tankDrive, 8000, 8000));
    }

    //print horizontal data to datatable
    //SmartDashboard.putString("DB/String 5", "Vision " + Vision.getHorizontalDistance());

  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
