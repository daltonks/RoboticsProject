package com.github.daltonks.pioneer.state;

import com.github.daltonks.Constants;
import com.github.daltonks.pioneer.imageProcessing.PioneerProcessedImage;
import com.github.daltonks.pioneer.imageProcessing.PioneerProcessedPixelType;

public class PioneerFollowPathState implements PioneerState {
    @Override
    public void update(PioneerStateUpdateDto dto) {
        int numBlockPixels = dto.processedImage.getNumPixels(PioneerProcessedPixelType.Block);
        if(numBlockPixels >= Constants.NUM_BLOCK_PIXELS_BEFORE_FOLLOW) {
            dto.nextState = PioneerStateType.FollowBlock;
        } else {
            dto.nextState = PioneerStateType.FollowPath;
            Float averageNormalizedPathScreenX = dto.processedImage.getAverageNormalizedScreenX(PioneerProcessedPixelType.Path);
            if(averageNormalizedPathScreenX != null) {
                dto.normalizedXMoveDirection = averageNormalizedPathScreenX;
            } else {
                dto.normalizedXMoveDirection = 1;
            }
        }
    }
}
