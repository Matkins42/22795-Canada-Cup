package org.firstinspires.ftc.teamcode.Subsystems;

import android.os.LimitExceededException;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.Constants.RobotConstants;

public class LimeLightSubsystem {

    private Limelight3A limeLight;
    private int currentPipeline;


    public LimeLightSubsystem(HardwareMap hardwareMap) {
        limeLight = hardwareMap.get(Limelight3A.class, "limeLight");
        limeLight.pipelineSwitch(0);
        currentPipeline = 0;

        limeLight.start();
    }

    public LLResult getData(){
        return limeLight.getLatestResult();
    }

    public double getXAngle(){
        return limeLight.getLatestResult().getTx();
    }

    public double getYAngle(){
        return limeLight.getLatestResult().getTy();
    }

    public void switchPipeline(int pipeline){
        limeLight.pipelineSwitch(pipeline);
        currentPipeline = pipeline;
    }

    public int getPipeline(){
        return currentPipeline;
    }

    public boolean seesTag(){
        LLResult data = limeLight.getLatestResult();
        return data != null && data.isValid();
    }

    public Pose3D get3dPose(){
        if (limeLight.getLatestResult().getFiducialResults().size() > 0) {
            return limeLight.getLatestResult().getFiducialResults().get(0).getTargetPoseCameraSpace();
        } else{
            return null;
        }
    }
    public double getDistancePose(){
        Pose3D pose = get3dPose();
        double y = pose.getPosition().z * Math.cos(Math.toRadians(RobotConstants.LL_ANGLE)) + pose.getPosition().y * Math.sin(Math.toRadians(RobotConstants.LL_ANGLE));
        return Math.sqrt(Math.pow(pose.getPosition().x, 2) + Math.pow(y, 2));
    }

    public double getTagAngle(){
        Pose3D pose = get3dPose();
        if(pose != null){
            return pose.getOrientation().getYaw();
        } else{
            return 0;
        }
    }

    public double getDistanceTrig(){
        return (RobotConstants.TAG_HEIGHT - RobotConstants.LL_HEIGHT)/(Math.tan(Math.toRadians(limeLight.getLatestResult().getTy() + RobotConstants.LL_ANGLE)));
    }

    public double getOffsetAngle(){ //Angle from april tag to be looking at the centre of the goal
        double angle = Math.abs(getTagAngle());
        double direction = (getTagAngle()/angle);
        double length = Math.sqrt(Math.pow(RobotConstants.TARGET_OFFSET, 2) + Math.pow(getDistanceTrig(), 2) - 2 * RobotConstants.TARGET_OFFSET * getDistanceTrig()*Math.acos(Math.toRadians(angle + 90)));
        return direction * Math.acos((Math.pow(getDistanceTrig(), 2) + Math.pow(length, 2) - Math.pow(RobotConstants.TARGET_OFFSET, 2))/(2 * getDistanceTrig() * length));
    }
}
