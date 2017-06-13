package com.github.daltonks.pioneer.state;

import com.github.daltonks.Constants;
import com.github.daltonks.pioneer.imageProcessing.PioneerProcessedPixelType;
import com.github.daltonks.pioneer.state.base.PioneerState;
import com.github.daltonks.pioneer.state.base.PioneerStateType;
import com.github.daltonks.pioneer.state.base.PioneerStateUpdateDto;

public class PioneerFollowPathState implements PioneerState {
    @Override
    public void onEnterState() {

    }

    @Override
    public void update(PioneerStateUpdateDto dto) {
        int numBlockPixels = dto.processedImage.getNumPixels(PioneerProcessedPixelType.Block);
        Float averageNormalizedPathScreenX = dto.processedImage.getAverageNormalizedScreenX(PioneerProcessedPixelType.Path);
        if(numBlockPixels >= Constants.NUM_BLOCK_PIXELS_BEFORE_FOLLOW) {
            dto.nextState = PioneerStateType.FollowBlock;
            Float averageNormalizedBlockScreenX = dto.processedImage.getAverageNormalizedScreenX(PioneerProcessedPixelType.Block);
            dto.followedBlockWasOnLeft = averageNormalizedBlockScreenX < averageNormalizedPathScreenX;
        } else {
            dto.nextState = PioneerStateType.FollowPath;
            if(averageNormalizedPathScreenX != null) {
                dto.normalizedXMoveDirection = averageNormalizedPathScreenX;
            } else {
                dto.normalizedXMoveDirection = 1;
            }
        }
    }
}
