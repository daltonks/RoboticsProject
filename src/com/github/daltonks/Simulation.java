package com.github.daltonks;

import coppelia.IntW;
import coppelia.IntWA;
import coppelia.remoteApi;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

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
        api.simxGetPingTime(clientID, pingTime);
        api.simxFinish(clientID);
    }

    public SimObject[] getAllObjects() {
        IntWA objectHandles = new IntWA(1);
        return SimAPIUtil.handleResponse(
            api.simxGetObjects(clientID, remoteApi.sim_handle_all, objectHandles, remoteApi.simx_opmode_blocking),
            () -> {
                int[] intArray = objectHandles.getArray();
                SimObject[] simObjectArray = new SimObject[intArray.length];
                for(int i = 0; i < simObjectArray.length; i++) {
                    simObjectArray[i] = new SimObject(intArray[i], this);
                }
                return simObjectArray;
            }
        );
    }
}
