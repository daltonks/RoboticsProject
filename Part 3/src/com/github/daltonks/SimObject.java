package com.github.daltonks;

import coppelia.CharWA;
import coppelia.IntWA;
import coppelia.remoteApi;

public class SimObject {
    private Simulation simulation;
    private int handle;

    public SimObject(int handle, Simulation simulation) {
        this.handle = handle;
        this.simulation = simulation;
    }

    public void setJointTargetVelocity(float velocity) {
        SimAPIUtil.handleResponse(
            simulation.getRemoteApi().simxSetJointTargetVelocity(simulation.getClientID(), handle, velocity, remoteApi.simx_opmode_streaming)
        );
    }

    public SensorImage getVisionSensorImage() {
        IntWA resolution = new IntWA(2);
        CharWA image = new CharWA(0);
        return SimAPIUtil.handleResponse(
            simulation.getRemoteApi().simxGetVisionSensorImage(simulation.getClientID(), handle, resolution, image, 0, remoteApi.simx_opmode_oneshot_wait),
            () -> new SensorImage(image.getArray(), resolution.getArray()[0], resolution.getArray()[1])
        );
    }
}
