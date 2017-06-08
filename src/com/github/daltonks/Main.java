package com.github.daltonks;

import java.net.ConnectException;

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
                Constants.COMM_THREAD_CYCLE_IN_MS
            );
            SimObject leftMotor = simulation.getObject(Constants.LEFT_MOTOR_NAME);
            leftMotor.setJointTargetVelocity(.2f);
            SimObject rightMotor = simulation.getObject(Constants.RIGHT_MOTOR_NAME);
            SimObject camera = simulation.getObject(Constants.CAMERA_NAME);
            while(true) {
                SensorImage image = camera.getVisionSensorImage();
                for(int y = 15; y < 60; y++) {
                    StringBuilder lineBuilder = new StringBuilder();
                    for(int x = 0; x < image.getResolutionX(); x++) {
                        Color color = image.getColor(x, y);
                        if(color.red > 150 && color.green < 50 && color.blue < 50) {
                            lineBuilder.append('X');
                        }
                        else {
                            lineBuilder.append(' ');
                        }
                    }
                    System.out.println(lineBuilder.toString());
                }
                System.out.println("----------------------------------------------------------------");
                Thread.sleep(200);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        simulation.end();

        System.out.println("Program ended");
    }
}

