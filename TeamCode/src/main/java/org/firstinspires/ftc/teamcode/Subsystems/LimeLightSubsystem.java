package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

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
}
