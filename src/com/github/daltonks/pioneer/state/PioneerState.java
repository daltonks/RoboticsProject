package com.github.daltonks.pioneer.state;

import com.github.daltonks.pioneer.imageProcessing.PioneerProcessedImage;

public interface PioneerState {
    PioneerStateUpdateResultDto update(PioneerProcessedImage processedImage);
}
