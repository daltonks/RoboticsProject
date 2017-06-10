package com.github.daltonks.pioneer.state;

import com.github.daltonks.pioneer.imageProcessing.PioneerProcessedImage;
import com.github.daltonks.pioneer.imageProcessing.PioneerProcessedImagePixel;

public class PioneerFollowPathState implements PioneerState {
    @Override
    public PioneerStateUpdateResultDto update(PioneerProcessedImage processedImage) {
        PioneerStateUpdateResultDto result = new PioneerStateUpdateResultDto();
        result.nextState = PioneerStateType.FollowPath;

        int halfWidth = processedImage.getResolutionX() / 2;
        int numPathPixels = 0;
        double totalNormalizedPathXes = 0;
        for(int y = 0; y < processedImage.getResolutionY(); y++) {
            for(int x = 0; x < processedImage.getResolutionX(); x++) {
                PioneerProcessedImagePixel color = processedImage.getPixel(x, y);
                switch(color) {
                    case Path:
                        totalNormalizedPathXes += (x - halfWidth) / (double) halfWidth;
                        numPathPixels++;
                        break;
                    case Block:

                        break;
                }
            }
        }
        float speed = 3f;
        if(numPathPixels != 0) {
            float averageNormalizedPathX = (float) (totalNormalizedPathXes / numPathPixels);
            float leftMotorVelocity;
            float rightMotorVelocity;
            if(averageNormalizedPathX >= 0) {
                leftMotorVelocity = 1 * speed;
                rightMotorVelocity = (-averageNormalizedPathX + 1) * speed;
            } else {
                leftMotorVelocity = averageNormalizedPathX + 1;
                rightMotorVelocity = 1 * speed;
            }
            result.leftMotorTargetVelocity = leftMotorVelocity;
            result.rightMotorTargetVelocity = rightMotorVelocity;
            System.out.println(averageNormalizedPathX);
        }
        else {
            result.leftMotorTargetVelocity = speed;
            result.rightMotorTargetVelocity = 0;
        }

        return result;
    }
}
