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
    private float lastLeftMotorVelocity;
    private float lastRightMotorVelocity;
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

        float targetLeftMotorVelocity;
        float targetRightMotorVelocity;
        if(stateUpdateResult.normalizedXMoveDirection >= 0) {
            targetLeftMotorVelocity = Constants.PIONEER_SPEED;
            targetRightMotorVelocity = (-stateUpdateResult.normalizedXMoveDirection + 1) * Constants.PIONEER_SPEED;
        } else {
            targetLeftMotorVelocity = (stateUpdateResult.normalizedXMoveDirection + 1) * Constants.PIONEER_SPEED;
            targetRightMotorVelocity = Constants.PIONEER_SPEED;
        }
        lastLeftMotorVelocity = lerp(lastLeftMotorVelocity, targetLeftMotorVelocity, 1 - Constants.PIONEER_MOTOR_VELOCITY_DAMPING);
        lastRightMotorVelocity = lerp(lastRightMotorVelocity, targetRightMotorVelocity, 1 - Constants.PIONEER_MOTOR_VELOCITY_DAMPING);
        leftMotor.setJointTargetVelocity(lastLeftMotorVelocity);
        rightMotor.setJointTargetVelocity(lastRightMotorVelocity);

        currentState = states.get(stateUpdateResult.nextState);
    }

    float lerp(float point1, float point2, float alpha)
    {
        return point1 + alpha * (point2 - point1);
    }
}
