package com.github.daltonks;

import com.github.daltonks.pioneer.Pioneer;

public class Main {
    public static void main(String[] args)
    {
        Simulation simulation = new Simulation();
        try {
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

        simulation.end();

        System.out.println("Program ended");
    }
}

