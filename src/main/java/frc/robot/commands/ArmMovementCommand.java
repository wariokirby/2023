// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Elbow;
import frc.robot.subsystems.Shoulder;
import frc.robot.subsystems.Wrist;

public class ArmMovementCommand extends CommandBase {
  //shoulder, elbow, wrist
  
  private final double[] PARKED_POSITION_F = {-90 , 0 , 0};//TODO need parked numbers
  private final double[] SCORE_HIGH_F = {0, 0, 0};
  private final double[] SCORE_MID_F = {-180, 0, 0};
  private final double[] FLOOR_F = {-90, 0, 0};
  private final double[] H_STATION_F = {-90, 0, 0};

  private final double[] PARKED_POSITION_R = {-90 , 0 , 0};//TODO need parked numbers
  private final double[] SCORE_HIGH_R = {0, 0, 0};
  private final double[] SCORE_MID_R = {-180, 0, 0};
  private final double[] FLOOR_R = {-90, 0, 0};
  private final double[] H_STATION_R = {-90, 0, 0};

  private final double[][] All_POSITIONS = {PARKED_POSITION_F , FLOOR_F  , SCORE_MID_F , SCORE_HIGH_F , H_STATION_F , 
    PARKED_POSITION_R , FLOOR_R , SCORE_MID_R , SCORE_HIGH_R , H_STATION_R};

  private final double S_PREP_FOR_SWITCH_F = 0; //TODO find lowest position to safely flip sides
  private final double S_PREP_FOR_SWITCH_R = -180;

  private Shoulder shoulder;
  private Elbow elbow;
  private Wrist wrist;
  private int position; //0-parked, 1-floor, 2-mid, 3-high, 4-h_station, 5-switch
  private double[] rPos;
  private boolean isFront;
  private boolean finished;
  private double prepSwitch;
  
  /** Creates a new ArmMovementCommand. */
  public ArmMovementCommand(Shoulder shoulder , Elbow elbow , Wrist wrist , int position) {
    this.shoulder = shoulder;
    this.elbow = elbow;
    this.wrist = wrist;
    this.position = position;
    isFront = shoulder.getMeasurement() >= -90;
    if(isFront){
      rPos = All_POSITIONS[position];
      prepSwitch = S_PREP_FOR_SWITCH_F;
    }
    else{
      prepSwitch = S_PREP_FOR_SWITCH_R;
      if(position <= 4){
        rPos = All_POSITIONS[position + 5];
      }
      else{
        rPos = All_POSITIONS[0];//front parked
      }
      
    }
    finished = false;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(shoulder , elbow , wrist);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(position == 0){
      if(Math.abs(rPos[2] - wrist.getMeasurement()) > wrist.getController().getPositionTolerance()){
        wrist.setSetpoint(rPos[2]);
      }
      else if(Math.abs(rPos[1] - elbow.getMeasurement()) > elbow.getController().getPositionTolerance()){
        elbow.setSetpoint(rPos[1]);
      }
      else if(Math.abs(rPos[0] - shoulder.getMeasurement()) > shoulder.getController().getPositionTolerance()){
        shoulder.setSetpoint(rPos[0]);
      }
      else{
        finished = true;
      }
    }
    else if(position <= 4){
      if(Math.abs(rPos[0] - shoulder.getMeasurement()) > shoulder.getController().getPositionTolerance()){
        shoulder.setSetpoint(rPos[0]);
      }
      else if(Math.abs(rPos[1] - elbow.getMeasurement()) > elbow.getController().getPositionTolerance()){
        elbow.setSetpoint(rPos[1]);
      }
      else if(Math.abs(rPos[2] - wrist.getMeasurement()) > wrist.getController().getPositionTolerance()){
        wrist.setSetpoint(rPos[2]);
      }
      else{
        finished = true;
      }
    }
    else{
      if(Math.abs(prepSwitch - shoulder.getMeasurement()) > shoulder.getController().getPositionTolerance()){
        shoulder.setSetpoint(prepSwitch);
      }
      else if(Math.abs(rPos[2] - wrist.getMeasurement()) > wrist.getController().getPositionTolerance()){
        wrist.setSetpoint(rPos[2]);
      }
      else if(Math.abs(rPos[1] - elbow.getMeasurement()) > elbow.getController().getPositionTolerance()){
        elbow.setSetpoint(rPos[1]);
      }
      else if(Math.abs(rPos[0] - shoulder.getMeasurement()) > shoulder.getController().getPositionTolerance()){
        shoulder.setSetpoint(rPos[0]);
      }
      else{
        finished = true;
      }
    }
  }
  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return finished;
  }
}
