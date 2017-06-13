package com.github.daltonks.pioneer.state.base;

public interface PioneerState {
    void onEnterState();
    void update(PioneerStateUpdateDto dto);
}
