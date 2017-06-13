package com.github.daltonks.pioneer.state;

import com.github.daltonks.pioneer.imageProcessing.PioneerProcessedImage;

public class PioneerStateUpdateDto {
    public PioneerProcessedImage processedImage;
    public float normalizedXMoveDirection;
    public PioneerStateType nextState;
}
