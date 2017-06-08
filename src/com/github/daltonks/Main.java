package com.github.daltonks;

import java.net.ConnectException;

public class Main {
    private static final String REMOTE_IP = "127.0.0.1";
    private static final int REMOTE_PORT = 19999;
    private static final boolean WAIT_UNTIL_CONNECTED = true;
    private static final boolean DO_NOT_RECONNECT_ONCE_DISCONNECTED = true;
    private static final int TIME_OUT_IN_MS = 10000;
    private static final int COMM_THREAD_CYCLE_IN_MS = 5;

    public static void main(String[] args)
    {
        Simulation simulation = new Simulation();
        try {
            simulation.start(REMOTE_IP, REMOTE_PORT, WAIT_UNTIL_CONNECTED, DO_NOT_RECONNECT_ONCE_DISCONNECTED, TIME_OUT_IN_MS, COMM_THREAD_CYCLE_IN_MS);
            System.out.println(simulation.getAllObjects().length);
        } catch (ConnectException e) {
            e.printStackTrace();
        }

        simulation.end();

        System.out.println("Program ended");
    }
}

