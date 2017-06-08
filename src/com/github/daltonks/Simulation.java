package com.github.daltonks;

import coppelia.IntW;
import coppelia.IntWA;
import coppelia.remoteApi;

import java.net.ConnectException;

public class Simulation {
    private remoteApi api;
    private int clientID;

    public void start(String ip, int port, boolean waitUntilConnected, boolean doNotReconnectOnceDisconnected, int timeOutInMs, int commThreadCycleInMs) throws ConnectException {
        api = new remoteApi();

        api.simxFinish(-1); // close all opened connections
        clientID = api.simxStart(ip, port, waitUntilConnected, doNotReconnectOnceDisconnected, timeOutInMs, commThreadCycleInMs);
        if (clientID != -1)
        {
            System.out.println("Connected to remote API server");
        }
        else {
            throw new ConnectException("Failed connecting to remote API server");
        }
    }

    public void end() {
        // Before closing the connection to V-REP, make sure that the last command sent out had time to arrive. You can guarantee this with (for example):
        IntW pingTime = new IntW(0);
        api.simxGetPingTime(clientID,pingTime);
        api.simxFinish(clientID);
    }

    public int[] getAllObjectHandles() {
        IntWA objectHandles = new IntWA(1);
        int ret = api.simxGetObjects(clientID, remoteApi.sim_handle_all, objectHandles, remoteApi.simx_opmode_blocking);
        if (ret == remoteApi.simx_return_ok)
            return objectHandles.getArray();
        else {
            System.out.format("Remote API function call returned with error code: %d\n",ret);
            return null;
        }
    }
}
