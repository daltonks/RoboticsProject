package com.github.daltonks.pioneer;

import com.github.daltonks.Constants;
import com.github.daltonks.SimObject;
import com.github.daltonks.pioneer.imageProcessing.PioneerProcessedImage;
import com.github.daltonks.pioneer.state.*;

import java.util.HashMap;

public class Pioneer {
    private SimObject leftMotor;
    private SimObject rightMotor;
    private SimObject camera;
    private HashMap<PioneerStateType, PioneerState> states;
    private PioneerState currentState;

    public Pioneer(SimObject leftMotor, SimObject rightMotor, SimObject camera) {
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;
        this.camera = camera;

        PioneerFollowPathState followPathState = new PioneerFollowPathState();
        states = new HashMap<>();
        states.put(PioneerStateType.FollowPath, followPathState);
        states.put(PioneerStateType.FollowBlock, new PioneerFollowBlockState());

        currentState = followPathState;
    }

    public void update() {
        PioneerProcessedImage processedImage = new PioneerProcessedImage(camera.getVisionSensorImage());
        PioneerStateUpdateResultDto stateUpdateResult = currentState.update(processedImage);

        float leftMotorVelocity;
        float rightMotorVelocity;
        if(stateUpdateResult.normalizedXMoveDirection >= 0) {
            leftMotorVelocity = Constants.PIONEER_SPEED;
            rightMotorVelocity = (-stateUpdateResult.normalizedXMoveDirection + 1) * Constants.PIONEER_SPEED;
        } else {
            leftMotorVelocity = stateUpdateResult.normalizedXMoveDirection + 1;
            rightMotorVelocity = Constants.PIONEER_SPEED;
        }
        leftMotor.setJointTargetVelocity(leftMotorVelocity);
        rightMotor.setJointTargetVelocity(rightMotorVelocity);

        currentState = states.get(stateUpdateResult.nextState);
    }
}
