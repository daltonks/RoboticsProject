package com.github.daltonks.pioneer.state;

import com.github.daltonks.Constants;
import com.github.daltonks.pioneer.imageProcessing.PioneerProcessedPixelType;
import com.github.daltonks.pioneer.state.base.PioneerState;
import com.github.daltonks.pioneer.state.base.PioneerStateType;
import com.github.daltonks.pioneer.state.base.PioneerStateUpdateDto;

public class PioneerTurnAwayFromBlockState implements PioneerState {
    private long stateStartTime;

    @Override
    public void onEnterState() {

    }

    @Override
    public void update(PioneerStateUpdateDto dto) {
        int numBlockPixels = dto.processedImage.getNumPixels(PioneerProcessedPixelType.Block);
        if(numBlockPixels <= Constants.NUM_BLOCK_PIXELS_BEFORE_FOLLOW / 2) {
            dto.nextState = PioneerStateType.FollowPath;
        } else {
            dto.nextState = PioneerStateType.TurnAwayFromBlock;
            //If the block was originally on the left, turn right. Otherwise, turn left
            dto.normalizedXMoveDirection = dto.followedBlockWasOnLeft ? .5f : -.5f;
        }
    }
}
