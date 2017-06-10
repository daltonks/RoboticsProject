package com.github.daltonks.pioneer.state;

import com.github.daltonks.pioneer.imageProcessing.PioneerProcessedImage;
import com.github.daltonks.pioneer.imageProcessing.PioneerProcessedPixelType;

public class PioneerFollowPathState implements PioneerState {
    @Override
    public PioneerStateUpdateResultDto update(PioneerProcessedImage processedImage) {
        PioneerStateUpdateResultDto result = new PioneerStateUpdateResultDto();
        result.nextState = PioneerStateType.FollowPath;

        Float averageNormalizedPathScreenX = processedImage.getAverageNormalizedScreenX(PioneerProcessedPixelType.Path);
        if(averageNormalizedPathScreenX != null) {
            result.normalizedXMoveDirection = averageNormalizedPathScreenX;
        } else {
            result.normalizedXMoveDirection = 1;
        }

        return result;
    }
}
