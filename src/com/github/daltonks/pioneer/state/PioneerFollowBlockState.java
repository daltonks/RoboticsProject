package com.github.daltonks.pioneer.state;

import com.github.daltonks.Constants;
import com.github.daltonks.pioneer.imageProcessing.PioneerProcessedPixelType;

public class PioneerFollowBlockState implements PioneerState {
    private int highestBlockPixelCount;
    private long lastFoundHighestCountTime;

    @Override
    public void update(PioneerStateUpdateDto dto) {
        int numBlockPixels = dto.processedImage.getNumPixels(PioneerProcessedPixelType.Block);

        if(numBlockPixels >= Constants.NUM_BLOCK_PIXELS_BEFORE_CONSIDERING_COLLISION
                && numBlockPixels > highestBlockPixelCount) {

            highestBlockPixelCount = numBlockPixels;
            lastFoundHighestCountTime = System.currentTimeMillis();
        }

        if(lastFoundHighestCountTime != 0  && System.currentTimeMillis() - lastFoundHighestCountTime > Constants.ACCEPT_BLOCK_COLLISION_MILLIS) {
            //We probably bumped into the block
            //Reset variables
            highestBlockPixelCount = 0;
            lastFoundHighestCountTime = 0;
            dto.nextState = PioneerStateType.FollowPath;
            System.out.println("booped the thing!");
        } else {
            dto.nextState = PioneerStateType.FollowBlock;
            Float averageNormalizedPathScreenX = dto.processedImage.getAverageNormalizedScreenX(PioneerProcessedPixelType.Block);
            if(averageNormalizedPathScreenX != null) {
                dto.normalizedXMoveDirection = averageNormalizedPathScreenX;
            } else {
                dto.normalizedXMoveDirection = 1;
            }
        }
    }
}
