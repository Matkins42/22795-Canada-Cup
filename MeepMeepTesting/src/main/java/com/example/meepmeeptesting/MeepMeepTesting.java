package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;

import org.rowlandhall.meepmeep.MeepMeep;
import org.rowlandhall.meepmeep.roadrunner.DefaultBotBuilder;
import org.rowlandhall.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        String imagePath = "C:/MeepMeep/decode.png";

        Image decodeField;
        try {
            decodeField = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            throw new RuntimeException("Could not load DECODE field image from " + imagePath, e);
        }


        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive -> drive.trajectorySequenceBuilder(new Pose2d(-60, -60 , 0))
//                        .forward(30)
//                        .turn(Math.toRadians(90))
//                        .forward(30)
//                        .turn(Math.toRadians(90))
//                        .forward(30)
//                        .turn(Math.toRadians(90))
//                        .forward(30)
//                        .turn(Math.toRadians(90))
                        .splineTo(new Vector2d(0, 0), Math.toRadians(45))
                        .lineToLinearHeading(new Pose2d(60, 60, Math.toRadians(-90)))
                        .splineTo(new Vector2d(-50, 20), Math.toRadians(135))
                        .build());


        meepMeep.setBackground(decodeField)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}