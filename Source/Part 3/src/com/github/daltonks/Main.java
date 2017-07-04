package com.github.daltonks;

import com.github.daltonks.pioneer.Pioneer;

import java.io.IOException;

public class Main {
    public static void main(String[] args)
    {
        Simulation simulation = null;
        try {
            simulation = new Simulation();

            simulation.start(
                Constants.REMOTE_IP,
                Constants.REMOTE_PORT,
                Constants.WAIT_UNTIL_CONNECTED,
                Constants.DO_NOT_RECONNECT_ONCE_DISCONNECTED,
                Constants.TIME_OUT_IN_MS,
                Constants.COMM_THREAD_CYCLE_MILLIS
            );

            SimObject leftMotor = simulation.getObject(Constants.LEFT_MOTOR_NAME);
            SimObject rightMotor = simulation.getObject(Constants.RIGHT_MOTOR_NAME);
            SimObject camera = simulation.getObject(Constants.CAMERA_NAME);
            Pioneer pioneer = new Pioneer(leftMotor, rightMotor, camera);
            while(true) {
                pioneer.update();
                Thread.sleep(Constants.UPDATE_LOOP_MILLIS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (simulation != null) {
            simulation.end();
        }

        System.out.println("Complete. Press enter to exit.");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

