package com.github.daltonks.pioneer;

import com.github.daltonks.Constants;
import com.github.daltonks.SimObject;
import com.github.daltonks.pioneer.imageProcessing.PioneerProcessedImage;
import com.github.daltonks.pioneer.state.*;
import com.github.daltonks.pioneer.state.base.PioneerState;
import com.github.daltonks.pioneer.state.base.PioneerStateType;
import com.github.daltonks.pioneer.state.base.PioneerStateUpdateDto;

import java.util.HashMap;

public class Pioneer {
    private SimObject leftMotor;
    private SimObject rightMotor;
    private SimObject camera;
    private float lastLeftMotorVelocity;
    private float lastRightMotorVelocity;
    private HashMap<PioneerStateType, PioneerState> states;
    private PioneerState currentState;
    private PioneerStateUpdateDto updateDto = new PioneerStateUpdateDto();

    public Pioneer(SimObject leftMotor, SimObject rightMotor, SimObject camera) {
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;
        this.camera = camera;

        states = new HashMap<>();
        states.put(PioneerStateType.FollowPath, new PioneerFollowPathState());
        states.put(PioneerStateType.FollowBlock, new PioneerFollowBlockState());
        states.put(PioneerStateType.Reverse, new PioneerReverseState());
        states.put(PioneerStateType.TurnAwayFromBlock, new PioneerTurnAwayFromBlockState());

        currentState = states.get(PioneerStateType.FollowPath);
    }

    public void update() {
        System.out.println(currentState != null ? currentState.getClass().getCanonicalName() : "None");

        updateDto.processedImage = new PioneerProcessedImage(camera.getVisionSensorImage());
        currentState.update(updateDto);

        float undampedLeftMotorVelocity;
        float undampedRightMotorVelocity;
        if(updateDto.normalizedXMoveDirection >= 0) {
            undampedLeftMotorVelocity = Constants.PIONEER_SPEED;
            undampedRightMotorVelocity = (-updateDto.normalizedXMoveDirection + 1) * Constants.PIONEER_SPEED;
        } else {
            undampedLeftMotorVelocity = (updateDto.normalizedXMoveDirection + 1) * Constants.PIONEER_SPEED;
            undampedRightMotorVelocity = Constants.PIONEER_SPEED;
        }
        if(updateDto.isReversing) {
            undampedLeftMotorVelocity *= -1;
            undampedRightMotorVelocity *= -1;
        }
        lastLeftMotorVelocity = lerp(lastLeftMotorVelocity, undampedLeftMotorVelocity, 1 - Constants.PIONEER_MOTOR_VELOCITY_DAMPING);
        lastRightMotorVelocity = lerp(lastRightMotorVelocity, undampedRightMotorVelocity, 1 - Constants.PIONEER_MOTOR_VELOCITY_DAMPING);
        leftMotor.setJointTargetVelocity(lastLeftMotorVelocity);
        rightMotor.setJointTargetVelocity(lastRightMotorVelocity);

        PioneerState nextState = states.get(updateDto.nextState);
        if(currentState != nextState) {
            nextState.onEnterState();
            currentState = nextState;
        }
    }

    private float lerp(float point1, float point2, float alpha)
    {
        return point1 + alpha * (point2 - point1);
    }
}
