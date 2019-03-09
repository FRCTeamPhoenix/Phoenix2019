/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.command.ParkManeuver;
import frc.robot.subsystems.BoxManipulator;
import frc.robot.subsystems.TankDrive;
import frc.util.CameraControl;
import frc.util.Constants;
import frc.util.PixyDriver;
import frc.util.Vision;
import io.github.pseudoresonance.pixy2api.Pixy2;

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

  Joystick driverJoystick;
  Joystick operatorJoystick;

  WPI_TalonSRX talonFR;
	WPI_TalonSRX talonFL;
	WPI_TalonSRX talonBR;
  WPI_TalonSRX talonBL;
  
  WPI_VictorSPX talonIntakeLeft;
  WPI_VictorSPX talonIntakeRight;
  WPI_TalonSRX talonTip;

  TankDrive tankDrive;

  public double targetCenterX = 11;
  public double targetDistance = 0;
  public boolean targetFound = true;

  BoxManipulator manipulator;
  PCMHandler pcm;

  CameraControl cameras;

  int presetPosition;

  boolean start = false;

  Pixy2 pixy;

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


    tankDrive = new TankDrive(talonFL, talonFR, talonBL, talonBR);
    //manipulator = new BoxManipulator(talonIntakeRight, talonIntakeLeft, talonTip, pcm);

    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    Gyro.init();
    cameras = new CameraControl(320, 240, 15);

    PixyDriver.init();

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
    teleopInit();
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    PixyDriver.get();
    teleopPeriodic();
  }


  public void teleopInit() {
    //pcm.turnOn();

    //tankDrive.teleopConfig();

    presetPosition = 0;
    talonBR.follow(talonFR);
    talonBL.follow(talonFL);

    talonFL.setSelectedSensorPosition(0);
    talonFR.setSelectedSensorPosition(0);

    tankDrive.teleopConfig();
    //Gyro.calibrate();
    Gyro.reset();
    
    talonFL.setInverted(InvertType.None);
    talonBL.setInverted(InvertType.FollowMaster);

    talonFR.setInverted(InvertType.InvertMotorOutput);
    talonBR.setInverted(InvertType.FollowMaster);

    talonTip.setInverted(InvertType.None);
  } 
  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {

    //talonFR.set(ControlMode.MotionMagic, 5);
    //talonFR.set(ControlMode.PercentOutput, driverJoystick.getRawAxis(Constants.XBOX_AXIS_RIGHT_Y));
    //talonFL.set(ControlMode.PercentOutput, driverJoystick.getRawAxis(Constants.XBOX_AXIS_LEFT_Y));
    if(operatorJoystick.getRawButton(Constants.LOGITECH_BUTTON_X)) {
      talonFR.setSelectedSensorPosition(0, 0, 10);
      talonFL.setSelectedSensorPosition(0, 0, 10);
      talonTip.setSelectedSensorPosition(0, 0, 10);
    }


    if(operatorJoystick.getRawButton(Constants.LOGITECH_BUTTON_A)) {
      Scheduler.getInstance().add(new ParkManeuver(this, operatorJoystick, tankDrive));
    }

    System.out.println(Vision.getHorizontalDistance());
    Scheduler.getInstance().run();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
