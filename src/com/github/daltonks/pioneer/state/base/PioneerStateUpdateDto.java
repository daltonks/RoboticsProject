package com.github.daltonks.pioneer.state.base;

import com.github.daltonks.pioneer.imageProcessing.PioneerProcessedImage;

public class PioneerStateUpdateDto {
    public boolean followedBlockWasOnLeft;
    public PioneerProcessedImage processedImage;
    public float normalizedXMoveDirection;
    public boolean isReversing;
    public PioneerStateType nextState;
}
