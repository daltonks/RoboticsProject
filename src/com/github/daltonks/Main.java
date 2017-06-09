package com.github.daltonks;

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
            SimObject rightMotor = simulation.getObject(Constants.RIGHT_MOTOR_NAME);
            SimObject camera = simulation.getObject(Constants.CAMERA_NAME);

            while(true) {
                SensorImage image = camera.getVisionSensorImage();
                int halfWidth = image.getResolutionX() / 2;
                int numPathPixels = 0;
                double totalNormalizedPathXes = 0;
                for(int y = 0; y < image.getResolutionY(); y++) {
                    for(int x = 0; x < image.getResolutionX(); x++) {
                        Color color = image.getColor(x, y);
                        //Black path
                        if(color.red < 20 && color.green < 20 && color.blue < 20) {
                            totalNormalizedPathXes += (x - halfWidth) / (double) halfWidth;
                            numPathPixels++;
                        //Red cube
                        } else if(color.red > 150 && color.green < 50 && color.blue < 50) {

                        }
                    }
                }
                float speed = 3f;
                if(numPathPixels != 0) {
                    float averageNormalizedPathX = (float) (totalNormalizedPathXes / numPathPixels);
                    if(averageNormalizedPathX >= 0) {
                        leftMotor.setJointTargetVelocity(1 * speed);
                        rightMotor.setJointTargetVelocity((-averageNormalizedPathX + 1) * speed);
                    } else if(averageNormalizedPathX < 0) {
                        leftMotor.setJointTargetVelocity(averageNormalizedPathX + 1);
                        rightMotor.setJointTargetVelocity(1 * speed);
                    }
                    System.out.println(averageNormalizedPathX);
                }
                else {
                    leftMotor.setJointTargetVelocity(speed);
                    rightMotor.setJointTargetVelocity(0);
                }

                Thread.sleep(30);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        simulation.end();

        System.out.println("Program ended");
    }
}

