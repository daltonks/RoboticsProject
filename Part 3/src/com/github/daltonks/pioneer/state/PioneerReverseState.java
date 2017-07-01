package com.github.daltonks.pioneer.state;

import com.github.daltonks.Constants;
import com.github.daltonks.pioneer.state.base.PioneerState;
import com.github.daltonks.pioneer.state.base.PioneerStateType;
import com.github.daltonks.pioneer.state.base.PioneerStateUpdateDto;

public class PioneerReverseState implements PioneerState {
    private long enterStateTime;

    @Override
    public void onEnterState() {
        enterStateTime = System.currentTimeMillis();
    }

    @Override
    public void update(PioneerStateUpdateDto dto) {
        if(System.currentTimeMillis() - enterStateTime >= Constants.REVERSE_TIME_MILLIS) {
            dto.nextState = PioneerStateType.TurnAwayFromBlock;
            dto.isReversing = false;
        } else {
            dto.nextState = PioneerStateType.Reverse;
            dto.isReversing = true;
            dto.normalizedXMoveDirection = 0;
        }
    }
}
