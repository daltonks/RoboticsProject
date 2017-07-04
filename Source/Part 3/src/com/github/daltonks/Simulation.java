package com.github.daltonks;

import coppelia.IntW;
import coppelia.remoteApi;

import java.net.ConnectException;

public class Simulation {
    private remoteApi api;
    private int clientID;

    public remoteApi getRemoteApi() { return api; }

    public int getClientID() { return clientID; }

    public void start(String ip, int port, boolean waitUntilConnected, boolean doNotReconnectOnceDisconnected, int timeOutInMs, int commThreadCycleInMs) throws ConnectException {
        api = new remoteApi();

        closeAllOpenedConnections();
        clientID = api.simxStart(ip, port, waitUntilConnected, doNotReconnectOnceDisconnected, timeOutInMs, commThreadCycleInMs);
        if (clientID != -1)
        {
            System.out.println("Connected to remote API server");
        } else {
            throw new ConnectException("Failed connecting to remote API server");
        }
    }

    private void closeAllOpenedConnections() {
        api.simxFinish(-1);
    }

    public void end() {
        guaranteeLastApiCallWasReceived();
        api.simxFinish(clientID);
    }

    private void guaranteeLastApiCallWasReceived() {
        IntW pingTime = new IntW(0);
        api.simxGetPingTime(clientID, pingTime);
    }

    public SimObject getObject(String objectName) {
        IntW handle = new IntW(0);
        return SimAPIUtil.handleResponse(
            api.simxGetObjectHandle(clientID, objectName, handle, api.simx_opmode_blocking),
            () -> new SimObject(handle.getValue(), this)
        );
    }
}
